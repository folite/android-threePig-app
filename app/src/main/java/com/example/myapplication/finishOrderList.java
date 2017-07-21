package com.example.myapplication;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;

public class finishOrderList extends AppCompatActivity {
    ListView listView;
    String[] list_orderId,list_orderTime,list_orderPrice,list_orderStaut;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle bundle =getIntent().getExtras();
        phone=bundle.getString("phone");
        HttpThread ht = new HttpThread();
        ht.postDataParams="phone=" + phone +
                            "&mod=finishOrderList";
        new Thread(ht).start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order_list);
        listView = (ListView)findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), "你選擇的是" + list[position], Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("select",list_orderId[position]);
                bundle.putString("phone",phone);
                intent.putExtras(bundle);
                intent.setClass(finishOrderList.this, shoppingCart.class);//login_main to register_page
                startActivity(intent);//可回傳資料
            }
        });
    }
    public void printQR(View v){
        Toast.makeText(this,list_orderId[Integer.parseInt(v.getTag().toString())],Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("orderID",list_orderId[Integer.parseInt(v.getTag().toString())]);
        bundle.putString("phone",phone);
        intent.putExtras(bundle);
        intent.setClass(finishOrderList.this, Qrcode.class);//login_main to register_page
        startActivity(intent);//可回傳資料
    }
    public void Listupdate(){
        orderlsit listAdapter = new orderlsit(this);
        listView.setAdapter(listAdapter);

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
//            Toast.makeText(finishOrderList.this,msg.getData().getString("response"),Toast.LENGTH_LONG).show();
            String[] tmp = msg.getData().getString("response").split("_");
//            Toast.makeText(finishOrderList.this,tmp[2],Toast.LENGTH_SHORT).show();
            Log.d("d",msg.getData().getString("response"));

            try {
                list_orderId=tmp[1].split(",");
                list_orderTime=tmp[2].split(",");
                list_orderPrice=tmp[3].split(",");
                list_orderStaut=tmp[4].split(",");
            }catch (Exception e){}
            finally {
                if(list_orderId != null){
                    Listupdate();

                }
                else Toast.makeText(finishOrderList.this,"目前尚無資料",Toast.LENGTH_SHORT).show();
            }
        }
    };
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
    public class orderlsit extends BaseAdapter{
        private LayoutInflater myInflater;
        public orderlsit (Context c){
            myInflater = LayoutInflater.from(c);
        }
        @Override
        public int getCount() {
            return list_orderId.length;
        }

        @Override
        public Object getItem(int position) {
            return list_orderId[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = myInflater.inflate(R.layout.finish_order_list_adapter, null);
            TextView textViewOrderID=(TextView)convertView.findViewById(R.id.textView136);
            TextView textViewOrderTime=(TextView)convertView.findViewById(R.id.textView135);
            TextView textViewOrderPrice=(TextView)convertView.findViewById(R.id.textView134);
            TextView textViewOrderStaut=(TextView)convertView.findViewById(R.id.textView133);
            ImageView qrtag=(ImageView)convertView.findViewById(R.id.imageView25);
            qrtag.setTag(position);
            textViewOrderID.setText(list_orderId[position]);
            textViewOrderTime.setText(list_orderTime[position].replace(" ","\n"));
            textViewOrderPrice.setText(list_orderPrice[position]);
            String stautCH;
            if (Objects.equals(list_orderStaut[position], "0")) stautCH="未取貨";
            else if (Objects.equals(list_orderStaut[position], "1")) stautCH="已取貨";
            else if (Objects.equals(list_orderStaut[position], "2")) stautCH="逾期未取";
            else stautCH="已取貨";
            textViewOrderStaut.setText("" + stautCH);
            return convertView;
        }
    }
}
