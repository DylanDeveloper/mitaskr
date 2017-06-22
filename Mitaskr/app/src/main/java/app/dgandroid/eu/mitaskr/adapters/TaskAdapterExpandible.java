package app.dgandroid.eu.mitaskr.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import app.dgandroid.eu.mitaskr.activities.ProjectActivity;
import app.dgandroid.eu.mitaskr.activities.TaskActivityReadOnly;
import app.dgandroid.eu.mitaskr.models.Task;
import app.dgandroid.eu.mitaskr.R;
import app.dgandroid.eu.mitaskr.storagecontrollers.DataStorage;

/**
 * Created by Duilio on 12/04/2017.
 */

public class TaskAdapterExpandible extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, ArrayList<Task>> _listDataChild;

    public TaskAdapterExpandible (Context context, List<String> listHeader, HashMap<String, ArrayList<Task>> listDataChild){
        this._context= context;
        this._listDataChild = listDataChild;
        this._listDataHeader = listHeader;
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Task getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)  {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.section_header, null);
            }
            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.separator);
            lblListHeader.setTypeface(null, Typeface.BOLD);

            lblListHeader.setText(headerTitle);
            ExpandableListView mExpandableListView = (ExpandableListView) parent;
            mExpandableListView.expandGroup(groupPosition);
            mExpandableListView.setGroupIndicator(null);
            return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Task task = getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_task_item, null);
        }
        TextView item_name = (TextView) convertView.findViewById(R.id.itemTask);
        TextView dateView = (TextView) convertView.findViewById(R.id.dateView);
        ImageView iconReview = (ImageView) convertView.findViewById(R.id.iconReview);
        if(task.isInReview()){
            iconReview.setVisibility(View.VISIBLE);
        }
        item_name.setText(task.getTitle());
        if(!task.getDate().startsWith("0")) {dateView.setText(task.getDate());}

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, TaskActivityReadOnly.class);
                intent.putExtra("task", task);
                _context.startActivity(intent);
            }
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setTitle("Delete " + task.getTitle() + " ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DataStorage.getInstance().getProjectWithID(task.getProjectID()).removeTask(task.getId());
                                DataStorage.getInstance().updateData();
                                ProjectActivity pActivity = (ProjectActivity)_context;
                                pActivity.onPopolateTaskList();
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

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}