package com.ood.clean.waterball.teampathy.Presentation.UI.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.ood.clean.waterball.teampathy.Domain.Model.Project;
import com.ood.clean.waterball.teampathy.MyApp;
import com.ood.clean.waterball.teampathy.MyUtils.EnglishAbbrDateConverter;
import com.ood.clean.waterball.teampathy.MyUtils.TeamPathyDialogFactory;
import com.ood.clean.waterball.teampathy.Presentation.Presenter.WbsConsolePresenterImp;
import com.ood.clean.waterball.teampathy.R;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateTodoTaskDialogFragment extends MakeSureToCancelBaseDialogFragment{
    private static final String PARENT = "ParentName";
    private String parentName;
    @Inject WbsConsolePresenterImp wbsConsolePresenterImp;
    @Inject Project project;
    @BindView(R.id.startDateBtn) Button startDateBtn;
    @BindView(R.id.endDateBtn) Button endDateBtn;
    @BindView(R.id.dependencySpin) Spinner dependencySpinner;
    @BindView(R.id.nameEd) TextInputEditText nameEd;
    @BindView(R.id.contributionEd) TextInputEditText contributionEd;
    @BindView(R.id.descriptionEd) TextInputEditText descriptionEd;

    public static CreateTodoTaskDialogFragment newInstance(String parentName){
        CreateTodoTaskDialogFragment fragment = new CreateTodoTaskDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARENT, parentName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentName = getArguments().getString(PARENT);
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
        return view;
    }

    private void bind(View view){
        ButterKnife.bind(this,view);
        MyApp.getWbsComponent(getActivity()).inject(this);
    }

    @OnClick({R.id.startDateBtn, R.id.endDateBtn})
    public void startDateButtonOnClick(final View view){
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
                        /*TodoTask todoTask = new TodoTask(nameEd.getText().toString(), parentName,
                                descriptionEd.getText().toString(), Integer.parseInt(contributionEd.getText().toString()),
                                startDate, endDate);*/
                        //WbsCommand<TodoTask> command = WbsCommand.createTaskChild(parentName, todoTask);
                    }catch (Exception err){

                    }

                }
            }
        };
    }

    private boolean checkValid() {
        boolean valid;
        String startDate = startDateBtn.getText().toString();
        String endDate = endDateBtn.getText().toString();
        if (valid = startDate.contains(getString(R.string.start_date)))
            createAndShowErrorMessage(getString(R.string.please_select_start_date));
        else if (endDate.contains(getString(R.string.end_date)))
            createAndShowErrorMessage(getString(R.string.please_select_end_date));
        if (valid &= nameEd.length() == 0)
            nameEd.setError(getString(R.string.name_cannot_be_empty));
        if (valid &= descriptionEd.length() == 0)
            descriptionEd.setError(getString(R.string.description_cannot_be_empty));



        return valid;
    }

    private void createAndShowErrorMessage(String message){
        TeamPathyDialogFactory.templateBuilder(getActivity())
                .setTitle(R.string.error)
                .setMessage(message)
                .setPositiveButton(R.string.confirm, null)
                .show();
    }

}
