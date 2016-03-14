package name.heqian.cs528.googlefit;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    private static ContentValues getContentValues(Activity activity){
        ContentValues values = new ContentValues();
        values.put(ActivityDbSchema.ActivityTable.Cols.ACTIVITY_NAME, activity.getActivityName());
        values.put(ActivityDbSchema.ActivityTable.Cols.START_TIME, activity.getStartTime());

        return values;
    }
}
