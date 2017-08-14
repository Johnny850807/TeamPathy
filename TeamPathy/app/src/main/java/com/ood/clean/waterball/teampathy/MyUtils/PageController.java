package com.ood.clean.waterball.teampathy.MyUtils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.util.Log;


public abstract class PageController {
    protected Context context;
    protected FragmentManager fragmentManager;
    protected int targetContainerId;

    public PageController(Context context, FragmentManager fragmentManager , int targetContainerId){
        this.fragmentManager = fragmentManager;
        this.targetContainerId = targetContainerId;
        this.context = context;
    }


    public void changePage(Fragment fragment,@Nullable Transition enterTransition ,@Nullable Transition exitTransition,
                           boolean addToBackStack){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String name = fragment.getClass().getName();

        fragmentTransaction.replace(targetContainerId , fragment , name);
        Log.d("PageController","Switch Page to " + name);

        if (enterTransition != null)
            fragment.setEnterTransition(enterTransition);
        else
            fragment.setEnterTransition(getEnterTransition());
        if (exitTransition != null)
            fragment.setExitTransition(exitTransition);
        else
            fragment.setExitTransition(getExitTransition());

        if(addToBackStack)
            fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();
    }

    public void changePage(Fragment fragment,@Nullable Transition enterTransition ,@Nullable Transition exitTransition){
        changePage(fragment,enterTransition,exitTransition,true);
    }

    public void changePage(Fragment fragment){
        Transition enterTransition = null;
        Transition exitTransition = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            enterTransition = getEnterTransition();
            exitTransition = getExitTransition();
        }

        changePage(fragment,enterTransition,exitTransition);
    }

    protected abstract Transition getEnterTransition();
    protected abstract Transition getExitTransition();

}
