package nkc.com.trackthebus;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverActivity extends AppCompatActivity   {
    EditText uname,upword;
    String username,upassword;
    MyFBHandler my;
    TextView tv1;
    ProgressDialog pd;
//    SharedPreferences sp;
//    SharedPreferences.Editor spe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        uname=findViewById(R.id.uname);
        upword=findViewById(R.id.upword);
        tv1=findViewById(R.id.tv1);
//        sp = getSharedPreferences("login_details",MODE_PRIVATE);
//        spe = sp.edit();
//        boolean res = sp.getBoolean("status",false);
//        if (res)
//        {
//            finish();
//            //startActivity(new Intent(this,DBUSActivity.class));
//            Intent i=new Intent(DriverActivity.this,DBUSActivity.class);
//            i.putExtra("k1",username);
//            startActivity(i);
//           // finish();
//
//        }


    }

    public void validate(View v)
    {
        my=new MyFBHandler();
        pd=new ProgressDialog(this);
        pd.setMessage("Please Wait");
        pd.show();

        username=uname.getText().toString().trim();
        upassword=upword.getText().toString().trim();

        DatabaseReference dr=FirebaseDatabase.getInstance().getReference("driver_details");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 String dcno,dpass;
                for(final DataSnapshot ds:dataSnapshot.getChildren())
                {
                    dcno=(String)ds.getKey();
                    if(dcno.equals(username))
                    {
                        DatabaseReference dr1=FirebaseDatabase.getInstance().getReference("driver_details").child(username);
                        dr1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                MyFBHandler my1=dataSnapshot.getValue(MyFBHandler.class);
                                String p;
                                p=my1.getDpass();
                                if(p.equals(upassword))
                                {
//                                    spe.putBoolean("status",true);
//                                    spe.commit();
//                                    //finish();
                                    //startActivity(new Intent(this,WelcomeActivity.class));

                                    Intent i=new Intent(DriverActivity.this,DBUSActivity.class);
                                    i.putExtra("k1",username);
                                    startActivity(i);
                                    finish();
                                    pd.dismiss();
                                }
                                else {
                                    Toast.makeText(DriverActivity.this, "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                                    uname.setText("");
                                    upword.setText("");
                                    uname.requestFocus();
                                    pd.dismiss();
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




       /* Intent i=new Intent(this,DBUSActivity.class);
        i.putExtra("k1",username);
        startActivity(i);*/


    }
    public void register(View v)
    {
        Intent i=new Intent(this,RegisterActivity.class);
        startActivity(i);
    }
    /*public void sendUname(View v)
    {
        Intent i=new Intent();
        i.putExtra("k1",username);
        setResult(1212,i);
        finish();
    }*/

}
