package app.dgandroid.eu.mitaskr.storagecontrollers;

import android.content.ContextWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import app.dgandroid.eu.mitaskr.controllers.DataProjects;
import app.dgandroid.eu.mitaskr.models.Comment;
import app.dgandroid.eu.mitaskr.models.Project;
import app.dgandroid.eu.mitaskr.models.Task;
import app.dgandroid.eu.mitaskr.interfaces.StorageDelegateInterface;
import app.dgandroid.eu.mitaskr.utils.Logger;

/**
 * Created by Duilio on 01/04/2017.
 */

public class DataStorage {
    private ContextWrapper context;
    private String SAVE_DATA_STRING;
    private static DataStorage Instance;
    private DataProjects dataProjects;

    public  DataStorage(ContextWrapper context){
        this.context = context;
        this.SAVE_DATA_STRING = LocalStorageController.loadDataString(context);
        Logger.i("SAVE_DATA_STRING = " + SAVE_DATA_STRING);
    }

    public static void init(ContextWrapper cw){
        Instance = new DataStorage(cw);
    }

    //Call it after init DataStorage
    public static DataStorage getInstance(){
        return Instance;
    }

    public DataProjects getDataProjects(){
        if(dataProjects == null) {
            dataProjects = new DataProjects();
        }
        return dataProjects;
    }

    public Project getProjectWithID(long id){
        for (Project project : dataProjects.getProjects()) {
            if(project.getId() == id) {
                return project;
            }
        }
        return  null;
    }

    public Task getTaskWithID(long projectId, long taskId){
        for (Project project : dataProjects.getProjects()) {
            if(project.getId() == projectId) {
                for (Task task : project.getTasks()) {
                    if(task.getId() == taskId) {
                        return task;
                    }
                }
            }
        }
        return  null;
    }

    public Comment getCommentWithID(long taskId, long commentID){
        for (Project project : dataProjects.getProjects()) {
            for(Task task : project.getTasks()) {
                if(task.getId() == taskId){
                    for(Comment comment : task.getComments()) {
                        if(comment.getId() == commentID) {
                            return  comment;
                        }
                    }
                }
            }
        }
        return  null;
    }

    public void readData(StorageDelegateInterface delegateInterface) {
        try{
            Gson gson = new GsonBuilder().create();
            dataProjects = gson.fromJson(this.SAVE_DATA_STRING, DataProjects.class);
        } catch (Exception e) {
            Logger.e("null data why " + e.getMessage());
        }
        delegateInterface.onGetDataFromStorage();
    }

    public void updateData() {
        Gson gson = new Gson();
        String s = gson.toJson(dataProjects);
        LocalStorageController.setSecureValue(s, context);
        Logger.i(s);
    }
}