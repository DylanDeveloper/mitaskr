package app.dgandroid.eu.mitaskr.controllers;

import java.util.ArrayList;
import app.dgandroid.eu.mitaskr.models.Comment;
import app.dgandroid.eu.mitaskr.models.Project;
import app.dgandroid.eu.mitaskr.models.Task;
import app.dgandroid.eu.mitaskr.utils.Utility;

/**
 * Created by Duilio on 05/04/2017.
 */

public class Manager {
    public static Project addProject(String nameProject){
        return new Project(nameProject, Utility.genRandomID(), new ArrayList<Task>()); //put projectID for webService
    }

    public static Task addTask(String title, long projectID, String description, String date, boolean inReview){
        return new Task(title, Utility.genRandomID(), projectID ,description, date, inReview, new ArrayList<Comment>()); //put projectID for webService
    }

    public static Comment addComment(String comment, Task task){
        return new Comment(Utility.genRandomID(), task.getId(), comment); //put taskID for webService
    }
}
