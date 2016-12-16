package com.example.anders.maptest;

import android.content.Context;
import android.location.Criteria;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.pm.PackageManager;
import android.Manifest;
import com.google.android.gms.common.api.GoogleApiClient.*;
import com.google.android.gms.location.LocationServices;
import android.location.Location;
import java.security.KeyStore;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import com.google.android.gms.maps.model.Marker;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener{

    private GoogleMap mMap;
    private LocationManager locMan;
    private Marker myMarker;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locMan.getBestProvider(criteria, false);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng oslo = new LatLng(59.911491, 10.757933);
        mMap.addMarker(new MarkerOptions().position(oslo).title("Oslo"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(oslo, 10.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(oslo));
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());
        Log.d("---TEST---", "onMapReady: Checking mylocation2");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            Log.d("---TEST---", "onMapReady: Setting mylocation");
            mMap.setMyLocationEnabled(true);
            try {
                LatLng self = new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
                myMarker = mMap.addMarker(new MarkerOptions().position(self).title("Start 1"));
            }
            catch(NullPointerException npe)
            {
                Log.d("---TEST---", "Could not create start position.");
            }
        }
        else
        {
            Log.d("---TEST---", "onMapReady: Permissions not given for mylocation.");
        }

        //Add test map
        BitmapDescriptor im = BitmapDescriptorFactory.fromResource(R.drawable.karttest2);

        LatLngBounds bounds = new LatLngBounds(new LatLng(59.970068, 10.724203), new LatLng(59.984807, 10.761013));

        GroundOverlay groundOverlay = mMap.addGroundOverlay(new GroundOverlayOptions()
                .image(im)
                .positionFromBounds(bounds)
                .transparency(0.5f));

    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            //return locMan.getLastKnownLocation(provider);
            onLocationChanged(locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER));

        }
        catch (NullPointerException npe)
        {
            Log.d("---TEST---", "onMapReady: Could not get location due to not finding provider.");
        }
        catch (SecurityException e)
        {
            Log.d("---TEST---", "onMapReady: Could not get location due to permissions.");
        }
    }

    public Location getLocation()
    {
        try{
            //return locMan.getLastKnownLocation(provider);
            return locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        }
        catch (NullPointerException npe)
        {
            Log.d("---TEST---", "onMapReady: Could not get location due to not finding provider.");
        }
        catch (SecurityException e)
        {
            Log.d("---TEST---", "onMapReady: Could not get location due to permissions.");
        }
        Location failed = new Location("");
        failed.setLongitude(0.0f);
        failed.setLatitude((0.0f));
        return failed;
    }

    @Override
    public void onLocationChanged(Location location) {

        myMarker.remove();
        try {
            LatLng self = new LatLng(getLocation().getLatitude(), getLocation().getLongitude());
            myMarker = mMap.addMarker(new MarkerOptions().position(self).title("2 Start"));
        }
        catch(NullPointerException npe)
        {
            Log.d("---TEST---", "Could not create mymarker position.");
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
}
