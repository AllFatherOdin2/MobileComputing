package name.heqian.cs528.googlefit;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

/**
 * Created by Paul on 2/1/16.
 */
public class ActivityRecognizedService extends IntentService {
   protected static final String TAG = "detection_is";


    public ActivityRecognizedService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        Intent localIntent = new Intent("BROADCAST_ACTION");

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();
        ArrayList<DetectedActivity> confidentActivities = new ArrayList<>();
        for (DetectedActivity thisActivity : detectedActivities) {
            //Log.e("Activity", thisActivity.toString() + " confidence: " + thisActivity.getConfidence());
            if(thisActivity.getConfidence() >= 35){
                switch (thisActivity.getType()) {
                    case DetectedActivity.IN_VEHICLE:
                    case DetectedActivity.RUNNING:
                    case DetectedActivity.STILL:
                    case DetectedActivity.WALKING:
                        confidentActivities.add(thisActivity);
                        break;
                }
            }
        }

        // Broadcast the list of detected activities.
        localIntent.putExtra("ACTIVITY_EXTRA", confidentActivities);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
}
