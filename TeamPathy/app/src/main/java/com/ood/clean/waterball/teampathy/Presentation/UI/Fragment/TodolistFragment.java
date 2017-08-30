package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.TodoListPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.TodolistPresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.TodotaskItemBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodolistFragment extends BaseFragment implements TodoListPresenter.TodoListView, SwipeRefreshLayout.OnRefreshListener {
    private final int COMMIT_TASK = 0;
    private final int CANCEL_COMMIT = 0;
    private final int SET_AS_DOING_TASK = 1;

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    TodoListAdapter adapter;
    @Inject User user;
    @Inject TodolistPresenterImp presenterImp;

    String[] uncommittedTodoTaskActions;
    String[] committedTodoTaskActions;
    List<TodoTask> todoList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_todolist_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getBaseView().showProgressBar();
        init();
        binding(view);
        presenterImp.setTodoListView(this);
        setupRecyclerview();
        presenterImp.loadTodoList();
    }

    private void init() {
        uncommittedTodoTaskActions = getResources().getStringArray(R.array.member_uncommitted_todolist_actions);
        committedTodoTaskActions = getResources().getStringArray(R.array.member_committed_todolist_actions);
    }

    private void binding(View view){
        ButterKnife.bind(this,view);
        MyApp.getProjectComponent(getActivity()).inject(this);
    }

    private void setupRecyclerview() {
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new TodoListAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void loadTodoTask(TodoTask todoTask) {
        todoList.add(todoTask);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFinishNotify() {
        getBaseView().hideProgressBar();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getBaseView().showProgressBar();
        presenterImp.loadTodoList();
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

    public class TodoListAdapter extends RecyclerView.Adapter<BindingViewHolder>{

        @Override
        public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            TodotaskItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.todotask_item,parent,false);
            return new BindingViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(BindingViewHolder holder, int position) {
            Object obj = todoList.get(position);
            holder.bind(obj);
            setOnClickListener(holder.itemView, position);
        }

        private void setOnClickListener(View view, final int position){
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TodoTask todoTask = todoList.get(position);
                    showTodoTaskActionList(todoTask);
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return todoList.size();
        }
    }

    private void showTodoTaskActionList(final TodoTask todoTask) {
        String[] actions;
        DialogInterface.OnClickListener listener;

        // if the task has been committed, show the different action list and different listener.
        if (todoTask.getStatus() == TodoTask.Status.pending)
        {
            actions = committedTodoTaskActions;
            listener = createCommittedTodoTaskActionListener(todoTask);
        }
        else
        {
            actions = uncommittedTodoTaskActions;
            listener = createUncommittedTodoTaskActionListener(todoTask);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getContext(), android.R.layout.simple_list_item_1,
                actions);

        new AlertDialog.Builder(getContext())
                .setAdapter(adapter, listener)
                .show();
    }

    private DialogInterface.OnClickListener createUncommittedTodoTaskActionListener
            (final TodoTask todoTask) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                switch (position) {
                    case COMMIT_TASK:
                        presenterImp.commitTask(todoTask);
                        break;
                    case SET_AS_DOING_TASK:
                        presenterImp.setAsDoingTask(todoTask);
                        break;
                }
            }
        };
    }

    private DialogInterface.OnClickListener createCommittedTodoTaskActionListener
            (final TodoTask todoTask) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                switch (position) {
                    case CANCEL_COMMIT:
                        presenterImp.cancelCommit(todoTask);
                        break;
                }
            }
        };
    }
}
