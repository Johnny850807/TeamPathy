package com.ood.clean.waterball.teampathy.Presentation.UI.Fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.ood.clean.waterball.teampathy.Domain.Model.Member.Member;
import com.ood.clean.waterball.teampathy.Domain.Model.Member.Position;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.ProjectsPresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.ProjectsPresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.CreateProjectDialogFragment;
import com.ood.clean.waterball.teampathy.Presentation.UI.Dialog.SearchProjectDialogFragment;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.ProjectItemBinding;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ProjectsFragment extends BaseFragment implements ProjectsPresenter.ProjectView, SwipeRefreshLayout.OnRefreshListener {
    private final int CREATE_PROJECT_ACTION = 0;
    private final int ATTEND_PROJECT_ACTION = 1;
    private String[] projectActions;  //  action list like : createProgressDialog a project, search ...

    GridLayoutManager gridLayoutManager;
    ProjectsAdapter adapter;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @BindView(R.id.swiperefreshlayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.fab) FloatingActionButton fab;
    @Inject ProjectsPresenterImp presenter;
    @Inject User user;

    ProgressDialog progressDialog;
    SearchProjectDialogFragment searchProjectDialogFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject();
    }

    private void inject() {
        MyApp.getUserComponent(getActivity())
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_page, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init();
        ButterKnife.bind(this, view);
        setupRecyclerView();
    }

    private void init() {
        presenter.setProjectsView(this);
        projectActions = new String[]{getString(R.string.create_a_new_project), getString(R.string.participate_to_exists_project)};
        progressDialog = TeamPathyDialogFactory.createProgressDialog(getContext());
    }

    private void setupRecyclerView() {
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new ProjectsAdapter();
        gridLayoutManager = new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.fab)
    public void showDialogForProjectActions(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,
                projectActions);
        new AlertDialog.Builder(getContext())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        switch (position){
                            case CREATE_PROJECT_ACTION:
                                createDialogForCreatingNewProject();
                                break;
                            case ATTEND_PROJECT_ACTION:
                                createDialogForSearchingProjects();
                        }
                    }
                })
                .show();
    }

    private void createDialogForCreatingNewProject(){
        showAlertDialogFragment(new CreateProjectDialogFragment());
    }

    private void createDialogForSearchingProjects(){
        showAlertDialogFragment(searchProjectDialogFragment = new SearchProjectDialogFragment());
    }

    @Override
    public void loadEntity(Project project) {
        user.getProjectList().add(project);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateFinishNotify(Project project) {
        getBaseView().hideProgressDialog();
        createProjectComponentAndAccess(project);
    }

    private void createProjectComponentAndAccess(Project project){
        // project creator also a leader
        Member member = Member.create(user,Position.leader);
        MyApp.createProjectComponent(getActivity(),project,member);
        getBaseView().getPageController().changePage(new TabLayoutFragment());
    }

    @Override
    public void onJoinProjectFinish(Project project) {
        getBaseView().hideProgressDialog();
        if (searchProjectDialogFragment != null)
            searchProjectDialogFragment.dismiss();

        // join to the project should be a member at first
        Member member = Member.create(user, Position.member);
        MyApp.createProjectComponent(getActivity(),project,member);
        getBaseView().getPageController().changePage(new TabLayoutFragment());
    }

    @Override
    public void onDeleteFinishNotify(Project project) {
        getBaseView().hideProgressDialog();
    }

    @Override
    public void onUpdateFinishNotify(Project project) {
        getBaseView().hideProgressDialog();
    }

    @Override
    public void onLoadFinishNotify() {
        getBaseView().hideProgressBar();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onMemberInfoLoadFinsih(Project project, Member member) {
        getBaseView().hideProgressDialog();
        MyApp.createProjectComponent(getActivity(),project,member);
        getBaseView().getPageController().changePage(new TabLayoutFragment());
    }

    @Override
    public void onPageIndexOutOfBound() {}

    @Override
    public void onOperationTimeout(Throwable err) {
        getBaseView().hideProgressDialog();
        Toast.makeText(getContext(),err.getMessage(),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        user.getProjectList().clear();
        presenter.loadEntities(0);  // No page needed, all projects will be loaded once the user sign in.
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
        getBaseView().finish();
    }

    public class ProjectsAdapter extends RecyclerView.Adapter<BindingViewHolder> {

        @Override
        public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater =
                    LayoutInflater.from(parent.getContext());
            ProjectItemBinding binding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.project_item , parent, false);
            return new BindingViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(BindingViewHolder holder, int position) {
            Object obj = user.getProjectList().get(position);
            holder.bind(obj);
            setOnClickListener(holder.itemView, position);
        }

        private void setOnClickListener(View view, final int position){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Project project = user.getProjectList().get(position);
                    getBaseView().showProgressDialog();
                    presenter.loadMemberInfo(project);
                }
            });
        }

        @Override
        public int getItemCount() {
            return user.getProjectList().size();
        }
    }

}
