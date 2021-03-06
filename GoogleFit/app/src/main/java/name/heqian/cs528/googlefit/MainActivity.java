package name.heqian.cs528.googlefit;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wallet.wobs.TimeInterval;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    public GoogleApiClient mApiClient;
    private TextView activityText;
    private ImageView activityImage;
    private ActivityTracker mActivity;
    private MediaPlayer mp;
    protected ActivityRecognizedBroadcastReceiver mBroadcastReceiver;
    protected GoogleMap googleMap;
    protected LocationManager locationManager;
    protected MyCurrentLocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mp = MediaPlayer.create(this, R.raw.beat_02);
        mActivity = new ActivityTracker(this);
        activityText = ((TextView)findViewById(R.id.activityText));
        activityImage = ((ImageView)findViewById(R.id.activityImage));
        mBroadcastReceiver = new ActivityRecognizedBroadcastReceiver();

        mApiClient = new GoogleApiClient.Builder(this)
                .addApi(ActivityRecognition.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mApiClient.connect();





        locationManager=    (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyCurrentLocationListener();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) locationListener);
        } catch (SecurityException e){

        }
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int status = apiAvailability.isGooglePlayServicesAvailable(this);

        if (status != ConnectionResult.SUCCESS){
            if(apiAvailability.isUserResolvableError(status)) {
                apiAvailability.getErrorDialog(this, status,
                        404).show();
            }
        } else {
            MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
            mf.getMapAsync(this);
        }

    }

    public void onMapReady(GoogleMap map){
        Log.e("Main Activity- MapReady", "Entering MapReady Function");
        googleMap = map;
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) locationListener);
        } catch (SecurityException e){

        }
        try {
            googleMap.setMyLocationEnabled(true);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null){
                LatLng loc = locationListener.getLatLong(location);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                googleMap.moveCamera(CameraUpdateFactory.zoomTo(16));
            }
        } catch (SecurityException e) {

        }
        Log.e("Main Activity- MapReady", "Exiting MapReady Function");
    }

    @Override
    protected void onStart(){
        super.onStart();
        mApiClient.connect();
    }

    @Override
    protected void onStop(){
        super.onStop();
        mp.stop();
        mApiClient.disconnect();
    }

    @Override
    protected void onPause(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
        mp.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, new IntentFilter("BROADCAST_ACTION"));
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
                activityImage.setImageResource(R.drawable.in_vehicle);
                result = "in a vehicle";
                if(mp.isPlaying()){
                    mp.stop();
                }
                break;
            }
            case DetectedActivity.RUNNING: {
                activityText.setText(R.string.running);
                activityImage.setImageResource(R.drawable.running);
                result = "running";
                if(!mp.isPlaying()){
                    mp.start();
                }
                break;
            }
            case DetectedActivity.STILL: {
                activityText.setText(R.string.still);
                activityImage.setImageResource(R.drawable.still);
                result = "still";

                if(mp.isPlaying()){
                    mp.stop();
                }
                break;
            }
            case DetectedActivity.WALKING: {
                activityText.setText(R.string.walking);
                activityImage.setImageResource(R.drawable.walking);
                result = "walking";
                if(!mp.isPlaying()){
                    mp.start();
                }
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

            //String strStatus = "";
            for(DetectedActivity thisActivity: updatedActivities){

                String activityString = getActivityString(thisActivity);
                //strStatus += activityString;

                List<Activity> activities = mActivity.getActivities();


                if(activities.size() > 0) {
                    Activity lastActivity = activities.get(activities.size() - 1);
                    if (!lastActivity.getActivityName().equals(activityString)) {

                        Activity newActivity = new Activity(activityString);
                        mActivity.addActivity(newActivity);

                        long timeChange = newActivity.getStartTime() - lastActivity.getStartTime();

                        String alertString = "You were " + lastActivity.getActivityName() + " for " + (int) timeChange / 1000 + " seconds";

                        callToast(alertString);

                        //Log.i("activity", alertString);
                    }
                } else {
                    Activity newActivity = new Activity(activityString);
                    mActivity.addActivity(newActivity);
                }

            }
            //Log.i("String Status", strStatus);
        }

    }

    private void callToast(String text){
        Context con = getApplicationContext();
        int duration = Toast.LENGTH_LONG;
        Toast.makeText(con, text, duration).show();
    }
}
