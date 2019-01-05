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
    TextView res;
    TextView idtext;
    EditText payAmttext;
    Button details;
    Button payButton;
    Button allowButton;

    String urlDetails;
    String urlPayment;
    String urlAllowEntry;

    private static String mode;
    private static String admin;

    private String url;
    private static String id;

    private final static int DETAILS = 1;
    private final static int PAYMENT= 2;
    private final static int ALLOW = 3;
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
            res = findViewById(R.id.resDetails);

            idtext = findViewById(R.id.ID);
            String s = "Ticket ID :" + id;
            idtext.setText(s);
            //payAmttext = findViewById(R.id.payment);

            details = findViewById(R.id.detailsbutton);
            payButton = findViewById(R.id.paybutton);
            allowButton = findViewById(R.id.allow);

            allowButton.setOnClickListener(new View.OnClickListener() {
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


            details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //id = idtext.getText().toString();
                    Log.d("aa", id);
                    MyAsyncTask myAsyncTask = new MyAsyncTask(getDetails.this,res, "", "", "", "", url, DETAILS);
                    myAsyncTask.execute();
                }
            });
        }


    }
}