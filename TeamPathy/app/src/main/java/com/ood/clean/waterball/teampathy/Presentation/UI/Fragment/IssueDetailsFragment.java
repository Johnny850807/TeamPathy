package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.CrudPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.IssueDetailsPresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateIssueCommentDialogFragment;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.FragmentIssueDetailsPageBinding;
import com.ood.clean.waterball.teampathy.databinding.IssueCommentItemBinding;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ood.clean.waterball.teampathy.MyUtils.MyLog.Log;

public class IssueDetailsFragment extends BaseFragment implements CrudPresenter.CrudView<IssueComment>, SwipeRefreshLayout.OnRefreshListener {
    int page = 1;
    @Inject Issue issue;
    @Inject IssueDetailsPresenterImp presenterImp;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    IssueCommentsAdapter adapter;
    LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentIssueDetailsPageBinding databinding =
                DataBindingUtil.inflate(inflater,R.layout.fragment_issue_details_page, container, false);
        binding(databinding);
        return databinding.getRoot();
    }

    private void binding(FragmentIssueDetailsPageBinding databinding){
        ButterKnife.bind(this,databinding.getRoot());
        MyApp.getIssueComponent(getActivity()).inject(this);
        databinding.setIssue(issue);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setupRecyclerview();
        presenterImp.setIssueDetailsView(this);
    }


    private void setupRecyclerview() {
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new IssueCommentsAdapter();
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
    }


    @OnClick(R.id.fab)
    public void fabOnClick(View view){
        showAlertDialogFragment(new CreateIssueCommentDialogFragment());
    }

    @Override
    public void loadEntity(IssueComment issueComment) {
        issue.getComments().add(issueComment);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateFinishNotify(IssueComment issueComment) {
        getBaseView().hideProgressDialog();
        layoutManager.scrollToPosition(issue.getComments().size() - 1);
    }

    @Override
    public void onDeleteFinishNotify(IssueComment issueComment) {

    }

    @Override
    public void onUpdateFinishNotify(IssueComment issueComment) {

    }

    @Override
    public void onLoadFinishNotify() {
        getBaseView().hideProgressBar();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getBaseView().showProgressBar();
        presenterImp.loadEntities(page++);
    }

    @Override
    public void onPageIndexOutOfBound() {
        onLoadFinishNotify();
    }

    @Override
    public void onOperationTimeout(Throwable err) {
        getBaseView().hideProgressDialog();
        Toast.makeText(getContext(),err.getMessage(),Toast.LENGTH_LONG).show();
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

    public class IssueCommentsAdapter extends RecyclerView.Adapter<BindingViewHolder>{

        @Override
        public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            IssueCommentItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.issue_comment_item,parent,false);
            return new BindingViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(BindingViewHolder holder, int position) {
            Object obj = issue.getComments().get(position);
            holder.bind(obj);
            setOnClickListener(holder.itemView, position);
        }

        private void setOnClickListener(View view, final int position){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    IssueComment issueComment = issue.getComments().get(position);
                    Log("IssueComment on click " + issueComment.getContent());
                    //todo IssueComment on click change to details
                }
            });
        }

        @Override
        public int getItemCount() {
            return issue.getComments().size();
        }
    }
}
