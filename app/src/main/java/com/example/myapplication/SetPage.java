package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SetPage extends AppCompatActivity {

    ListView listView4,listView2,listView3;
    String phone,list1[]={"","登出","修改密碼"},list2[]={"三隻小豬公告"},list3[]={"關於我們","常見問題","使用條款","隱私權政策"};
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_page);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        setElement();
        setTitleBar();
        Bundle bundle =getIntent().getExtras();
        //String phone =bundle.getString("phone");
        phone=bundle.getString("phone");


        list1[0]=bundle.getString("phone");
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list1);
        listView2.setAdapter(listAdapter);
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1:
                        finishAffinity();
                        Intent intent1 = new Intent(getApplicationContext(), login_main.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent1);
                        Toast.makeText(SetPage.this,"已登出",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putString("phone",list1[0]);
                        intent.putExtras(bundle);
                        intent.setClass(SetPage.this, resetPassword.class);
                        startActivity(intent);
                        break;

                }
                //Toast.makeText(getApplicationContext(), "你選擇的是" + list1[position], Toast.LENGTH_SHORT).show();
                //setnum(position);
            }


        });
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list2);
        listView3.setAdapter(listAdapter);
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("phone",list1[0]);
                intent.putExtras(bundle);
                intent.setClass(SetPage.this, PostList.class);
                startActivityForResult(intent, 0);
                //SetPage.this.finish();
            }
        });
        listAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,list3);
        listView4.setAdapter(listAdapter);
        listView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                setnum(position);
            }
        });
    }
    private void setTitleBar(){
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
    }

    public void back(View v){
        this.finish();
    }
    private void setnum(int position){
        switch (position){
            case 0:
                position=1;
                break;
            case 1:
                position=6;
                break;
            case 2:
                position=7;
                break;
            case 3:
                position=5;
                break;
        }
        //Toast.makeText(getApplicationContext(), "你選擇的是" + position, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("select", String.valueOf(position));
        bundle.putString("phone",list1[0]);
        intent.putExtras(bundle);
        intent.setClass(SetPage.this, echoPage.class);//login_main to register_page
        startActivityForResult(intent, 0);//可回傳資料
    }
    private void setElement(){
        listView2 = (ListView)findViewById(R.id.listView2);
        listView3 = (ListView)findViewById(R.id.listView3);
        listView4 = (ListView)findViewById(R.id.listView4);
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
