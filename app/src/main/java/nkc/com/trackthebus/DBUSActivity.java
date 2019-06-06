package nkc.com.trackthebus;

        import android.app.ProgressDialog;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;

public class DBUSActivity extends AppCompatActivity {
    Spinner spin;
    String busname,user_name;
    // EditText et1;
//   SharedPreferences sp;
//    SharedPreferences.Editor spe;
    ProgressDialog pd;
    String[] bus = {"Select Bus", "Kukatpally", "Ecil", "LB Nagar", "BHEL", "Uppal", "Chilkalguda", "Alwal", "Sagar Ring Road",
            "Nagole", "Mehdipatnam", "Hyder Nagar"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbus);
        // et1=findViewById(R.id.det1);
//        sp = getSharedPreferences("login_details",MODE_PRIVATE);
//        spe = sp.edit();

        spin = findViewById(R.id.sp1);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        user_name=b.getString("k1");
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, bus);
        spin.setAdapter(aa);
        pd = new ProgressDialog(this);
        pd.setMessage("Please wait");

        /*
         */
    }

    /*
     */
    public void submit(View v) {

        busname = spin.getSelectedItem().toString().trim();
        if (busname == bus[0]) {
            Toast.makeText(this, "Select Any Bus", Toast.LENGTH_SHORT).show();
        } else {

         /*   Toast.makeText(this, "Selecting Bus", Toast.LENGTH_SHORT).show();
            FirebaseDatabase fd = FirebaseDatabase.getInstance();
            DatabaseReference dr=fd.getReference("bus_details").child(busname).child(user_name);
            //DatabaseReference dr = fd.getReference("bus_details").child(user_name);
            Log.e("bus_details: ----->", "" + dr);
            pd.show();
            MyDBHandler mdb = new MyDBHandler(user_name,busname);
            dr.setValue(mdb);
            pd.dismiss();
            Toast.makeText(this, "Selected Bus SuccessFully", Toast.LENGTH_SHORT).show();
            finish();*/
            pd.show();
            // Intent i2=new Intent(this,LocActivity.class);
            //Intent i2=new Intent(this,Maps3Activity.class);
            Intent i2=new Intent(this,DMap.class);
            i2.putExtra("k1",user_name);
            i2.putExtra("k2",busname);
            startActivity(i2);
            pd.dismiss();

        }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1212)
        {
            user_name=data.getExtras().getString("k1");

        }
    }*/
    }
    public void logout(View v)
    {
//        spe.clear();
//        spe.commit();
//        finish();
        startActivity(new Intent(this,DriverActivity.class));
        finish();
    }
}
