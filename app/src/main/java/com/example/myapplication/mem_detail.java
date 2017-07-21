package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bi on 2016/5/26.
 */

public class mem_detail extends Activity implements View.OnClickListener {


    TextView textView17, textView20, textView21, textView26, textView27, textView33, textView34, textView39;
    EditText editText6, editText7, editText8;
    RadioGroup radioGroup, radioGroup1;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;
    Button button3;
    String msg, sexval,phone,email,password,favorite,name;
    int h,k;
    int sex,year,a;
    double sportval;
    double kal,kg;
    //int kg;
    private GoogleApiClient client;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mem_detail);

        textView17 = (TextView) findViewById(R.id.textView17);
        textView20 = (TextView) findViewById(R.id.textView20);
        textView21 = (TextView) findViewById(R.id.textView21);
        textView26 = (TextView) findViewById(R.id.textView26);
        textView27 = (TextView) findViewById(R.id.textView27);
        textView33 = (TextView) findViewById(R.id.textView33);
        textView34 = (TextView) findViewById(R.id.textView34);//textView39
        textView39 = (TextView) findViewById(R.id.textView39);
        editText6 = (EditText) findViewById(R.id.editText6);
        editText7 = (EditText) findViewById(R.id.editText7);
        editText8 = (EditText) findViewById(R.id.editText8);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);

        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);
        radioButton5 = (RadioButton) findViewById(R.id.radioButton5);
        button3 = (Button) findViewById(R.id.button3);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        radioGroup.setOnCheckedChangeListener(listener);
        radioGroup1.setOnCheckedChangeListener(listener1);
        editText6.addTextChangedListener(watcher2);
        editText7.addTextChangedListener(watcher);
        editText8.addTextChangedListener(watcher1);
        Bundle bundle =getIntent().getExtras();
//透過Bundle 接收MainActiviy傳遞過來的資料
        try {
            editText6.setText(bundle.getString("height"));
            editText7.setText(bundle.getString("weight"));
            editText8.setText(bundle.getInt("kg"));

        }catch (Exception e){

        }
        msg = bundle.getString("message");
        sexval = bundle.getString("sex");
        if(sexval.equals("男"))
            sex=0;
        else
            sex=1;

        int h =Integer.parseInt(msg.substring(0,4));
        Toast.makeText(mem_detail.this,msg,Toast.LENGTH_LONG).show();
        year=2016-h;
        textView17.setText(""+year);
//用textView 印出來
       // int k =Integer.parseInt(editText7.getText().toString());選擇不同運動量分別做相乘 輕30中35重40
       // textView20.setText(""+(k*運動量));


        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
// 透過Intent 轉跳Activity ,Intent 只是傳遞的媒介
                Bundle bundle = new Bundle();
// Bundle 裡面是裝Activity裡面的資料等於容器
                bundle.putString("height",editText6.getText().toString());
                bundle.putString("weight",editText7.getText().toString());
                bundle.putString("kg", editText8.getText().toString());
                bundle.putString("kal",  textView20.getText().toString());
                bundle.putString("idealweight",  textView21.getText().toString());
                bundle.putString("daykal",  textView26.getText().toString());
                bundle.putString("breakfastkal",textView27.getText().toString());
                bundle.putInt("adjust",a);
// 要給他一個key值(名稱)"Message"取得edittext1裡的輸入字串
                intent.putExtras(bundle);
// 放入bundle
                //intent.setClass(mem_detail.this, register_page.class);
