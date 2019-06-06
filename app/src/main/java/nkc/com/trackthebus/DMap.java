package nkc.com.trackthebus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.Manifest;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ebanx.swipebtn.OnStateChangeListener;
import com.ebanx.swipebtn.SwipeButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DMap extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    private TextView txtcoordinates;
    private Button btnGetCoordinates, btnLocationUpdates;
    private boolean mRequestLocatonUpdates = false;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    SwipeButton ride;

    private static int UPDATE_INTERVAL = 5000;//SEC
    private static int FASTEST_INTERVAL = 3000;//SEC
    private static int DISPLACEMENT = 10;//METERS
    String user_name,busname;
    ProgressDialog pd,pd2;
    DatabaseReference dr;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices())
                        buildGoogleApiClient();
                }
                break;


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmap);

        //txtcoordinates = (TextView) findViewById(R.id.tv);
        //btnGetCoordinates = (Button) findViewById(R.id.get);
        btnLocationUpdates = (Button) findViewById(R.id.loc);
        pd = new ProgressDialog(this);
        pd2 = new ProgressDialog(this);
        Intent i=getIntent();
        Bundle b1=i.getExtras();
        ride = (SwipeButton)findViewById(R.id.button_create);
        user_name=b1.getString("k1");
        busname=b1.getString("k2");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //run time request
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, MY_PERMISSIONS_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
            }
        }
//        btnGetCoordinates.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                displayLocation();
//            }
//
//
//        });
ride.setOnStateChangeListener(new OnStateChangeListener() {
    @Override
    public void onStateChange(boolean active) {
        togglePeriodicLocationUpdates();
        displayLocation();
    }
});

        btnLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // togglePeriodicLocationUpdates();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {

        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        if(mGoogleApiClient!=null)
        {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private void togglePeriodicLocationUpdates()
    {
        if(!mRequestLocatonUpdates)
        {
            btnLocationUpdates.setText("Stop Location");
            mRequestLocatonUpdates=true;
            startLocationUpdates();

        }
        else
        {
            btnLocationUpdates.setText("Start Location");
            mRequestLocatonUpdates=false;
            stopLocationUpdates();
        }
    }
    private void displayLocation() {
//        Toast.makeText(this, "user_name: "+user_name+" busname: "+busname, Toast.LENGTH_SHORT).show();
//        dr = FirebaseDatabase.getInstance().getReference("bus_details").child(busname).child(user_name);
//        MyLOCHandler myLOC = new MyLOCHandler(0.00, 1.00);
//        dr.setValue(myLOC);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            //txtcoordinates.setText(latitude + "," + longitude);
            MyLOCHandler myLOC = new MyLOCHandler(latitude, longitude);
            dr = FirebaseDatabase.getInstance().getReference("bus_details").child(busname).child(user_name);
            dr.setValue(myLOC);
//            Toast.makeText(this, "Location updated ", Toast.LENGTH_SHORT).show();

            Toast.makeText(this, "Lat-->" + latitude + "Lag-->" + longitude, Toast.LENGTH_LONG).show();


        } else {
            //txtcoordinates.setText("Could not fetch location ,Enable it");
            Toast.makeText(this, "Could not fetch location ,Enable it", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        }

    }


    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);

    }


    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultcode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultcode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultcode)) {
                GooglePlayServicesUtil.getErrorDialog(resultcode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(this, "This Device IS Not Supported", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if (mRequestLocatonUpdates)
            startLocationUpdates();

    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling


            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        displayLocation();
    }
}





