package app.dgandroid.eu.mitaskr.controllers;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import app.dgandroid.eu.mitaskr.models.Project;

/**
 * Created by Duilio on 04/04/2017.
 */

public class DataProjects {

    @SerializedName("projects")
    private List<Project> projects;

    public DataProjects(){
       projects = new ArrayList<Project>();
    }

    public void addProjectInList(Project project){
        this.projects.add(project);
    }

    public List<Project> getProjects(){
        return this.projects;
    }

    public void removeProject(long projectID){
        for(int i = 0; i<projects.size(); i++) {
            if(projects.get(i).getId() == projectID){
                projects.remove(i);
            }
        }
    }
}