package com.example.myapplication;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class shoppingCart extends AppCompatActivity {
    ListView listView,listView1,listView2;
    TextView textView,textView2,textView3,textView4,textView5,textView6,textView7,textView8;
    RelativeLayout RL_kal;
    LinearLayout mem1,mem2,mem3;
    EditText editTextRemark;
    Button button1,button2;
    String[] list1={"not date"},list2={""},list3={""},list4={""},list5={""},couList={"未選擇"},couListId={""},couListHId={""},
            slist1={"not date"},slist2={""},slist3={""},slist4={""},
            s2list1={""},s2list2={""};
    showlist listAdapter = null;
    Spinner spinner;
    String phone,menuOld,id,out;
    int tmp=-1,menuTatol[]=new int[2],saleTatol[]=new int[3],couListIdSelect=0,kal=0;
    boolean checkPage=false,detail=false,finishPage=false;
    ArrayAdapter<String> couponList;
    private void findView(){
        listView=(ListView)findViewById(R.id.listView9);
        textView=(TextView)findViewById(R.id.textView83);
        textView2=(TextView)findViewById(R.id.textView112);
        listView1=(ListView)findViewById(R.id.listView7);
        listView2=(ListView)findViewById(R.id.listView10);
        spinner=(Spinner)findViewById(R.id.spinner2);
        editTextRemark=(EditText) findViewById(R.id.editText19);
        button1=(Button)findViewById(R.id.button7);
        button2=(Button)findViewById(R.id.button8);
        textView3=(TextView)findViewById(R.id.textView86);
        textView4=(TextView)findViewById(R.id.textView88);
        textView5=(TextView)findViewById(R.id.textView99);
        textView6=(TextView)findViewById(R.id.textView127);
        textView7=(TextView)findViewById(R.id.textView128);
        textView8=(TextView)findViewById(R.id.textView161);
        mem1=(LinearLayout)findViewById(R.id.mem1);
        mem2=(LinearLayout)findViewById(R.id.mem2);
        mem3=(LinearLayout)findViewById(R.id.mem3);
        RL_kal = (RelativeLayout) findViewById(R.id.Rl_kal);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        findView();
        getPhone();
        getcoupon();
        setTitleBar();
        listView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!checkPage) {
//                    Toast.makeText(getApplicationContext(), "你選擇的是" + list4[position], Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", list4[position]);
                    bundle.putString("phone", phone);
                    bundle.putString("mod2", "change");
                    bundle.putString("sum", list2[position]);
                    intent.putExtras(bundle);
                    intent.setClass(shoppingCart.this, menuOrder.class);
                    startActivity(intent);
                }

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(tmp!=position && position!=0){
                    couListIdSelect=position;
//                    Toast.makeText(getApplicationContext(), couListHId[position], Toast.LENGTH_SHORT).show();
                    HttpThread ht = new HttpThread();
                    ht.requestURL="http://120.110.7.88:8080/test/have.php";
                    ht.postDataParams="phone="+phone
                            +"&=select="+couListId[position]
                            +"&mod=use";
                    //new Thread(ht).start();
                }
                tmp=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
    protected void onStart() {
        super.onStart();
        Bundle bundle =getIntent().getExtras();
        if(bundle.getString("select") != null){
            id=bundle.getString("select");
            finishPage=true;
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            spinner.setEnabled(false);
            editTextRemark.setEnabled(false);
            HttpThread ht =new HttpThread();
            ht.requestURL="http://120.110.7.88:8080/test/order.php";
            ht.postDataParams="phone="+phone+
                    "&mod=list"
                    +"&select=" + bundle.getString("select");
            new Thread(ht).start();
            HttpThread ht2 =new HttpThread();
            ht2.requestURL="http://120.110.7.88:8080/test/sale1.php";
            ht2.postDataParams="phone="+phone
                    +"&mod=list"
                    +"&select=" + bundle.getString("select");
            new Thread(ht2).start();
            if(!finishPage)return;

//            HttpThread ht3 =new HttpThread();
//            ht3.requestURL="http://120.110.7.88:8080/test/order.php";
//            ht3.postDataParams="phone="+phone
//                    +"&mod=sale2"
//                    +"&select=" + bundle.getString("select");
//            new Thread(ht3).start();
        }
        else resetList();
    }
    private void checkKal(){
        if(kal==0 || !detail){
            //RL_kal.setVisibility(View.GONE);
            textView6.setText("");
        }
        else if((kal - 100) > Integer.parseInt(textView4.getText().toString()))
            textView6.setText("提醒您\n今天的早餐攝取卡路里(" + kal + "kal)稍微少，中午在吃點主食喔！");
        else if((kal + 100) < Integer.parseInt(textView4.getText().toString()))
            textView6.setText("提醒您\n今天的早餐攝取卡路里(" + kal + "kal)稍微超過，中午在吃點輕食喔！");
        else {
            textView6.setText("");
        }
    }
    public void tatol(){
        if(textView.getText().length()>0 && textView2.getText().length()>0){
            int t = Integer.parseInt(textView.getText().toString()) + Integer.parseInt(textView2.getText().toString());
            textView3.setText("" + t);
        }
        else textView3.setText(Integer.toString(menuTatol[0]+saleTatol[0]));
        textView4.setText(Integer.toString(menuTatol[1]+saleTatol[1]));
        HttpThread ht =new HttpThread();
        ht.requestURL="http://120.110.7.88:8080/test/date.php";
        ht.postDataParams="mem_phone="+phone+
                    "&mod=kal";
        new Thread(ht).start();
        if(finishPage){
            try {
                int t = Integer.parseInt(out) - saleTatol[0];
                textView.setText("" + t);
                textView3.setText("" + out);
            }catch (Exception e){
             e.printStackTrace();
            }

        }

    }
    public void selectTime(View v){
        if(spinner.isEnabled()==false)return;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = sDateFormat.format(new java.util.Date());
//        Toast.makeText(this,date,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("time", date);
        intent.putExtras(bundle);
        intent.setClass(shoppingCart.this, selectTime.class);
        startActivityForResult(intent,0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch(requestCode ){
                case 0:
//                Toast.makeText(this, data.getExtras().getString("time"), Toast.LENGTH_SHORT).show();
                    textView5.setText(data.getExtras().getString("time"));
            }
        }

    }
    public void printTag(final View v){
        //Toast.makeText(shoppingCart.this,""+(int) v.getTag(),Toast.LENGTH_SHORT).show();
        if(checkPage)return;
        if ((int)v.getTag()<1000){
            new AlertDialog.Builder(shoppingCart.this)
                .setTitle("注意")
                .setMessage("確定要刪除"+list1[(int) v.getTag()]+"?")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        HttpThread ht =new HttpThread();
                        ht.postDataParams="phone="+phone
                                    +"&mod=delete"
                                    +"&id="+list5[(int) v.getTag()];

                        new Thread(ht).start();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"沒有刪除喔", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
        }else{
            new AlertDialog.Builder(shoppingCart.this)
                    .setTitle("注意")
                    .setMessage("確定要刪除"+slist1[(int) v.getTag()-1000]+"?")
                    .setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            HttpThread ht =new HttpThread();
                            ht.requestURL="http://120.110.7.88:8080/test/order.php";
                            ht.postDataParams="phone="+phone
                                        +"&mod=delete"
                                        +"&id="+slist4[(int) v.getTag()-1000];


                            new Thread(ht).start();
                        }
                    })
                    .setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"沒有刪除喔", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        }


    }

    private void getPhone(){
        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
    }
    private void getcoupon(){
        HttpThread ht =new HttpThread();
        ht.requestURL="http://120.110.7.88:8080/test/have.php";
        ht.postDataParams="phone="+phone
                        +"&mod=list";
        new Thread(ht).start();
    }
    private void showcoupon(){
        couponList = new ArrayAdapter<String>(shoppingCart.this, android.R.layout.simple_spinner_item, couList);
        spinner.setAdapter(couponList);
    }
    private void resetList(){
        HttpThread ht =new HttpThread();
        ht.requestURL="http://120.110.7.88:8080/test/order.php";
        ht.postDataParams="phone="+phone
                +"&mod=list";
        new Thread(ht).start();
        HttpThread ht2 =new HttpThread();
        ht2.requestURL="http://120.110.7.88:8080/test/sale1.php";
        ht2.postDataParams="phone="+phone
                +"&mod=list";
        new Thread(ht2).start();

    }
    public void Listupdate(){
        showlist2 listAdapter = new showlist2(this);
        listView1.setAdapter(listAdapter);
        int h=107;
        if(slist1.length ==1 && Objects.equals(slist1[0], "")){
            h=0;
            mem1.setVisibility(View.GONE);
        }
        else{
            mem1.setVisibility(View.VISIBLE);
        }
        listView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,h*slist1.length));

    }
    public void List2update(){
        listAdapter = new showlist(this);
        listView.setAdapter(listAdapter);
        int h=107;
        if(list1.length ==1 && Objects.equals(list1[0], "")){
            h=0;
            mem2.setVisibility(View.GONE);
        }
        else{
            mem2.setVisibility(View.VISIBLE);
        }
        listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,h*list1.length));

    }
    public void List3update(){
        showlist3 listAdapter = new showlist3(this);
        listView2.setAdapter(listAdapter);
        int h=107;
        if(s2list1.length ==1 && Objects.equals(s2list1[0], "")){
            h=0;
            mem3.setVisibility(View.GONE);
        }
        else{
            mem3.setVisibility(View.VISIBLE);
        }
        listView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,h*s2list1.length));
        if(!finishPage)textView.setText("" + saleTatol[2]);
        tatol();
    }
    public void toMenu(View v){
        if(checkPage){
            button1.setText("繼續點餐");
            button2.setText("確認訂單");
            checkPage=false;
            editTextRemark.setEnabled(true);
            spinner.setEnabled(true);
            resetList();
            listView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0));
        }
        else {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("phone",phone);
            intent.putExtras(bundle);
            intent.setClass(shoppingCart.this, menuTypePage.class);//login_main to register_page
            startActivity(intent);//可回傳資料
        }
    }
    public void checkOrder(View v){
        if(checkPage){

            HttpThread ht =new HttpThread();
            ht.requestURL="http://120.110.7.88:8080/test/order.php";
            ht.postDataParams="phone="+phone
                    +"&mod=finish"
                    +"&getTime=" + textView5.getText().toString()
                    +"&remak="+editTextRemark.getText();
            if(spinner.getSelectedItemPosition() > 0)ht.postDataParams+="&h_id=" + couListHId[spinner.getSelectedItemPosition()];
            new Thread(ht).start();
        }
        else{
            if(Objects.equals(textView5.getText().toString(), "請點選")){
                Toast.makeText(this,"請填寫定購日期",Toast.LENGTH_LONG).show();
                return;
            }
            button1.setText("上一步");
            button2.setText("完成訂單");
            checkPage=true;
            editTextRemark.setEnabled(false);
            spinner.setEnabled(false);
            HttpThread ht =new HttpThread();
            ht.requestURL="http://120.110.7.88:8080/test/have.php";
            ht.postDataParams="phone="+phone
                            +"&mod=check"
                            + "&tatol=" + textView.getText();
            if(spinner.getSelectedItemPosition() > 0)ht.postDataParams+="&select=" + couListHId[spinner.getSelectedItemPosition()];
//            Toast.makeText(this,spinner.getSelectedItemPosition(),Toast.LENGTH_SHORT).show();
            new Thread(ht).start();

        }

    }
    class HttpThread extends Thread {

        public String postDataParams=null;
        public String requestURL = "http://120.110.7.88:8080/test/order.php";

        @Override
        public void run() {
            Bundle myBundle = new Bundle();
            URL url;
            Log.d("data", postDataParams);
            Log.d("data", requestURL);
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
//                Toast.makeText(shoppingCart.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();
                Log.d("msg",msg.getData().getString("response"));
                String[] tmp=msg.getData().getString("response").split("-");
                if(Objects.equals(tmp[0], "menu")) {
                    menuOld=msg.getData().getString("response");
                    list1 = tmp[1].split(",");
                    list2 = tmp[2].split(",");
                    list3 = tmp[3].split(",");
                    list4 = tmp[4].split(",");
                    list5 = tmp[5].split(",");
                    menuTatol[0]=Integer.parseInt(tmp[6]);
                    menuTatol[1]=Integer.parseInt(tmp[7]);
                    textView7.setText("");

                    if(finishPage){
                        textView5.setText(tmp[8]);
                        editTextRemark.setText(tmp[9]);
                        try {
                            couList[0]=tmp[10];
                            out=tmp[11];
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        HttpThread ht2 =new HttpThread();
                        ht2.requestURL="http://120.110.7.88:8080/test/order.php";
                        ht2.postDataParams="phone="+phone
                                +"&mod=sale2"+
                                "&tatol=" + menuTatol[0];
                        if(finishPage) ht2.postDataParams+="&select=" + id;
//            Toast.makeText(this,spinner.getSelectedItemPosition(),Toast.LENGTH_SHORT).show();
                        new Thread(ht2).start();
                        showcoupon();
                    }
                    else{
                        try {
                            if(tmp[8] != null)textView7.setText("沒有符合選擇的折價券條件");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }



                    if(checkPage){

                    }
                    else {
                        textView.setText("" + menuTatol[0]);
                        HttpThread ht2 =new HttpThread();
                        ht2.requestURL="http://120.110.7.88:8080/test/order.php";
                        ht2.postDataParams="phone="+phone
                                +"&mod=sale2"+
                                "&tatol=" + menuTatol[0];
                        if(finishPage) ht2.postDataParams+="&select=" + id;
//            Toast.makeText(this,spinner.getSelectedItemPosition(),Toast.LENGTH_SHORT).show();
                        new Thread(ht2).start();
                    }
//                    Toast.makeText(shoppingCart.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();
                    List2update();
                    tatol();
                }
                else if(Objects.equals(tmp[0], "sale1")) {
                    slist1 = tmp[1].split(",");
                    slist2 = tmp[2].split(",");
                    slist3 = tmp[3].split(",");
                    slist4 = tmp[4].split(",");
                    saleTatol[0]=Integer.parseInt(tmp[5]);
                    saleTatol[1]=Integer.parseInt(tmp[6]);
                    textView2.setText("" + saleTatol[0]);
                    tatol();
//                    Toast.makeText(shoppingCart.this,tmp[0],Toast.LENGTH_LONG).show();
                    Listupdate();
                }
                else if(Objects.equals(tmp[0], "coupon")){
                    couList=tmp[1].split(",");
                    couListId=tmp[2].split(",");
                    couListHId=tmp[3].split(",");
                    showcoupon();
//                    Toast.makeText(shoppingCart.this,tmp[1],Toast.LENGTH_SHORT).show();
                }

                else if(Objects.equals(tmp[0], "delete OK")){
                    Toast.makeText(shoppingCart.this,"選擇以刪除",Toast.LENGTH_LONG).show();
                    resetList();
                }
                else if(Objects.equals(tmp[0], "orderFinist")){
                    Toast.makeText(shoppingCart.this,"下訂成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("order", tmp[1]);
                    bundle.putString("phone", phone);
                    intent.putExtras(bundle);
                    intent.setClass(shoppingCart.this, shoppingCartSend.class);
                    startActivity(intent);
                    finish();
                }
                else if(Objects.equals(tmp[0], "mem")){
                    kal=Integer.parseInt(tmp[1]);
                    if(Objects.equals(tmp[2], "1"))detail = true;
                    checkKal();
                }
                else if(Objects.equals(tmp[0], "message")){
                    Toast.makeText(shoppingCart.this, tmp[1], Toast.LENGTH_LONG).show();
                }
                else if(Objects.equals(tmp[0], "sale2")){
//                    Toast.makeidText(shoppingCart.thishoppingCarts, msg.getData().getString("response"), Toast.LENGTH_LONG).show();
                    s2list1=tmp[1].split("#");
                    s2list2=tmp[2].split("#");
                    saleTatol[2]=Integer.parseInt(tmp[3]);
                    List3update();
                    tatol();
                }
            }
        };
    }
    public  class showlist extends BaseAdapter {
        private LayoutInflater myInflater;

        public showlist (Context c){
            myInflater = LayoutInflater.from(c);
        }
        @Override
        public int getCount() {
            return list1.length;
        }

        @Override
        public Object getItem(int position) {
            return list1[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = myInflater.inflate(R.layout.shopping_cart_list, null);
            TextView name = (TextView) convertView.findViewById(R.id.textView95);
            TextView Quantity = (TextView) convertView.findViewById(R.id.textView96);
            TextView price = (TextView) convertView.findViewById(R.id.textView97);
            ImageButton de = (ImageButton) convertView.findViewById(R.id.imageButton27);

            name.setText(list1[position]);
            Quantity.setText("數量"+list2[position]);
            price.setText("$" + Integer.parseInt(list3[position]) * Integer.parseInt(list2[position]) );
            de.setTag(position);
            return convertView;
        }
    }
    public  class showlist2 extends BaseAdapter {
        private LayoutInflater myInflater;

        public showlist2 (Context c){
            myInflater = LayoutInflater.from(c);
        }
        @Override
        public int getCount() {
            return slist1.length;
        }

        @Override
        public Object getItem(int position) {
            return slist1[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = myInflater.inflate(R.layout.shopping_cart_list, null);
            TextView name = (TextView) convertView.findViewById(R.id.textView95);
            TextView Quantity = (TextView) convertView.findViewById(R.id.textView96);
            TextView price = (TextView) convertView.findViewById(R.id.textView97);
            ImageButton de = (ImageButton) convertView.findViewById(R.id.imageButton27);

            name.setText(slist1[position]);
            Quantity.setText("數量"+slist2[position]);
            price.setText("$"+ slist3[position] );
            de.setTag(position+1000);
            return convertView;
        }
    }
    public  class showlist3 extends BaseAdapter {
        private LayoutInflater myInflater;

        public showlist3 (Context c){
            myInflater = LayoutInflater.from(c);
        }
        @Override
        public int getCount() {
            return s2list1.length;
        }

        @Override
        public Object getItem(int position) {
            return s2list1[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = myInflater.inflate(R.layout.sale2_list, null);
            TextView name = (TextView) convertView.findViewById(R.id.textView162);
            ImageView de = (ImageView) convertView.findViewById(R.id.imageView36);

            name.setText(s2list1[position]);
            if(Objects.equals(s2list2[position], "OK"))de.setImageResource(R.drawable.success);
            else de.setImageResource(R.drawable.error);
            return convertView;
        }
    }
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
