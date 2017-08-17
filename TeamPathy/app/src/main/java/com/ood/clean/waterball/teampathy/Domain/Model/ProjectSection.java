package com.ood.clean.waterball.teampathy.Domain.Model;

/*
* An enum defines all the sections of the project system in TeamPathy.
 */

import android.content.IntentFilter;

public enum ProjectSection {
    /** Top four sections define the order of the tabs **/
    TIMELINE("postTimeline","putTimeline","deleteTimeline"),
    FORUM("postIssue","putIssue","deleteIssue"),
    TODOLIST,
    OFFICE,
    ISSUECOMMENT("postComment","putComment","deleteComment");

    private IntentFilter intentFilter = new IntentFilter();

    private ProjectSection(String ...actions){
        for (String action : actions)
            intentFilter.addAction(action);
    }

    public IntentFilter getIntentFilter(){
        return intentFilter;
    }
}
