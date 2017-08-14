package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.CrudPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.IssuePresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateIssueDialogFragment;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.IssueItemBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IssuesFragment extends BaseFragment implements CrudPresenter.CrudView<Issue>, SwipeRefreshLayout.OnRefreshListener {
    int page = 0;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    IssuesAdapter adapter;
    LinearLayoutManager layoutManager;
    @Inject IssuePresenterImp presenterImp;
    List<Issue> issueList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_forum_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getBaseView().showProgressBar();
        binding(view);
        setupRecyclerview();
        presenterImp.setIssueView(this);
        presenterImp.loadEntities(page++);
    }

    private void binding(View view){
        ButterKnife.bind(this,view);
        MyApp.getProjectComponent(getActivity()).inject(this);
    }

    private void setupRecyclerview() {
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new IssuesAdapter();
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void loadEntity(Issue issue) {
        issueList.add(issue);
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fab)
    public void fabOnClick(View view){
        showAlertDialogFragment(new CreateIssueDialogFragment());
    }

    @Override
    public void onCreateFinishNotify(final Issue issue) {
        getBaseView().hideProgressDialog();
        Snackbar.make(getView(), R.string.issue_created_completed, Snackbar.LENGTH_LONG)
                .setAction(R.string.enter, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        layoutManager.scrollToPosition(0);
                        MyApp.createIssueComponent(getActivity(),issue);
                        getBaseView().getPageController().changePage(new IssueDetailsFragment());
                    }
                }).show();
    }

    @Override
    public void onDeleteFinishNotify(Issue issue) {
        issueList.remove(issue);
    }

    @Override
    public void onUpdateFinishNotify(Issue issue) {

    }

    @Override
    public void onLoadFinishNotify() {
        swipeRefreshLayout.setRefreshing(false);
        getBaseView().hideProgressBar();
    }

    @Override
    public void onRefresh() {
        getBaseView().showProgressBar();
        presenterImp.loadEntities(page++);
    }

    @Override
    public void onOperationTimeout(Throwable err) {
        getBaseView().hideProgressDialog();
        Toast.makeText(getContext(),err.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPageIndexOutOfBound() {
        page --;
        onLoadFinishNotify();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterImp.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterImp.onDestroy();
    }

    public class IssuesAdapter extends RecyclerView.Adapter<BindingViewHolder>{

        @Override
        public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            IssueItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.issue_item,parent,false);
            return new BindingViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(BindingViewHolder holder, int position) {
            Object obj = issueList.get(position);
            holder.bind(obj);
            setOnClickListener(holder.itemView, position);
        }

        private void setOnClickListener(View view, final int position){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Issue issue = issueList.get(position);
                    MyApp.createIssueComponent(getActivity(),issue);
                    getBaseView().getPageController().changePage(new IssueDetailsFragment());
                }
            });
        }

        @Override
        public int getItemCount() {
            return issueList.size();
        }
    }

}
