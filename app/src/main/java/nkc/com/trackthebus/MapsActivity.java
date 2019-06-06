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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener,
        GoogleMap.OnPolylineClickListener, DirectionCallback,LocationListener{

    private String serverKey = "AIzaSyAFDb4FHKQB3k7zI_YLFpm_RNMYVcUKtvE";
//    private LatLng origin = new LatLng(17.52675125,78.39217528);
//    private LatLng destination = new LatLng(17.380249,78.382483);
 private LatLng origin,destination;
    private LocationManager locationManager;
    LocationManager lm;
    private LocationListener locationListener;
    private GoogleMap mMap;
    private LatLng org;
   String bname,username;
    double ulat,ulag;
   double dlat,dlag;
    double lat,lag;
    private GoogleApiClient mGoogleApiClient;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent i = getIntent();
        Bundle b = i.getExtras();
//        bname=b.getString("bname");
//        username=b.getString("user_name");
        lat=b.getDouble("lat");
        lag=b.getDouble("lag");

        dlat = b.getDouble("dlat");
        dlag = b.getDouble("dlag");
        origin=new LatLng(dlat,dlag);
        destination=new LatLng(ulat,ulag);

              lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);


//        dlat=b.getDouble("dlat");
//        dlag=b.getDouble("dlag");
        Toast.makeText(this, "dlat@>"+dlat+"dlag@>"+dlag, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "ulat@>"+lat+"ulag@>"+lag, Toast.LENGTH_SHORT).show();

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
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
       /* LatLng sydney = new LatLng(dlat, dlag);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker At Driver"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16.0f));
        */

        /*  if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/

       mMap.setMyLocationEnabled(true);
       /* mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,15.0f));

        Polyline polyline=mMap.addPolyline(new PolylineOptions().clickable(true).add(
                new LatLng(dlat,dlag),
                new LatLng(17.4392106,78.4468465)
        ));
        polyline.setEndCap(new RoundCap());
        polyline.setWidth(12);
        polyline.setColor(0xff00fff0);
        polyline.setJointType(JointType.ROUND);
        mMap.setOnPolygonClickListener(this);
        mMap.setOnPolylineClickListener(this);
*/
       //the following code is added in 24/10/18 7:19pm
//        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if(location!=null) {
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            CameraUpdate cameraupdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//            mMap.animateCamera(cameraupdate);
//        }
//        else{
//            Toast.makeText(MapsActivity.this, "Location is unavailable", Toast.LENGTH_SHORT).show();
//
//        }
        requestDirection();

        //Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");




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
           // requestLocation();
           // org=new LatLng(lat,lag);

       // Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
            mMap.addMarker(new MarkerOptions().position(destination));

            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
            setCameraWithCoordinationBounds(route);


        } else {
           // Snackbar.make(btnRequestDirection, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onDirectionFailure(Throwable t) {

    }

    @Override
    public void onLocationChanged(Location location) {
        ulat=location.getLatitude();
        ulag=location.getLongitude();
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
}
