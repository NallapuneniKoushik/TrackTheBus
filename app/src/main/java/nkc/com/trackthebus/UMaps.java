package nkc.com.trackthebus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
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
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UMaps extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener,
        GoogleMap.OnPolylineClickListener, DirectionCallback, LocationListener {

    private String serverKey = "AIzaSyAFDb4FHKQB3k7zI_YLFpm_RNMYVcUKtvE";
    //    private LatLng origin = new LatLng(17.52675125,78.39217528);
//    private LatLng destination = new LatLng(17.380249,78.382483);
    private LatLng origin, destination;
    private LocationManager locationManager;
    LocationManager lm;
    DatabaseReference dr;
    private GoogleMap mMap;
    private LatLng org;
    String bname = "Kukatpally", username = "9505624932";
    double ulat, ulag;
    double dlat, dlag;
    //double lat,lag;
    private GoogleApiClient mGoogleApiClient;
    private FragmentManager childFragmentManager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umaps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//              .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(UMaps.this);
  //     SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager();
//        mapFragment.getMapAsync(this);
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.umap);
        mapFrag.getMapAsync(this);
//        Intent i = getIntent();
//        Bundle b = i.getExtras();
//        bname=b.getString("bname");
////        username=b.getString("user_name");
//        lat=b.getDouble("lat");
//        lag=b.getDouble("lag");
//
//        dlat = b.getDouble("dlat");
//        dlag = b.getDouble("dlag");
//        origin = new LatLng(dlat, dlag);
//        destination = new LatLng(ulat, ulag);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        //  fb();

//        dlat=b.getDouble("dlat");
//        dlag=b.getDouble("dlag");
//        Toast.makeText(this, "dlat@>" + dlat + "dlag@>" + dlag, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "ulat@>" + ulat + "ulag@>" + ulag, Toast.LENGTH_SHORT).show();

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMaxZoomPreference(15);
        //code added on 24/10/18 12:33 am
        try {
            boolean isSuccess = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style_map)
            );
            if (!isSuccess)
                Log.e("ERROR", "Map style Load failed");
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
        }
        mMap.setMyLocationEnabled(true);

         fb();
       // requestDirection();

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


    public void fb() {
        dr = FirebaseDatabase.getInstance().getReference("bus_details").child(bname).child(username);
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                MyLOCHandler my = dataSnapshot.getValue(MyLOCHandler.class);
                dlat = (double) my.getLat();
                dlag = (double) my.getLag();
                origin = new LatLng(dlat, dlag);
                destination = new LatLng(ulat, ulag);

                Location loc1=getLocation();
                destination=new LatLng(loc1.getLatitude(),loc1.getLongitude());
                Toast.makeText(UMaps.this, "org==>"+origin+"dest==>"+destination, Toast.LENGTH_SHORT).show();
                Log.e("origin==>",""+origin);
                Log.e("dest==>",""+destination);
                requestDirection();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        // requestLocation();
        // org=new LatLng(lat,lag);

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
//        ulat = location.getLatitude();
//        ulag = location.getLongitude();
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

    public Location getLocation() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

            }
            Location lastKnownLocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocationGPS != null) {
                return lastKnownLocationGPS;
            } else {
                Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
//                System.out.println("1::"+loc);----getting null over here
//                System.out.println("2::"+loc.getLatitude());
                return loc;
            }
        } else {
            return null;
        }
    }

    public FragmentManager getChildFragmentManager() {
        return childFragmentManager;
    }
}
