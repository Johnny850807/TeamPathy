package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.ood.clean.waterball.teampathy.Domain.Model.Issue;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.IssuePresenterImp;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateIssueDialogFragment extends MakeSureToCancelBaseDialogFragment {
    @BindView(R.id.titleEd) TextInputEditText titleTxt;
    @BindView(R.id.contentEd) TextInputEditText contentTxt;
    @BindView(R.id.errorTxt) TextView errorTxt;
    @BindView(R.id.categorySpinner) Spinner categoryIssueSpinner;

    @Inject IssuePresenterImp presenterImp;
    @Inject Project project;
    @Inject User user;

    @Override
    protected Dialog onCustomDialogSetting(AlertDialog alertDialog) {
        alertDialog.setTitle(R.string.CreateIssue);
        return alertDialog;
    }

    @Override
    protected View onViewCreated() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_issue_dialog,null);
        bind(view);
        setupSpinner();
        return view;
    }

    @Override
    protected View.OnClickListener getOnPositiveButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInputFormatAvailable())
                {
                    String title = titleTxt.getText().toString();
                    String content = contentTxt.getText().toString();
                    String category = categoryIssueSpinner.getSelectedItem().toString();
                    Issue issue = new Issue(user,title,content,category);
                    getBaseView().showProgressDialog();
                    presenterImp.create(issue);
                    dismiss();
                }
            }
        };
    }

    private void bind(View view){
        ButterKnife.bind(this,view);
        MyApp.getProjectComponent(getActivity()).inject(this);
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1
                ,project.getIssueCategoryList());
        categoryIssueSpinner.setAdapter(adapter);
    }



    private boolean checkInputFormatAvailable(){
        boolean hasError;
        if ( hasError = titleTxt.getText().toString().isEmpty() )
            titleTxt.setError(getContext().getString(R.string.title_cannot_be_empty));
        if ( hasError |= contentTxt.getText().toString().isEmpty() )
            contentTxt.setError(getContext().getString(R.string.issue_content_cannot_be_empty));
        else
            errorTxt.setVisibility(View.GONE);
        return !hasError;
    }
}
