package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskEventVisitor;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskGroup;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.WbsConsolePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.WbsConsolePresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.AssignTaskDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateTaskGroupDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateTodoTaskDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Factory.TaskItemViewFactory;
import com.ood.clean.waterball.teampathy.R;

import org.apmem.tools.layouts.FlowLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WbsConsoleFragment extends BaseFragment implements WbsConsolePresenter.WbsView{
    private final int EDIT_TASK = 0;
    private final int DELETE_TASK = 1;
    private final int ASSIGN_TODOTASK = 2;
    private final int CREATE_TASK_GROUP = 0;
    private final int CREATE_TODOTASK = 1;

    private String[] todotaskActions;
    private String[] taskGroupActions;
    private String[] createTaskTypeActions;

    @BindView(R.id.flowlayout) FlowLayout flowLayout;
    @Inject TaskItemViewFactory taskItemViewFactory;
    @Inject WbsConsolePresenterImp presenterImp;
    @Inject Member member;
    private TaskItem taskRoot;

    private TaskEventVisitor onEditEventVisitor;
    private TaskEventVisitor onClickEventVisitor;
    private TaskEventVisitor onLongClickEventVisitor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        onClickEventVisitor = new OnClickEventVisitor();
        onEditEventVisitor = new OnEditEventVisitor();
        onLongClickEventVisitor = new OnLongClickEventVisitor();
        todotaskActions = getResources().getStringArray(R.array.wbs_console_todotask_actions);
        taskGroupActions = getResources().getStringArray(R.array.wbs_console_taskgroup_actions);
        createTaskTypeActions = getResources().getStringArray(R.array.create_task_type_list);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_wbs_console,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getBaseView().showProgressDialog();
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
        presenterImp.setWbsView(this);
        setupConsoleView();
    }

    private void setupConsoleView() {
        taskItemViewFactory.setOnClickEventVisitor(onClickEventVisitor);
        taskItemViewFactory.setOnLongClickEventVisitor(onLongClickEventVisitor);
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
    public void onError(Throwable err) {
        TeamPathyDialogFactory.networkErrorDialogBuilder(getActivity())
                .setMessage(err.getMessage())
                .show();
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

    /** taskview event visitor's **/

    private class OnClickEventVisitor implements TaskEventVisitor{
        @Override
        public void eventOnTask(final TaskGroup taskGroup) {
            showDialogListForTaskGroupOnClickActions(taskGroup);
        }

        @Override
        public void eventOnTask(final TodoTask task) {
            showDialogForDetailsOfTodo(task);
        }
    }

    private void showDialogListForTaskGroupOnClickActions(final TaskGroup taskGroup){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, createTaskTypeActions);
        if (member.isManager())
            TeamPathyDialogFactory.templateBuilder(getActivity())
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            switch (position)
                            {
                                case CREATE_TASK_GROUP:
                                    showDialogForCreateTaskChild(taskGroup);
                                    break;
                                case CREATE_TODOTASK:
                                    showDialogForCreatingTodoTask(taskGroup);
                                    break;
                            }
                        }
                    })
                    .show();
    }


    private void showDialogForCreatingTodoTask(TaskItem parent) {
        CreateTodoTaskDialogFragment fragment = CreateTodoTaskDialogFragment.newInstance(parent.getName());
        fragment.setBaseView(getBaseView());
        showAlertDialogFragment(fragment);
    }

    private void showDialogForCreateTaskChild(TaskItem parent) {
        CreateTaskGroupDialogFragment fragment = CreateTaskGroupDialogFragment.newInstance(parent.getName());
        fragment.setBaseView(getBaseView());
        showAlertDialogFragment(fragment);
    }

    private void showDialogForDetailsOfTodo(TodoTask todoTask) {
        //todo
    }

    private class OnLongClickEventVisitor implements TaskEventVisitor{
        // All by the same handler function

        @Override
        public void eventOnTask(TaskGroup taskGroup) {
            // taskgroup actions should not contain the assign task option
            showDialogListForTaskItemOnLongClickActions(taskGroupActions, taskGroup);
        }

        @Override
        public void eventOnTask(TodoTask task) {
            showDialogListForTaskItemOnLongClickActions(todotaskActions, task);
        }
    }

    private void showDialogListForTaskItemOnLongClickActions(final String[] actions, final TaskItem taskItem) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, actions);
        if (member.isManager()) // if the member is not a management level position, he can't do anything
            TeamPathyDialogFactory.templateBuilder(getActivity())
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            switch (position){
                                case EDIT_TASK:
                                    taskItem.acceptEventVisitor(onEditEventVisitor);
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

    private class OnEditEventVisitor implements TaskEventVisitor{

        @Override
        public void eventOnTask(TaskGroup taskGroup) {
            showDialogForEdittingTaskGroup(taskGroup);
        }

        @Override
        public void eventOnTask(TodoTask task) {
            showDialogForEdittingTodoTask(task);
        }
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
                        WbsCommand command = WbsCommand.removeTaskItem(taskItem);
                        Log.d("wbs",new Gson().toJson(command));
                        getBaseView().showProgressDialog();
                        presenterImp.executeCommand(command);
                    }
                }).setNegativeButton(R.string.cancel, null).show();
    }

    private void showDialogForAssigningTaskItem(TaskItem taskItem) {
        AssignTaskDialogFragment fragment = AssignTaskDialogFragment.getInstance((TodoTask) taskItem);
        fragment.setWbsPresenter(presenterImp);
        fragment.setBaseView(getBaseView());
        showAlertDialogFragment(fragment);
    }

}
