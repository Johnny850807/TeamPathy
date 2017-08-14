package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.ood.clean.waterball.teampathy.Domain.Model.IssueComment;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.IssueDetailsPresenterImp;
import com.ood.clean.waterball.teampathy.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateIssueCommentDialogFragment extends MakeSureToCancelBaseDialogFragment {
    @BindView(R.id.contentEd) TextInputEditText contentEd;
    @Inject User user;
    @Inject IssueDetailsPresenterImp presenterImp;

    @Override
    protected View onViewCreated() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_issue_comment_dialog,null);
        bind(view);
        return view;
    }

    private void bind(View view){
        ButterKnife.bind(this,view);
        MyApp.getIssueComponent(getActivity()).inject(this);
    }

    @Override
    protected Dialog onCustomDialogSetting(AlertDialog alertDialog) {
        alertDialog.setTitle(R.string.create_issue_comment);
        return alertDialog;
    }

    @Override
    protected DialogInterface.OnClickListener getOnPositiveButtonClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkInputValid())
                    createComment();
            }
        };
    }

    private boolean checkInputValid() {
        if (contentEd.getText().toString().isEmpty())
        {
            contentEd.setError(getActivity().getString(R.string.issue_comment_content_cannot_be_empty));
            return false;
        }
        return true;
    }

    private void createComment() {
        String content = contentEd.getText().toString();
        getBaseView().showProgressDialog();
        presenterImp.create(new IssueComment(user, content));
    }

}
