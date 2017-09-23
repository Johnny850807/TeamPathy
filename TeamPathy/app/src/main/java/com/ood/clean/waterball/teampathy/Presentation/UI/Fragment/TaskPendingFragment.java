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
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

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

    private class ItemGroup extends ExpandableGroup<TodoTask>{
        public ItemGroup(String title, List<TodoTask> items) {
            super(title, items);
        }
    }

    private class ItemGroupViewHolder extends GroupViewHolder {
        public ItemGroupViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class ItemViewHolder extends ChildViewHolder {

        public ItemViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ReviewTaskAdapter extends ExpandableRecyclerViewAdapter<ItemGroupViewHolder, ItemViewHolder> {

        public ReviewTaskAdapter(List<? extends ExpandableGroup> groups) {
            super(groups);
        }

        @Override
        public ItemGroupViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public ItemViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int i) {
            return null;
        }

        @Override
        public void onBindChildViewHolder(ItemViewHolder itemViewHolder, int i, ExpandableGroup expandableGroup, int i1) {

        }

        @Override
        public void onBindGroupViewHolder(ItemGroupViewHolder itemGroupViewHolder, int i, ExpandableGroup expandableGroup) {

        }
    }
}
