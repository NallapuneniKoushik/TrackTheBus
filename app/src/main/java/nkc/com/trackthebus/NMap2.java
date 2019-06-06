
//https://drive.google.com/drive/folders/0B0vGAkJc-5MqVGJiSEotRTJiVXc


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
import android.widget.RelativeLayout;
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

import nkc.com.trackthebus.R;

public class NMap2 extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnPolygonClickListener,
        GoogleMap.OnPolylineClickListener, DirectionCallback,LocationListener{

   private String serverKey = "AIzaSyAFDb4FHKQB3k7zI_YLFpm_RNMYVcUKtvE";
//    private String serverKey="AIzaSyCAcfy-02UHSu2F6WeQ1rhQhkCr51eBL9"; //mojo
//    private LatLng origin = new LatLng(17.52675125,78.39217528);
//    private LatLng destination = new LatLng(17.380249,78.382483);

    private LatLng origin, destination;
    private LocationManager locationManager;
    LocationManager lm;
    DatabaseReference dr,dr1,dr2;
      private GoogleMap mMap;
    private LatLng org;
    String bname,username,busname;
    double ulat, ulag;
    double dlat, dlag;
    int count=0;
    //double lat,lag;
    private GoogleApiClient mGoogleApiClient;
    private FragmentManager childFragmentManager;

    RelativeLayout rl;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmap2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
       // rl=findViewById(R.id.rl);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        bname=b.getString("bname");

       // change();
//        Intent i = getIntent();
//        Bundle b = i.getExtras();
//        // dlatt = b.getSt("dlat");
//        //dlagg = b.getString("dlag");
//        ulat=b.getDouble("lat");
//        ulag=b.getDouble("lag");
//        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
//
//
//        dlat=b.getDouble("dlat");
//        dlag=b.getDouble("dlag");
//        Toast.makeText(this, "dlat---->"+dlat+"dlag----->"+dlag, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "ulat---->"+ulat+"ulag----->"+ulag, Toast.LENGTH_SHORT).show();

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

        mMap.setMaxZoomPreference(16);
//rl.setBackgroundColor(Color.YELLOW);

        try {
            boolean isSuccess = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style_map)
            );
            if (!isSuccess)
                Log.e("ERROR", "Map style Load failed");
        } catch (Resources.NotFoundException ex) {
            ex.printStackTrace();
        }




//        try{
//
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

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
          // change();
       fb();  //kept commmented on 1/11/18
        //Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");




    }

//    public  void change()
//    {
//           if(count==0)
//           {
//               mMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(this, R.raw.uber_style_map)
//           );
//               count++;
//               //onMapReady(mMap);
//               fb();
//           }
//           else if(count==1)
//           {
//               mMap.setMapStyle(
//                       MapStyleOptions.loadRawResourceStyle(this, R.raw.dark)
//               );
//               count++;
//               //onMapReady(mMap);
//               fb();
//           }
//           else if(count==2)
//           {
//               mMap.setMapStyle(
//                       MapStyleOptions.loadRawResourceStyle(this, R.raw.night)
//               );
//               count++;
//               //onMapReady(mMap);
//               fb();
//           }
//           else if(count==3)
//           {
//               mMap.setMapStyle(
//                       MapStyleOptions.loadRawResourceStyle(this, R.raw.retro)
//               );
//               count++;
//               //onMapReady(mMap);
//               fb();
//           }
//           else if(count==4)
//           {
//               mMap.setMapStyle(
//                       MapStyleOptions.loadRawResourceStyle(this, R.raw.silver)
//               );
//               count++;
//               //onMapReady(mMap);
//               fb();
//           }
//           else if(count==5)
//           {
//               mMap.setMapStyle(
//                       MapStyleOptions.loadRawResourceStyle(this, R.raw.aubergine)
//               );
//               count=0;
//               //onMapReady(mMap);
//               fb();
//           }
//
//    }

