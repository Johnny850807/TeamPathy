package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.User;
import com.ood.clean.waterball.teampathy.Domain.UseCase.Project.JoinProject;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.BasePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.SearchProjectView;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.ProjectsPresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.SearchProjectDialogPresenterImp;
import com.ood.clean.waterball.teampathy.Presentation.UI.Adapter.BindingViewHolder;
import com.ood.clean.waterball.teampathy.R;
import com.ood.clean.waterball.teampathy.databinding.SearchProjectItemBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SearchProjectDialogFragment extends DialogFragment  implements SearchProjectView {
    @BindView(R.id.searchEd) TextInputEditText searchEd;
    @BindView(R.id.recyclerview) RecyclerView recyclerView;
    @Inject ProjectsPresenterImp projectPresenterImp;
    @Inject SearchProjectDialogPresenterImp searchProjectPresenterImp;
    @Inject User user;

    private SearchProjectDialogFragment.ProjectSearchResultAdapter adapter;
    private List<Project> searchProjectResultList = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return TeamPathyDialogFactory.templateBuilder(getActivity())
                .setTitle(R.string.search_projects)
                .setView(createView())
                .setPositiveButton(R.string.confirm, null)
                .create();
    }

    private View createView(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_project_dialog,null);
        bind(view);
        initPresenters();
        setupRecyclerView();
        return view;
    }

    private void bind(View view) {
        ButterKnife.bind(this,view);
        MyApp.getUserComponent(getActivity()).inject(this);
    }

    private void initPresenters() {
        searchProjectPresenterImp.setSearchProjectView(this);
        searchProjectPresenterImp.setProjectView(projectPresenterImp.getProjectsView());
    }

    private void setupRecyclerView() {
        adapter = new SearchProjectDialogFragment.ProjectSearchResultAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.searchBtn)
    public void onSearchButtonClick(View view){
        if (checkValid())
        {
            getBaseView().showProgressDialog();
            searchProjectResultList.clear();
            searchProjectPresenterImp.searchProjectByName(searchEd.getText().toString());
        }
    }

    private boolean checkValid() {
        if (searchEd.getText().toString().isEmpty()) {
            searchEd.setError(getString(R.string.please_input_search_keyword));
            return false;
        }
        return true;
    }

    @Override
    public void loadEntity(Project project) {
        searchProjectResultList.add(project);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchFinish() {
        getBaseView().hideProgressDialog();
    }

    @Override
    public void onProjectPasswordInvalid() {
        getBaseView().hideProgressDialog();
        TeamPathyDialogFactory.templateBuilder(getContext())
                .setMessage(R.string.password_wrong)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

    public class ProjectSearchResultAdapter extends RecyclerView.Adapter<BindingViewHolder>{
        public EventHandler eventHandler =
                new EventHandler();

        @Override
        public BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            SearchProjectItemBinding binding = DataBindingUtil.inflate(inflater,R.layout.search_project_item,parent,false);
            return new BindingViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(BindingViewHolder holder, int position) {
            holder.bind(searchProjectResultList.get(position));
            ((SearchProjectItemBinding)holder.getBinding()).setHandler(eventHandler);
        }

        @Override
        public int getItemCount() {
            return searchProjectResultList.size();
        }

        //for binding use to handle the join event
        public class EventHandler{

            public boolean hasProjectBeenJoined(Project project){
                return user.hasJoinedTo(project);
            }

            public void joinProjectOnClick(Project project){
                if (hasProjectBeenJoined(project))
                    Toast.makeText(getContext(), R.string.project_has_been_joined, Toast.LENGTH_SHORT).show();
                else
                {
                    if (project.hasPassword())
                        createDialogForInputingPassword(project);
                    else
                    {
                        getBaseView().showProgressDialog();
                        joinProject(project, Project.NO_PASSWORD);
                    }
                }
            }

            private void createDialogForInputingPassword(final Project project) {
                final EditText passwordEd = new EditText(getContext());
                passwordEd.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                passwordEd.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                ));

                ViewGroup viewGroup = new RelativeLayout(getContext());
                viewGroup.setPadding(15,40,15,40);
                viewGroup.addView(passwordEd);

                TeamPathyDialogFactory.templateBuilder(getContext())
                        .setTitle(R.string.input_project_password)
                        .setView(viewGroup)
                        .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String password = passwordEd.getText().toString().trim();
                                joinProject(project, password);
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
            }

            private void joinProject(Project project, String password){
                getBaseView().showProgressDialog();
                JoinProject.Params params = new JoinProject.Params(project,password);
                searchProjectPresenterImp.joinProject(params);
            }

        }
    }

    private BasePresenter.BaseView getBaseView(){
        return (BasePresenter.BaseView) getActivity();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        searchProjectPresenterImp.onDestroy();
    }
}
