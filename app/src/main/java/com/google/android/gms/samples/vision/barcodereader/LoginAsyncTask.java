package com.google.android.gms.samples.vision.barcodereader;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class LoginAsyncTask extends AsyncTask<String[], Void, String> {
    private String user,pass,url,name;
    public String flag="%jjvv@#";
    TextView res;
    Context context;
    LoginAsyncTask(Context context,TextView res,String user, String pass, String url)
    {
        this.context = context;
        this.user = user;
        this.pass = pass;
        this.url = url;
        this.res = res;
    }

    @Override
    protected String doInBackground(String[]... strings) {
        String json = "NO DATA FOUND";
        Uri uriBuild = Uri.parse(url).buildUpon().build();
        JSONObject jsonObject =new JSONObject();
        int responseCode = 0;
        try {
            jsonObject.put("email",user);
            jsonObject.put("password",pass);

            URL urlPOST= new URL(uriBuild.toString());
            HttpURLConnection  httpURLConnection = (HttpURLConnection) urlPOST.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream opt = httpURLConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(opt));
            bw.write(getPostDataString(jsonObject));
            bw.flush();
            bw.close();
            opt.close();

            responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d("aa",responseCode+"");
                BufferedReader in=new BufferedReader(
                        new InputStreamReader(
                                httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder("");
                String line="";

                line = in.readLine();
                Log.d("aa",line);
                sb.append(line);

                in.close();
                return sb.toString();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false : "+responseCode;
    }

    private String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String jsob)
    {
        String reply = "";
        String login = "";
        Log.d("aa",jsob);
        super.onPostExecute(jsob);
        String fields[] = {"reply","login","name"};
        try {
            JSONObject jsonObject = new JSONObject(jsob);
            reply = jsonObject.getString(fields[0]);
            login = jsonObject.getString(fields[1]);
            name = jsonObject.getString(fields[2]);
            if(reply.equals("Access Granted!") && login.equals("success"))
            {
                flag ="SUCCESS";
                Log.d("aa","//*"+flag);
                res.setText(flag);
                SharedPreferences sh = context.getSharedPreferences("loginData", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sh.edit();
                edit.putString("username",user);
                edit.putString("password",pass);
                edit.putString("name",name);
                edit.putString("LoginStatus",flag);
                edit.apply();
            }
            else
                res.setText(login);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
