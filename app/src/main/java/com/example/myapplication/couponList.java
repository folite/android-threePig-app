package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class couponList extends AppCompatActivity {
    ListView listView;
    String phone,list1[]={"not date"},list2[];
    private ArrayAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);
        setTitleBar();
        listView=(ListView)findViewById(R.id.listView6);
        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
        HttpThread ht =new HttpThread();
        ht.postDataParams="phone="+bundle.getString("phone");
        new Thread(ht).start();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "你選擇的是" + list[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("select",list2[position]);
                bundle.putString("phone", phone);
                intent.putExtras(bundle);
                intent.setClass(couponList.this, couponOrder.class);//login_main to register_page
                startActivityForResult(intent, 0);//可回傳資料
            }
        });
    }
    public void Listupdate(){
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list1);
        listView.setAdapter(listAdapter);

    }
    class HttpThread extends Thread {

        public String postDataParams=null;
        public String requestURL = "http://120.110.7.88:8080/test/have.php";

        @Override
        public void run() {
            Bundle myBundle = new Bundle();
            URL url;
            Log.d("data", postDataParams);
            StringBuilder sb = new StringBuilder();
            try {
                url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postDataParams);
                writer.flush();
                writer.close();
                os.close();
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
        public Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
//msg.getData會傳回Bundle，Bundle類別可以由getString(<KEY_NAME>)取出指定KEY_NAME的值
                //Toast.makeText(couponList.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();
                try {
                    String[] tmp=msg.getData().getString("response").split("-");
                    list1=tmp[1].split(",");
                    list2=tmp[2].split(",");
                }catch (Exception e){}
                finally {
                    Listupdate();
                }
            }
        };
    }
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
