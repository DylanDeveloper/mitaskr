package app.dgandroid.eu.mitaskr.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Duilio on 01/04/2017.
 */

public class Project implements Serializable {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("task")
    private List<Task> task;

    public Project(String name, long id, List<Task> task){
        this.name = name;
        this.id = id;
        this.task = task;
    }

    public long getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<Task> getTasks() {
        return task;
    }

    public void removeTask(long taskID) {
        for(int i = 0; i < task.size(); i++ ) {
            if(task.get(i).getId() == taskID) {
                task.remove(i);
                break;
            }
        }
    }
}