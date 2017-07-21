package com.example.myapplication;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
import java.util.Objects;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class recommendation extends AppCompatActivity {
    String phone;
    LinearLayout sll,sll2;
    TextView textViewRec[][] = new TextView[3][3],textView;
    ImageView imageView[] = new ImageView[3];
    CheckBox checkBox[] = new CheckBox[3];
    int textviewid[] = {R.id.textView151, R.id.textView152, R.id.textView153,
                        R.id.textView154, R.id.textView155, R.id.textView156,
                        R.id.textView157, R.id.textView158, R.id.textView159},
        imageviewID[] = {R.id.imageView33, R.id.imageView34, R.id.imageView35},
        chechboxID[] = {R.id.checkBox13, R.id.checkBox14, R.id.checkBox15},kal=0,memdetail=0;
    JSONObject json[] = new JSONObject[3];
    Bitmap bitmap[] = new Bitmap[3];

    private void findView(){
        sll = (LinearLayout)findViewById(R.id.sll);
        sll2 = (LinearLayout)findViewById(R.id.sll2);
        textView = (TextView)findViewById(R.id.textView160);
        for(int i=0; i<3; i++){
            for(int j=0;j<3;j++)textViewRec[i][j] = (TextView)findViewById(textviewid[(i*3)+j]);
            imageView[i] = (ImageView) findViewById(imageviewID[i]);
            checkBox[i] = (CheckBox)findViewById(chechboxID[i]);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        findView();
        setTitleBar();
        Bundle bundle =getIntent().getExtras();
        phone = bundle.getString("phone");
        HttpThread ht = new HttpThread();
        ht.postDataParams="phone=" + phone;
        new Thread(ht).start();

    }
    public void select(View v){
        int k=0;
        for(int i=0;i<3;i++){
            if(checkBox[i].isChecked()) try {
                k+=json[i].getInt("m_kal");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        checkKal(k);
    }
    private void checkKal(int nk){
        if(kal==0 || memdetail==0){
            //RL_kal.setVisibility(View.GONE);
            textView.setText("");
        }
        else if((kal - 100) > nk)
            textView.setText("提醒您\n今天的早餐攝取卡路里(" + kal + "kal)稍微少，中午再吃點主食喔！");
        else if((kal + 100) < nk)
            textView.setText("提醒您\n今天的早餐攝取卡路里(" + kal + "kal)稍微超過，中午再吃點輕食喔！");
        else {
            textView.setText("");
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
    public void toOrder(View v){
        for(int i=0;i<3;i++){
            if(checkBox[i].isChecked()){
                HttpThread th = new HttpThread();
                th.requestURL="http://120.110.7.88:8080/test/order.php";
                try {
                    th.postDataParams="id="+ json[i].getInt("m_id") +
                            "&phone="+phone+
                            "&sum=1"+
                            "&mod=addMenu";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                    imageView[now].setBackgroundColor(0x00000000);
                    imageView[now].setImageBitmap(bitmap[now]);
                }}
            );

        }
    }
    class HttpThread extends Thread {

        public String postDataParams=null;
        public String requestURL = "http://120.110.7.88:8080/test/recommendation.php";

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
                case "recom":
//                    Toast.makeText(rank.this,tmp[1],Toast.LENGTH_SHORT).show();
                    try {
                        for(int i=0;i<3;i++){
                            json[i] = new JSONObject(tmp[i+1]);
                            textViewRec[i][0].setText(json[i].getString("m_name"));
                            textViewRec[i][1].setText(json[i].getString("m_kal") + "kal");
                            textViewRec[i][2].setText("$" + json[i].getString("m_price"));
                            if(json[i].getString("m_picture") != null ){
                                getImage gi = new getImage();
                                gi.now = i;
                                gi.purl = "http://120.110.7.88:8080/menu-ns/i-menu/img/" + json[i].getString("m_picture");
                                new Thread(gi).start();
                            }

                        }
                        HttpThread ht2 = new HttpThread();
                        ht2.requestURL="http://120.110.7.88:8080/test/date.php";
                        ht2.postDataParams="mem_phone="+phone+
                                "&mod=kal";
                        new Thread(ht2).start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "mem":

                case "new OK":
                case "update OK":
                    Toast.makeText(recommendation.this,"已加入購物車",Toast.LENGTH_SHORT).show();
                    break;
            }
            tmp = msg.getData().getString("response").split("-");
            if(Objects.equals(tmp[0], "mem")){
                kal=Integer.parseInt(tmp[1]);
                int k=0;
                for (int i = 0; i < json.length; i++) {
                    try {
                        k+=json[i].getInt("m_kal");
                        if(k > kal+100)break;
                        switch (i){
                            case 1:
                                sll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                break;
                            case 2:
                                sll2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                memdetail=Integer.parseInt(tmp[2]);
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