// 跳到哪一頁
                mem_detail.this.setResult(RESULT_OK, intent); //requestCode需跟A.class的一樣
                //startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

    }


    public TextWatcher watcher = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            if(s.length()>0) {

                int ww = Integer.parseInt(s.toString());
                int hh = Integer.parseInt(editText6.getText().toString());
                if (sex == 0) {
                    kal = 66 + (13.7 * ww) + (5 * hh) - (6.8 * year) * sportval;
                    kg = (hh - 80) * 0.7;
                } else {
                    kal = 655 + (9.6 * ww) + (1.8 * hh) - (4.7 * year) * sportval;
                    kg = (hh - 70) * 0.6;
                }
                DecimalFormat df = new DecimalFormat("#.##");
                textView27.setText("" + df.format(kal * 0.3));
                textView21.setText("" + df.format(kg));
                textView20.setText("" + df.format(kal));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };


    public TextWatcher watcher2 = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            if(s.length()>0) {

                int hh = Integer.parseInt(s.toString());
                if (sex == 0) {
                    kg = (hh - 80) * 0.7;
                } else {
                    kg = (hh - 70) * 0.6;
                }
                DecimalFormat df = new DecimalFormat("#.##");
                textView21.setText("" + df.format(kg));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };


    public TextWatcher watcher1 = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if(s.length()>0) {
                int ww = Integer.parseInt(editText7.getText().toString());
                int hh = Integer.parseInt(editText6.getText().toString());
                int ch = Integer.parseInt(s.toString());
                if (sex == 0) {
                    if(a==1)
                    kal = 66 + (13.7 * (ww + ch)) + (5 * hh) - (6.8 * year) * sportval;
                    else
                    kal = 66 + (13.7 * (ww - ch)) + (5 * hh) - (6.8 * year) * sportval;
                } else {
                    if(a==1)
                    kal = 655 + (9.6 * (ww + ch)) + (1.8 * hh) - (4.7 * year) * sportval;
                    else
                    kal = 655 + (9.6 * (ww - ch)) + (1.8 * hh) - (4.7 * year) * sportval;
                }
                DecimalFormat df = new DecimalFormat("#.##");
                textView26.setText("" + df.format(kal));
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    };



    private RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
// TODO Auto-generated method stub
            int p = group.indexOfChild((RadioButton) findViewById(checkedId));
            int count = group.getChildCount();
            switch (checkedId) {
                case R.id.radioButton1:
                    sportval=1.375;
                    break;
                case R.id.radioButton2:
                    sportval=1.55;
                    break;
                case R.id.radioButton3:
                    sportval=1.725;
                    break;
            }
            int ww = Integer.parseInt(editText7.getText().toString());
            int hh = Integer.parseInt(editText6.getText().toString());
            DecimalFormat df = new DecimalFormat("#.##");
            if(!editText8.getText().toString().equals("")) {
                int ch = Integer.parseInt(editText8.getText().toString());
                if (sex == 0) {
                    if (a == 1)
                        kal = 66 + (13.7 * (ww + ch)) + (5 * hh) - (6.8 * year) * sportval;
                    else
                        kal = 66 + (13.7 * (ww - ch)) + (5 * hh) - (6.8 * year) * sportval;
                } else {
                    if (a == 1)
                        kal = 655 + (9.6 * (ww + ch)) + (1.8 * hh) - (4.7 * year) * sportval;
                    else
                        kal = 655 + (9.6 * (ww - ch)) + (1.8 * hh) - (4.7 * year) * sportval;
                }
                textView26.setText("" + df.format(kal));
            }
            if (sex == 0) {
                kal = 66 + (13.7 * ww) + (5 * hh) - (6.8 * year) * sportval;
            } else {
                kal = 655 + (9.6 * ww) + (1.8 * hh) - (4.7 * year) * sportval;
            }
            textView27.setText("" + df.format(kal * 0.3));
            textView20.setText("" + df.format(kal));
        }
    };

    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
// TODO Auto-generated method stub
            int p = group.indexOfChild((RadioButton) findViewById(checkedId));
            int count = group.getChildCount();
            switch (checkedId) {
                case R.id.radioButton4:
                    a=1;
                    break;
                case R.id.radioButton5:
                    a=0;
                    break;
            }
        }
    };

    //按鈕的Click事件
    public void btn_onClick(View v) {




/*
//產生新的HttpThread物件
        HttpThread myThread = new HttpThread();
//設定變數值
        myThread.mem_height = editText6.getText().toString();
        myThread.mem_weight = editText7.getText().toString();
        myThread.mem_kg = editText8.getText().toString();
        myThread.start();*/



    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://example.myapplication/http/host/path")
        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://example.myapplication/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }


}