package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.ProjectSection;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.NormalDateConverter;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.CrudPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.IssueDetailsPresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateIssueCommentDialogFragment;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.FragmentIssueDetailsPageBinding;
import com.ood.clean.waterball.teampathy.databinding.IssueCommentItemBinding;

import java.text.ParseException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ood.clean.waterball.teampathy.MyUtils.MyLog.Log;

public class IssueDetailsFragment extends BaseFragment implements CrudPresenter.CrudView<IssueComment>, SwipeRefreshLayout.OnRefreshListener {
    int page = 1;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    @Inject IssueDetailsPresenterImp presenterImp;
    @Inject Issue issue;
    @Inject Project project;
    @Inject User user;
    IssueCommentsAdapter adapter;
    LinearLayoutManager layoutManager;
    private BroadcastReceiver receiver = new IssueDetailsBroadcastReceiver();

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
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = ProjectSection.ISSUECOMMENT.getIntentFilter();

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
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
                }
            });
        }

        @Override
        public int getItemCount() {
            return issue.getComments().size();
        }
    }

    class IssueDetailsBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                String eventType = intent.getAction();
                String content = intent.getStringExtra("content");
                String posterImageUrl = intent.getStringExtra("posterImageUrl");
                String postDate = intent.getStringExtra("postDate");
                int posterId = Integer.parseInt(intent.getStringExtra("posterId"));
                int projectId = Integer.parseInt(intent.getStringExtra("projectId"));
                String posterName = intent.getStringExtra("posterName");
                User poster = new User(posterName, posterImageUrl);
                poster.setId(posterId);
                IssueComment comment = new IssueComment(poster, content);
                comment.setPostDate(NormalDateConverter.stringToDate(postDate));
                if (projectId == project.getId() && !poster.equals(user))
                {
                    if (eventType.contains("post"))
                        issue.getComments().add(comment);
                    else if (eventType.contains("delete"))
                        issue.getComments().remove(comment);
                    else if (eventType.contains("put"))
                    {
                        issue.getComments().remove(comment);
                        issue.getComments().add(comment);
                    }

                    adapter.notifyDataSetChanged();
                }
            }catch (ParseException err){
                onOperationTimeout(err);
            }

        }
    }
}
