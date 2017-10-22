package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.TodoListPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.TodolistPresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.TodotaskItemBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TodolistFragment extends BaseFragment implements TodoListPresenter.TodoListView, SwipeRefreshLayout.OnRefreshListener {
    private static final String TARGET_MEMBER = "Member Id";
    private static final String WATCH_MODE = "Operation Enabled";
    private final int COMMIT_TASK = 0;
    private final int CANCEL_COMMIT = 0;
    private final int SET_AS_DOING_TASK = 1;
    private final int CANCEL_DOING = 1;

    private Member targetMember; // the target member's todolist showing.
    private boolean watchMode; // if false that the user cannot do any thing with the todolist.
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    TodoListAdapter adapter;
    @Inject TodolistPresenterImp presenterImp;

    String[] uncommittedTodoTaskActions;
    String[] undoingTodoTaskActions;
    String[] todoActions;
    List<TodoTask> todoList = new ArrayList<>();

    /**
     * @param member show who's todolist
     * @param watchMode if it's watchMode, means the user doesn't own the todolist, cannot operate the todolist neither.
     */
    public static TodolistFragment newInstance(Member member, boolean watchMode){
        TodolistFragment fragment = new TodolistFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TARGET_MEMBER, member);
        bundle.putBoolean(WATCH_MODE, watchMode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        targetMember = (Member) getArguments().getSerializable(TARGET_MEMBER);
        watchMode = getArguments().getBoolean(WATCH_MODE);
        uncommittedTodoTaskActions = getResources().getStringArray(R.array.member_uncommitted_todolist_actions);
        undoingTodoTaskActions = getResources().getStringArray(R.array.member_undoing_todolist_actions);
        todoActions = getResources().getStringArray(R.array.member_todo_todolist_actions);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_todolist_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getBaseView().showProgressBar();
        if (watchMode)
            getBaseView().setToolbarTitle(getString(R.string.app_name) + " (" + targetMember.getUser().getName() + ")");
        todoList.clear();
        binding(view);
        presenterImp.setTodoListView(this);
        setupRecyclerview();
        presenterImp.loadTodoList(targetMember);
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
        Collections.sort(todoList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFinishNotify() {
        getBaseView().hideProgressBar();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAlterFinishNotify(TaskItem todoTask, TodoTask.Status status) {
        getBaseView().hideProgressBar();
        Snackbar.make(getView(), R.string.alter_complete, Snackbar.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onOperationError(Throwable err) {
        getBaseView().hideProgressBar();
        TeamPathyDialogFactory.networkErrorDialogBuilder(getActivity())
                .setMessage(err.getMessage())
                .show();
    }

    @Override
    public void onRefresh() {
        todoList.clear();
        getBaseView().showProgressBar();
        presenterImp.loadTodoList(targetMember);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenterImp.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getBaseView().setToolbarTitle(getString(R.string.app_name));
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
            if (!watchMode) // if it's watchMode, the items should have no reaction to the user.
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
        if (todoTask.getStatus() != TodoTask.Status.pass)
        {
            String[] actions = createActionsUponStatus(todoTask);
            DialogInterface.OnClickListener listener = createListenerUponStatus(todoTask);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getContext(), android.R.layout.simple_list_item_1,
                            actions);

            new AlertDialog.Builder(getContext())
                    .setAdapter(adapter, listener)
                    .show();
        }
    }

    private String[] createActionsUponStatus(TodoTask todoTask){
        return todoTask.getStatus() == TodoTask.Status.pending ? uncommittedTodoTaskActions :
                todoTask.getStatus() == TodoTask.Status.doing ? undoingTodoTaskActions :
                        todoActions;
    }

    private DialogInterface.OnClickListener createListenerUponStatus(TodoTask todoTask){
        return todoTask.getStatus() == TodoTask.Status.pending ? createCommittedTodoTaskActionListener(todoTask) :
                todoTask.getStatus() == TodoTask.Status.doing ? createDoingActionListener(todoTask) :
                        createTodoActionListener(todoTask);
    }

    private DialogInterface.OnClickListener createTodoActionListener
            (final TodoTask todoTask) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                switch (position) {
                    case COMMIT_TASK:
                        alterTaskStatus(todoTask, TodoTask.Status.pending);
                        break;
                    case SET_AS_DOING_TASK:
                        alterTaskStatus(todoTask, TodoTask.Status.doing);
                        break;
                }
            }
        };
    }

    private DialogInterface.OnClickListener createDoingActionListener(final TodoTask todoTask){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                switch (position) {
                    case COMMIT_TASK:
                        alterTaskStatus(todoTask, TodoTask.Status.pending);
                        break;
                    case CANCEL_DOING:
                        alterTaskStatus(todoTask, TodoTask.Status.assigned);
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
                        alterTaskStatus(todoTask, TodoTask.Status.assigned);
                        break;
                }
            }
        };
    }

    private void alterTaskStatus(TodoTask todoTask, TodoTask.Status status){
        getBaseView().showProgressBar();
        presenterImp.alterTaskStatus(todoTask, status);
    }
}
