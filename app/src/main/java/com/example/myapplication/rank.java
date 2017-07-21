package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class rank extends AppCompatActivity {
    TextView textViewRank[][] = new TextView[3][4],textViewRankone[] = new TextView[4];
    ImageView imageViewRankone;
    CheckBox checkBox[] = new CheckBox[3];
    Bitmap bitmap[] = new Bitmap[3];
    String phone,rank[],rankName[]={"一","二","三"};
    JSONObject json[] = new JSONObject[3];
    int now=0,select=0;
    private void findView(){
        textViewRank[0][0] = (TextView) findViewById(R.id.textView138);
        textViewRank[0][1] = (TextView) findViewById(R.id.textView139);
        textViewRank[0][2] = (TextView) findViewById(R.id.textView140);
        textViewRank[0][3] = (TextView) findViewById(R.id.textView132);
        textViewRank[1][0] = (TextView) findViewById(R.id.textView141);
        textViewRank[1][1] = (TextView) findViewById(R.id.textView142);
        textViewRank[1][2] = (TextView) findViewById(R.id.textView137);
        textViewRank[1][3] = (TextView) findViewById(R.id.textView143);
        textViewRank[2][0] = (TextView) findViewById(R.id.textView144);
        textViewRank[2][1] = (TextView) findViewById(R.id.textView145);
        textViewRank[2][2] = (TextView) findViewById(R.id.textView147);
        textViewRank[2][3] = (TextView) findViewById(R.id.textView146);
        textViewRankone[0] = (TextView) findViewById(R.id.textView130);
        textViewRankone[1] = (TextView) findViewById(R.id.textView149);
        textViewRankone[2] = (TextView) findViewById(R.id.textView150);
        textViewRankone[3] = (TextView) findViewById(R.id.textView131);
        imageViewRankone = (ImageView) findViewById(R.id.imageView28);
        checkBox[0] = (CheckBox) findViewById(R.id.checkBox10);
        checkBox[1] = (CheckBox) findViewById(R.id.checkBox11);
        checkBox[2] = (CheckBox) findViewById(R.id.checkBox12);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        findView();
        setTitleBar();
        Bundle bundle =getIntent().getExtras();
        phone = bundle.getString("phone");
        HttpThread ht = new HttpThread();
        new Thread(ht).start();
    }
    private void showRank(int n){
        if(now>3)return;
        HttpThread ht = new HttpThread();
        ht.requestURL = "http://120.110.7.88:8080/test/menu.php";
        ht.postDataParams = "id=" + n;
        new Thread(ht).start();
    }
    private void showOneData(int select){
        try {
            textViewRankone[0].setText("第" + rankName[select] + "名");
            textViewRankone[1].setText(json[select].getString("m_name"));
            textViewRankone[2].setText(json[select].getString("m_kal") + "kal");
            textViewRankone[3].setText(json[select].getString("m_text"));
            if(json[select].getInt("m_hide") == 0)checkBox[select].setEnabled(false);
            imageViewRankone.setImageBitmap(bitmap[select]);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void rankList(){
        for(int i=0;i<3;i++){
            try {
                textViewRank[i][1].setText(json[i].getString("m_name"));
                textViewRank[i][2].setText(json[i].getString("m_kal") + "kal");
                textViewRank[i][3].setText("$" + json[i].getString("m_price"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
    public void toMenu(View v){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("phone",phone);
            intent.putExtras(bundle);
            intent.setClass(this, menuTypePage.class);//login_main to register_page
            startActivity(intent);//可回傳資料
    }
    public void back(View v){
        if(--select<0)select=2;
        showOneData(select);
    }
    public void next(View v){
        if(++select>2)select=0;
        showOneData(select);
    }
    public void toOrder(View v){
        int n[] = {R.id.checkBox10,R.id.checkBox11,R.id.checkBox12};
        for(int i=0;i<3;i++){
            CheckBox checkBox = (CheckBox)findViewById(n[i]);
            if(checkBox.isChecked()){
                HttpThread th = new HttpThread();
                th.requestURL="http://120.110.7.88:8080/test/order.php";
                th.postDataParams="id="+ rank[i] +
                        "&phone="+phone+
                        "&sum=1"+
                        "&mod=addMenu";
                new Thread(th).start();
            }
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
    class getImage extends Thread {
        public String purl="";
        public int now;
        @Override
        public void run() {
            // TODO Auto-generated method stub

            bitmap[now] = getBitmapFromURL(purl);
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    showOneData(select);
                }}
            );
        }
    }
    class HttpThread extends Thread {

        public String postDataParams=null;
        public String requestURL = "http://120.110.7.88:8080/test/rank.php";

        @Override
        public void run() {
            //super.run();
            Bundle myBundle = new Bundle();
            URL url;
            //Log.d("data", postDataParams);
            Log.d("url", requestURL);
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
                if(postDataParams != null){
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(postDataParams);
                    writer.flush();
                    writer.close();
                }
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
    }
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d("data",msg.getData().getString("response"));
            String[] tmp = msg.getData().getString("response").split("#");
            switch (tmp[0]){
                case "rank":
//                    Toast.makeText(rank.this,tmp[1],Toast.LENGTH_SHORT).show();
                    rank = tmp[1].split(",");

                    showRank(Integer.parseInt(rank[now]));
                    break;
                case "menu":
                    try {
                        json[now] = new JSONObject(tmp[1]);
                        if(json[now].getString("m_picture") != null){
                            getImage gi = new getImage();
                            gi.purl = "http://120.110.7.88:8080/menu-ns/i-menu/img/" + json[now].getString("m_picture");
                            gi.now = now;
                            new Thread(gi).start();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(now<2){
                        showRank(Integer.parseInt(rank[++now]));
                    }
                    else {
                        rankList();
                    }
                    break;
                case "new OK":
                case "update OK":
                    Toast.makeText(rank.this,"已加入購物車",Toast.LENGTH_SHORT).show();
                    break;
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
