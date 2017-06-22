package app.dgandroid.eu.mitaskr.storagecontrollers;

import android.content.Context;
import android.content.ContextWrapper;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import app.dgandroid.eu.mitaskr.utils.Constants;

/**
 * Created by Duilio on 01/04/2017.
 */

public class LocalStorageController {

    static final int READ_BLOCK_SIZE = 100;

    public static String loadDataString(ContextWrapper cw) {
        try {
            FileInputStream fileIn= cw.openFileInput(Constants.LOCAL_DATA);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            return s;

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static void setSecureValue(String elem, ContextWrapper cw) {
        FileOutputStream outputStream = null;
        try {
            outputStream = cw.openFileOutput(Constants.LOCAL_DATA, Context.MODE_PRIVATE);
            outputStream.write(elem.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}