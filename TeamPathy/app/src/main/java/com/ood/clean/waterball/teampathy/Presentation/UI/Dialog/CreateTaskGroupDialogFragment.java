package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskGroup;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.BasePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.WbsConsolePresenterImp;
import com.ood.clean.waterball.teampathy.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateTaskGroupDialogFragment extends MakeSureToCancelBaseDialogFragment{
    private static final String ALL_GROUPS = "AllTaskGroups";
    private static final String PARENT = "ParentName";
    private BasePresenter.BaseView baseView;
    private String parentName;
    private List<String> allGroupNames = new ArrayList<>();
    @Inject WbsConsolePresenterImp wbsConsolePresenterImp;
    @BindView(R.id.nameEd) TextInputEditText nameEd;

    public static CreateTaskGroupDialogFragment newInstance(ArrayList<TaskGroup> taskGroups, String parentName){
        CreateTaskGroupDialogFragment fragment = new CreateTaskGroupDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALL_GROUPS, taskGroups);
        bundle.putString(PARENT, parentName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setBaseView(BasePresenter.BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        parentName = bundle.getString(PARENT);
        ArrayList<TaskGroup> taskGroups = (ArrayList<TaskGroup>) bundle.getSerializable(ALL_GROUPS);
        for (TaskGroup group : taskGroups)
            allGroupNames.add(group.getName());
    }

    @Override
    protected Dialog onCustomDialogSetting(AlertDialog alertDialog) {
        alertDialog.setTitle(getString(R.string.create_task_group));
        return alertDialog;
    }

    @Override
    protected View onViewCreated() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_task_group_dialog,null);
        bind(view);
        return view;
    }

    private void bind(View view){
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
    }

    @Override
    protected View.OnClickListener getOnPositiveButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValid())
                {
                    TaskGroup taskGroup = new TaskGroup(nameEd.getText().toString(), parentName);
                    WbsCommand command = WbsCommand.createTaskChild(parentName, taskGroup);
                    Log.d("TaskGroup", taskGroup.toString());
                    baseView.showProgressDialog();
                    wbsConsolePresenterImp.executeCommand(command);
                    dismiss();
                }
            }
        };
    }

    private boolean checkValid() {
        boolean value = true;
        if (nameEd.length() == 0)
        {
            nameEd.setError(getString(R.string.name_cannot_be_empty));
            value = false;
        }
        else if (hasDuplicatedTaskName(nameEd.getText().toString()))
        {
            nameEd.setError(getString(R.string.name_cannot_be_duplicated));
            value = false;
        }
        return value;
    }

    private boolean hasDuplicatedTaskName(String name){
        for (String n : allGroupNames)
            if (n.equals(name))
                return true;
        return false;
    }
}
