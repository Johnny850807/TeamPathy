package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskPendingFragment extends BaseFragment {
    @BindView(R.id.recyclerview) RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_taskpending_page,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        bind(view);
    }

    private void init(){

    }

    private void bind(View view) {
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
    }

    private class Group extends ExpandableGroup<TodoTask>{

        public Group(String title, List<TodoTask> items) {
            super(title, items);
        }
    }

}
