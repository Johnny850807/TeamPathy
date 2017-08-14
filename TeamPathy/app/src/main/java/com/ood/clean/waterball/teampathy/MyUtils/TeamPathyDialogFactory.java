package com.ood.clean.waterball.teampathy.MyUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.ood.clean.waterball.teampathy.R;

public class TeamPathyDialogFactory {

    public static ProgressDialog createProgressDialog(Context context){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setIcon(R.drawable.logo_icon);
        progressDialog.setMessage(context.getString(R.string.loading));
        return progressDialog;
    }

    public static AlertDialog.Builder templateBuilder(Context context){
        return new AlertDialog.Builder(context)
                .setIcon(R.drawable.logo_icon);
    }


}
