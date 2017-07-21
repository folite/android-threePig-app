package com.example.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class resetPassword extends AppCompatActivity {
    EditText password1,password2,oldpassword;
    Button enter;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
        setElement();
    }
    public void button_enter_click(View v){
        HttpThread ht =new HttpThread();

        ht.postDataParams="phone="+phone
                            +"&passwd1="+password1.getText()
                            +"&newpasswd="+password2.getText()
                            +"&oldpasswd="+oldpassword.getText();
        new Thread(ht).start();
    }
    private void setElement(){
        password1=(EditText)findViewById(R.id.editText2);
        password2=(EditText)findViewById(R.id.editText5);
        oldpassword=(EditText)findViewById(R.id.editText9);
        enter=(Button)findViewById(R.id.button5);
    }
    class HttpThread extends Thread {

        public String postDataParams=null;
        public String requestURL = "http://120.110.7.88:8080/test/resetPassword.php";

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
            Toast.makeText(resetPassword.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();
        }
    };
}
