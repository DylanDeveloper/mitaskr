package app.dgandroid.eu.mitaskr.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import app.dgandroid.eu.mitaskr.models.Project;
import app.dgandroid.eu.mitaskr.R;
import app.dgandroid.eu.mitaskr.activities.DashboardActivity;
import app.dgandroid.eu.mitaskr.activities.ProjectActivity;
import app.dgandroid.eu.mitaskr.storagecontrollers.DataStorage;

/**
 * Created by Duilio on 04/04/2017.
 */

public class ProjectsAdapter extends ArrayAdapter<Project> {

    public ProjectsAdapter(Context context, ArrayList<Project> project) {
        super(context, 0, project);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final Project project = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_project_item, parent, false);
        }
        TextView item_name = (TextView) convertView.findViewById(R.id.itemProject);
        item_name.setText(project.getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProjectActivity.class);
                intent.putExtra("project", project);
                getContext().startActivity(intent);
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete " + project.getName() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DataStorage.getInstance().getDataProjects().removeProject(project.getId());
                                DataStorage.getInstance().updateData();
                                DashboardActivity pActivity = (DashboardActivity)getContext();
                                pActivity.onPopolateList();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return false;
            }
        });
        return convertView;
    }
}