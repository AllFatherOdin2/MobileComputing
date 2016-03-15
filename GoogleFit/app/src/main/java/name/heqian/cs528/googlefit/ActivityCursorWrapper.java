package name.heqian.cs528.googlefit;

import android.database.Cursor;
import android.database.CursorWrapper;

import database.ActivityDbSchema;

/**
 * Created by Helios on 3/14/2016.
 */
public class ActivityCursorWrapper extends CursorWrapper {
    public ActivityCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Activity getActivity(){
        String UUID = getString(getColumnIndex(ActivityDbSchema.ActivityTable.Cols.UUID));
        String activityName = getString(getColumnIndex(ActivityDbSchema.ActivityTable.Cols.ACTIVITY_NAME));
        Long startTime = getLong(getColumnIndex(ActivityDbSchema.ActivityTable.Cols.START_TIME));

        return new Activity(UUID, activityName, startTime);
    }
}
