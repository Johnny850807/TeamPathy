package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.WbsConsolePresenter;
import com.ood.clean.waterball.teampathy.R;

import butterknife.ButterKnife;


public class CreateTodoTaskDialogFragment extends MakeSureToCancelBaseDialogFragment{
    private static final String PARENT = "ParentName";
    private WbsConsolePresenter wbsConsolePresenter;
    private String parentName;

    public static CreateTodoTaskDialogFragment newInstance(String parentName){
        CreateTodoTaskDialogFragment fragment = new CreateTodoTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARENT, parentName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setWbsConsolePresenter(WbsConsolePresenter wbsConsolePresenter) {
        this.wbsConsolePresenter = wbsConsolePresenter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentName = getArguments().getString(PARENT);
    }

    @Override
    protected Dialog onCustomDialogSetting(AlertDialog alertDialog) {
        alertDialog.setTitle(getString(R.string.create_todotask));
        return alertDialog;
    }

    @Override
    protected View onViewCreated() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_task_item_dialog,null);
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

            }
        };
    }

}
