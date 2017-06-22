package app.dgandroid.eu.mitaskr.activities;

import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import app.dgandroid.eu.mitaskr.models.Project;
import app.dgandroid.eu.mitaskr.models.Task;
import app.dgandroid.eu.mitaskr.R;
import app.dgandroid.eu.mitaskr.utils.Constants;
import app.dgandroid.eu.mitaskr.utils.Logger;
import app.dgandroid.eu.mitaskr.utils.Utility;
import app.dgandroid.eu.mitaskr.controllers.Manager;
import app.dgandroid.eu.mitaskr.storagecontrollers.DataStorage;

public class TaskActivityWriter extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Project project;
    private Task task;
    private int mYear, mMonth, mDay;
    private TextView dateText, titleTask, descriptionTask;
    private boolean isEdit = false, isEmptyDate = false;
    private Switch inReview;
    private boolean status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_writer);
        setTitle("Create Task");

        ActionBar ab =getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setElevation(0);

        dateText        = (TextView) findViewById(R.id.dateText);
        titleTask       = (TextView) findViewById(R.id.titleTask);
        descriptionTask = (TextView) findViewById(R.id.detailsTask);
        inReview        = (Switch) findViewById(R.id.switchReview);

        final Intent intent = getIntent();
        project = (Project)intent.getSerializableExtra("project");
        if(project == null) {
            isEdit = true;
            task = (Task)intent.getSerializableExtra("task");
            setTitle("Edit " + task.getTitle());
            if(task.getDate().startsWith("0")){
                dateText.setText("No Date");
                isEmptyDate = true;
            } else {
                dateText.setText(task.getDate());
                mDay = Utility.getValueFromStringDate(task.getDate(),   Constants.DAY);
                mMonth = Utility.getValueFromStringDate(task.getDate(), Constants.MONTH);
                mYear = Utility.getValueFromStringDate(task.getDate(),  Constants.YEAR);
            }
            titleTask.setText(task.getTitle());
            descriptionTask.setText(task.getDescription());

            status = task.isInReview();
            inReview.setChecked(status);
        }
        dateText.setFocusable(false);
        dateText.setClickable(true);
        dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd;
                if(isEdit && !isEmptyDate){
                    dpd = DatePickerDialog.newInstance(
                            TaskActivityWriter.this,
                            mYear,
                            (mMonth-1),
                            mDay
                    );
                } else {
                    dpd = DatePickerDialog.newInstance(
                            TaskActivityWriter.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        inReview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    status= true;
                }else{
                    status=false;
                }
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        this.mYear   = year;
        this.mMonth  = monthOfYear+1;
        this.mDay    = dayOfMonth;
        Logger.i("mYear = " + mYear + " mMonth = " + mMonth + " mDay = " + mDay);
        dateText.setText(Utility.getDateToString(mDay,mMonth,mYear));
    }
    //handle click event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        menu.findItem(R.id.action_name).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String title             = titleTask.getText().toString();
                String description       = descriptionTask.getText().toString();
                String date              = Utility.getDateToString(mDay, mMonth, mYear);
                long projectID;
                if(title.length() != 0){
                    if(project == null) {
                        projectID = task.getProjectID();
                    } else projectID = project.getId();
                    if(isEdit) {
                        DataStorage.getInstance().getTaskWithID(projectID, task.getId()).editTaskWith(title, description,date, status);
                    } else {
                        Task task = Manager.addTask(title, projectID, description, date, status);
                        DataStorage.getInstance().getProjectWithID(projectID).getTasks().add(task);
                    }
                    DataStorage.getInstance().updateData();
                    finish();
                }
                return false;
            }
        });
        return true;
    }
}