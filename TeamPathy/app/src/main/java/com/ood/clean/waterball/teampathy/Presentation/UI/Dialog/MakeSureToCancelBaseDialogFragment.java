package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;

import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.BasePresenter;
import com.ood.clean.waterball.teampathy.R;

public abstract class MakeSureToCancelBaseDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = createBaseBuilder().create();
        alertDialog = setupMakeSureToCancelDialog(alertDialog);
        return onCustomDialogSetting(alertDialog);
    }

    private AlertDialog.Builder createBaseBuilder(){
        return TeamPathyDialogFactory.templateBuilder(getActivity())
                .setView(onViewCreated())
                .setPositiveButton(getPositiveButtonText(), null)
                .setNegativeButton(getNegativeButtonText(), null);
    }

    protected String getPositiveButtonText(){
        return getString(R.string.confirm);
    }

    protected String getNegativeButtonText(){
        return getString(R.string.cancel);
    }

    private AlertDialog setupMakeSureToCancelDialog(final AlertDialog alertDialog){
        // get the positive button during onShow to avoid auto dismiss.
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button negativeBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                Button positiveBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                // setting maually to avoid auto dismissing
                negativeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // make sure to cancel dialog showing
                        TeamPathyDialogFactory.templateBuilder(getActivity())
                                .setMessage(R.string.make_sure_to_cancel)
                                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, null)
                                .show();
                    }
                });

                positiveBtn.setOnClickListener(getOnPositiveButtonClickListener());
            }
        });
        return alertDialog;
    }

    public BasePresenter.BaseView getBaseView(){
        return (BasePresenter.BaseView) getActivity();
    }


    // the phase to do further setting like : setTitle(..) to the dialog
    protected abstract Dialog onCustomDialogSetting(AlertDialog alertDialog);

    // the phase to create and return the custom view to the dialog
    protected abstract View onViewCreated();

    // the phase to decide which to do when the click event to the positive button
    protected abstract View.OnClickListener getOnPositiveButtonClickListener();

}
