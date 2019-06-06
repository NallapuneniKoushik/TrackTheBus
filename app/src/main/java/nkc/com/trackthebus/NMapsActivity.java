package nkc.com.trackthebus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener,
        GoogleMap.OnPolylineClickListener, DirectionCallback,LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    private static final int MY_PERMISSIONS_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 7172;
    private TextView txtcoordinates;
    private Button btnGetCoordinates, btnLocationUpdates;
    private boolean mRequestLocatonUpdates = false;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;//SEC
    private static int FASTEST_INTERVAL = 3000;//SEC
    private static int DISPLACEMENT = 10;//METERS

    private String serverKey = "AIzaSyAFDb4FHKQB3k7zI_YLFpm_RNMYVcUKtvE";
//    private LatLng origin = new LatLng(17.52675125,78.39217528);
//    private LatLng destination = new LatLng(17.380249,78.382483);
    LatLng origin,destination;

    private LocationManager locationManager;
    LocationManager lm;
    private LocationListener locationListener;
    private GoogleMap mMap;
    private LatLng org;
    String bname,user_name;
    double ulat,ulag;
    double dlat,dlag;
    double lat,lag;
   // private GoogleApiClient mGoogleApiClient;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmaps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        bname=b.getString("bname");
        user_name=b.getString("user_name");
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

            get();
           // loc();

        }

       /* btnGetCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayLocation();
            }


        });*/

       /* btnLocationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePeriodicLocationUpdates();
            }
        });*/

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);



    }
public void get()
{
    displayLocation();
}

public void loc()
{
    togglePeriodicLocationUpdates();
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
           // btnLocationUpdates.setText("Stop Location");
            mRequestLocatonUpdates=true;
            startLocationUpdates();

        }
        else
        {
           // btnLocationUpdates.setText("Start Location");
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
            final double latitude = mLastLocation.getLatitude();
            final double longitude = mLastLocation.getLongitude();

            DatabaseReference dr2=FirebaseDatabase.getInstance().getReference("bus_details").child(bname).child(user_name);
            dr2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    MyLOCHandler my=dataSnapshot.getValue(MyLOCHandler.class);
                    dlat=(double)my.getLat();
                    dlag=(double) my.getLag();
                   destination=new LatLng(dlat,dlag);
                   origin=new LatLng(latitude,longitude);
                    Toast.makeText(NMapsActivity.this, "ulat-->"+latitude+"ulag-->"+longitude, Toast.LENGTH_SHORT).show();
                    Toast.makeText(NMapsActivity.this, "dlat-->"+dlat+"dlag-->"+dlag, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
          //  MyLOCHandler myLOC = new MyLOCHandler(latitude, longitude);
//            dr = FirebaseDatabase.getInstance().getReference("bus_details").child(busname).child(user_name);
//            dr.setValue(myLOC);
////            Toast.makeText(this, "Location updated ", Toast.LENGTH_SHORT).show();

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


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMaxZoomPreference(15);
        //code added on 24/10/18 12:33 am
        try{
            boolean isSuccess=googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this,R.raw.uber_style_map)
            );
            if(!isSuccess)
                Log.e("ERROR","Map style Load failed");
        }
        catch (Resources.NotFoundException ex)
        {
            ex.printStackTrace();
        }
        mMap.setMyLocationEnabled(true);
        requestDirection();






    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
    }

    public void requestDirection() {
        //  Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }





    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {

        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
            mMap.addMarker(new MarkerOptions().position(destination));

            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
            setCameraWithCoordinationBounds(route);


        } else {


        }

    }



    @Override
    public void onDirectionFailure(Throwable t) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        displayLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
