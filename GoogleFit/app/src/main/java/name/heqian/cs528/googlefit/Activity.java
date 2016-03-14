package name.heqian.cs528.googlefit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import database.ActivityBaseHelper;

/**
 * Created by Helios on 3/14/2016.
 */
public class Activity {
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public Activity(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ActivityBaseHelper(mContext).getWritableDatabase();

    }
}
