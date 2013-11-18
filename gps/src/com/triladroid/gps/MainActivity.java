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
  private TextView markerLat;
  private TextView markerLng;
  LocationManager locationManager;
  private String provider;
  double latitude;
  double longitude;
//GPSTracker class
  Gpstracker gps;
  private GoogleMap map;
  private LatLng mylocation;
  private Location mylocationlocation;
  private Marker mylocationmarker; 
  private Marker customlocationmarker;
  private Timer myTimer;
  private String sharetext;
  private LatLng pointt;
  
  //private Location gpslocation;

  
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
    map.getUiSettings().setZoomControlsEnabled(false);
    /*
    if(gps.canGetLocation()){
    	//Toast.makeText(getApplicationContext(),"can get location",Toast.LENGTH_LONG).show();
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        double speed = gps.getSpeed();
        double altitude = gps.getaltitude();
        
        //gpslocation = gps.getLocation();
        
        String strLongitude = "Longitude: " + MyConvert(longitude);
        String strLatitude = "Latitude: " + MyConvert(latitude);
        
        latituteField.setText(strLatitude);
        longitudeField.setText(strLongitude);
        
        mylocation = new LatLng(latitude, longitude);
        mylocationmarker = map.addMarker(new MarkerOptions().position(mylocation).title("You are here!"));
        
        Log.i("test", "Lat and Long  " + latitude + " " + longitude);
        
     // Move the camera instantly with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17));

     // Zoom in, animating the camera.
     //map.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        
    }
    else{
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        gps.showSettingsAlert();
    }*/
    
    //mylocation = new LatLng(0,0);
    mylocationmarker = map.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("You are here!"));
     
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
    
    //timer task
    myTimer = new Timer();
    myTimer.scheduleAtFixedRate(gps , 0, 300000); 
    //timer task
    
    //ads
    AdView ad = (AdView) findViewById(R.id.adView);
    ad.loadAd(new AdRequest());
    //ads
  
  }

  /* Request updates at startup */
  @Override
  protected void onResume() {
    super.onResume();
    /*
    Location gpslocation = gps.getLocation();
    latitude = gps.getLatitude();
    longitude = gps.getLongitude();
    //gps = new Gpstracker(MainActivity.this, this);
    map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    
    mylocation = new LatLng(latitude, longitude);
    mylocationmarker.setPosition(mylocation);
    
    if (gpslocation == null)
    {
    Log.i("test", "location is null "  );
    }
    
    String strLongitude = "Longitude: " + MyConvert(gpslocation.getLongitude());
    String strLatitude = "Latitude: " + MyConvert(gpslocation.getLatitude());
    
    latituteField.setText(strLatitude);
    longitudeField.setText(strLongitude);
    */
    gps.register();
    
    
    
    if (customlocationmarker == null && mylocation != null)
    {
    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17));
    }
    
  }

  /* Remove the locationlistener updates when Activity is paused */
  @Override
  protected void onPause() {
    super.onPause();
    //gps.stopUsingGPS();
    gps.remove();
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
	
	
	if (getBetterLocation(arg0, mylocationlocation) == arg0)
	{
	
	LatLng mylatlang = new LatLng(arg0.getLatitude(), arg0.getLongitude());
	if (mylocation == null)
	{
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylatlang, 17));
	}
	
	latitude = arg0.getLatitude();
    longitude = arg0.getLongitude();
    mylocationlocation = arg0;
	
    String strLongitude = "Longitude: " + MyConvert(arg0.getLongitude());
    String strLatitude = "Latitude: " + MyConvert(arg0.getLatitude());
    
    latituteField.setText(strLatitude);
    longitudeField.setText(strLongitude);
    
    mylocation = new LatLng(latitude, longitude);
    mylocationmarker.setPosition(mylocation);
    
    //Toast.makeText(getApplicationContext(),"ONLOCATIONCHANGED",Toast.LENGTH_LONG).show();
    //mylocationmarker = map.addMarker(new MarkerOptions().position(mylocation).title("You are here! 2"));
    
 // Move the camera instantly with a zoom of 15.
   // map.moveCamera(CameraUpdateFactory.newLatLngZoom(mylocation, 17));
	}
	
}


