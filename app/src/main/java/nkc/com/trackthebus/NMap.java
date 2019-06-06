package nkc.com.trackthebus;

import android.Manifest;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.content.res.Resources;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.app.FragmentActivity;
        import android.os.Bundle;
        import android.support.v4.app.FragmentManager;
        import android.util.Log;
        import android.widget.Toast;

        import com.akexorcist.googledirection.DirectionCallback;
        import com.akexorcist.googledirection.model.Direction;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MapStyleOptions;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

public class NMap extends FragmentActivity implements OnMapReadyCallback,DirectionCallback,LocationListener {

    private GoogleMap mMap;
    //added
    private LatLng origin, destination;
    private LocationManager locationManager;
    LocationManager lm;
    DatabaseReference dr, dr1, dr2;
    //   private GoogleMap mMap;
    private LatLng org;
    //String bname =  "BHEL", username = "9505624932";
    String bname, username, busname;
    double ulat, ulag;
    double dlat, dlag;
    //double lat,lag;
    private GoogleApiClient mGoogleApiClient;
    private FragmentManager childFragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nmap);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        bname = b.getString("bname");
        //  username=b.getString("user_name");
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 50, this);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        fb();

    }
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
                                            Toast.makeText(NMap.this, "org==>"+origin+"dest==>"+destination, Toast.LENGTH_SHORT).show();
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
                                            if(mMap==null)
                                            {
                                                mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.bbus)));
                                                // mMap.addMarker(new MarkerOptions().position(destination));
                                            }
                                            else
                                            {
                                                mMap.clear();
                                                // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(, 15));
                                                mMap.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.fromResource(R.drawable.bbus)));
                                                //  mMap.addMarker(new MarkerOptions().position(destination));

                                            }


                                            // requestDirection();



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
    public void onLocationChanged(Location location) {

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
    public void onDirectionSuccess(Direction direction, String rawBody) {

    }
    @Override
    public void onDirectionFailure(Throwable t) {

    }
}
/*
    @Override
    public void onDirectionFailure(Throwable t) {

    }

    @Override
    public void onLocationChanged(Location location) {

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
    public void onDirectionSuccess(Direction direction, String rawBody) {

    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }
}
*/