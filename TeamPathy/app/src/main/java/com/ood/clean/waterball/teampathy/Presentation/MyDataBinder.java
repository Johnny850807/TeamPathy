package com.ood.clean.waterball.teampathy.Presentation;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ood.clean.waterball.teampathy.Domain.Model.Timeline;
import com.ood.clean.waterball.teampathy.MyUtils.GlideHelper;
import com.ood.clean.waterball.teampathy.R;

import us.feras.mdv.MarkdownView;


/**
 * The binder to help Android bind data to the view.
 */
public final class MyDataBinder {

    private MyDataBinder() {
        //NO-OP
    }

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).fitCenter().into(view);
    }

    @BindingAdapter("circleImageUrl")
    public static void loadCircleImage(ImageView view, String url) {
        GlideHelper.loadToCircularImage(view.getContext(),view,url);
    }

    @BindingAdapter("markdownContent")
    public static void loadMarkdownView(MarkdownView view, String content) {
        view.loadMarkdown(content);
    }

    @BindingAdapter("timelineContent")
    public static void bindTimelineContent(TextView textView, Timeline timeline) {
        Resources resources = textView.getResources();
        if (timeline.getCategory() == Timeline.Category.jotting)
            textView.setText(timeline.getContent());
        else
        {
            String content = null;
            switch (timeline.getContent())
            {
                case "task_assigned":
                    content = resources.getString(R.string.task_assigned, timeline.getPoster().getName(), timeline.getComplement(), timeline.getObj());
                    break;
                case "task_doing":
                    content = resources.getString(R.string.task_doing, timeline.getPoster().getName(), timeline.getObj());
                    break;
                case "task_undoing":
                    content = resources.getString(R.string.task_undoing, timeline.getPoster().getName(), timeline.getObj());
                    break;
                case "task_commit":
                    content = resources.getString(R.string.task_commit, timeline.getPoster().getName(), timeline.getObj());
                    break;
                case "task_uncommit":
                    content = resources.getString(R.string.task_uncommit, timeline.getPoster().getName(), timeline.getObj());
                    break;
                case "task_pass":
                    content = resources.getString(R.string.task_pass, timeline.getPoster().getName(), timeline.getObj());
                    break;
                case "task_reject":
                    content = resources.getString(R.string.task_reject, timeline.getPoster().getName(), timeline.getObj());
                    break;
                case "task_delete":
                    content = resources.getString(R.string.task_delete, timeline.getPoster().getName(), timeline.getObj());
                    break;
                case "task_create":
                    content = resources.getString(R.string.task_create, timeline.getPoster().getName(), timeline.getObj());
                    break;
                case "notify_issue_post":
                    content = resources.getString(R.string.notify_issue_post, timeline.getPoster().getName(), timeline.getComplement());
                    break;
                case "notify_issue_comment_post":
                    content = resources.getString(R.string.notify_issue_comment_post, timeline.getPoster().getName(), timeline.getComplement());
                    break;
                case "notify_edit_project_info":
                    content = resources.getString(R.string.notify_edit_project_info, timeline.getPoster().getName());
                    break;
                case "notify_handover_leader":
                    content = resources.getString(R.string.notify_handover_leader, timeline.getPoster().getName(), timeline.getComplement());
                    break;
                case "notify_position_change":
                    content = resources.getString(R.string.notify_position_change, timeline.getPoster().getName(), timeline.getObj());
                    break;
                case "notify_member_join":
                    content = resources.getString(R.string.notify_member_join, timeline.getPoster().getName());
                    break;
                case "notify_member_boot":
                    content = resources.getString(R.string.notify_member_boot, timeline.getPoster().getName());
                    break;
                default:
                    throw new IllegalStateException("No matched timeline action : got " + timeline.getContent());
            }
            textView.setText(content);
        }

    }
}