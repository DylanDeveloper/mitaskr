package app.dgandroid.eu.mitaskr.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Duilio on 04/04/2017.
 */

public class Task implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("date")
    private String date;
    @SerializedName("id")
    private long id;
    @SerializedName("projectID")
    private long projectID;
    @SerializedName("inReview")
    private boolean inReview;
    @SerializedName("comments")
    private List<Comment> comments;

    public long getProjectID() {
        return projectID;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }

    public boolean isInReview() {
        return inReview;
    }

    public void setInReview(boolean inReview) {
        this.inReview = inReview;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void editTaskWith(String title, String description, String date, boolean inReview){
        this.title = title;
        this.description = description;
        this.date = date;
        this.inReview = inReview;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Task(String title, long id, long projectID, String description, String date, boolean inReview, List<Comment> comments){
        this.title = title;
        this.description = description;
        this.date = date;
        this.id = id;
        this.projectID = projectID;
        this.inReview = inReview;
        this.comments = comments;
    }

    public void removeComment(long commentID){
        for(int i = 0; i<comments.size(); i++) {
            if(comments.get(i).getId() == commentID) {
                comments.remove(i);
                break;
            }
        }
    }
}