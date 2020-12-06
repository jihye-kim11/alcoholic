package com.company.my.alchoholic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.company.my.alchoholic.sensor.ButtonCallback;
import com.company.my.alchoholic.sensor.Sensor;
import com.company.my.alchoholic.sensor.SensorInstance;

public class select extends AppCompatActivity {
    private ImageButton btn1,btn2,btn3,btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        final Sensor sensor = SensorInstance.getInstance();
        sensor.readySensor();
        btn1=(ImageButton)findViewById(R.id.button1);
        btn1.setOnClickListener(new  View.OnClickListener() {//슈팅버블
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), bubble_info.class);
                startActivity(intent);
            }
        });
        btn2=(ImageButton)findViewById(R.id.button2);
        btn2.setOnClickListener(new  View.OnClickListener() {//헌팅버드
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), bird_info.class);
                startActivity(intent);
            }
        });
        btn3=(ImageButton)findViewById(R.id.button3);
        btn3.setOnClickListener(new  View.OnClickListener() {//스파이더
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), spider_info.class);
                startActivity(intent);
            }
        });

        btn4=(ImageButton)findViewById(R.id.button4);
        btn4.setOnClickListener(new  View.OnClickListener() {//스파이더
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), loading.class);
                startActivity(intent);
            }
        });

        sensor.registerButtonCallback(6, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("6 번!");
                Intent intent = new Intent(getApplicationContext(), spider_info.class);
                startActivity(intent);
            }
        });

        sensor.registerButtonCallback(7, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("7 번!");
                Intent intent = new Intent(getApplicationContext(), bird_info.class);
                startActivity(intent);
            }
        });

        sensor.registerButtonCallback(8, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("8 번!" + value);
                Intent intent = new Intent(getApplicationContext(), bubble_info.class);
                startActivity(intent);
            }
        });
    }
}