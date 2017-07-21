package com.example.myapplication;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class sale1List extends AppCompatActivity {
    ListView listView;
    String list1[]={"not date"},list2[]={""},lt1="",lt2="";
    String phone;
    private ArrayAdapter listAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale1_list);
        getPhone();
        setTitleBar();
        listView = (ListView)findViewById(R.id.listView8);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), "你選擇的是" + list2[position], Toast.LENGTH_SHORT).show();
                if(Objects.equals(list2[position], ""))return;
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("id",list2[position]);
                bundle.putString("phone",phone);
                intent.putExtras(bundle);
                intent.setClass(sale1List.this, sale1Order.class);//login_main to register_page
                startActivityForResult(intent, 0);//可回傳資料
            }
        });
    }
    protected void onStart(){
        super.onStart();
        lt1="";
        lt2="";
        showList();
    }
    public void Listupdate(){
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list1);
        listView.setAdapter(listAdapter);

    }
    private void getPhone(){
        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
    }
    private void showList(){
        HttpThread ht = new HttpThread();
        new Thread(ht).start();
    }
    private void setTitleBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }
    class HttpThread extends Thread {

        public String postDataParams=null;
        public String requestURL = "http://120.110.7.88:8080/test/sale1.php";

        @Override
        public void run() {
            Bundle myBundle = new Bundle();
            URL url;
            //Log.d("data", postDataParams);
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
//            Toast.makeText(sale1List.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();
            String[] tmp=msg.getData().getString("response").split("#");
            switch (tmp[0]){
                case "sale1":
                    if(tmp.length > 1){
                        lt1=tmp[1];
                        lt2=tmp[2];
                    }
                    list1=lt1.split("\"");
                    list2=lt2.split("\"");
                    Listupdate();
//                    HttpThread ht = new HttpThread();
//                    ht.requestURL = "http://120.110.7.88:8080/test/sale2.php";
//                    new Thread(ht).start();
                    break;
                case "sale2":
                    if(!Objects.equals(lt1, "")){
                        lt1+= "\"------------------長期活動------------------\"";
                        lt2+= "\"\"";
                    }
                    if(tmp.length > 1){
                        lt1+= tmp[1];
                        lt2+= tmp[2];
                    }
                    list1=lt1.split("\"");
                    list2=lt2.split("\"");
                    Listupdate();
                    break;
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
