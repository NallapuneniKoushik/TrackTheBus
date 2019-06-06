package nkc.com.trackthebus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserActivity extends AppCompatActivity {
    Spinner spin;
    String busname;
    double lat,lag;
    String[] bus = {"Select Bus", "Kukatpally", "Ecil", "LB Nagar", "BHEL", "Uppal", "Chilkalguda", "Alwal", "Sagar Ring Road",
            "Nagole", "Mehdipatnam", "Hyder Nagar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        spin = findViewById(R.id.sp1);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bus);
        spin.setAdapter(aa);

    }

    public void submit(View v)
    {
        busname = spin.getSelectedItem().toString().trim();
        if (busname == bus[0]) {
            Toast.makeText(this, "Select Any Bus", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference dr= FirebaseDatabase.getInstance().getReference("bus_details");
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String bname;
                  for(final DataSnapshot ds:dataSnapshot.getChildren()){
                       bname=(String)ds.getKey();
                       if(bname.equals(busname))
                       {
                           DatabaseReference dr1=FirebaseDatabase.getInstance().getReference("bus_details").child(busname);
                           dr1.addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   MyLOCHandler my=dataSnapshot.getValue(MyLOCHandler.class);
                                   lat=(double)my.getLat();
                                   lag=(double) my.getLag();
                                   Intent i=new Intent(UserActivity.this,MapsActivity.class);
                                   i.putExtra("dlat",lat);
                                   i.putExtra("dlag",lag);
                                   startActivity(i);
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           });
                       }
                       else
                       {
                           Toast.makeText(UserActivity.this, "Sorry,Your Bus is Not Available", Toast.LENGTH_LONG).show();
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
