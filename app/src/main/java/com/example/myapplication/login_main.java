package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnFocusChangeListener;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;


import javax.net.ssl.HttpsURLConnection;

public class login_main extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    EditText login_account, login_password;
    ImageButton ib1,ib2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
//        View decorView = getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.login_menu);
        //action標題導入
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        login_account = (EditText) findViewById(R.id.login_account);
        login_password = (EditText) findViewById(R.id.login_password);
        ib1=(ImageButton)findViewById(R.id.imageButton13);
        ib2=(ImageButton)findViewById(R.id.imageButton14);
        ib1.setVisibility(View.GONE);
        ib2.setVisibility(View.GONE);


        //keybondd按下enter鍵觸發動作
        login_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                button_login_Click(v);
                return false;
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void button_login_Click(View v) {
        String data ="phone="+ login_account.getText().toString()
                    +"&passwd="+login_password.getText().toString();
        //Bundle bundle =getIntent().getExtras();
        HttpThread myThread = new HttpThread();
        myThread.postDataParams=data;
        myThread.requestURL="http://120.110.7.88:8080/test/login.php";
        new Thread(myThread).start();

    }
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //msg.getData會傳回Bundle，Bundle類別可以由getString(<KEY_NAME>)取出指定KEY_NAME的值
            //Toast.makeText(login_main.this,msg.getData().getString("response"),Toast.LENGTH_SHORT).show();
            if(Objects.equals(msg.getData().getString("response"), "ture")){
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("phone", login_account.getText().toString());
                intent.putExtras(bundle);
                intent.setClass(login_main.this, mintPage.class);//login_main to register_page
                startActivityForResult(intent, 0);//可回傳資料
                login_main.this.finish();
            }
            else if(Objects.equals(msg.getData().getString("response"), "false")){
                Toast.makeText(login_main.this,"帳號或密碼錯誤", Toast.LENGTH_LONG).show();
            }
            else if(Objects.equals(msg.getData().getString("response"), "ban")){
                Toast.makeText(login_main.this,"帳號已被停權中", Toast.LENGTH_LONG).show();
            }
        }
    };
    public void button_register_Click(View v) {
        Intent intent = new Intent();
        intent.setClass(login_main.this, test.class);
        startActivityForResult(intent, 0);
    }

    public void  button_forget_click(View v){
        Intent intent = new Intent();
        intent.setClass(login_main.this, forgetPasswd.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "login_main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "login_main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    protected void onDestroy() {
        super.onDestroy();
        //closeDB();
    }
    class HttpThread implements Runnable{
        public String postDataParams=null;
        public String requestURL="http://120.110.7.88:8080/test/login.php";

        public void run() {
            //super.run();
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
                    while ((response = br.readLine()) != null){
                        sb.append(response);
                    }
                    myBundle.putString("response",sb.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Message msg = new Message();
            msg.setData(myBundle);
            mHandler.sendMessage(msg);
        }

    }

}