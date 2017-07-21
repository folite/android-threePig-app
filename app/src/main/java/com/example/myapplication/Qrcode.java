package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.magiclen.magicqrcode.QRCodeEncoder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Qrcode extends AppCompatActivity {
    ImageView qrcode;
    String phone;
    Bitmap baseBitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        qrcode=(ImageView)findViewById(R.id.imageView26);
        Bundle bundle =getIntent().getExtras();
        phone = bundle.getString("phone");
        QRCodeEncoder qr = new QRCodeEncoder("http://120.110.7.88:8080/3pigs/menu-ns/i-menu/cart_detail.php?id="+bundle.getString("orderID"));
//            qr.setErrorCorrect(QRCodeEncoder.ErrorCorrect.MAX);
        boolean[][] qrData = qr.encode();
        if (baseBitmap == null) {
            baseBitmap = Bitmap.createBitmap(1000,1000, Bitmap.Config.ARGB_8888);

        }
        Canvas canvas = new Canvas(baseBitmap);
        if(qrData != null){
//            Toast.makeText(shoppingCartSend.this,""+qrData.toString(),Toast.LENGTH_SHORT).show();
            drawQRCode(canvas,qrData);
            qrcode.setImageBitmap(baseBitmap);
        }
    }
    public static void drawQRCode (final Canvas canvas, final boolean[][] qrData){
        final Paint paint = new Paint();
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        //畫背景(全白)
        paint.setColor(Color.WHITE);
        canvas.drawRect(new Rect(0, 0, width, height), paint);

        final int imageSize = Math.min(width, height);
        final int length = qrData.length;
        final int size = imageSize / length;
        final int actualImageSize = size * length;
        final int offsetImageX = (width - actualImageSize) / 2;
        final int offsetImageY = (height - actualImageSize) / 2;

        //畫資料(true為黑色)
        paint.setColor(Color.BLACK);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (qrData[i][j]) {
                    final int x = i * size + offsetImageX;
                    final int y = j * size + offsetImageY;
                    canvas.drawRect(new Rect(x, y, x + size, y + size), paint);
                }
            }
        }
    }
    class HttpThread extends Thread {

        public String postDataParams=null;
        public String requestURL = "http://120.110.7.88:8080/test/order.php";

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
//            Toast.makeText(Qrcode.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();
            String tmp = msg.getData().getString("response");
            String data="DATA ERROR!";
            try {
                JSONObject json = new JSONObject(tmp);
                data="訂單編號:" + json.getString("o_id") +"\n"
                            + "購買者" +json.getString("mem_phone") +"\n"
                            + "取貨時間" +json.getString("o_buytime") +"\n"
                            + "總金額" +json.getString("o_price");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            QRCodeEncoder qr = new QRCodeEncoder(data);
//            qr.setErrorCorrect(QRCodeEncoder.ErrorCorrect.MAX);
            boolean[][] qrData = qr.encode();
            if (baseBitmap == null) {
                baseBitmap = Bitmap.createBitmap(1000,1000, Bitmap.Config.ARGB_8888);

            }
            Canvas canvas = new Canvas(baseBitmap);
            if(qrData != null){
//            Toast.makeText(shoppingCartSend.this,""+qrData.toString(),Toast.LENGTH_SHORT).show();
                drawQRCode(canvas,qrData);
                qrcode.setImageBitmap(baseBitmap);
            }

        }
    };
}
