package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class couponOrder extends AppCompatActivity {
    TextView textView1,textView2,textView3;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_order);
        textView1 = (TextView) findViewById(R.id.textView82);
        textView2 = (TextView) findViewById(R.id.textView100);
        textView3 = (TextView) findViewById(R.id.textView101);
        Bundle bundle = getIntent().getExtras();
        setTitleBar();
        phone=bundle.getString("phone");
        HttpThread th = new HttpThread();
        th.postDataParams="select="+bundle.getString("select");
        new Thread(th).start();

    }

    class HttpThread extends Thread {

        public String postDataParams = null;
        public String requestURL = "http://120.110.7.88:8080/test/have.php";

        @Override
        public void run() {
            Bundle myBundle = new Bundle();
            URL url;
            Log.d("date", postDataParams);
            StringBuilder sb = new StringBuilder();
            try {
                url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                if (postDataParams != null) {
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postDataParams);
                    writer.flush();
                    writer.close();
                    os.close();
                }
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String response;
                    while ((response = br.readLine()) != null) {
                        sb.append(response);
                    }
                    myBundle.putString("response", sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.setData(myBundle);
            mHandler.sendMessage(msg);
        }
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//msg.getData會傳回Bundle，Bundle類別可以由getString(<KEY_NAME>)取出指定KEY_NAME的值
            //Toast.makeText(couponOrder.this, msg.getData().getString("response"), Toast.LENGTH_LONG).show();

            String tmp = msg.getData().getString("response");
            try {
                JSONObject json = new JSONObject(tmp);
                textView1.setText(json.getString("c_name"));
                textView2.setText("使用期限"+json.getString("c_start_day")+"至"+json.getString("c_finish_day"));
                textView3.setText("*說明\n\n"+json.getString("c_text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private void setTitleBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }
    public void button_ShoppingPage_click(View v) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, shoppingCart.class);
        startActivityForResult(intent, 0);
    }
    public void button_setPage_click(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, SetPage.class);//login_main to register_page
        startActivityForResult(intent, 0);
    }
    public void button_date_click(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, resetdatePage.class);
        startActivityForResult(intent, 0);
    }
    public void listpage(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, CollectMenuPage.class);
        startActivityForResult(intent, 0);
    }
    public void couponListPage(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, couponList.class);
        startActivityForResult(intent, 0);
    }
    public void button_finishOrderList (View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, finishOrderList.class);
        startActivityForResult(intent, 0);
    }
    public void toHome(View v){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        intent.putExtras(bundle);
        intent.setClass(this, mintPage.class);
        startActivityForResult(intent, 0);
    }
}
