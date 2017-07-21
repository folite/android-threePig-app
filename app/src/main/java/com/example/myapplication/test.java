package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.Thread.sleep;

public class test extends AppCompatActivity {
    LinearLayout mem,linearLayout3;
    ImageView imageViewhear,imageViewPasswdCheck,imageViewPasswd;
    TextView textViewPreferences,textViewYear,textViewKal,textViewKg,textViewd,textViewf;
    EditText editTextBirthday,editTextPasswd,editTextpasswdCheck,editTextHeight,editTextWeight,editTextNewWeight,editTextName,editTextPhone,editTextEmail;
    CheckBox checkBoxvegetable,checkBoxCattle,checkBoxpig,checkBoxchicken,checkBoxseafood;
    Calendar m_Calendar = Calendar.getInstance();
    String preferences,phone;
    int sex=1,details=0,years=0,adjust=0,exercise=0,breakfastkal=0,daykal=0,kal=0,kg=0,newW=0,ww=0,hh=0;
    double sportval=1.375;
    Boolean passwdCheck=false;
    Bitmap bitmap;
    DecimalFormat df=new DecimalFormat("#");
    private void findView(){
        textViewPreferences=(TextView)findViewById(R.id.textView42);
        mem = (LinearLayout)findViewById(R.id.mem);
        linearLayout3 = (LinearLayout)findViewById(R.id.linearLayout3);
        checkBoxvegetable=(CheckBox)findViewById(R.id.checkBox);
        checkBoxCattle=(CheckBox)findViewById(R.id.checkBox6);
        checkBoxpig=(CheckBox)findViewById(R.id.checkBox7);
        checkBoxchicken=(CheckBox)findViewById(R.id.checkBox8);
        checkBoxseafood=(CheckBox)findViewById(R.id.checkBox9);
        editTextBirthday=(EditText)findViewById(R.id.editText17);
        editTextBirthday.setInputType(InputType.TYPE_NULL);
        editTextPasswd=(EditText)findViewById(R.id.editText12);
        editTextpasswdCheck=(EditText)findViewById(R.id.editText13);
        imageViewPasswdCheck=(ImageView)findViewById(R.id.imageView8);
        imageViewPasswd=(ImageView)findViewById(R.id.imageView7);
        editTextHeight=(EditText)findViewById(R.id.editText14);
        editTextWeight=(EditText)findViewById(R.id.editText15);
        editTextNewWeight=(EditText)findViewById(R.id.editText16);
        textViewYear=(TextView)findViewById(R.id.textView69);
        textViewd=(TextView)findViewById(R.id.textView59);
        textViewf=(TextView)findViewById(R.id.textView60);
        textViewKg=(TextView)findViewById(R.id.textView71);
        textViewKal=(TextView)findViewById(R.id.textView70);
        editTextName=(EditText)findViewById(R.id.editText10);
        editTextPhone=(EditText)findViewById(R.id.editText11);
        editTextEmail=(EditText)findViewById(R.id.editText18);
        imageViewhear=(ImageView)findViewById(R.id.imageView4);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findView();
        setTitleBar();
        linearLayout3.setVisibility(View.GONE);
        editTextBirthday.setOnFocusChangeListener(myEditTextFocus);
        editTextPasswd.addTextChangedListener(textPasswdCheck);
        editTextpasswdCheck.addTextChangedListener(textPasswdCheck);
        editTextpasswdCheck.addTextChangedListener(textPasswdCheck);
        editTextWeight.addTextChangedListener(editHW);
        editTextHeight.addTextChangedListener(editHW);
        editTextNewWeight.addTextChangedListener(editNewHW);
        imageViewhear.setOnClickListener(selectImage);
        textViewPreferences.setText("");
    }
    public TextWatcher textPasswdCheck = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
            if(editTextpasswdCheck.getText().toString().equals(editTextPasswd.getText().toString())){
                imageViewPasswdCheck.setBackgroundResource(R.drawable.ok);
                passwdCheck=true;
            }else{
                imageViewPasswdCheck.setBackgroundResource(R.drawable.passwd);
                passwdCheck=false;
            }
            if(editTextPasswd.getText().length()!=0)imageViewPasswd.setBackgroundResource(R.drawable.ok);
            else {
                imageViewPasswd.setBackgroundResource(R.drawable.passwd);
                imageViewPasswdCheck.setBackgroundResource(R.drawable.passwd);
                passwdCheck=false;
            }

        }
    };
    public TextWatcher editNewHW = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(editTextNewWeight.getText().length()>0)newW=Integer.parseInt(editTextNewWeight.getText().toString());
            if(newW==0){
                textViewf.setText(""+df.format(kal*0.3));
                textViewd.setText(""+df.format(kal));
            }
            setData1(newW);

        }
    };
    public TextWatcher editHW = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setData1(0);
        }
    };
    private View.OnClickListener selectImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 建立 "選擇檔案 Action" 的 Intent
            Intent intent = new Intent( Intent.ACTION_PICK );

            // 過濾檔案格式
            intent.setType( "image/*" );

            // 建立 "檔案選擇器" 的 Intent  (第二個參數: 選擇器的標題)
            Intent destIntent = Intent.createChooser( intent, "選擇檔案" );

            // 切換到檔案選擇器 (它的處理結果, 會觸發 onActivityResult 事件)
            startActivityForResult( destIntent, 0 );
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // 有選擇檔案
        if ( resultCode == RESULT_OK && requestCode == 0 ){
            // 取得檔案的 Uri
            Uri uri = data.getData();
            if( uri != null ){
                // 利用 Uri 顯示 ImageView 圖片
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    imageViewhear.setBackgroundColor(0x00ffffff);
                    imageViewhear.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(test.this,uri.toString(),Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(test.this,"無效的檔案路徑 !!",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(test.this,"取消 !!",Toast.LENGTH_SHORT).show();
        }
    }
    public View.OnFocusChangeListener myEditTextFocus = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() == R.id.editText17 && hasFocus) {
                DatePickerDialog dialog = new DatePickerDialog(test.this,
                            datepicker,
                            m_Calendar.get(Calendar.YEAR),
                            m_Calendar.get(Calendar.MONTH),
                            m_Calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                editTextBirthday.clearFocus();
            }
            if((v.getId()==R.id.editText14 ||v.getId()==R.id.editText15) &&  hasFocus == false){
                setData1(newW);
            }
        }
    };
    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            m_Calendar.set(Calendar.YEAR, year);
            m_Calendar.set(Calendar.MONTH, monthOfYear);
            m_Calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "yyyy/MM/dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.TAIWAN);
            editTextBirthday.setText(sdf.format(m_Calendar.getTime()));
            //取得今日年份
            Date today = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            years=Integer.parseInt(dateFormat.format(today))-Integer.parseInt(editTextBirthday.getText().toString().substring(0,4));
            textViewYear.setText(""+years);
            setData1(newW);
        }
    };
    private void setData1(int nw){

        if(editTextWeight.getText().length()>0)ww=Integer.parseInt(editTextWeight.getText().toString())+nw;
        if(editTextHeight.getText().length()>0)hh=Integer.parseInt(editTextHeight.getText().toString());
        if(!(ww != 0 && hh != 0 && years>0))return;
        if(sex==0){
            kal= (int) ((66+(13.7*ww)+(5*hh)-(6.8*years))*sportval);
            kg= (int) ((hh-80)*0.7);
        }else{
            kal= (int) ((655+(9.6*ww)+(1.8*hh)-(4.7*years))*sportval);
            kg= (int) ((hh-70)*0.6);
        }

        if(nw!=0){
            textViewf.setText(""+df.format(kal*0.3));
            textViewd.setText(""+df.format(kal));
            setData1(0);
        }
        else{
            textViewKg.setText(""+df.format(kg));
            textViewKal.setText(""+df.format(kal));

        }
    }
    public void setSportval(View v){
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.radioButton9:
                if(checked){
                    sportval=1.375;
                    exercise=0;
                }
                break;
            case R.id.radioButton10:
                if(checked){
                    sportval=1.55;
                    exercise=1;
                }
                break;
            case R.id.radioButton11:
                if(checked){
                    sportval=1.725;
                    exercise=2;
                }
                break;
        }
        setData1(newW);
    }
    public void rb2(View v){
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.radioButton12:
                if (checked){
                    newW=Math.abs(newW);
                    adjust=0;
                }
                break;
            case R.id.radioButton13:
                if (checked){
                    newW=(-1)*Math.abs(newW);
                    adjust=1;
                }
                break;
        }
        setData1(newW);
    }
    public void getPreferences(View v){
        preferences="";
        if(checkBoxvegetable.isChecked())preferences+="蔬食,";
        if(checkBoxCattle.isChecked())preferences+="牛肉,";
        if(checkBoxpig.isChecked())preferences+="豬肉,";
        if(checkBoxchicken.isChecked())preferences+="雞肉,";
        if(checkBoxseafood.isChecked())preferences+="海鮮,";
        if(preferences.length()!=0)preferences=preferences.substring(0,preferences.length()-1);
        textViewPreferences.setText(preferences);
    }
    public void setSex(View v){
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.radioButton://女
                if (checked){
                    sex=1;
                }
            case R.id.radioButton6://男
                if (checked){
                    sex=0;
                }
        }
        setData1(newW);
    }
    public void openDetails(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton7:
                if (checked){
                    mem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    details=1;
                }
                break;
            case R.id.radioButton8:
                if (checked){
                    mem.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                    details=0;
                }
                break;
        }
    }
    public void register(View v) throws InterruptedException {
        if(textViewf.getText().length()>0)breakfastkal=Integer.parseInt(textViewf.getText().toString());
        if(textViewd.getText().length()>0)daykal=Integer.parseInt(textViewd.getText().toString());
        if(editTextNewWeight.getText().length()>0)newW=Integer.parseInt(editTextNewWeight.getText().toString());
        phone=editTextPhone.getText().toString();
        HttpThread th = new HttpThread();
        String data="mem_name="+editTextName.getText().toString()
                +"&mem_birthday="+editTextBirthday.getText().toString()
                +"&mem_phone="+editTextPhone.getText().toString()
                +"&mem_sexual="+sex
                +"&mem_email="+editTextEmail.getText().toString()
                +"&mem_password="+ editTextPasswd.getText().toString()
                +"&mem_favorite="+textViewPreferences.getText().toString()
                +"&mem_details="+details
                +"&mem_height="+hh
                +"&mem_weight="+ww
                +"&mem_kg="+newW
                +"&mem_kal="+kal
                +"&mem_idealweight="+kg
                +"&mem_breakfastkal="+breakfastkal
                +"&mem_adjust="+adjust
                +"&mem_exercise="+exercise
                +"&mem_daykal="+daykal;

        th.postDataParams=data;
        new Thread(th).start();


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
                loading = ProgressDialog.show(test.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                if(Objects.equals(s, "success")){
                    Toast.makeText(getApplicationContext(), "註冊完成", Toast.LENGTH_LONG).show();
                    finish();
                }
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String UPLOAD_KEY="http://120.110.7.88:8080/test/date.php";
                String uploadImage = getStringImage(bitmap);
                HashMap<String,String> data = new HashMap<>();
                data.put("mem_phone", phone);
                data.put("image", uploadImage);
                String result = rh.sendPostRequest(UPLOAD_KEY,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }
    public void back(View v){
        this.finish();
    }
    private void setTitleBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        ImageButton ib1=(ImageButton)findViewById(R.id.imageButton13);
        ImageButton ib2=(ImageButton)findViewById(R.id.imageButton14);
        ib1.setVisibility(View.GONE);
        ib2.setVisibility(View.GONE);
    }
    class HttpThread extends Thread {

        public String postDataParams=null;
        public String requestURL = "http://120.110.7.88:8080/test/date.php";

        @Override
        public void run() {
            //super.run();
            Bundle myBundle = new Bundle();
            URL url;
            Log.d("data", postDataParams);
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
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(Objects.equals(msg.getData().getString("response"), "dataOK")){
                if(bitmap != null){
                    uploadImage();
                    return;
                }
                else {
                    Toast.makeText(getApplicationContext(), "註冊完成", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            Toast.makeText(test.this,msg.getData().getString("response"),Toast.LENGTH_SHORT).show();
        }
    };
}
