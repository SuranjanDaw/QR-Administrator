package com.google.android.gms.samples.vision.barcodereader;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class getDetails extends AppCompatActivity {
    TextView[] res;
    TextView idtext;
    Button payButton;
    Button allowButton1;

    String urlDetails;
    String urlPayment;
    String urlAllowEntry;

    private static String mode;
    private static String admin;

    private String url;
    private static String id;

    private final static int DETAILS = 1;
    public static final int PAYMENT= 2;
    public final static int ALLOW = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_details);

        Intent intent = getIntent();
        url = intent.getStringExtra("qrValue");

        id = url.substring(url.length()-10);

        SharedPreferences sh = getSharedPreferences("loginData",Context.MODE_PRIVATE);

        urlDetails = getString(R.string.qrGetData);
        urlPayment = getString(R.string.payA);
        urlAllowEntry = getString(R.string.attendA);

        String username = sh.getString("name","NULL");
        Log.d("aa",sh.toString());
        Log.d("aa",username);
        mode = "OFFLINE_"+username;
        admin = username;

        if(username.equalsIgnoreCase("NULL"))
        {
            Toast.makeText(this,"You must Login First",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getDetails.this,LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }else {
            int count = 0;
            res = new TextView[11];
            //res[10] = findViewById(R.id.t11);
            //res[0] = findViewById(R.id.t1);
            setIDs(res);


            idtext = findViewById(R.id.ID);
            String s = "Ticket ID :" + id;
            idtext.setText(s);
            //payAmttext = findViewById(R.id.payment);
            payButton = findViewById(R.id.paybutton);
            allowButton1 = findViewById(R.id.allow1);

            details();//calling for details when intent opens the activity

            allowButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //id = url.substring(url.length()-10);
                    MyAsyncTask myAsyncTask = new MyAsyncTask(getDetails.this,res, id, "", "", admin, urlAllowEntry, ALLOW);
                    myAsyncTask.execute();
                }
            });
            payButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //id = idtext.getText().toString();
                    //pay = payAmttext.getText().toString();
                    MyAsyncTask myAsyncTask = new MyAsyncTask(getDetails.this,res, id, "", mode, "", urlPayment, PAYMENT);
                    myAsyncTask.execute();
                }
            });

        }



    }
    private void details() {
        Log.d("aa", id);
        MyAsyncTask myAsyncTask = new MyAsyncTask(getDetails.this,res, id, "", "", "", urlDetails, DETAILS);
        myAsyncTask.execute();
    }

    private void setIDs(TextView[] res)
    {
        res[0] = findViewById(R.id.t1);
        res[1] = findViewById(R.id.t2);
        res[2] = findViewById(R.id.t3);
        res[3] = findViewById(R.id.t4);
        res[4] = findViewById(R.id.t5);
        res[5] = findViewById(R.id.t6);
        res[6] = findViewById(R.id.t7);
        res[7] = findViewById(R.id.t8);
        res[8] = findViewById(R.id.t9);
        res[9] = findViewById(R.id.t10);
        res[10] = findViewById(R.id.t11);

    }

}
