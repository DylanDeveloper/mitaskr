package app.dgandroid.eu.mitaskr.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import app.dgandroid.eu.mitaskr.activities.TaskActivityReadOnly;
import app.dgandroid.eu.mitaskr.models.Comment;
import app.dgandroid.eu.mitaskr.models.Task;
import app.dgandroid.eu.mitaskr.R;
import app.dgandroid.eu.mitaskr.storagecontrollers.DataStorage;

/**
 * Created by Duilio on 04/04/2017.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {

    Task task;

    public CommentAdapter(Context context, ArrayList<Comment> comments, Task task) {
        super(context, 0, comments);
        this.task = task;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final Comment comment = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_comment_item, parent, false);
        }
        TextView item_name = (TextView) convertView.findViewById(R.id.itemComment);
        item_name.setText(comment.getComment());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_add_comment);
                final TextView commentText = (TextView)dialog.findViewById(R.id.editText);
                TextView title = (TextView)dialog.findViewById(R.id.title);
                title.setText("Edit Comment");
                commentText.setText(comment.getComment());
                dialog.findViewById(R.id.buttonConfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String message = commentText.getText().toString();
                        if(message.length() != 0){
                            DataStorage.getInstance().getCommentWithID(comment.getTaskID(), comment.getId()).setComment(message);
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
        });
        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete this Comment?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DataStorage.getInstance().getTaskWithID(task.getProjectID(), task.getId()).removeComment(comment.getId());
                                DataStorage.getInstance().updateData();
                                TaskActivityReadOnly pActivity = (TaskActivityReadOnly)getContext();
                                pActivity.onPopolateCommentList();
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