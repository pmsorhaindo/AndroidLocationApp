package com.mikey;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikey.travelapp.R;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;


public class MainActivity extends android.support.v4.app.FragmentActivity
        implements OnMapClickListener, GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener{
        
        private GoogleMap mMap;
        LocationClient mLocationClient;
        
        @Override
        protected void onCreate(Bundle savedInstanceState) 
        {
        	
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
               
                
                InetAddress serverAddress = null;
    			try {
    				serverAddress = InetAddress.getByName("10.0.41.83");
    			} catch (UnknownHostException e) {
    				System.err.println("Unable to determine server address!");
    				e.printStackTrace();
    			}
            	
            	Thread comms = new Thread(new AppComms(serverAddress),"comms");
            	
            	comms.start();
            	
            	 Toast.makeText(this, "Running communication thread!", Toast.LENGTH_SHORT).show();
                
                
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                       .getMap();
                
            mMap.setOnMapClickListener(this);            
            //  Google Play services check
            int resultCode =
                        GooglePlayServicesUtil.
                                isGooglePlayServicesAvailable(this);
                    
                    if (ConnectionResult.SUCCESS == resultCode) {
                    Log.d("Location Updates",
                            "Google Play services is available.");
                    }
                    else{
                            Log.d("Location Updates",
                                    "Google Play services is unavailable.");
                            }
                    
                    mLocationClient = new LocationClient(this, this, this);
         }
         protected void onStart() {
        super.onStart();
         // Connect the client.
        mLocationClient.connect();
        
        
        
        //mMap.addMarker(new MarkerOptions().position(
        //                new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude())).
                //                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
        
        
     }
         protected void onStop() {
                // Disconnecting the client invalidates it.
                mLocationClient.disconnect();
                super.onStop();
              }
        
        public void onMapClick(LatLng point) {
                mMap.addMarker(new MarkerOptions().position(point).
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
                
                try{
                Location mCurrentLocation = mLocationClient.getLastLocation();
                mMap.addMarker(new MarkerOptions().position(
                                        new LatLng(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude())).
                                                icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
           } 
             catch (Exception e) 
                     {
                             Log.d("Location Updates", "Caught Exception: " + e.getMessage());
                             Toast.makeText(this, "GetLastLocation failed"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                     } 
                 }


        @Override
        public void onConnectionFailed(ConnectionResult result) {
                Log.d("Location Updates",
                    "Connection failed.");
                Toast.makeText(this, "Connection failed", Toast.LENGTH_SHORT).show();
                
        }


        @Override
        public void onConnected(Bundle connectionHint) {
                Log.d("Location Updates",
                    "Connected.");
                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
                
        }


        @Override
        public void onDisconnected() {
                Log.d("Location Updates",
                    "Disconnected.");
                
        }
                
}