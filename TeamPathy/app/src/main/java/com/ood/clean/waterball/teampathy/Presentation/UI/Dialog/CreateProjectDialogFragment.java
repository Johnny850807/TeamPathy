package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.ProjectsPresenterImp;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 2017/8/3.
 */

public class CreateProjectDialogFragment extends MakeSureToCancelBaseDialogFragment {
    @BindView(R.id.titleEd) TextInputEditText nameEd;
    @BindView(R.id.categoryEd) TextInputEditText categoryEd;
    @BindView(R.id.descriptionEd) TextInputEditText descriptionEd;
    @BindView(R.id.passwordEd) TextInputEditText passwordEd;
    @BindView(R.id.passwordChbx) CheckBox passwordChb;
    @Inject ProjectsPresenterImp presenterImp;

    //todo imageUrl should be fetched from the picture the user uploads instead of the null parameter.

    @Override
    protected Dialog onCustomDialogSetting(AlertDialog alertDialog) {
        alertDialog.setTitle(R.string.create_a_new_project);
        return alertDialog;
    }

    @Override
    protected View onViewCreated() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_project_dialog,null);
        bind(view);
        return view;
    }

    private void bind(View view) {
        ButterKnife.bind(this,view);
        MyApp.getUserComponent(getActivity()).inject(this);
    }

    @Override
    protected DialogInterface.OnClickListener getOnPositiveButtonClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEd.getText().toString();
                String type = categoryEd.getText().toString();
                boolean hasPassword = passwordChb.isChecked();
                String password = hasPassword ? passwordEd.getText().toString() : Project.NO_PASSWORD;
                String description = descriptionEd.getText().toString();
                if (isAvailable(name,type,description))
                {
                    presenterImp.create(new Project(name,type,description,null,password));
                    getBaseView().showProgressDialog();
                }

            }
        };
    }

    private boolean isAvailable(String name, String type, String description) {
        boolean error;
        if ( error = name.isEmpty() )
            nameEd.setError(getContext().getString(R.string.please_input_project_title));
        if ( error |= type.isEmpty() )
            categoryEd.setError(getContext().getString(R.string.please_input_project_type));
        if ( error |= description.isEmpty() )
            descriptionEd.setError(getContext().getString(R.string.please_input_project_description));
        return !error;
    }

}