//changed on 1/11/18
//
//    public void change(View v)
//    {
//
//    }
 //till here for 1/11/18

    public void fb() {
        dr= FirebaseDatabase.getInstance().getReference("bus_details");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot ds:dataSnapshot.getChildren())
                {
                    busname=(String)ds.getKey();
                    if(busname.equals(bname))
                    {
                        dr1=FirebaseDatabase.getInstance().getReference("bus_details").child(bname);
                        dr1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(final DataSnapshot ds:dataSnapshot.getChildren())
                                {
                                    username=ds.getKey();
                                    dr2=FirebaseDatabase.getInstance().getReference("bus_details").child(bname).child(username);
                                    dr2.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            MyLOCHandler my = dataSnapshot.getValue(MyLOCHandler.class);
                                            dlat = (double) my.getLat();
                                            dlag = (double) my.getLag();
                                            origin = new LatLng(dlat, dlag);

                                            Location loc1=getLocation();
                                            ulat=loc1.getLatitude();
                                            ulag=loc1.getLongitude();
                                            destination=new LatLng(ulat,ulag);
                                            //          destination=new LatLng(loc1.getLatitude(),loc1.getLongitude());
                                            Toast.makeText(NMap2.this, "org==>"+origin+"dest==>"+destination, Toast.LENGTH_SHORT).show();
                                            Log.e("origin==>",""+origin);
                                            Log.e("dest==>",""+destination);

                                            //function calculates disance between 2 locations
                                            Location user=new Location("");
                                            user.setLatitude(ulat);
                                            user.setLongitude(ulag);

                                            Location driver=new Location("");
                                            driver.setLatitude(dlat);
                                            driver.setLongitude(dlag);
                                            // distance(user,driver);
                                            //  Toast.makeText(KMap.this, "Distance is"+distance(user,driver), Toast.LENGTH_SHORT).show();
                                            double dbu=(driver.distanceTo(user))/1000;//returns in metres
                                            double dkm=dbu/1000;


                                            //  Toast.makeText(KMap.this, "Diatance  dkm is"+dkm, Toast.LENGTH_SHORT).show();
//                                            float dist1 = (float) Math.abs(dlat-ulat);
//                                            float dist2 = dist1 * 111.2f;
//                                            Toast.makeText(KMap.this, "dist is"+dist2, Toast.LENGTH_SHORT).show();

                                            //  addNotification();

                                            requestDirection();

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        dr = FirebaseDatabase.getInstance().getReference("bus_details").child(bname).child(username);
//        dr.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                MyLOCHandler my = dataSnapshot.getValue(MyLOCHandler.class);
//                dlat = (double) my.getLat();
//                dlag = (double) my.getLag();
//                origin = new LatLng(dlat, dlag);
//                //destination = new LatLng(ulat, ulag);
//
//                Location loc1=getLocation();
//                destination=new LatLng(loc1.getLatitude(),loc1.getLongitude());
//                Toast.makeText(KMap.this, "org==>"+origin+"dest==>"+destination, Toast.LENGTH_SHORT).show();
//                Log.e("origin==>",""+origin);
//                Log.e("dest==>",""+destination);
//
//                requestDirection();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//
//            }
//        });
//        dr.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                MyLOCHandler my = dataSnapshot.getValue(MyLOCHandler.class);
//                dlat = (double) my.getLat();
//                dlag = (double) my.getLag();
//                origin = new LatLng(dlat, dlag);
//                //destination = new LatLng(ulat, ulag);
//
//                Location loc1=getLocation();
//                destination=new LatLng(loc1.getLatitude(),loc1.getLongitude());
//                Toast.makeText(KMap.this, "org==>"+origin+"dest==>"+destination, Toast.LENGTH_SHORT).show();
//                Log.e("origin==>",""+origin);
//                Log.e("dest==>",""+destination);
//
//                requestDirection();
//
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
    public void requestDirection() {
        //  Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }
    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
    }



    public Location getLocation() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


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
        Route route = direction.getRouteList().get(0);
        //original code added on 26/10/18 2:18 AM
//            mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.bus)));
//            mMap.addMarker(new MarkerOptions().position(destination));
        if(mMap==null)
        {
            mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.bbus)));
            mMap.addMarker(new MarkerOptions().position(destination));
        }
        else
        {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.bbus)));
            mMap.addMarker(new MarkerOptions().position(destination));

        }
//till here added
        ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
        mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
        setCameraWithCoordinationBounds(route);


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
