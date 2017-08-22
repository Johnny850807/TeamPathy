package com.ood.clean.waterball.teampathy.Framework.Firebase;

import android.content.Context;
import android.support.annotation.StringRes;

import com.ood.clean.waterball.teampathy.R;

import java.util.Map;


public class NotificationParser {
    private Context context;
    private static final String EVENTTYPE = "eventType";

    public NotificationParser(Context context) {
        this.context = context;
    }


    /* Define the title and the message body of the nofitication,
     * Title phrase : {where : optional} - {who} {action} {entity : optional}
     * Message phrase : {content}
     * **/
    public MyFirebaseMessagingService.NotificationModel parseModel(Map<String,String> data){
        String event = data.get(EVENTTYPE).toLowerCase();
        String where = parseWhereByData(data);
        String who = parseWhoByData(data);
        String action = parseOperationByEvent(event);
        String entity = parseEntityByEvent(event);
        String content = parseContentByData(data);
        String title = where + who + action + entity;

        return new MyFirebaseMessagingService.NotificationModel(title, content);
    }

    private String parseWhereByData(Map<String,String> data) {
        //todo server side doesn't send a projectName as a data key back
        if (data.containsKey("projectName"))
            return data.get("projectName") + "-";
        return "";
    }

    private String parseWhoByData(Map<String,String> data) {
        if (data.containsKey("userName"))
            return data.get("userName");
        return data.get("posterName");
    }

    private String parseEntityByEvent(String event){
        if (event.contains("timeline"))
            return getString(R.string.timeline);
        if (event.contains("issue"))
            return getString(R.string.issue);
        if (event.contains("comment"))
            return getString(R.string.issueComment);
        return "";
    }

    // content -> date
    private String parseContentByData(Map<String, String> data) {
        if (data.containsKey("content"))
            return data.get("content");
        for (String key : data.keySet())
            if (key.contains("date"))
                return data.get(key);
        return "";
    }

    private String parseOperationByEvent(String event){
        if (event.contains("post"))
            return getString(R.string.postAction);
        if (event.contains("put"))
            return getString(R.string.putAction);
        if (event.contains("delete"))
            return getString(R.string.deleteAction);
        if (event.contains("sign"))
        {
            if (event.contains("in"))
                return getString(R.string.signInAction);
            return getString(R.string.signOutAction);
        }
        return "";
    }

    private String getString(@StringRes int id){
        return context.getString(id);
    }

}
