package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
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
import com.ood.clean.waterball.teampathy.Presentation.Presenter.WbsConsolePresenterImp;
import com.ood.clean.waterball.teampathy.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateTaskGroupDialogFragment extends MakeSureToCancelBaseDialogFragment{
    private static final String PARENT = "ParentName";
    private String parentName;
    private WbsConsolePresenterImp wbsConsolePresenterImp;
    @BindView(R.id.nameEd) TextInputEditText nameEd;

    public static CreateTaskGroupDialogFragment newInstance(String parentName){
        CreateTaskGroupDialogFragment fragment = new CreateTaskGroupDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARENT, parentName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setWbsConsolePresenterImp(WbsConsolePresenterImp wbsConsolePresenterImp) {
        this.wbsConsolePresenterImp = wbsConsolePresenterImp;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentName = getArguments().getString(PARENT);
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
        MyApp.getProjectComponent(getActivity()).inject(this);
    }

    @Override
    protected DialogInterface.OnClickListener getOnPositiveButtonClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (checkValid())
                {
                    TaskGroup taskGroup = new TaskGroup(nameEd.getText().toString(), parentName);
                    WbsCommand<TaskGroup> command = WbsCommand.createTaskChild(parentName, taskGroup);
                    Log.d("TaskGroup", taskGroup.toString());
                    wbsConsolePresenterImp.executeCommand(command);
                }
            }
        };
    }

    private boolean checkValid() {
        if (nameEd.getText().toString().isEmpty())
        {
            nameEd.setError(getString(R.string.name_cannot_be_empty));
            return false;
        }
        return true;
    }
}
