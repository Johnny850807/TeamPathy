package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.OfficePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.OfficePresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.MemberIdCardItemBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfficeFragment extends BaseFragment implements OfficePresenter.OfficeView, SwipeRefreshLayout.OnRefreshListener, ExpandableListView.OnChildClickListener, ExpandableListView.OnGroupClickListener {
    private static final int WATCH_TODOLIST = 0;
    private static final int CHANGE_POSITION = 1;
    private static final int BOOT_MEMBER = 2;
    private static final int PROMOTE_AS_MANAGER = 0;
    private static final int DEMOTE_AS_MEMBER = 1;
    private static final int HANOVER_LEADER = 2;

    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    OfficeAdapter adapter;
    @Inject Member currentUser;
    @Inject OfficePresenterImp presenterImp;
    List<MemberIdCard> memberCardList = new ArrayList<>();
    String[] officeOptions;
    String[] changePositionOptions;
    AlertDialog officeFunctionDialog;
    Member selectedMember;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        officeOptions = getResources().getStringArray(R.array.office_options);
        changePositionOptions = getResources().getStringArray(R.array.change_position_options);
    }

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
        setupOfficeFunctionDialog();
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

    private void setupOfficeFunctionDialog() {
        officeFunctionDialog = TeamPathyDialogFactory.templateBuilder(getActivity())
                                .setTitle(R.string.select_actions_for_member)
                                .setView(createExpandableListView())
                                .create();
    }

    private ExpandableListView createExpandableListView(){
        final List<String> headers = Arrays.asList(officeOptions);
        final Map<String, List<String>> dataset = new HashMap<>();

        BaseExpandableListAdapter adapter = new ExpandableAdapter(getActivity(), headers, dataset);

        dataset.put(headers.get(WATCH_TODOLIST), Collections.<String>emptyList());
        dataset.put(headers.get(BOOT_MEMBER), Collections.<String>emptyList());

        ExpandableListView epListview = new ExpandableListView(getActivity());
        epListview.setGroupIndicator(null);
        epListview.setAdapter(adapter);

        expandAndAddPositionChangingHeaderIfLeader(headers, dataset, epListview);

        epListview.setOnGroupClickListener(this);
        epListview.setOnChildClickListener(this);
        return epListview;
    }

    private void expandAndAddPositionChangingHeaderIfLeader(List<String> headers,  Map<String, List<String>> dataset, ExpandableListView epListview){
        if (currentUser.getMemberDetails().getPosition() == Position.leader)
        {
            dataset.put(headers.get(CHANGE_POSITION),  Arrays.asList(changePositionOptions));
            adapter.notifyDataSetChanged();
            epListview.expandGroup(CHANGE_POSITION);
        }
    }

    @Override
    public void loadMemberCard(MemberIdCard memberIdCard) {
        memberCardList.add(memberIdCard);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMemberInfoUpdated(Member member) {
        for (MemberIdCard card : memberCardList)
            if (card.getUserId() == member.getUserId())
            {
                card.setMember(member);
                break;
            }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadFinish() {
        getBaseView().hideProgressBar();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onOperationTimeout(Throwable err) {
        TeamPathyDialogFactory.networkErrorDialogBuilder(getActivity()).setMessage(err.getMessage()).show();
    }

    @Override
    public void onMemberBootedCompleted(Member member) {
        for (int i = 0 ; i < memberCardList.size() ; i ++)
            if (memberCardList.get(i).getUserId() == member.getUserId())
            {
                memberCardList.remove(i);
                break;
            }

        adapter.notifyDataSetChanged();
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

    @Override
    public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long l) {
        switch (position)
        {
            case WATCH_TODOLIST:
                Log.d("office", "go watch todolist.");
                break;
            case BOOT_MEMBER:
                if (currentUser.getMemberDetails().getPosition() == Position.leader)
                {
                    Log.d("office", "booting currentUser.");
                    presenterImp.bootMember(selectedMember);
                }
                Log.d("office", "booting disabled, current user not a leader of the project.");
                break;
            case CHANGE_POSITION: // no action on clicking the header of change position
                return true;
        }
        officeFunctionDialog.dismiss();
        return true;
    }

    @Override
    public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
        if (groupPosition != CHANGE_POSITION)
            throw new IllegalStateException("The only group having the child options is at the position of " + CHANGE_POSITION + ", getting " + groupPosition);

        switch (childPosition)
        {
            case PROMOTE_AS_MANAGER:
                Log.d("office", "promoting the currentUser as a manager");
                presenterImp.changeMemberPosition(selectedMember, Position.manager);
                break;
            case DEMOTE_AS_MEMBER:
                Log.d("office", "demote the currentUser as a normal currentUser");
                presenterImp.changeMemberPosition(selectedMember, Position.member);
                break;
            case HANOVER_LEADER:
                Log.d("office", "the handover beginning of the position of the leader");
                presenterImp.leaderHandover(selectedMember);
                break;
            default:
                return false;
        }
        officeFunctionDialog.dismiss();
        return true;
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
                    selectedMember = memberIdCard.getMember();

                    // don't click own self
                    if (selectedMember.getUserId() != currentUser.getUserId())
                        officeFunctionDialog.show();

                    Log.d("office", "Card on click " + memberIdCard.getMember().getUser().getName());

                }
            });
        }

        @Override
        public int getItemCount() {
            return memberCardList.size();
        }
    }


    /**
     * This expandable adapter used for creating the expandablelistview within the alertdialog that
     * showing all the functionalities currentUser can do to the specified currentUser.
     */
    private class ExpandableAdapter extends BaseExpandableListAdapter {
        private Context context;
        private List<String> parent;
        private Map<String, List<String>> bindDataset;

        public ExpandableAdapter(Context context, List<String> listDataHeader,
                                   Map<String, List<String>> bindDataset) {
            this.context = context;
            this.parent = listDataHeader;
            this.bindDataset = bindDataset;
        }

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this.bindDataset.get(this.parent.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this.parent.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.bindDataset.get(this.parent.get(groupPosition))
                    .get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(25);
            textView.setPadding(12, 35, 12 ,35);
            String text = (String) getGroup(groupPosition);
            textView.setText(text);

            if ((groupPosition == CHANGE_POSITION || groupPosition == BOOT_MEMBER) && currentUser.getMemberDetails().getPosition() != Position.leader) // only leader can select this option
                textView.setEnabled(false);
            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(20);
            textView.setPadding(1, 5, 1 ,5);
            textView.setBackgroundColor(Color.parseColor("#FFDBF8FA"));
            textView.setTextColor(getResources().getColor(R.color.app_black_word_color));
            String text = (String) getChild(groupPosition, childPosition);
            textView.setText(text);
            return textView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

}
