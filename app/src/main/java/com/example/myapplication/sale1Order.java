package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class sale1Order extends AppCompatActivity {
    String phone,id,text,Sday,Fday;
    TextView textViewName,textViewKal,textViewMenu,textViewOldPrice,textViewNewPrice,textViewQuantity,textViewQQuantity,textViewmessage;
    private void findview(){
        textViewName = (TextView)findViewById(R.id.textView103);
        textViewKal = (TextView)findViewById(R.id.textView102);
        textViewMenu = (TextView)findViewById(R.id.textView104);
        textViewOldPrice = (TextView)findViewById(R.id.textView105);
        textViewNewPrice = (TextView)findViewById(R.id.textView106);
        textViewQuantity = (TextView)findViewById(R.id.textView107);
        textViewQQuantity = (TextView)findViewById(R.id.textView108);
        textViewmessage = (TextView)findViewById(R.id.textView116);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getPhone();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale1_order);
        setTitleBar();
        getData();
        findview();
    }
    private void getPhone(){
        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
        id=bundle.getString("id");
    }
    private void setTitleBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }
    public void sale1ToAdd(View v){
        if(Integer.parseInt(id)>20000)return;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("phone", phone);
        bundle.putString("id", id);
        intent.putExtras(bundle);
        intent.setClass(sale1Order.this, sale1OrderAdd.class);
        startActivityForResult(intent, 0);
    }
    private void getData(){
        HttpThread ht = new HttpThread();
        if(Integer.parseInt(id) >= 20000)ht.requestURL = "http://120.110.7.88:8080/test/sale2.php";
        ht.postDataParams="id="+id;
        new Thread(ht).start();
    }
    public void message(View v){
//        Toast.makeText(this,""+v.getId(),Toast.LENGTH_SHORT).show();
        switch (v.getId()){
            case R.id.textView85:
                textViewmessage.setText(text);
                break;
            case R.id.textView87:
                textViewmessage.setText("﹡限隔日 05:30—10:30 使用\n" +
                        "﹡搶購促銷活動不得合併使用\n" +
                        "﹡請務必完成訂單才算成立\n" +
                        "﹡優惠期間" + Sday + "至" + Fday);
                break;
            case R.id.textView89:
                textViewmessage.setText("電話： 04-24962648\n" +
                        "地址：台中市大里區中興路一段382巷71號\n" +
                        "營業時間：05:30AM—10:30PM");
                break;
        }
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
            //Toast.makeText(sale1Order.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();

            String tmp[] = msg.getData().getString("response").split("#");
            switch (tmp[0]){
                case "sale1":
                    try {
                        JSONObject json = new JSONObject(tmp[1]);
                        if (json.getString("s1_picture") != null) {
                            sale1Order.getImage gi = new sale1Order.getImage();
                            gi.purl = "http://120.110.7.88:8080/test/圖片/" + json.getString("s1_picture");
                            new Thread(gi).start();
                        }
                        textViewName.setText(tmp[2]);
                        textViewKal.setText(json.getString("s1_kal")+"kal");
                        textViewMenu.setText("-"+tmp[2]+"數量1份");
                        textViewOldPrice.setText("原價$"+json.getString("s1_price"));
                        textViewNewPrice.setText("特價$"+json.getString("s2_pprice"));
                        switch (json.getInt("s1_type")){
                            case 0:
                                textViewQQuantity.setVisibility(View.GONE);
                                textViewQuantity.setText(json.getString("s1_finish_day"));
                                break;
                            case 1:
                                textViewQuantity.setText("限量"+json.getString("s1_quantity")+"份");
                                textViewQQuantity.setText("剩"+json.getString("s1_qqantity")+"份");
                                break;
                        }

                        text=json.getString("s1_text");
                        textViewmessage.setText(text);
                        Sday = json.getString("s1_start_day");
                        Fday = json.getString("s1_finish_day");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "sale2":
                    try {
                        JSONObject json = new JSONObject(tmp[1]);
                        if (json.getString("s2_picture") != null) {
                            sale1Order.getImage gi = new sale1Order.getImage();
                            gi.purl = "http://120.110.7.88:8080/test/圖片/" + json.getString("s2_picture");
                            new Thread(gi).start();
                            textViewName.setText(json.getString("s2_name"));
                            textViewKal.setText("");
                            textViewMenu.setText("");
                            text=json.getString("s2_text");
                            textViewmessage.setText(text);
                            textViewQQuantity.setVisibility(View.GONE);
                            textViewQuantity.setText(json.getString("s2_finish_day"));
                            textViewNewPrice.setVisibility(View.GONE);
                            switch (tmp[2]){
                                case "1":
                                    textViewOldPrice.setText("買" + tmp[3] + "送" + tmp[4]);
                                    break;
                                case "2":
                                    textViewOldPrice.setText("滿" + tmp[3] + "送" + tmp[4]);
                                    break;
                                case "3":
                                    textViewOldPrice.setText("滿" + tmp[3] + "打" + tmp[4] + "折");
                                    break;
                                case "4":
                                    textViewOldPrice.setText("滿" + tmp[3] + "現折" + tmp[4]);
                                    break;
                                case "5":
                                    textViewOldPrice.setText("買" + tmp[3] + "第二件打" + tmp[4] + "折");
                                    break;
                                case "6":
                                    textViewOldPrice.setText(tmp[3] + "原價$" + tmp[4] +",特價$" + tmp[5]);
                                    break;
                            }
                            Sday = json.getString("s2_start_day");
                            Fday = json.getString("s2_finish_day");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }

            //msg.getData().getString("response").split(",");
        }
    };
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
                    ImageView mImageView = (ImageView) findViewById(R.id.imageView14);
                    mImageView.setBackgroundColor(0x000000);
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
