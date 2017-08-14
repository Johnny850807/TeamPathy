package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.ood.clean.waterball.teampathy.Presentation.Interfaces.BasePresenter;


public class BaseFragment extends Fragment {

    private BasePresenter.BaseView baseView;

    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BasePresenter.BaseView) {
            baseView = (BasePresenter.BaseView) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BasePresenter.BaseView");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        baseView = null;
    }

    public BasePresenter.BaseView getBaseView() {
        return baseView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy(); // clear all doing task records
        getBaseView().hideProgressBar();
        getBaseView().hideProgressDialog();
    }

    public void showAlertDialogFragment(DialogFragment dialogFragment) {
        dialogFragment.show(getFragmentManager(), "dialog");
    }
}
