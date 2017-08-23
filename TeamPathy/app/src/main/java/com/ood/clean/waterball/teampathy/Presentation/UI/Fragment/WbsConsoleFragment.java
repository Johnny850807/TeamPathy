package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskGroup;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskOnClickVisitor;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskOnEditVisitor;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.WbsConsolePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.WbsConsolePresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateTaskGroupDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateTodoTaskDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Factory.TaskItemViewFactory;
import com.ood.clean.waterball.teampathy.R;

import org.apmem.tools.layouts.FlowLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WbsConsoleFragment extends BaseFragment implements WbsConsolePresenter.WbsView , TaskOnClickVisitor, TaskOnEditVisitor {
    private final int EDIT_TASK = 0;
    private final int DELETE_TASK = 1;
    private final int ASSIGN_TODOTASK = 2;
    private final int CREATE_TASK_GROUP = 0;
    private final int CREATE_TODOTASK = 1;

    private String[] todotaskActions;
    private String[] todoGroupActions;
    private String[] createTaskTypeActions;

    @BindView(R.id.flowlayout) FlowLayout flowLayout;
    @Inject TaskItemViewFactory taskItemViewFactory;
    @Inject WbsConsolePresenterImp presenterImp;
    @Inject Member member;
    private TaskItem taskRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_wbs_console,container,false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.wbs_console_menu,menu);  //Task analysis button on the toolbar
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.save_and_update_wbs:  //todo no longer needed
                getBaseView().showProgressDialog();
                presenterImp.updateTasks(taskRoot);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getBaseView().showProgressDialog();
        init();
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
        presenterImp.setWbsView(this);
        setupConsoleView();
    }

    private void init() {
        todotaskActions = getResources().getStringArray(R.array.wbs_console_todotask_actions);
        createTaskTypeActions = getResources().getStringArray(R.array.create_task_type_list);
    }

    private void setupConsoleView() {
        taskItemViewFactory.setViewVisitor(this);
        taskItemViewFactory.setFlowLayout(flowLayout);
        presenterImp.loadTasks();
    }

    @Override
    public void onLoadTasksFinish(TaskItem taskroot) {
        getBaseView().hideProgressDialog();
        this.taskRoot = taskroot;
        refreshFlowLayout();
    }

    @Override
    public void onUpdateTasksFinish(TaskItem taskRoot) {
        getBaseView().hideProgressDialog();
        this.taskRoot = taskRoot;
        Snackbar.make(getView(),"儲存完畢",Snackbar.LENGTH_SHORT).show();
        refreshFlowLayout();
    }

    private void refreshFlowLayout(){
        flowLayout.removeAllViews();
        for ( TaskItem taskItem : taskRoot )
            flowLayout.addView(taskItemViewFactory.createItemView(taskItem));
    }

    @Override
    public void onError(Exception err) {
        Toast.makeText(getContext(),err.getMessage(),Toast.LENGTH_LONG).show();  //todo testing
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

    /** taskview visitor's overloading functions **/

    @Override
    public void taskViewOnClick(final TaskGroup taskGroup) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, createTaskTypeActions);
        new AlertDialog.Builder(getActivity())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        switch (position)
                        {
                            case CREATE_TASK_GROUP:
                                showDialogForCreateTaskChild(taskGroup);
                                break;
                            case CREATE_TODOTASK:
                                showDialogForCreateTodoTask(taskGroup);
                                break;
                        }
                    }
                })
                .show();
    }

    private void showDialogForCreateTodoTask(TaskItem parent) {
        if (!member.isNotManager())
            showAlertDialogFragment(CreateTodoTaskDialogFragment.newInstance(parent.getName()));
    }

    private void showDialogForCreateTaskChild(TaskItem parent) {
        if (!member.isNotManager())
            showAlertDialogFragment(CreateTaskGroupDialogFragment.newInstance(parent.getName()));
    }

    @Override
    public void taskViewOnClick(TodoTask task) {
        showDialogForDetailsOfTodo(task);
    }

    private void showDialogForDetailsOfTodo(TodoTask todoTask) {
        //todo
    }

    @Override
    public void taskViewOnLongClick(TaskItem taskItem) {
        showDialogListForTaskItemLongClickActions(taskItem);
    }



    private void showDialogListForTaskItemLongClickActions(final TaskItem taskItem) {
        MyLongClickActionsAdapter adapter = new MyLongClickActionsAdapter();
        new AlertDialog.Builder(getActivity())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        switch (position){
                            case EDIT_TASK:
                                taskItem.acceptOnEditVisitor(WbsConsoleFragment.this);
                                break;
                            case DELETE_TASK:
                                showDialogForDeletingTaskItem(taskItem);
                                break;
                            case ASSIGN_TODOTASK:
                                showDialogForAssigningTaskItem(taskItem);
                        }
                    }
                })
                .show();
    }

    public class MyLongClickActionsAdapter extends ArrayAdapter<String>{
        public MyLongClickActionsAdapter() {
            super(WbsConsoleFragment.this.getContext(),
                    android.R.layout.simple_list_item_1,
                    todotaskActions);
        }
        @Override
        public int getCount() {
            // if the member not a management position, he can't do anything
            return member.isNotManager() ? 0 : 3;
        }
    }

    @Override
    public void taskOnEdit(TaskGroup taskGroup) {
        showDialogForEdittingTaskGroup(taskGroup);
    }

    @Override
    public void taskOnEdit(TodoTask todoTask) {
        showDialogForEdittingTodoTask(todoTask);
    }

    private void showDialogForEdittingTaskGroup(TaskGroup taskGroup) {
        //todo edit dialog
    }

    private void showDialogForEdittingTodoTask(TodoTask todoTask) {
        //todo edit dialog
    }

    private void showDialogForDeletingTaskItem(final TaskItem taskItem) {
        TeamPathyDialogFactory.templateBuilder(getContext())
                .setTitle(R.string.delete)
                .setMessage(getString(R.string.sure_to_delete) + ' ' + taskItem.getName() + '?')
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        taskRoot.removeTaskChild(taskItem);
                        refreshFlowLayout();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        }).show();
    }

    private void showDialogForAssigningTaskItem(TaskItem taskItem) {
        //presenterImp.assignTask(task, TodoTask.Status.ASSIGNED);
    }

}
