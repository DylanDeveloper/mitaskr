package app.dgandroid.eu.mitaskr.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 * Created by Duilio on 01/04/2017.
 */

public class Comment implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("taskID")
    private long taskID;
    @SerializedName("comment")
    private String comment;

    public long getTaskID() {
        return taskID;
    }

    public void setTaskID(long taskID) {
        this.taskID = taskID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comment(long id, long taskID, String comment){
        this.id = id;
        this.comment = comment;
        this.taskID = taskID;
    }
}