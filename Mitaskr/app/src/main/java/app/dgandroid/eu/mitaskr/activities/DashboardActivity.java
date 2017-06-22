package app.dgandroid.eu.mitaskr.activities;

import android.app.Dialog;
import android.content.ContextWrapper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import app.dgandroid.eu.mitaskr.models.Project;
import app.dgandroid.eu.mitaskr.R;
import app.dgandroid.eu.mitaskr.adapters.ProjectsAdapter;
import app.dgandroid.eu.mitaskr.controllers.Manager;
import app.dgandroid.eu.mitaskr.interfaces.StorageDelegateInterface;
import app.dgandroid.eu.mitaskr.storagecontrollers.DataStorage;

public class DashboardActivity extends AppCompatActivity {

    private ArrayList<Project> arrayList;
    private ProjectsAdapter adapter;
    private  ListView listView;
    private ContextWrapper context;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.dashboard);
        listView = (ListView) findViewById(R.id.listView);
        context = this;
        DataStorage.init(context); //Simulate Call on web Services
        DataStorage.getInstance().readData(new StorageDelegateInterface() { //CALL
            @Override
            public void onGetDataFromStorage() {
                onPopolateList();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAddProject();
            }
        });
    }

    public void openDialogAddProject(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_project);
        dialog.findViewById(R.id.buttonConfirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView titleDialog = (TextView)dialog.findViewById(R.id.editText);
                String text = titleDialog.getText().toString();
                if(text.length() != 0){
                    Project project = Manager.addProject(text);
                    DataStorage.getInstance().getDataProjects().addProjectInList(project);
                    if(arrayList == null) {arrayList = new ArrayList<Project>();}
                    arrayList.add(project);
                    DataStorage.getInstance().updateData();
                    adapter = new ProjectsAdapter(DashboardActivity.this, arrayList);
                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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

    public void onPopolateList(){
        if(DataStorage.getInstance().getDataProjects() != null){
            arrayList = new ArrayList<Project>();
            for(int i = 0; i < DataStorage.getInstance().getDataProjects().getProjects().size(); i++) {
                arrayList.add(DataStorage.getInstance().getDataProjects().getProjects().get(i));
            }
            adapter = new ProjectsAdapter(DashboardActivity.this, arrayList);
            listView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}