package nkc.com.trackthebus;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    //Button button3,button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

        //setContentView(R.layout.activity_nmain);
//        button3=findViewById(R.id.button3);
//        button4=findViewById(R.id.button4);
    }



    public void driver(View v)
    {
        Intent i=new Intent(this,DriverActivity.class);
        startActivity(i);
    }

    public void user(View v)
    {
        Intent i=new Intent(this,GetActivity.class);
       // Intent i=new Intent(this,Get2Activity.class);
        startActivity(i);
    }
}
