package com.ood.clean.waterball.teampathy.MyUtils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.Transition;
import android.util.Log;
import android.view.View;

import com.ood.clean.waterball.teampathy.Presentation.UI.Animation.SharedTransition;

import java.util.ArrayList;
import java.util.List;


public abstract class PageController {
    protected Context context;
    protected FragmentManager fragmentManager;
    protected int targetContainerId;
    private List<TransitionBag> transitionBags = new ArrayList<>();

    public PageController(Context context, FragmentManager fragmentManager , int targetContainerId){
        this.fragmentManager = fragmentManager;
        this.targetContainerId = targetContainerId;
        this.context = context;
    }


    public void changePage(Fragment fragment, @Nullable Transition enterTransition , @Nullable Transition exitTransition,
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

        if (!transitionBags.isEmpty())
        {
            Transition sharedTransition = new SharedTransition();
            fragment.setSharedElementEnterTransition(sharedTransition);
            for (TransitionBag bag : transitionBags)
                fragmentTransaction.addSharedElement(bag.sharedElement, bag.secondSharedViewName);
            transitionBags.clear();
        }

        if(addToBackStack)
            fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();

    }

    /**
     * @param transitionBag the sharedElement to be added, when the sharedElement commited, it will be null.
     */
    public PageController addSharedElement(TransitionBag transitionBag){
        transitionBags.add(transitionBag);
        return this;
    }

    public static class TransitionBag{
        public View sharedElement;
        public String secondSharedViewName;

        public TransitionBag(View sharedElement, String secondSharedViewName) {
            this.sharedElement = sharedElement;
            this.secondSharedViewName = secondSharedViewName;
        }
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
