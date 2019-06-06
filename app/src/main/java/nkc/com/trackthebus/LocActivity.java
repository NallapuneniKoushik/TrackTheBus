package nkc.com.trackthebus;

import android.app.ProgressDialog;
import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.content.pm.PackageManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LocActivity extends AppCompatActivity implements LocationListener {

    LocationManager lm;
    double lat,lag;
    ProgressDialog pd,pd2;
    DatabaseReference dr;
    String user_name,busname;

    //newly added
    private LocationManager locationManager;

    private LocationListener locationListener;

    private String origin;
    private String destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc);
        pd = new ProgressDialog(this);
        pd2 = new ProgressDialog(this);
        Intent i=getIntent();
        Bundle b1=i.getExtras();
        user_name=b1.getString("k1");
        busname=b1.getString("k2");


    }
    public void update(View v)
    {
//        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,250,this);
        requestLocation();

    }


    @Override
    public void onLocationChanged(Location location) {


           lat = location.getLatitude();
           lag = location.getLongitude();
           MyLOCHandler myLOC = new MyLOCHandler(lat, lag);
           dr = FirebaseDatabase.getInstance().getReference("bus_details").child(busname).child(user_name);
           dr.setValue(myLOC);
           Toast.makeText(this, "Location updated ", Toast.LENGTH_SHORT).show();
           Toast.makeText(this, "Lat-->" + lat + "Lag-->" + lag, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Gps on", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Gps off,Please Turn on", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    private void requestLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(LocActivity.this, "Please enable Location Services", Toast.LENGTH_LONG).show();
            finish();
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
              //  origin = location.getLatitude() + "," + location.getLongitude();
                lat = location.getLatitude();
                lag = location.getLongitude();
                Toast.makeText(LocActivity.this, "Location Updated", Toast.LENGTH_SHORT).show();
                Toast.makeText(LocActivity.this, "lat-->"+lat+"lag--->"+lag, Toast.LENGTH_SHORT).show();
                // sendRequest();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
}
