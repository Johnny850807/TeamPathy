package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;
import com.ood.clean.waterball.teampathy.Domain.Model.ReviewTaskCard;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ReviewTaskPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.ReviewTaskPresenterImp;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.ReviewCardContentItemBinding;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class TaskPendingFragment extends BaseFragment implements ReviewTaskPresenter.ReviewTaskView{
    @Inject ReviewTaskPresenterImp presenterImp;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    ReviewTaskAdapter adapter;
    List<ReviewTaskCard> reviewTaskCards = new ArrayList<>();

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
        presenterImp.setReviewTaskView(this);
        presenterImp.loadReviewTaskCard();
    }

    private void init(){

    }

    private void bind(View view) {
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
    }

    @Override
    public void onLoadReviewTaskCard(ReviewTaskCard card) {
        reviewTaskCards.add(card);
    }

    @Override
    public void onReviewTasksLoadComplete() {
        initRecyclerView();
    }

    private void initRecyclerView(){
        List<ItemGroup> groups = new ArrayList<>();
        for (ReviewTaskCard card : reviewTaskCards)
            groups.add(new ItemGroup(card.getCommiter().getName(), card.getPendingTasks()));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new ReviewTaskAdapter(groups);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private class ItemGroup extends ExpandableGroup<TodoTask>{
        public ItemGroup(String title, List<TodoTask> items) {
            super(title, items);
        }
    }

    private class ItemGroupViewHolder extends GroupViewHolder {
        private ViewDataBinding binding;
        public ItemGroupViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object obj){
            binding.setVariable(BR.obj, obj);
            binding.executePendingBindings();
        }
    }

    private class ItemViewHolder extends ChildViewHolder {
        private ViewDataBinding binding;
        public ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Object obj){
            binding.setVariable(BR.obj, obj);
            binding.executePendingBindings();
        }
    }

    public class ReviewTaskAdapter extends ExpandableRecyclerViewAdapter<ItemGroupViewHolder, ItemViewHolder> {

        public ReviewTaskAdapter(List<? extends ExpandableGroup> groups) {
            super(groups);
        }

        @Override
        public ItemGroupViewHolder onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
            ReviewCardContentItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.review_card_commiter_group_item, viewGroup, false);
            return new ItemGroupViewHolder(binding);
        }

        @Override
        public ItemViewHolder onCreateChildViewHolder(ViewGroup viewGroup, int i) {
            ReviewCardContentItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()),
                    R.layout.review_card_content_item, viewGroup, false);
            return new ItemViewHolder(binding);
        }

        @Override
        public void onBindChildViewHolder(ItemViewHolder itemViewHolder, int flatPosition, ExpandableGroup expandableGroup, int childIndex) {
            ItemGroup group = (ItemGroup) expandableGroup;
            TodoTask todoTask = group.getItems().get(childIndex);
            itemViewHolder.bind(todoTask);
        }

        @Override
        public void onBindGroupViewHolder(ItemGroupViewHolder itemGroupViewHolder, int flatPosition, ExpandableGroup expandableGroup) {
            User user = reviewTaskCards.get(flatPosition).getCommiter();
            itemGroupViewHolder.bind(user);
        }
    }
}
