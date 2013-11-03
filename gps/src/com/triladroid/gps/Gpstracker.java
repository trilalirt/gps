package com.triladroid.gps;

import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class Gpstracker extends TimerTask  {

	
	private LocationListener listener;
	
	private final Context mContext;
	 
    // flag for GPS status
    boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    // flag for GPS status
    boolean canGetLocation = false;
 
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    double altitude;
    double speed;
    
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 10000; // 1\4 minute
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
 
    public Gpstracker(Context context, LocationListener listener) {
        this.mContext = context;
        this.listener = listener;
    }
 
    public void register() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Service.LOCATION_SERVICE);
 
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
        if (!isGPSEnabled && !isNetworkEnabled) {
            	Log.i("test", "no network provider is enabled");
            	showSettingsAlert();
            } else {
                if (isNetworkEnabled) {
                	Log.i("test", "First get location from Network Provider");
                	
                	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
                			MIN_TIME_BW_UPDATES, 
                			MIN_DISTANCE_CHANGE_FOR_UPDATES, 
                			listener);
                }
                if (isGPSEnabled) {
                	Log.i("test", " if GPS Enabled get lat/long using GPS Services");

                	locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, listener);
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void remove()
    {
    	if(locationManager != null){
            locationManager.removeUpdates(listener);
            
        }  
    }
     
    /*
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(listener);
            
        }       
    }
     
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();  
        }
        
        return latitude;
        
    }
     
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
            
        }
         return longitude;
    }
     
    public double getSpeed(){
        if(location != null){
            speed = location.getSpeed();
            
        }
        
        return speed;
    }
    
    public double getaltitude(){
        if(location != null){
            altitude = location.getAltitude();
            
            
        }
        
        return altitude;
    }
    
    
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
    */ 
    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//remove();
		//register();
	}
	
	

	

}
