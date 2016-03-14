package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Helios on 3/14/2016.
 */
public class ActivityBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "activityBase.db";

    public ActivityBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + ActivityDbSchema.ActivityTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ActivityDbSchema.ActivityTable.Cols.ACTIVITY_NAME + ", " +
                ActivityDbSchema.ActivityTable.Cols.START_TIME +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int newVersion){

    }
}
