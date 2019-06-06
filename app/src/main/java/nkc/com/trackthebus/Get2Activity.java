package nkc.com.trackthebus;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Get2Activity extends AppCompatActivity implements LocationListener {
    double lat, lag;
    Spinner spin;
    String busname;
    // double lat, lag;
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
        setContentView(R.layout.activity_get2);
        pd = new ProgressDialog(this);
        pd2 = new ProgressDialog(this);

        spin = findViewById(R.id.sp1);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bus);
        spin.setAdapter(aa);
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
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);


    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lag = location.getLongitude();
        Toast.makeText(this, "Location Updated", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Lat-->" + lat + "lag-->" + lag, Toast.LENGTH_LONG).show();
        Log.e("Lat====>", lat + "," + "Lag=====>" + lag);
        Log.e("dLat====>", +dlat + "," + "dLag=====>" + dlag);

    }

    public void submit(View v) {
        pd.setMessage("Please Wait");
        pd.show();

        busname = spin.getSelectedItem().toString().trim();
        if (busname == bus[0]) {
            Toast.makeText(this, "Select Any Bus", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference dr = FirebaseDatabase.getInstance().getReference("bus_details");
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                        bname = (String) ds.getKey();
                        if (bname.equals(busname)) {
                            DatabaseReference dr1 = FirebaseDatabase.getInstance().getReference("bus_details").child(busname);
                            dr1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                        user_name = (String) ds.getKey();
                                        DatabaseReference dr2 = FirebaseDatabase.getInstance().getReference("bus_details").child(busname).child(user_name);
                                        dr2.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                MyLOCHandler my = dataSnapshot.getValue(MyLOCHandler.class);
                                                dlat = (double) my.getLat();
                                                dlag = (double) my.getLag();

                                                // Intent i=new Intent(GetActivity.this,MapsActivity.class);
                                                Intent i = new Intent(Get2Activity.this, MapsActivity.class);

                                                i.putExtra("lat", lat);
                                                i.putExtra("lag", lag);
                                                i.putExtra("dlat", dlat);
                                                i.putExtra("dlag", dlag);
                                                startActivity(i);
                                                finish();
                                                pd.dismiss();

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

        }
    }
}



