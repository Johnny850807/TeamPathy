package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.TodoTask;
import com.ood.clean.waterball.teampathy.Domain.Model.WBS.WbsCommand;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.EnglishAbbrDateConverter;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Interfaces.BasePresenter;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.WbsConsolePresenterImp;
import com.ood.clean.waterball.teampathy.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateTodoTaskDialogFragment extends MakeSureToCancelBaseDialogFragment{
    private static final String ALL_TASKS = "AllTask";
    private static final String PARENT = "ParentName";
    private List<String> allTaskNames = new ArrayList<>();
    private String parentName;
    private BasePresenter.BaseView baseView;
    @Inject WbsConsolePresenterImp wbsConsolePresenterImp;
    @Inject Project project;
    @BindView(R.id.startDateBtn) Button startDateBtn;
    @BindView(R.id.endDateBtn) Button endDateBtn;
    @BindView(R.id.dependencySpin) Spinner dependencySpinner;
    @BindView(R.id.nameEd) TextInputEditText nameEd;
    @BindView(R.id.contributionEd) TextInputEditText contributionEd;
    @BindView(R.id.descriptionEd) TextInputEditText descriptionEd;

    public static CreateTodoTaskDialogFragment newInstance(@NonNull ArrayList<TodoTask> todotasks, @NonNull String parentName){
        CreateTodoTaskDialogFragment fragment = new CreateTodoTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ALL_TASKS, todotasks);
        bundle.putString(PARENT, parentName);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setBaseView(BasePresenter.BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        ArrayList<TodoTask> todotasks = (ArrayList<TodoTask>) bundle.getSerializable(ALL_TASKS);
        allTaskNames.add(getString(R.string.todo_no_dependency));
        for (TodoTask todo : todotasks)
            allTaskNames.add(todo.getName());
        parentName = bundle.getString(PARENT);
    }

    @Override
    protected Dialog onCustomDialogSetting(AlertDialog alertDialog) {
        alertDialog.setTitle(getString(R.string.create_todotask));
        return alertDialog;
    }

    @Override
    protected View onViewCreated() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_task_item_dialog,null);
        bind(view);
        setupDependenciesSpinner();
        return view;
    }

    private void bind(View view){
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
    }

    private void setupDependenciesSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, allTaskNames);
        dependencySpinner.setAdapter(adapter);
    }

    @OnClick({R.id.startDateBtn, R.id.endDateBtn})
    public void settingDateButtonOnClick(final View view){
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Button button = (Button) view;
                calendar.set(year, month, dayOfMonth);
                Date date = calendar.getTime();
                button.setText(EnglishAbbrDateConverter.dateToTime(date, false));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    @Override
    protected DialogInterface.OnClickListener getOnPositiveButtonClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (checkValid())
                {
                    try{
                        Date startDate = EnglishAbbrDateConverter.timeToDate(startDateBtn.getText().toString());
                        Date endDate = EnglishAbbrDateConverter.timeToDate(endDateBtn.getText().toString());
                        String dependency = dependencySpinner.getSelectedItemPosition() == 0 ? "" : (String)dependencySpinner.getSelectedItem(); //empty string stand for no dependency
                        TodoTask todoTask = new TodoTask(nameEd.getText().toString(), parentName,
                                descriptionEd.getText().toString(), Integer.parseInt(contributionEd.getText().toString()),
                                startDate, endDate, dependency, TodoTask.Status.none, TodoTask.UNASSIGNED_ID, "");
                        WbsCommand command = WbsCommand.createTaskChild(parentName, todoTask);
                        baseView.showProgressDialog();
                        wbsConsolePresenterImp.executeCommand(command);
                        Log.d("Wbs",new Gson().toJson(command));
                    }catch (Exception err){
                        err.printStackTrace();
                    }

                }
            }
        };
    }

    private boolean checkValid() {
        boolean valid = true;
        String startDate = startDateBtn.getText().toString();
        String endDate = endDateBtn.getText().toString();
        if (startDate.contains(getString(R.string.start_date)))
        {
            valid = false;
            createAndShowErrorMessage(getString(R.string.please_select_start_date));
        }
        else if (endDate.contains(getString(R.string.end_date)))
        {
            valid = false;
            createAndShowErrorMessage(getString(R.string.please_select_end_date));
        }
        if (nameEd.length() == 0)
        {
            valid = false;
            nameEd.setError(getString(R.string.name_cannot_be_empty));
        }
        else if (hasDuplicatedTaskName(nameEd.getText().toString()))
        {
            valid = false;
            nameEd.setError(getString(R.string.name_cannot_be_duplicated));
        }

        if (descriptionEd.length() == 0)
        {
            valid = false;
            descriptionEd.setError(getString(R.string.description_cannot_be_empty));
        }

        return valid;
    }

    private boolean hasDuplicatedTaskName(String name){
        for (String n : allTaskNames)
            if (n.equals(name))
                return true;
        return false;
    }

    private void createAndShowErrorMessage(String message){
        TeamPathyDialogFactory.templateBuilder(getActivity())
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

}
