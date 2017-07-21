package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class sale1OrderAdd extends AppCompatActivity {
    String phone,id;
    TextView textViewName,textViewMenu,textViewKal,textViewPrice,textViewDate;
    private void findview(){
        textViewName = (TextView)findViewById(R.id.textView109);
        textViewMenu = (TextView)findViewById(R.id.textView90);
        textViewKal = (TextView)findViewById(R.id.textView114);
        textViewPrice = (TextView)findViewById(R.id.textView111);
        textViewDate= (TextView)findViewById(R.id.textView94);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale1_order_add);
        setTitleBar();
        getPhone();
        getData();
        findview();
    }
    private void getPhone(){
        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
        id=bundle.getString("id");
    }
    private void getData(){
        HttpThread ht = new HttpThread();
        ht.postDataParams="id="+id;
        new Thread(ht).start();
    }
    public void add(View v){
        HttpThread ht = new HttpThread();
        ht.postDataParams="id="+id
                        +"&phone="+phone
                        +"&mod=add";
        new Thread(ht).start();

    }
    class HttpThread extends Thread {

        public String postDataParams = null;
        public String requestURL = "http://120.110.7.88:8080/test/sale1.php";
        @Override
        public void run() {
            Bundle myBundle = new Bundle();
            URL url;
            Log.d("date",postDataParams);
            StringBuilder sb = new StringBuilder();
            try {
                url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                if(postDataParams!=null){
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
//            Toast.makeText(sale1OrderAdd.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();
            if(Objects.equals(msg.getData().getString("response"), "exist")){
                Toast.makeText(sale1OrderAdd.this,"閃購活動每人限購1份喔!",Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(msg.getData().getString("response"), "exist2")){
                Toast.makeText(sale1OrderAdd.this,"你已經搶購過了喔~~~",Toast.LENGTH_SHORT).show();
            }
            else if(Objects.equals(msg.getData().getString("response"), "add OK")){
                Toast.makeText(sale1OrderAdd.this,"已加入購物車",Toast.LENGTH_SHORT).show();
            }
            else {
                String tmp[] = msg.getData().getString("response").split("#");

                try {
                    JSONObject json = new JSONObject(tmp[1]);
                    textViewName.setText(json.getString("s1_name"));
                    textViewKal.setText(json.getString("s1_kal")+"kal");
                    textViewMenu.setText("-"+tmp[2]+"數量1份");
                    //textViewOldPrice.setText("原價$"+json.getString("s1_price"));
                    textViewPrice.setText("特價$"+json.getString("s2_pprice"));
                    textViewDate.setText("﹡優惠期間"+json.getString("s1_start_day")+"至"+json.getString("s1_finish_day"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
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
    private void setTitleBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
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
