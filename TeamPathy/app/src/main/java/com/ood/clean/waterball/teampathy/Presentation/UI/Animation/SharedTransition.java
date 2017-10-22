package com.ood.clean.waterball.teampathy.Presentation.UI.Animation;

import android.annotation.TargetApi;
import android.os.Build;
import android.transition.ChangeBounds;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;



@TargetApi(Build.VERSION_CODES.KITKAT)
public class SharedTransition extends TransitionSet {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SharedTransition() {
        setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        addTransition(new ChangeBounds()).addTransition(new ChangeTransform());

    }

}
