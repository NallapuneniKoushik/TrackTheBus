package nkc.com.trackthebus;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

      EditText et1,et2,et3,et4,et5;
    TextView tv1;
    String dfname,dlname,dcno,demail,dob,dpass,dbname;
    ProgressDialog pd;
    String values[] = {"Select DOB"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et1=findViewById(R.id.et1);
        et2=findViewById(R.id.et2);
        et3=findViewById(R.id.et3);
        et4=findViewById(R.id.et4);
        et5=findViewById(R.id.et5);
        tv1=findViewById(R.id.tv1);
        pd=new ProgressDialog(this);
        pd.setMessage("Please wait");



    }

    public void register(View v)
    {
        dfname=et1.getText().toString().trim();
        dlname=et2.getText().toString().trim();
        dcno=et3.getText().toString().trim();
        demail=et4.getText().toString().trim();
        dpass=et5.getText().toString().trim();
         dbname=null;//Change this one
           if(dfname.isEmpty())
           {
               et1.setError("Empty");
               et1.requestFocus();
           }
           else {
               if(dlname.isEmpty())
               {
                  et2.setError("Empty");
                  et2.requestFocus();
               }
               else
               {
                   if(dcno.isEmpty())
                   {
                       et3.setError("Empty");
                       et3.requestFocus();
                   }
                   else{
                       if(demail.isEmpty())
                       {
                           et4.setError("Empty");
                           et4.requestFocus();
                       }

                       else {
                           if(dpass.isEmpty())
                           {
                               et5.setError("Empty");
                               et5.requestFocus();
                           }
                           else
                           {
                             dob=(String)tv1.getText();
                             if(dob.equals(values[0]))
                             {
                                 Toast.makeText(this, "Select Date of Birth", Toast.LENGTH_LONG).show();
                             }
                             else
                             {
                                 Toast.makeText(this, "Start Registering", Toast.LENGTH_LONG).show();
                                 FirebaseDatabase fd=FirebaseDatabase.getInstance();
                                 DatabaseReference dr=fd.getReference("driver_details").child(dcno);
                                 Log.e( "register: ----->",""+dr );
                                 pd.show();
                                 MyFBHandler my=new MyFBHandler(dfname,dlname,dcno,demail,dob,dpass);//taked off dbname from here
                                 dr.setValue(my);
                                 pd.dismiss();
                                 Toast.makeText(this, "Registration Succesfull", Toast.LENGTH_LONG).show();
                                 finish();

                             }
                           }
                       }
                   }
               }

           }


    }

    public void selectDOB(View v){
        Calendar c=Calendar.getInstance();
        int cyear=c.get(Calendar.YEAR);
        int cmonth=c.get(Calendar.MONTH);
        int cday=c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dpd=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                tv1.setText(i2+"-"+(i1+1)+"-"+i);
            }
        }, cyear, cmonth, cday);
        DatePicker dp=dpd.getDatePicker();
        dp.setMaxDate(System.currentTimeMillis());
        dpd.show();
    }



}
