package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;


public class register_page extends Activity implements View.OnClickListener {

    public static final String UPLOAD_URL = "http://120.110.7.88:8080/test/date.php";//upload php
    public static final String UPLOAD_KEY = "image";
    public int PICK_IMAGE_REQUEST = 1;
    Bitmap bitmap;
    Uri filePath;
    Thread HttpThread;
    EditText ed1, ed2, ed3, ed4, ed5;
    TextView tv1, textView10,textView40;
    Button button,button1,button2;
    // String[] items = {"男", "女"};
    Spinner spinner;
    CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5;
    ImageView imageView;
    String height,kg,kal,idealweight,daykal,details,sex,msg,phone,weight,breakfastkal;
    int adjust;
    public Calendar m_Calendar = Calendar.getInstance();
    public EditText etBirthday = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public GoogleApiClient client;
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        tv1 = (TextView) findViewById(R.id.textView4);
        textView10 = (TextView) findViewById(R.id.textView10);
        textView40 = (TextView) findViewById(R.id.textView40);
        button = (Button) findViewById(R.id.button);
        ed1 = (EditText) findViewById(R.id.editText1);
        ed2 = (EditText) findViewById(R.id.ed2);
        ed3 = (EditText) findViewById(R.id.editText3);
        ed4 = (EditText) findViewById(R.id.editText4);
        ed5 = (EditText) findViewById(R.id.ed5);
        etBirthday = (EditText) findViewById(R.id.birthday);
        etBirthday.setOnClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"男", "女"});
        //ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//下拉選單完時如何呈現
        spinner.setAdapter(adapter);
        sex=spinner.getSelectedItem().toString();

        String text = spinner.getSelectedItem().toString();//抓spinner的值為字串
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        imageView=(ImageView) findViewById(R.id.imageView);
        ed5.addTextChangedListener(watcher);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
// 透過Intent 轉跳Activity ,Intent 只是傳遞的媒介
                Bundle bundle = new Bundle();
// Bundle 裡面是裝Activity裡面的資料等於容器
                bundle.putString("message", etBirthday.getText().toString());
                bundle.putString("sex",spinner.getSelectedItem().toString());
                intent.putExtras(bundle);
// 放入bundle
                intent.setClass(register_page.this, mem_detail.class);
// 跳到哪一頁
                //startActivity(intent);
                startActivityForResult(intent, 0);
            }
        });


        button2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == button2) {
                    showFileChooser();
                }
            }
        });


        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String a = "";
                if (checkBox1.isChecked()) {
                    a = a + " " + checkBox1.getText().toString() + ",";
                }

                if (checkBox2.isChecked()) {
                    a = a + " " + checkBox2.getText().toString() + ",";
                }

                if (checkBox3.isChecked()) {
                    a = a + " " + checkBox3.getText().toString() + ",";
                }
                if (checkBox4.isChecked()) {
                    a = a + " " + checkBox4.getText().toString() + ",";
                }
                if (checkBox5.isChecked()) {
                    a = a + " " + checkBox5.getText().toString();
                }
                textView10.setText(a);
            }
        };
        checkBox1.setOnCheckedChangeListener(listener);
        checkBox2.setOnCheckedChangeListener(listener);
        checkBox3.setOnCheckedChangeListener(listener);
        checkBox4.setOnCheckedChangeListener(listener);
        checkBox5.setOnCheckedChangeListener(listener);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void gonewui(View v){
        Intent intent = new Intent();
        intent.setClass(register_page.this, test.class);
        startActivity(intent);
    }
    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //tv1.setText(data.getData().getClass().toString());
        if(resultCode == RESULT_OK){
            if(requestCode == 0){
                //resultText.setText(data.getExtras().getString(OtherActivity.FLAG));
                try {
                    //Bundle bundle1 =getIntent().getExtras();
                    height= data.getExtras().getString("height");
                    weight=data.getExtras().getString("weight");
                    kg =  data.getExtras().getString("kg");
                    kal= data.getExtras().getString("kal");
                    idealweight= data.getExtras().getString("idealweight");
                    daykal= data.getExtras().getString("daykal");
                    breakfastkal=data.getExtras().getString("breakfastkal");
                    adjust=data.getExtras().getInt("adjust");
                    tv1.setText(spinner.getSelectedItem().toString());
                    details="0";
                }catch (Exception ex){
//                    height="";
//                    kg="";
//                    kal="";
//                    idealweight="";
//                    daykal="";
//                    details="0";
                }
            }
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(register_page.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);
                HashMap<String,String> data = new HashMap<>();
                data.put(UPLOAD_KEY, uploadImage);
                String result = rh.sendPostRequest(UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            m_Calendar.set(Calendar.YEAR, year);
            m_Calendar.set(Calendar.MONTH, monthOfYear);
            m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy/MM/dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            etBirthday.setText(sdf.format(m_Calendar.getTime()));
        }
    };
    @Override
    public void onClick(View v) {
        if (v.getId() == etBirthday.getId()) {
            DatePickerDialog dialog =
                    new DatePickerDialog(register_page.this,
                            datepicker,
                            m_Calendar.get(Calendar.YEAR),
                            m_Calendar.get(Calendar.MONTH),
                            m_Calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }

    }
    public TextWatcher watcher = new TextWatcher(){

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if(s.toString().equals(ed4.getText().toString())){
                textView40.setText("密碼相同");
            }else{
                textView40.setText("密碼不相同");
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

    //按鈕的Click事件
    public void btn_onClick(View v) {
        //產生新的HttpThread物件
        //Bundle bundle =getIntent().getExtras();
        HttpThread myThread = new HttpThread();

        String data="mem_name="+ed1.getText().toString()
                +"&mem_birthday="+etBirthday.getText().toString()
                +"&mem_phone="+ed2.getText().toString()
                +"&mem_sexual="+spinner.getSelectedItem().toString()
                +"&mem_email="+ed3.getText().toString()
                +"&mem_password="+ ed4.getText().toString()
                +"&mem_favorite="+textView10.getText().toString()
                +"&mem_details="+details
                +"&mem_height="+height
                +"&mem_weight="+weight
                +"&mem_kg="+kg
                +"&mem_kal="+kal
                +"&mem_idealweight="+idealweight
                +"&mem_breakfastkal="+breakfastkal
                +"&mem_adjust="+adjust
                +"&mem_daykal="+daykal;

        myThread.postDataParams=data;
        new Thread(myThread).start();
        uploadImage();

    }

    //必須利用Handler來改變主執行緒的UI值
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//msg.getData會傳回Bundle，Bundle類別可以由getString(<KEY_NAME>)取出指定KEY_NAME的值
            tv1.setText(msg.getData().getString("response"));
        }
    };


    @Override
    public void onStart() {
        super.onStart();

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://example.myapplication/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Main Page", // TODO: Define a title for the content shown.
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


    //宣告一個新的類別並擴充Thread
    class HttpThread extends Thread {

        public String postDataParams=null;
        public String requestURL = "http://120.110.7.88:8080/test/date.php";

        @Override
        public void run() {
            //super.run();
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
    }
}