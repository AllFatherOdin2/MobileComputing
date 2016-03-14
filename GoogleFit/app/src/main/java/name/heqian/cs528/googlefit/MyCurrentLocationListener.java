package name.heqian.cs528.googlefit;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by David on 3/14/2016.
 */
public class MyCurrentLocationListener implements LocationListener {

    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        String myLocation = "Latitude = " + lat + " Longitude = " + lng;

        //Log.i("MY CURRENT LOCATION", myLocation);
    }

    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    public void onProviderEnabled(String s) {

    }

    public void onProviderDisabled(String s) {

    }

    public LatLng getLatLong (Location loc){
        return new LatLng(loc.getLatitude(),loc.getLongitude());
    }
}
