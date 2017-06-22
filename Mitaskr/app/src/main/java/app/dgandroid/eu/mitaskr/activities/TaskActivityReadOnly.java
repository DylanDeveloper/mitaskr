package app.dgandroid.eu.mitaskr.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import app.dgandroid.eu.mitaskr.models.Comment;
import app.dgandroid.eu.mitaskr.models.Task;
import app.dgandroid.eu.mitaskr.R;
import app.dgandroid.eu.mitaskr.utils.Utility;
import app.dgandroid.eu.mitaskr.adapters.CommentAdapter;
import app.dgandroid.eu.mitaskr.controllers.Manager;
import app.dgandroid.eu.mitaskr.storagecontrollers.DataStorage;

public class TaskActivityReadOnly extends AppCompatActivity {

    private Task task;
    private TextView dateLabel, detailsLabel;
    private View layoutEmptyTask, ReviewLayout;
    private ArrayList<Comment> arrayList;
    private CommentAdapter adapter;
    private ListView listView;
    private EditText textComment;
    private ImageButton sendIconButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        ActionBar ab =getSupportActionBar();
        ab.setElevation(0);

        layoutEmptyTask = (View) findViewById(R.id.layoutEmptyTask);
        ReviewLayout = (View) findViewById(R.id.ReviewLayout);
        listView = (ListView) findViewById(R.id.listView);
        textComment = (EditText) findViewById(R.id.textComment);
        sendIconButton = (ImageButton) findViewById(R.id.btnAddComment);
        dateLabel = (TextView) findViewById(R.id.dateLabel);
        detailsLabel = (TextView) findViewById(R.id.detailsLabel);

        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");
        setTitle(task.getTitle());

        if(task.getDescription().length() != 0) {
            detailsLabel.setText(task.getDescription());
        } else detailsLabel.setText("No Details.");

        if(task.getDate().startsWith("0")){
            dateLabel.setText("No Date.");
        } else dateLabel.setText(task.getDate());

        if(task.isInReview()) {
            ReviewLayout.setVisibility(View.VISIBLE);
        } else  ReviewLayout.setVisibility(View.GONE);

        context = this;

        textComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0) {sendIconButton.setColorFilter(Color.LTGRAY);}}
            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() != 0){
                    sendIconButton.setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
                } else sendIconButton.setColorFilter(context.getResources().getColor(R.color.grey_500), PorterDuff.Mode.SRC_IN);
            }
        });
        sendIconButton.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {addComment();}});
    }

    public void editTask(){
        Intent intent1 = new Intent(TaskActivityReadOnly.this, TaskActivityWriter.class);
        intent1.putExtra("task", task);
        startActivity(intent1);
        finish();
    }

    public void addComment(){
        String message = textComment.getText().toString();
        if(message.length() != 0){
            Comment comment = Manager.addComment(message, task);
            DataStorage.getInstance().getTaskWithID(task.getProjectID(), task.getId()).getComments().add(comment);
            if(arrayList == null) {arrayList = new ArrayList<Comment>();}
            arrayList.add(comment);
            DataStorage.getInstance().updateData();
            adapter = new CommentAdapter(TaskActivityReadOnly.this, arrayList, task);
            listView.setAdapter(adapter);
            layoutEmptyTask.setVisibility(View.GONE);
            textComment.setText("");
            textComment.clearFocus();
            Utility.dismissKeyboard(this);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        onPopolateCommentList();
        if(adapter != null) {adapter.notifyDataSetChanged();}
    }

    public void onPopolateCommentList(){
        arrayList = new ArrayList<Comment>();
        task = DataStorage.getInstance().getTaskWithID(task.getProjectID(), task.getId());
        for(int i = 0; i < task.getComments().size(); i++) {
            arrayList.add(task.getComments().get(i));
        }
        adapter = new CommentAdapter(TaskActivityReadOnly.this, arrayList, task);
        listView.setAdapter(adapter);
        if(task.getComments().size() == 0) {layoutEmptyTask.setVisibility(View.VISIBLE);}
        else {layoutEmptyTask.setVisibility(View.GONE);}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        menu.findItem(R.id.action_name).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                editTask();
                return false;
            }
        });
        Utility.setColorMenuIcon(R.id.action_name, R.color.light_blue_800, menu, this);
        return true;
    }
}