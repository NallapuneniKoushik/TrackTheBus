package nkc.com.trackthebus;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetActivity extends AppCompatActivity implements LocationListener {
    Spinner spin;
    String busname;
    double lat, lag;
    String[] bus = {"Select Bus", "Kukatpally", "Ecil", "LB Nagar", "BHEL", "Uppal", "Chilkalguda", "Alwal", "Sagar Ring Road",
            "Nagole", "Mehdipatnam", "Hyder Nagar"};

    LocationManager lm;
    //  double lat,lag;
    double dlat, dlag;
    ProgressDialog pd, pd2;
    DatabaseReference dr;
    String user_name;
    String bname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new ProgressDialog(this);
        pd2 = new ProgressDialog(this);
       /* Intent i=getIntent();
        Bundle b1=i.getExtras();
        user_name=b1.getString("k1");
        busname=b1.getString("k2");*/

        setContentView(R.layout.activity_get);
        spin = findViewById(R.id.sp1);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bus);
        spin.setAdapter(aa);
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
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
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


    }

    @Override
    public void onLocationChanged(Location location) {
        lat=location.getLatitude();
        lag=location.getLongitude();
        Log.e("Lat_====>",lat+","+"Lag====_=>"+lag);
        Log.e("dLat_====>",+dlat+","+"dLag====_=>"+dlag);

    }

    @SuppressLint("MissingPermission")
    public void submit(View v)
    {
        pd.setMessage("Please Wait");
        pd.show();

        busname = spin.getSelectedItem().toString().trim();
        if (busname == bus[0]) {
            Toast.makeText(this, "Select Any Bus", Toast.LENGTH_SHORT).show();
        }
        else
        {
//           //Intent i=new Intent(GetActivity.this,KMap.class);
          Intent i=new Intent(GetActivity.this,NMap2.class);
          i.putExtra("bname",busname);
          //  Intent i=new Intent(GetActivity.this,NMap2.class);
            startActivity(i);
            finish();
            pd.dismiss();
        }
//        else
//        {
//            DatabaseReference dr= FirebaseDatabase.getInstance().getReference("bus_details");
//            dr.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                    for(final DataSnapshot ds:dataSnapshot.getChildren()){
//                        bname=(String)ds.getKey();
//                        if(bname.equals(busname))
//                        {
//                            //changing following code on 24/10/18
//                          DatabaseReference dr1=FirebaseDatabase.getInstance().getReference("bus_details").child( busname);
//                            dr1.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                    for(final  DataSnapshot ds:dataSnapshot.getChildren()){
//                                        user_name=(String) ds.getKey();
//                                        DatabaseReference dr2=FirebaseDatabase.getInstance().getReference("bus_details").child(busname).child(user_name);
//                                        dr2.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                                MyLOCHandler my=dataSnapshot.getValue(MyLOCHandler.class);
//                                                dlat=(double)my.getLat();
//                                                dlag=(double) my.getLag();
//
//
////                                                Intent i=new Intent(GetActivity.this,NMaActivity.class);
////                                                i.putExtra("bname",bname);
////                                                i.putExtra("user_name",user_name);
//                                               // Intent i=new Intent(GetActivity.this,Maps2Activity.class);
//                                                //work with down code form original working maps activity
//                                                  //Intent i=new Intent(GetActivity.this,MapsActivity.class);
////                                                i.putExtra("lat",lat);
////                                                i.putExtra("lag",lag);
////                                                i.putExtra("dlat",dlat);
////                                                i.putExtra("dlag",dlag);
//                                               // startActivity(i);*/
//////                                                Intent i=new Intent(GetActivity.this,UserMap.class);
////                                                i.putExtra("bname",bname);
//                                                Intent i=new Intent(GetActivity.this,KMap.class);
//                                                i.putExtra("bname",bname);
//                                              i.putExtra("user_name",user_name);
//                                               startActivity(i);
//
//                                                finish();
//                                                pd.dismiss();
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(DatabaseError databaseError) {
//
//                                            }
//                                        });
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//
//
//
//
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }
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


     @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
