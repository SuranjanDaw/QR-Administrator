/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.samples.vision.barcodereader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Map;

/**
 * Main activity demonstrating how to pass extra parameters to an activity that
 * reads barcodes.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;
    private TextView user_nav_header;

    private EditText ticketId;
    ImageView qrImage;

    Button getDetailsQR;
    String qrValue;

    public SharedPreferences sh;

    private static final int RC_BARCODE_CAPTURE = 9001;
    private boolean flagQR = false;
    private static final String TAG = "BarcodeMain";

    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Techtronista 2K19");
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        sh = getSharedPreferences("loginData",Context.MODE_PRIVATE);

        mDrawerLayout = findViewById(R.id.drawerLayout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        user_nav_header = header.findViewById(R.id.username_nav_header);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id = item.getItemId();
                        Intent intent;
                        mDrawerLayout.closeDrawers();
                        item.setChecked(true);
                        switch (id)
                        {
                            case R.id.info:
                                intent = new Intent(MainActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                            case R.id.scan:
                                if(sh.getString("LoginStatus","NULL").equalsIgnoreCase("SUCCESS"))
                                {
                                    intent = new Intent(MainActivity.this, BarcodeCaptureActivity.class);
                                    intent.putExtra(BarcodeCaptureActivity.AutoFocus,autoFocus.isChecked() );
                                    intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivityForResult(intent, RC_BARCODE_CAPTURE);
                                }else {
                                    Toast.makeText(MainActivity.this,"Login First",Toast.LENGTH_LONG).show();
                                    intent = new Intent(MainActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }

                                break;
                            case R.id.login :
                                intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                            case R.id.logout:

                                SharedPreferences.Editor edit = sh.edit();
                                edit.putString("username","NULL");
                                edit.putString("password","NULL");
                                edit.putString("name","NULL");
                                edit.putString("LoginStatus","NULL");
                                edit.apply();
                                Toast.makeText(MainActivity.this,"LOGGED OUT: GO TO Login Page",Toast.LENGTH_SHORT).show();
                                intent = new Intent(MainActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                        }
                        return true;
                    }
                }
        );

        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);
        ticketId = findViewById(R.id.ticketIdEdit);


        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);



        findViewById(R.id.read_barcode).setOnClickListener(this);

        getDetailsQR = findViewById(R.id.valueQR);


        qrImage = (ImageView) findViewById(R.id.qrImage);

        final String flag = sh.getString("LoginStatus","NULL");

        user_nav_header.setText(sh.getString("name","NULL"));

        if(!flag.equalsIgnoreCase("SUCCESS"))
        {
            Toast.makeText(this,"Please Login.",Toast.LENGTH_LONG).show();
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        else
        {
            Toast.makeText(this, "You are Logged In: To Log in with a different anccount: Please Log out first.",
                    Toast.LENGTH_LONG).show();
        }


        getDetailsQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s;
                String user = sh.getString("username","NULL");
                if(flagQR && qrValue.substring(qrValue.length()-10).length()==10){
                    //Log.d("aa","xxx");
                    s = qrValue;
                }else if(!ticketId.getText().toString().equals("") && ticketId.getText().toString().length()==10) {
                    //Log.d("aa","xxp");
                    s = ticketId.getText().toString();
                }else{
                    //Log.d("aa","xxi");
                    Toast.makeText(MainActivity.this,"Scan a QR code or Enter ticket ID or Wrong ID",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!user.equals("NULL"))
                {
                    Intent i = new Intent(MainActivity.this, getDetails.class);
                    i.putExtra("qrValue",s);
                    startActivity(i);
                    flagQR = false;
                    barcodeValue.setText(R.string.expectedUrl);
                }else Toast.makeText(MainActivity.this,"Login First",Toast.LENGTH_SHORT).show();

            }
        }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);


    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        boolean flag = sh.getString("LoginStatus","NULL").equalsIgnoreCase("SUCCESS");
        if (v.getId() == R.id.read_barcode && flag) {
            // launch barcode activity.
            Intent intent = new Intent(this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
        }else {
            Toast.makeText(MainActivity.this,"Login First",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);

                    Log.d("aa",barcode.rawValue);

                    statusMessage.setText(R.string.barcode_success);
                    String barString = "ID :- "+barcode.displayValue.substring(barcode.displayValue.length()-10);
                    barcodeValue.setText(barString);
                    ticketId.setText(barcode.displayValue.substring(barcode.displayValue.length()-10));
                    qrValue = barcode.displayValue;
                    flagQR = true;
                    Log.d(TAG, "Barcode read: " + barcode.displayValue);

                } else {
                    statusMessage.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                statusMessage.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
