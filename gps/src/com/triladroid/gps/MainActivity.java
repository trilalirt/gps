package com.triladroid.gps;

import java.util.Timer;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener; 
import android.widget.Button;

public class MainActivity extends Activity implements LocationListener{
  private TextView latituteField;
  private TextView longitudeField;
  private TextView altitudeField;
  private TextView speedField;
  LocationManager locationManager;
  private String provider;
  double latitude;
  double longitude;
//GPSTracker class
  Gpstracker gps;
  private GoogleMap map;
  private LatLng mylocation;
  private Marker mylocationmarker; 
  private Marker customlocationmarker;
  private Timer myTimer;
  private String sharetext;
  private LatLng pointt;

  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    latituteField = (TextView) findViewById(R.id.TextView02);
    longitudeField = (TextView) findViewById(R.id.TextView04);
    //speedField = (TextView) findViewById(R.id.TextView05);
    //altitudeField = (TextView) findViewById(R.id.TextView08);

    gps = new Gpstracker(MainActivity.this, this);
    
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    
    
    
    if(gps.canGetLocation()){
    	//Toast.makeText(getApplicationContext(),"can get location",Toast.LENGTH_LONG).show();
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        double speed = gps.getSpeed();
        double altitude = gps.getaltitude();
        
        latituteField.setText(String.valueOf(latitude));
        longitudeField.setText(String.valueOf(longitude));
        //speedField.setText(String.valueOf(speed));
        //altitudeField.setText(String.valueOf(altitude));
         
        mylocation = new LatLng(latitude, longitude);
        mylocationmarker = map.addMarker(new MarkerOptions().position(mylocation).title("You are here!"));
//        
//        customlocationmarker = map.addMarker(new MarkerOptions()
//		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//		.position(mylocation));
        
        Log.i("test", "Lat and Long  " + latitude + " " + longitude);
        
     // Move the camera instantly with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17));

        // Zoom in, animating the camera.
        //map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        
        // \n is for new line
        //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
    }else{
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        gps.showSettingsAlert();
    }
     
  //share
    Button getButton = (Button)findViewById(R.id.sh);
    getButton.setOnClickListener(ShareListener);
   //share
    
    //find me
    Button findMeButton = (Button) findViewById(R.id.location);
    findMeButton.setOnClickListener(FindMeListener);
    //find me
    
    //map listener
    map.setOnMapClickListener(mapClickListener);
    //map listener
    
    //marker listener    
    
    	map.setOnMarkerClickListener(blueMarkerListener);
    
    //marker listener
    
    myTimer = new Timer();
    myTimer.scheduleAtFixedRate(gps , 0, 600000); 
    
    AdView ad = (AdView) findViewById(R.id.adView);
    ad.loadAd(new AdRequest());
    
  
//    Intent intent = new Intent(Intent.ACTION_VIEW);
//    intent.setData(Uri.parse("geo:0,0?q=" + latitude + (", ")+ longitude ));
//    try {
//        startActivity(intent);
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
    
  
  }

  /* Request updates at startup */
  @Override
  protected void onResume() {
    super.onResume();
    
    //gps = new Gpstracker(MainActivity.this, this);
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    
    mylocation = new LatLng(latitude, longitude);
    mylocationmarker.setPosition(mylocation);
    
    if (customlocationmarker == null)
    {
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17));
    }
    
  }

  /* Remove the locationlistener updates when Activity is paused */
  @Override
  protected void onPause() {
    super.onPause();
    gps.stopUsingGPS();
  }
  
  @Override
  protected void onStop()
  {
	  super.onStop();
	  myTimer.cancel();
	  
  }

@Override
public void onLocationChanged(Location arg0) {
	// TODO Auto-generated method stub
	//gps.getLocation();
	
	latitude = arg0.getLatitude();
    longitude = arg0.getLongitude();
	
	latituteField.setText(String.valueOf(arg0.getLatitude()));
    longitudeField.setText(String.valueOf(arg0.getLongitude()));
    //speedField.setText(String.valueOf(gps.getSpeed()));
    //altitudeField.setText(String.valueOf(gps.getaltitude()));
    
    mylocation = new LatLng(latitude, longitude);
    mylocationmarker.setPosition(mylocation);
    
    //mylocationmarker = map.addMarker(new MarkerOptions().position(mylocation).title("You are here! 2"));
    
 // Move the camera instantly with a zoom of 15.
   // map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17));

	
}

@Override
public void onProviderDisabled(String arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onProviderEnabled(String arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	// TODO Auto-generated method stub
	
}

private OnClickListener ShareListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TextView latitudetext = (TextView)findViewById(R.id.TextView02);
			TextView longitutetext = (TextView)findViewById(R.id.TextView04);
           String strlatitude = latitudetext.getText().toString();
           String strlongitute = longitutetext.getText().toString();
           
		   Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
           emailIntent.setType("text/plain");
           if (customlocationmarker == null)
           {
        	   sharetext = "I'm at this latitude " + strlatitude + " and this longitute " + strlongitute + " and this is link to the map: http://maps.google.com.au/maps?ll=" + strlatitude + "," + strlongitute;
           }
           else
           {
        	   sharetext = "I want to share this location with you " + "http://maps.google.com.au/maps?ll=" +  pointt.latitude + "," + pointt.longitude;
           }
           
        	   emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharetext);
           //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getResources().getString(R.string.recommendation_body));
           startActivity(emailIntent);
			
		}  
	};
 
	private OnClickListener FindMeListener = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//map.clear();
			if (customlocationmarker != null)
			{
				customlocationmarker.remove();
				customlocationmarker = null;
			}
			mylocation = new LatLng(latitude, longitude);
		    mylocationmarker.setPosition(mylocation);
		    //map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17));
		    map.animateCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17), 1000, null);
			
		}  
	};
	
	private GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener()
	{
		@Override
        public void onMapClick(LatLng point) {
		//map.clear();
			if (customlocationmarker != null)
			{
		customlocationmarker.remove();
			}
		customlocationmarker = map.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		.position(point));
		
		
		customlocationmarker.setPosition(point);
		pointt = point;
			
		}
	
	};
	
	private GoogleMap.OnMarkerClickListener blueMarkerListener = new GoogleMap.OnMarkerClickListener()
	{

		@Override
		public boolean onMarkerClick(Marker arg0) {
			// TODO Auto-generated method stub
			if (customlocationmarker != null)
		    {
			customlocationmarker.remove();
		    }
			return false;
		}
		
		
	};
	
} 