protected Location getBetterLocation(Location newLocation, Location currentBestLocation) {
    if (currentBestLocation == null) {
        // A new location is always better than no location
        return newLocation;
    }

    // Check whether the new location fix is newer or older
    long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
    boolean isSignificantlyNewer = timeDelta > 30000;
    boolean isSignificantlyOlder = timeDelta < -30000;
    boolean isNewer = timeDelta > 0;

    // If it's been more than two minutes since the current location, use the new location
    // because the user has likely moved.
    if (isSignificantlyNewer) {
        return newLocation;
    // If the new location is more than two minutes older, it must be worse
    } else if (isSignificantlyOlder) {
        return currentBestLocation;
    }

    // Check whether the new location fix is more or less accurate
    int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
    boolean isLessAccurate = accuracyDelta > 0;
    boolean isMoreAccurate = accuracyDelta < 0;
    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

    // Check if the old and new location are from the same provider
    //boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),currentBestLocation.getProvider());

    boolean isFromSameProvider = newLocation.getProvider() == currentBestLocation.getProvider();
    
    // Determine location quality using a combination of timeliness and accuracy
    if (isMoreAccurate) {
        return newLocation;
    } else if (isNewer && !isLessAccurate) {
        return newLocation;
    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
        return newLocation;
    }
    return currentBestLocation;
}


@Override
public void onProviderDisabled(String arg0) {
	// TODO Auto-generated method stub
	gps.remove();
	gps.register();
}

@Override
public void onProviderEnabled(String arg0) {
	// TODO Auto-generated method stub
	gps.remove();
	gps.register();
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
			
		  //gpslocation = gps.getLocation();
	      //latitude = gps.getLatitude();
	      //longitude = gps.getLongitude();
			
		  TextView latitudetext = (TextView)findViewById(R.id.TextView02);
		  TextView longitutetext = (TextView)findViewById(R.id.TextView04);
          String strlatitude = latitudetext.getText().toString();
          String strlongitute = longitutetext.getText().toString();
          //strlongitute = strlongitute.substring(0, strlongitute.indexOf('.'));
          //strlatitude = strlatitude.substring(0, strlatitude.indexOf('.')); 
          
           Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
           emailIntent.setType("text/plain");
           if (customlocationmarker == null)
           {
        	   
        	   
        	   sharetext = "I'm at this latitude " + strlatitude + " and this longitute " + strlongitute + " and this is link to the map: http://maps.google.com/maps?ll=" + latitude + "," + longitude;
           }
           else
           {
        	   sharetext = "I want to share this location with you " + "http://maps.google.com/maps?ll=" +  pointt.latitude + "," + pointt.longitude;
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
				markerLat = (TextView) findViewById(R.id.TextView05);
				markerLat.setVisibility(View.GONE);
				markerLng = (TextView) findViewById(R.id.TextView06);
				markerLng.setVisibility(View.GONE);
				
				Button getButton = (Button)findViewById(R.id.sh);
				getButton.setText("Share your location");
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
		customlocationmarker = null;
		
		
			}
		customlocationmarker = map.addMarker(new MarkerOptions()
		.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
		.position(point));
		
		
		customlocationmarker.setPosition(point);
		pointt = point;
		
		String markerLatText = "Marker Latiude: " + MyConvert(pointt.latitude);
        String markerLngText = "Marker Longitude: " + MyConvert(pointt.longitude);
		
		markerLat = (TextView) findViewById(R.id.TextView05);
		markerLat.setText(markerLatText);
		markerLat.setVisibility(View.VISIBLE);
		
		markerLng = (TextView) findViewById(R.id.TextView06);
		markerLng.setText(markerLngText);
		markerLng.setVisibility(View.VISIBLE);
		
		Button getButton = (Button)findViewById(R.id.sh);
		getButton.setText("Share blue marker location");
		
		
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
			customlocationmarker = null;
			
			Button getButton = (Button)findViewById(R.id.sh);
			getButton.setText("Share your location");
			
			markerLat = (TextView) findViewById(R.id.TextView05);
			markerLat.setVisibility(View.GONE);
			
			markerLng = (TextView) findViewById(R.id.TextView06);
			markerLng.setVisibility(View.GONE);
		    }
			return false;
		}
		
		
	};
	
	private String MyConvert(double value)
	{
		String converttosec = Location.convert(value , Location.FORMAT_SECONDS);
		
		 if (converttosec.indexOf('.') != -1)
		    {
			 converttosec = converttosec.substring(0, converttosec.indexOf('.'));
		    }

		 return converttosec;
		
	};
	
} 
