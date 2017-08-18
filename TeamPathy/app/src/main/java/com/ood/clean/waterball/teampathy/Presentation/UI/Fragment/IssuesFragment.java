package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ProjectSection;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.IndexSet;
import com.ood.clean.waterball.teampathy.MyUtils.NormalDateConverter;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.CrudPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.IssuePresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateIssueDialogFragment;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.IssueItemBinding;

import java.text.ParseException;
import java.util.TreeSet;

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
    @Inject Project project;
    @Inject User user;
    @Inject IssuePresenterImp presenterImp;
    IndexSet<Issue> issues = new IndexSet<>(new TreeSet<Issue>());
    BroadcastReceiver receiver = new IssueBroadCastReceiver();

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
        issues.add(issue);
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
        issues.remove(issue);
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
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = ProjectSection.FORUM.getIntentFilter();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
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
            Object obj = issues.get(position);
            holder.bind(obj);
            setOnClickListener(holder.itemView, position);
        }

        private void setOnClickListener(View view, final int position){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Issue issue = issues.get(position);
                    MyApp.createIssueComponent(getActivity(),issue);
                    getBaseView().getPageController().changePage(new IssueDetailsFragment());
                }
            });
        }

        @Override
        public int getItemCount() {
            return issues.size();
        }
    }


    class IssueBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                String eventType = intent.getAction();
                int issueId = Integer.parseInt(intent.getStringExtra("id"));
                String name = intent.getStringExtra("name");
                String category = intent.getStringExtra("category");
                String content = intent.getStringExtra("content");
                String posterImageUrl = intent.getStringExtra("posterImageUrl");
                String postDate = intent.getStringExtra("postDate");
                int posterId = Integer.parseInt(intent.getStringExtra("posterId"));
                int projectId = Integer.parseInt(intent.getStringExtra("projectId"));
                String posterName = intent.getStringExtra("posterName");
                User poster = new User(posterName, posterImageUrl);
                poster.setId(posterId);
                Issue issue = new Issue(poster, name, content, category);
                issue.setId(issueId);
                issue.setPostDate(NormalDateConverter.stringToDate(postDate));
                if (projectId == project.getId() && !poster.equals(user))
                {
                    if (eventType.contains("post"))
                        issues.add(issue);
                    else if (eventType.contains("delete"))
                        issues.remove(issue);
                    else if (eventType.contains("put"))
                        issues.update(issue);

                    adapter.notifyDataSetChanged();
                }
            }catch(ParseException err){
                onOperationTimeout(err);
            }

        }
    }

}
