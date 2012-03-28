package com.htc.LBSTestForCT;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.htc.HTCLBSTest.R;


public class LBSTestForCTActivity extends Activity 
    implements LocationListener {
	private static final String TAG = "LBSTestForCT";
	private LocationManager mService;
    private LocationProvider mProvider;
    
    private Button				nav;
	private Button				msa;
	private Button 				stop;
	//private TextView            mLocationView;
	private TextView			mLantiView;
	private TextView 			mLontiView;
	private boolean  			positionFix = false;
	private boolean             requestStarted =false;
	private boolean				singleshot=false;
	private long 				time;
	private boolean 			expired=false;	
	private String				product;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mService = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mProvider = mService.getProvider(LocationManager.GPS_PROVIDER);
        if (mProvider == null) {
            // FIXME - fail gracefully here
            Log.e(TAG, "Unable to get GPS_PROVIDER");
        }
        setContentView(R.layout.main);
        nav = (Button)findViewById(R.id.button1);
        msa = (Button)findViewById(R.id.button2);
        stop = (Button)findViewById(R.id.button3);
        if(!requestStarted)
        stop.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape2));
       // nav.setBackgroundColor(color.darker_gray);
        //msa.setBackgroundColor(color.widget_edittext_dark);
       // stop.setBackgroundColor(color.primary_text_dark);
        mLantiView=(TextView)findViewById(R.id.location1);
        mLontiView=(TextView)findViewById(R.id.location2);
        
       // Date now			= new Date();
    	//String date      	= (now.getYear()+1900)+"-"+(now.getMonth()+1)+"-"+now.getDate()+" "+now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
       // now.UTC(year, month, day, hour, minute, second);
         
      // if( product!=null||product.equals("primodd")||product.equals("jevelldd"))
    	   
       {

        stop.setEnabled(false);
        nav.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(mService.isProviderEnabled("gps"))
				{
					requestStarted=true;
					mLantiView.setText("");
					mLontiView.setText("");
					stop.setEnabled(true);
					msa.setEnabled(false);
					nav.setEnabled(false);
					nav.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape2));  
	        	    msa.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape2));        	    
	        	    stop.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));
					startTracking();
					
				}
				else
				{
					Toast.makeText(getApplicationContext(), R.string.gpsDisable_error, Toast.LENGTH_LONG).show();
				}

			}
        	
        });
        
        msa.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				if(mService.isProviderEnabled("gps"))
				{
					requestStarted=true;
					mLantiView.setText("");
					mLontiView.setText("");
					stop.setEnabled(true);
					nav.setEnabled(false);
					msa.setEnabled(false);
					nav.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape2)); 
	        	    msa.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape2)); 
	        	    stop.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));
					startSingleShot();
				}
				else
				{
					Toast.makeText(getApplicationContext(), R.string.gpsDisable_error, Toast.LENGTH_LONG).show();
				}
				

			}
        	
        });
        stop.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				
				onRequestStop();

			}
        	
        });
       }
      // else
      // {
    	//   Toast.makeText(getApplicationContext(), "This is for HTC product only", Toast.LENGTH_LONG).show();
      // }
         
    }
    public void onStart(){
    	super.onStart();
    	//product= System.getProperty("ro.hardware");
    	//product=SystemProperty.get("ro.hardware","unknown");
       // Log.d(TAG,"product get ="+product);
    	
    }
    public void onLocationChanged(Location location) {
    	
    	Log.d(TAG, "onLocationChanged");
    	//mLocationView.setText(Location.);
    	mLantiView.setText(doubleToString(location.getLatitude(), 7) + " ");
        mLontiView.setText( doubleToString(location.getLongitude(), 7) + " ");
        //time=location.getTime();
        
        Date now  =new Date(location.getTime());
        
        Log.d(TAG,"Current GPS time :"+location.getTime());
        String date   = (now.getYear()+1900)+"-"+(now.getMonth()+1)+"-"+now.getDate()+" "+now.getHours()+":"+now.getMinutes()+":"+now.getSeconds();
         //now.UTC(year, month, day, hour, minute, second);
        Log.d(TAG,"system  time :"+date);
        if(now.getYear()>=112&&now.getMonth()>4)
        {
        	expired=true;
        	Toast.makeText(getApplicationContext(), "Tool is expired!", Toast.LENGTH_LONG).show();
        	onRequestStop();
        }
        else
        	expired=false;

        if(singleshot)
        {
        	singleshot=false;
        	Toast.makeText(getApplicationContext(), R.string.single_shot_end, Toast.LENGTH_LONG).show();
        	onRequestStop();
        }
   	
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // ignore
    	Log.d(TAG, "onStatusChanged");
    	
    }

    public void onProviderEnabled(String provider) {
        // ignore
    	Log.d(TAG, "onProviderEnabled");
    	
    }

    public void onProviderDisabled(String provider) {
        // ignore
    	Log.d(TAG, "onProviderDisabled");
    	Toast.makeText(getApplicationContext(), R.string.gpsDisable_error, Toast.LENGTH_LONG).show();
    	onRequestStop();
    		
    }
    public void onRequestStop(){
    	if(requestStarted){
    	mService.removeUpdates(this);
    	requestStarted=false;
    	stop.setEnabled(false);
    	msa.setEnabled(true);
    	nav.setEnabled(true);
    	nav.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape)); 
	    msa.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape));
	    stop.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape2));

    	}
    }

    
    public void startTracking(){
    	
    	mService.requestLocationUpdates(mProvider.getName(), 1000, 0.0f, this);
    }
    
    public void startSingleShot(){
    	singleshot=true;
    	mService.requestSingleUpdate(mProvider.getName(), this, null);
    
    }
    private static String doubleToString(double value, int decimals) {
        String result = Double.toString(value);
        // truncate to specified number of decimal places
        int dot = result.indexOf('.');
        if (dot > 0) {
            int end = dot + decimals + 1;
            if (end < result.length()) {
                result = result.substring(0, end);
            }
        }
        return result;
    }

}