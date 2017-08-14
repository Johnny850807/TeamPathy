package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.OfficePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.OfficePresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.MemberIdCardItemBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ood.clean.waterball.teampathy.MyUtils.MyLog.Log;

public class OfficeFragment extends BaseFragment implements OfficePresenter.OfficeView, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    OfficeAdapter adapter;
    @Inject OfficePresenterImp presenterImp;
    List<MemberIdCard> memberCardList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.fragment_office_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getBaseView().showProgressBar();
        binding(view);
        setupRecyclerview();
        presenterImp.setOfficeView(this);
        presenterImp.loadAllMemberCards();
    }

    private void binding(View view){
        ButterKnife.bind(this,view);
        MyApp.getProjectComponent(getActivity()).inject(this);
    }

    private void setupRecyclerview() {
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new OfficeAdapter();
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void loadMemberCard(MemberIdCard memberIdCard) {
        memberCardList.add(memberIdCard);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFinish() {
        getBaseView().hideProgressBar();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        getBaseView().showProgressBar();
        memberCardList.clear();
        presenterImp.loadAllMemberCards();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenterImp.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenterImp.onDestroy();
    }

    public class OfficeAdapter extends RecyclerView.Adapter<BindingViewHolder>{

        @Override
        public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            MemberIdCardItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                    R.layout.member_id_card_item,parent,false);
            return new BindingViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(BindingViewHolder holder, int position) {
            Object obj = memberCardList.get(position);
            holder.bind(obj);
            setOnClickListener(holder.itemView, position);
        }

        private void setOnClickListener(View view, final int position){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MemberIdCard memberIdCard = memberCardList.get(position);
                    Log("Card on click " + memberIdCard.getMember().getUser().getName());
                    //todo when card on click, scale to show the details of member
                }
            });
        }

        @Override
        public int getItemCount() {
            return memberCardList.size();
        }
    }

}
