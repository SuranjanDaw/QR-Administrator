package com.google.android.gms.samples.vision.barcodereader;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.InputMismatchException;

public class MyAsyncTask extends AsyncTask<String[], Integer, String> {
    private TextView[] details;
    String id;
    private String payAmt;
    private String url;
    private int MODE;
    private String mode;
    private String admin;
    Context getDetailsClass;
    private ProgressDialog progress;
    String API_KEY;
    MyAsyncTask(Context get,TextView[] det,String id,String pay,String mode,String admin, String url1,int MODE){
        getDetailsClass = get;
        details = det;
        this.id = id;
        payAmt = pay;
        this.mode = mode;
        this.admin = admin;
        url = url1;
        Log.d("aa",id);
        this.MODE = MODE;
    }

    @Override
    protected void onPreExecute()
    {
        progress = new ProgressDialog(getDetailsClass);
        progress.setMessage("Downloading from server");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
    }

    @Override
    public void onProgressUpdate(Integer... args){
        progress.setProgress(args[0]);
    }

    @Override
    protected String doInBackground(String[]... s) {
        String s1 = null;
        API_KEY = getDetailsClass.getString(R.string.API_KEY);
        try {
            //String urlnet = "http://192.168.137.1/myAPI/details.php";
            //String urlpay = "http://localhost/TravellersHome/API/payment.php";
            //String urlnet = "http://192.168.137.1/TravellersHome/API/detail.php";
            Uri uriBuilder;

            switch (MODE)
            {
                case 1:uriBuilder = Uri.parse(url).buildUpon()
                        .appendQueryParameter("id",id)
                        .appendQueryParameter("key",API_KEY)
                        .build();
                    break;
                case 2:uriBuilder = Uri.parse(url).buildUpon()
                        .appendQueryParameter("id",id)
                        .appendQueryParameter("mode",mode)
                        .appendQueryParameter("key",API_KEY)
                        .build();
                    break;
                case 3:uriBuilder = Uri.parse(url).buildUpon()
                        .appendQueryParameter("id",id)
                        .appendQueryParameter("admin",admin)
                        .appendQueryParameter("key",API_KEY)
                        .build();
                    break;
                default:uriBuilder = Uri.parse(url).buildUpon()
                        .build();//SHOW DETAILS IF NONE OF THE ABOVE BECOME TRUE
            }
            Log.d("aa", uriBuilder.toString());
            URL url = new URL(uriBuilder.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream input = httpURLConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            s1 = br.readLine();

            publishProgress(100);//calls onProgressUpdate(Integer...)

            Log.d("aa","s1="+s1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return s1;
    }
    @Override
    protected void onPostExecute(String s)
    {

        Log.d("aa","in Post"+s);
        super.onPostExecute(s);
        String res="No Data Found";
        try {
            res = "";
            String[] field = {"ticket_code", "name", "email", "phone", "price", "registered", "paid", "p_admin", "attend", "a_admin", "days"};
            JSONObject jsob = new JSONObject(s);
            for (String item: field) {
                switch (item)
                {
                    case "ticket_code":
                        Log.d("aa","t1 id:"+details[0].getId()+"text="+jsob.getString(item)+" tag="+item+" gettext "+details[0].getText());
                        details[0].setText(jsob.getString(item));
                        break;

                    case "name":
                        Log.d("aa","t1 id:"+details[1].getId()+"text="+jsob.getString(item)+" tag="+item+" gettext "+details[1].getText());
                        details[1].setText(jsob.getString(item));
                         break;
                    case "email":
                        details[2].setText(jsob.getString(item));
                        Log.d("aa","t1 id:"+details[2].getId()+"text="+jsob.getString(item)+" tag="+item);break;
                    case "phone":
                        details[3].setText(jsob.getString(item));
                        Log.d("aa","t1 id:"+details[3].getId()+"text="+jsob.getString(item)+" tag="+item);break;
                    case "price":
                        details[4].setText(jsob.getString(item));
                        Log.d("aa","t1 id:"+details[4].getId()+"text="+jsob.getString(item)+" tag="+item);break;
                    case "registered":
                        details[5].setText(jsob.getString(item));
                        Log.d("aa","t1 id:"+details[5].getId()+"text="+jsob.getString(item)+" tag="+item);break;


                    case "paid":
                        details[6].setText(jsob.getString(item));
                        Log.d("aa","t1 id:"+details[6].getId()+"text="+jsob.getString(item)+" tag="+item);break;
                    case "p_admin":
                        details[7].setText(jsob.getString(item));
                        Log.d("aa","t1 id:"+details[7].getId()+"text="+jsob.getString(item)+" tag="+item);break;
                    case "attend":
                        details[8].setText(jsob.getString(item));
                        Log.d("aa","t1 id:"+details[8].getId()+"text="+jsob.getString(item)+" tag="+item);break;
                    case "a_admin":
                        details[9].setText(jsob.getString(item));
                        Log.d("aa","t1 id:"+details[9].getId()+"text="+jsob.getString(item)+" tag="+item);break;
                    case "days":
                        details[10].setText(jsob.getString(item));
                        Log.d("aa","t1 id:"+details[10].getId()+"text="+jsob.getString(item)+" tag="+item);break;
                }
                if(item.equals("p_admin") && jsob.getString(item)!=null && MODE == getDetails.PAYMENT)
                    Toast.makeText(getDetailsClass,"ALREADY PAID",Toast.LENGTH_LONG).show();
                if(item.equals("a_admin") && jsob.getString(item)!=null && MODE == getDetails.ALLOW)
                    Toast.makeText(getDetailsClass,"ALREADY ALLOWED",Toast.LENGTH_LONG).show();
            }


        } catch (Exception e) {
            if(s.equalsIgnoreCase("NO DATA FOUND") || s.equalsIgnoreCase("null"))
                Toast.makeText(getDetailsClass,"Invalid ID",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        progress.dismiss();
        //details.setText(res);
    }
}
