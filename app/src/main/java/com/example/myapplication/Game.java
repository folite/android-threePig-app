package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

public class Game extends AppCompatActivity {
    ImageButton imv[] = new ImageButton[8];
    TextView textViewTime;
    int v[]={R.id.imageButton8,R.id.imageButton9,R.id.imageButton10,R.id.imageButton11,R.id.imageButton12,R.id.imageButton15,R.id.imageButton16,R.id.imageButton17};
    int[] img={R.drawable.p0,R.drawable.p1,R.drawable.p2,R.drawable.p3};
    Bitmap[] bitmap = new Bitmap[4];
    int time=20,tmpId=-1,tmpId2,win=0,tmp[] = new int[3];
    String phone;
    boolean timeOut=false,pause=false;
    Timer timer = new Timer(true);
    Random ran = new Random();
    private Handler handler = new Handler();

    public void findView() {
        textViewTime = (TextView)this.findViewById(R.id.textView164);
        for (int i = 0; i < imv.length; i++) {
            imv[i] = (ImageButton)findViewById(v[i]);
            imv[i].setBackgroundResource(R.color.colorPrimary);
            imv[i].setTag(i/2);
            imv[i].setOnClickListener(ocl);
        }
        for (int i = 0; i < 25; i++) {
            tmp[0] = ran.nextInt(imv.length);
            tmp[1] = ran.nextInt(imv.length);
            tmp[2] = (int) imv[tmp[0]].getTag();
            imv[tmp[0]].setTag(imv[tmp[1]].getTag());
            imv[tmp[1]].setTag(tmp[2]);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
        findView();
        timer.schedule(new MyTimerTask(), 1000, 1000);
    }
    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
        finish();
    }
    View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Toast.makeText(Game.this,"" +tmpId2 + " " + tmpId,Toast.LENGTH_SHORT).show();
            if (!timeOut && !pause) {
                v.setBackgroundResource(img[(int) v.getTag()]);
                if(tmpId==-1)tmpId=(int) v.getTag();
                else{
                    if(tmpId == (int) v.getTag()){
                        for (int i = 0; i < imv.length; i++) {
                            if((int) imv[i].getTag() == tmpId)imv[i].setOnClickListener(null);
                        }
//                        imv[tmpId*2].setOnClickListener(null);
//                        imv[tmpId*2+1].setOnClickListener(null);
                        tmpId=-1;
                        win++;
                        if(win==4){
                            Toast.makeText(Game.this,"恭喜您成功過關！\n" +
                                    "系統將會發送折價券，請至折價券專區確認",Toast.LENGTH_SHORT).show();
                            timer.cancel();
                            HttpThread ht =new HttpThread();
                            ht.postDataParams="phone="+ phone + "&mod=gameWin";
                            new Thread(ht).start();
                        }
                        return;
                    }
                    else{
                        tmpId2=(int) v.getTag();
                        pause=true;
                        handler.postDelayed(updateTimer, 1000);
                    }

                }
            }

        }
    };
    private Runnable updateTimer = new Runnable() {
        public void run() {
            for (int i = 0; i < imv.length; i++) {
                if((int) imv[i].getTag() == tmpId2 || (int) imv[i].getTag() == tmpId)imv[i].setBackgroundResource(R.color.colorPrimary);
            }
            tmpId=-1;
            pause=false;
        }
    };
    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Game.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    time--;
                    textViewTime.setText("時間剩下"+ time + "秒");
                    if(time==0){
                        Toast.makeText(Game.this,"不要氣餒！明天再接再厲喔",Toast.LENGTH_SHORT).show();
                        timer.cancel();
                        timeOut = !timeOut;
                    }
                }
            });
        }
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
//                Toast.makeText(getApplication(),msg.getData().getString("response"),Toast.LENGTH_LONG).show();

            }
        };
    }
}
