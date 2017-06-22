package app.dgandroid.eu.mitaskr.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import app.dgandroid.eu.mitaskr.adapters.TaskAdapterExpandible;
import app.dgandroid.eu.mitaskr.models.Project;
import app.dgandroid.eu.mitaskr.models.Task;
import app.dgandroid.eu.mitaskr.R;
import app.dgandroid.eu.mitaskr.storagecontrollers.DataStorage;
import app.dgandroid.eu.mitaskr.utils.Utility;

public class ProjectActivity extends AppCompatActivity {

    private Project project;
    private Dialog dialog;
    private View layoutEmptyTask;
    TaskAdapterExpandible listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, ArrayList<Task>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        layoutEmptyTask = (View) findViewById(R.id.layoutEmptyTask);
        expListView = (ExpandableListView) findViewById(R.id.expList);
        final Intent intent = getIntent();
        project = (Project)intent.getSerializableExtra("project");
        setTitle(project.getName());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //Open Activity create Task
                Intent intent1 = new Intent(ProjectActivity.this, TaskActivityWriter.class);
                intent1.putExtra("project", project);
                startActivity(intent1);
            }
        });
    }

    public void openDialogEdit(){
        dialog = new Dialog(ProjectActivity.this);
        dialog.setContentView(R.layout.dialog_project);
        final TextView titleDialog = (TextView)dialog.findViewById(R.id.editText);
        titleDialog.setText(project.getName());
        dialog.findViewById(R.id.buttonConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = titleDialog.getText().toString();
                if(text.length() != 0){
                    setTitle(text);
                    DataStorage.getInstance().getProjectWithID(project.getId()).setName(text);
                    DataStorage.getInstance().updateData();
                    dialog.dismiss();
                }
            }
        });
        dialog.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPopolateTaskList();
        if(listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public void onPopolateTaskList() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList<Task>>();
        listDataHeader.add("In Review");
        listDataHeader.add("Tasks");
        ArrayList<Task> arrayListInReview = new ArrayList<Task>();
        ArrayList<Task>  arrayListRegular = new ArrayList<Task>();

        project = DataStorage.getInstance().getProjectWithID(project.getId());
        for(int i = 0; i < project.getTasks().size(); i++) {
            if(project.getTasks().get(i).isInReview()){
                arrayListInReview.add(project.getTasks().get(i));
            } else {
                arrayListRegular.add(project.getTasks().get(i));
            }

        }
        listDataChild.put(listDataHeader.get(0), arrayListInReview);
        listDataChild.put(listDataHeader.get(1), arrayListRegular);
        listAdapter = new TaskAdapterExpandible(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        if(project.getTasks().size() == 0) {
            layoutEmptyTask.setVisibility(View.VISIBLE);
        } else {
            layoutEmptyTask.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        menu.findItem(R.id.action_name).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                openDialogEdit();
                return false;
            }
        });
        Utility.setColorMenuIcon(R.id.action_name, R.color.light_blue_800, menu, this);
        return true;
    }
}