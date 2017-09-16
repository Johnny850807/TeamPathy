package com.ood.clean.waterball.teampathy.Presentation.UI.Factory;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.ood.clean.waterball.teampathy.Domain.DI.Scope.ProjectScope;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskItem;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TaskEventVisitor;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.TaskAnalyticsItemBinding;

import org.apmem.tools.layouts.FlowLayout;

import javax.inject.Inject;

@ProjectScope
public class TaskItemViewFactory {
    private Context context;
    private TaskEventVisitor onClickEventVisitor;
    private TaskEventVisitor onLongClickEventVisitor;
    private FlowLayout flowLayout;

    @Inject
    public TaskItemViewFactory(Context context) {
        this.context = context;
    }

    public void setOnClickEventVisitor(TaskEventVisitor OnClickEventVisitor) {
        this.onClickEventVisitor = OnClickEventVisitor;
    }

    public void setOnLongClickEventVisitor(TaskEventVisitor OnLongClickEventVisitor) {
        this.onLongClickEventVisitor = OnLongClickEventVisitor;
    }

    public void setFlowLayout(FlowLayout flowLayout) {
        this.flowLayout = flowLayout;
    }

    public View createItemView(@NonNull TaskItem taskItem){
        LayoutInflater inflater = LayoutInflater.from(context);
        TaskAnalyticsItemBinding binding = DataBindingUtil.inflate(inflater
                , R.layout.task_analytics_item, flowLayout, false);

        binding.setTask(taskItem);
        View view = binding.getRoot();
        setTaskViewOnClick(view,taskItem);
        setTaskViewOnLongClick(view,taskItem);

        return binding.getRoot();
    }

    public void setTaskViewOnClick(View view, final TaskItem taskItem) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskItem.acceptEventVisitor(onClickEventVisitor);
            }
        });
    }

    public void setTaskViewOnLongClick(View view, final TaskItem taskItem) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                taskItem.acceptEventVisitor(onLongClickEventVisitor);
                return false;
            }
        });
    }


}
