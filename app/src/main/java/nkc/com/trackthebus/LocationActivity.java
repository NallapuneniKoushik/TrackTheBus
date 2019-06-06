package nkc.com.trackthebus;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocationActivity extends AppCompatActivity implements LocationListener{
    LocationManager lm;
    long lat,lag;
    ProgressDialog pd,pd2;
    DatabaseReference dr;
    String user_name,busname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        Intent i=getIntent();
        Bundle b1=i.getExtras();
        user_name=b1.getString("k1");
        busname=b1.getString("k2");

    }

    public void update(View v)
    {
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);


    }


    @Override
    public void onLocationChanged(Location location) {
        lat=(long)location.getLatitude();
        lag=(long)location.getLongitude();
        MyLOCHandler myloc=new MyLOCHandler(lat,lag);
       /* dr= FirebaseDatabase.getInstance().getReference("bus_details").child(user_name);
        dr.setValue(myloc);*/
       FirebaseDatabase fd=FirebaseDatabase.getInstance();
      // DatabaseReference dr=fd.getReference("bus_details");
        DatabaseReference dr=fd.getReference("bus_details").child(busname).child(user_name);
        dr.setValue(myloc);
        Toast.makeText(this, "Location updated ", Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Lat-->"+lat+"Lag-->"+lag, Toast.LENGTH_LONG).show();




     /*  dr.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (final DataSnapshot ds:dataSnapshot.getChildren())
               {
                   if(ds.getKey().equals(user_name))
                   {
                       DatabaseReference dr1=FirebaseDatabase.getInstance().getReference("bus_details").child(user_name);
                       dr1.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(DataSnapshot dataSnapshot) {
                               MyLOCHandler myloc=new MyLOCHandler(lat,lag);
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
       });*/


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(this, "Gps on", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider)
    {
        Toast.makeText(this, "Gps off", Toast.LENGTH_SHORT).show();
    }


 }

