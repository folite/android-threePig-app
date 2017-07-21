package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class selectTime extends Activity {
    String time,d1,d2,d3;
    TextView textView;
    String[] h = {"5", "6", "7", "8", "9","10"}, m = {"00", "30"};
    Spinner s1,s2;
    Button button[]=new Button[3];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_time);
        Bundle bundle =getIntent().getExtras();
        time=bundle.getString("time");
        textView = (TextView) findViewById(R.id.textView125);
        textView.setText(time);
        setButtonName(time.substring(5,10));
        setSpinner();
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
                d2=h[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,int position, long arg3) {
                d3=m[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }
    private void setButtonName(String date) {
        button[0]=(Button)findViewById(R.id.button13);
        button[1]=(Button)findViewById(R.id.button12);
        button[2]=(Button)findViewById(R.id.button11);
        String[] temp=date.split("-");
        int checkNum=31,day=Integer.parseInt(temp[1]),month=Integer.parseInt(temp[0]);
        for(int i=0;i<3;i++){
            day++;
            if(month == 2||month == 4||month == 6||month == 9||month == 11)checkNum=30;
            if(day>checkNum){
                month+=1;
                day=1;
            }
            if(month>12)month=1;
            button[i].setText("2016-" + month + "-" + day);
        }
        d1=button[0].getText().toString();
    }
    private void setSpinner(){
        s1=(Spinner)findViewById(R.id.spinner3);
        s2=(Spinner)findViewById(R.id.spinner4);
        ArrayAdapter<String> List;
        List=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, h);
        s1.setAdapter(List);
        List=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m);
        s2.setAdapter(List);
    }
    public void select(View v){
        button[0].setTextColor(Color.BLACK);
        button[1].setTextColor(Color.BLACK);
        button[2].setTextColor(Color.BLACK);

        switch (v.getId()){
            case R.id.button13:
                d1=button[0].getText().toString();
                button[0].setTextColor(Color.BLUE);
                break;
            case R.id.button12:
                d1=button[1].getText().toString();
                button[1].setTextColor(Color.BLUE);
                break;
            case R.id.button11:
                d1=button[2].getText().toString();
                button[2].setTextColor(Color.BLUE);
                break;
        }
//        Toast.makeText(this,d1,Toast.LENGTH_SHORT).show();
    }
    public void send(View v){
        Intent i=new Intent();
        Bundle b=new Bundle();
        b.putString("time", d1+" "+d2+":"+d3);
        i.putExtras(b);
        setResult(RESULT_OK,i);
        finish();
    }
}
