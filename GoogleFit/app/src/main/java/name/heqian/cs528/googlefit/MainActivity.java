package name.heqian.cs528.googlefit;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.wallet.wobs.TimeInterval;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public GoogleApiClient mApiClient;
    private TextView activityText;
    protected ActivityRecognizedBroadcastReceiver mBroadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityText = ((TextView)findViewById(R.id.activityText));
        mBroadcastReceiver = new ActivityRecognizedBroadcastReceiver();

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mApiClient.disconnect();
    }

    @Override
    protected void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter("BROADCAST_ACTION"));
        super.onResume();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Intent intent = new Intent( this, ActivityRecognizedService.class );
        PendingIntent pendingIntent = PendingIntent.getService( this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(mApiClient, 500, pendingIntent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public String getActivityString(DetectedActivity activity){
        String result = "";

        switch (activity.getType()) {
            case DetectedActivity.IN_VEHICLE: {
                activityText.setText(R.string.vehicle);
                result = "vehicle";
                break;
            }
            case DetectedActivity.RUNNING: {
                activityText.setText(R.string.running);
                result = "running";
                break;
            }
            case DetectedActivity.STILL: {
                activityText.setText(R.string.still);
                result = "still";
                break;
            }
            case DetectedActivity.WALKING: {
                activityText.setText(R.string.walking);
                result = "walking";
                break;
            }
        }

        return result;
    }

    public class ActivityRecognizedBroadcastReceiver extends BroadcastReceiver{
        //protected  static final String TAG = "receiver";

        @Override
        public void onReceive(Context context, Intent intent){
            ArrayList<DetectedActivity> updatedActivities = intent.getParcelableArrayListExtra("ACTIVITY_EXTRA");

            String strStatus = "";
            for(DetectedActivity thisActivity: updatedActivities){
                strStatus += getActivityString(thisActivity);
            }

            Log.i("String Status", strStatus);



        }

    }
}
