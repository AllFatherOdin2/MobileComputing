package name.heqian.cs528.googlefit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import database.ActivityBaseHelper;
import database.ActivityDbSchema;

/**
 * Created by Helios on 3/14/2016.
 */
public class ActivityTracker {
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public ActivityTracker(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ActivityBaseHelper(mContext).getWritableDatabase();

    }

    public void addActivity(Activity activity){
        ContentValues values = getContentValues(activity);

        mDatabase.insert(ActivityDbSchema.ActivityTable.NAME, null, values);
    }

    public ActivityCursorWrapper queryActivities(String where, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                ActivityDbSchema.ActivityTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null
        );
        return new ActivityCursorWrapper(cursor);
    }

    public Activity getActivity(int id){
        ActivityCursorWrapper cursor = queryActivities(
                ActivityDbSchema.ActivityTable.Cols.UUID + " =?",
                new String[]{String.valueOf(id)}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getActivity();
        } finally {
            cursor.close();
        }
    }

    public List<Activity> getActivities(){
        List<Activity> activities = new ArrayList<>();
        ActivityCursorWrapper cursor = queryActivities(null, null);
         try{
             cursor.moveToFirst();
             while(!cursor.isAfterLast()){
                 activities.add(cursor.getActivity());
                 cursor.moveToNext();
             }
         } finally {
             cursor.close();
         }

        return activities;
    }

    private static ContentValues getContentValues(Activity activity){
        ContentValues values = new ContentValues();
        values.put(ActivityDbSchema.ActivityTable.Cols.UUID, activity.getid());
        values.put(ActivityDbSchema.ActivityTable.Cols.ACTIVITY_NAME, activity.getActivityName());
        values.put(ActivityDbSchema.ActivityTable.Cols.START_TIME, String.valueOf(activity.getStartTime()));

        return values;
    }
}
