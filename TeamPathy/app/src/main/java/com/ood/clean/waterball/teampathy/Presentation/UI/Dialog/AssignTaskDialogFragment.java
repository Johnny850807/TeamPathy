package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.MemberIdCard;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.AssignTaskPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.BasePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.AssignTaskPresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.WbsConsolePresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.MemberIdCardItemBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.ood.clean.waterball.teampathy.MyUtils.MyLog.Log;


public class AssignTaskDialogFragment extends DialogFragment implements AssignTaskPresenter.AssignTaskView{
    private final static String TODOTASK = "todotask";
    private BasePresenter.BaseView baseView;
    private RecyclerView recyclerView;
    private MyAdpater adpater;
    private List<MemberIdCard> memberCardList = new ArrayList<>();
    private WbsConsolePresenterImp wbsPresenter;
    private TodoTask todoTask;
    @Inject AssignTaskPresenterImp assignTaskPresenter;

    public static AssignTaskDialogFragment getInstance(TodoTask todoTask){
        AssignTaskDialogFragment fragment = new AssignTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TODOTASK, todoTask);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setBaseView(BasePresenter.BaseView baseView) {
        this.baseView = baseView;
    }

    public void setWbsPresenter(WbsConsolePresenterImp wbsPresenter) {
        this.wbsPresenter = wbsPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoTask = (TodoTask) getArguments().getSerializable(TODOTASK);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = TeamPathyDialogFactory.templateBuilder(getActivity())
                .setTitle(getString(R.string.assign_task) + " - " + todoTask.getName())
                .setView(createView())
                .setPositiveButton(R.string.confirm, null)
                .create();
        assignTaskPresenter.setAssignTaskView(this);
        assignTaskPresenter.loadMemberCard();
        return dialog;
    }

    private View createView() {
        MyApp.getWbsComponent(getActivity()).inject(this);
        initView();

        return recyclerView;
    }

    private void initView(){
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setPadding(10,10,10,10);
        recyclerView.setAdapter(adpater = new MyAdpater());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void onLoadMember(MemberIdCard member) {
        memberCardList.add(member);
        adpater.notifyDataSetChanged();
    }

    private class MyAdpater extends RecyclerView.Adapter<BindingViewHolder>{

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
                    createDialogForAskingAssign(memberCardList.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return memberCardList.size();
        }
    }

    private void createDialogForAskingAssign(final MemberIdCard card) {
        if (todoTask.getStatus() == TodoTask.Status.assigned) // ask if the task has been assigned
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.are_u_sure_to_change_assigned_member)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            assignTaskAndDismiss(card);
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        else
            assignTaskAndDismiss(card);
    }


    private void assignTaskAndDismiss(MemberIdCard card){
        int memberId = card.getMember().getUser().getId();
        try {
            TodoTask copy = todoTask.clone();  // cannot update the original todotask before the operation succeed, so we need the copied one.
            copy.setAssignedId(memberId);
            copy.setAssignedUserImageUrl(card.getMember().getUser().getImageUrl());
            copy.setStatus(TodoTask.Status.assigned);
            WbsCommand command = WbsCommand.updateTaskItem(copy.getName(), copy);
            Log.d("wbs", new Gson().toJson(command));
            baseView.showProgressDialog();
            wbsPresenter.executeCommand(command);
            dismiss();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }
}
