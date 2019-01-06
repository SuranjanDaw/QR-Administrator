package com.google.android.gms.samples.vision.barcodereader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.vision.L;

public class LoginActivity extends AppCompatActivity {
    private String username;
    private String password;

    private static final int RC_BARCODE_CAPTURE = 9001;

    private EditText user;
    private EditText pass;
    Button submit;
    //Button goToInfo;
    //private TextView res;
    private TextView user_nav_header;

    private SharedPreferences sh;

    LoginAsyncTask loginAsyncTask;
    DrawerLayout mDrawerLayout;

    private String urlLogin;
    @Override
    protected void onCreate(Bundle b)
    {
        super.onCreate(b);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("Techtronista 2K19");
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        sh = getSharedPreferences("loginData",Context.MODE_PRIVATE);

        urlLogin = this.getString(R.string.loginUrl);

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
                                intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                            case R.id.scan:
                                if(sh.getString("LoginStatus","NULL").equalsIgnoreCase("SUCCESS"))
                                {
                                    intent = new Intent(LoginActivity.this, BarcodeCaptureActivity.class);
                                    intent.putExtra(BarcodeCaptureActivity.AutoFocus,true);
                                    intent.putExtra(BarcodeCaptureActivity.UseFlash, false);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivityForResult(intent, RC_BARCODE_CAPTURE);
                                }else {
                                    Toast.makeText(LoginActivity.this,"Login First",Toast.LENGTH_LONG).show();
                                    intent = new Intent(LoginActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                                break;
                            case R.id.login :
                                intent = new Intent(LoginActivity.this, LoginActivity.class);
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
                                Toast.makeText(LoginActivity.this,"LOGGED OUT: GO TO Login Page",Toast.LENGTH_SHORT).show();
                                intent = new Intent(LoginActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                break;
                        }

                        return true;
                    }
                }
        );

        //SharedPreferences sh = getSharedPreferences("loginData", Context.MODE_PRIVATE);
        Log.d("aa","Usr"+sh.getString("name","NULL")+user_nav_header.getText());
        user_nav_header.setText(sh.getString("name","NULL"));
        if( sh.getString("LoginStatus","NULL").equals("SUCCESS"))
        {
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        user = findViewById(R.id.txtUser);
        pass = findViewById(R.id.txtPass);
        //res = findViewById(R.id.result);
        submit = findViewById(R.id.btnAuth);
        //goToInfo = findViewById(R.id.goToInfo);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                password = pass.getText().toString();
                loginAsyncTask = new LoginAsyncTask(LoginActivity.this,username,password,urlLogin);
                loginAsyncTask.execute();
                Log.d("aa","OudsideShared"+urlLogin);

            }
        });
        /*goToInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences sh = getSharedPreferences("loginData",Context.MODE_PRIVATE);
                String s = sh.getString("LoginStatus","NULL");
                if(s.equalsIgnoreCase("SUCCESS"))
                {
                    Log.d("aa","insideShared");
                    //TextView user_nav_header = findViewById(R.id.username_nav_header);

                    //user_nav_header.setText(sh.getString("name","NULL"));

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else {
                    res.setText("You Have to Login First");
                }
            }
        });*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);


    }
}
