package app.dgandroid.eu.mitaskr.utils;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.inputmethod.InputMethodManager;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Duilio on 06/04/2017.
 */

public class Utility {

    public static long genRandomID() {
        long timestamp = System.currentTimeMillis();
        long id = UUID.randomUUID().getMostSignificantBits() + timestamp;
        return id;
    }

    public static void dismissKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }

    public static String getDateToString(int day, int month, int year){
        String date = "";
        String separator = "/";
        date = day+separator+month+separator+year;
        return date;
    }

    /**
    @param type if 0 = day; 1 = month; 2 = year;
     **/
    public static int getValueFromStringDate(String date, int type){
        String DEFAULT_PATTERN = "dd/MM/yyyy";
        DateFormat formatter = new SimpleDateFormat(DEFAULT_PATTERN);
        String timeString = "";
        Date time;
        try{
            time = formatter.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
            return  -9;
        }
        switch (type) {
            case Constants.DAY:
                timeString = (String) android.text.format.DateFormat.format("dd", time);
                break;
            case Constants.MONTH:
                timeString = (String) android.text.format.DateFormat.format("MM", time);
                break;
            case Constants.YEAR:
                timeString = (String) android.text.format.DateFormat.format("yyyy", time);
                break;
            default:
                timeString =" -9";
                break;
        }
        return Integer.parseInt(timeString);
    }

    public static void setColorMenuIcon(int id_resource, int id_colour, Menu menu, Context context){
        Drawable drawable = menu.findItem(id_resource).getIcon();
        if(drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(context.getResources().getColor(id_colour), PorterDuff.Mode.SRC_IN);
        }
    }
}