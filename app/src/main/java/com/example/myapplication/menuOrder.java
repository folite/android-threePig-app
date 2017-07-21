package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Interpolator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class menuOrder extends AppCompatActivity {
    TextView textView1,textView2,textView3,orderNum,textView4,textView5,textView6;
    ImageView listimage;
    String id,phone,mod2="new";
    int sum=1,price=0,list_check=0,kal=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_order);
        setTitleBar();
        textView1=(TextView)findViewById(R.id.textView41);
        textView2=(TextView)findViewById(R.id.textView45);
        textView3=(TextView)findViewById(R.id.textView51);
        textView4=(TextView)findViewById(R.id.textView53);
        textView5=(TextView)findViewById(R.id.textView46);
        textView6=(TextView)findViewById(R.id.textView115);
        orderNum=(TextView)findViewById(R.id.orderNum);
        listimage=(ImageView)findViewById(R.id.imageView3);
        setNum(sum);

        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
        HttpThread th = new HttpThread();
        if(bundle.getString("select")!=null){
            th.postDataParams="select="+bundle.getString("select");
        }
        else if(bundle.getString("id")!=null){
            th.postDataParams="id="+bundle.getString("id");
        }
        if (bundle.getString("mod2")!=null)mod2=bundle.getString("mod2");
        if (bundle.getString("sum")!=null)setNum(Integer.parseInt(bundle.getString("sum")));
        new Thread(th).start();


    }
    private void setNum(int n){
        sum=n;
        orderNum.setText(String.valueOf(n));
        subtotal();
    }
    public void checkClist(){
        HttpThread th = new HttpThread();
        th.requestURL="http://120.110.7.88:8080/test/list.php";
        th.postDataParams="id="+id+
                          "&phone="+phone+
                          "&mod=check";
        new Thread(th).start();
    }
    public void addCList(View v){
        String mod = null;
        if(1 == list_check){
            mod="delete";
        }
        if(0 == list_check){
            mod="add";
        }
        HttpThread th = new HttpThread();
        th.requestURL="http://120.110.7.88:8080/test/list.php";
        th.postDataParams="id="+id+
                          "&phone="+phone+
                          "&mod="+mod;
        new Thread(th).start();
    }
    public void addSum(View v){
        setNum(sum+1);
    }
    public void lessSum(View v){
        if(sum>1)setNum(sum-1);
    }
    public void subtotal(){
        textView4.setText("$"+sum*price);
        textView3.setText(kal*sum+"kal");
    }
    public void addD(View v){
        HttpThread th = new HttpThread();
        th.requestURL="http://120.110.7.88:8080/test/order.php";
        th.postDataParams="id="+id+
                "&phone="+phone+
                "&sum="+sum+
                "&mod=addMenu"+
                "&mod2="+mod2;
        new Thread(th).start();
        Toast.makeText(this,"已加入購物車",Toast.LENGTH_SHORT).show();
    }
    class getImage extends Thread {
        public String purl="";
        @Override
        public void run() {
            // TODO Auto-generated method stub

            final Bitmap mBitmap = getBitmapFromURL(purl);

            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ImageView mImageView = (ImageView) findViewById(R.id.imageView2);
                    mImageView.setBackgroundColor(0xedc592);
                    mImageView.setImageBitmap(mBitmap);
                }}
            );

        }
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    class HttpThread extends Thread {

        public String postDataParams = null;
        public String requestURL = "http://120.110.7.88:8080/test/menu.php";
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
            //Toast.makeText(menuOrder.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();
            Log.d("requit",msg.getData().getString("response"));
            if(Objects.equals(msg.getData().getString("response"), "CListExist") || Objects.equals(msg.getData().getString("response"), "加入成功~")){
                listimage.setBackgroundResource(R.drawable.heart_icons_02);
                textView5.setText("已收藏");
                list_check=1;
            }
            else if(Objects.equals(msg.getData().getString("response"), "delete OK")){
                listimage.setBackgroundResource(R.drawable.heart_icons_01);
                textView5.setText("收藏");
                list_check=0;
            }

            else if (msg.getData().getString("response")!= null){
                String[] tmp = msg.getData().getString("response").split("#");
                if(!Objects.equals(tmp[0], "menu")) return;
                try {
                    JSONObject json = new JSONObject(tmp[1]);
                    id = json.getString("m_id");
                    textView1.setText(json.getString("m_name"));
                    textView2.setText("$" + json.getString("m_price"));
                    textView6.setText(json.getString("m_text"));
                    price=json.getInt("m_price");
                    kal=json.getInt("m_kal");
                    subtotal();
                    //textView4.setText(json.getString("m_picture"));
                    //textView5.setText(json.getString("m_text"));
                    if (json.getString("m_picture") != "") {
                        getImage gi = new getImage();
                        gi.purl = "http://120.110.7.88:8080/menu-ns/i-menu/img/" + json.getString("m_picture");
                        new Thread(gi).start();
                    }
                    checkClist();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //msg.getData().getString("response").split(",");
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
