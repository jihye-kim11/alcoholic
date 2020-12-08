package com.company.my.alchoholic;

import android.content.Intent;
import android.os.SystemClock;
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

    private ButtonCallback btnCallback6, btnCallback7, btnCallback8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        btn1=(ImageButton)findViewById(R.id.button1);
        btn1.setOnClickListener(new  View.OnClickListener() {//슈팅버블
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), bubble_info.class);
                startActivity(intent);
            }
        });
        btn2=(ImageButton)findViewById(R.id.button2);
        btn2.setOnClickListener(new  OnSingleClickListener() {//헌팅버드
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(), bird_info.class);
                startActivity(intent);
            }
        });
        btn3=(ImageButton)findViewById(R.id.button3);
        btn3.setOnClickListener(new  OnSingleClickListener() {//스파이더
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(getApplicationContext(), spider_info.class);
                startActivity(intent);
            }
        });

        btn4=(ImageButton)findViewById(R.id.button4);
        btn4.setOnClickListener(new  OnSingleClickListener() {//스파이더
            @Override
            public void onSingleClick(View v) {
                finish();
                Intent intent = new Intent(getApplicationContext(), loading.class);
                startActivity(intent);
            }
        });

        btnCallback6 = new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1) {
                    System.out.println("6 번!");
                    Intent intent = new Intent(getApplicationContext(), spider_info.class);
                    startActivity(intent);
                }
            }
        };

        btnCallback7 = new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1) {
                    System.out.println("7 번!");
                    Intent intent = new Intent(getApplicationContext(), bird_info.class);
                    startActivity(intent);
                }
            }
        };

        btnCallback8 = new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1) {
                    System.out.println("8 번!" + value);
                    Intent intent = new Intent(getApplicationContext(), bubble_info.class);
                    startActivity(intent);
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor sensor = SensorInstance.getInstance();
        sensor.registerButtonCallback(6, btnCallback6);
        sensor.registerButtonCallback(7, btnCallback7);
        sensor.registerButtonCallback(8, btnCallback8);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Sensor sensor = SensorInstance.getInstance();
        sensor.unregisterButtonCallback(6, btnCallback6);
        sensor.unregisterButtonCallback(7, btnCallback7);
        sensor.unregisterButtonCallback(8, btnCallback8);
    }
    public abstract class OnSingleClickListener implements View.OnClickListener{

        //중복 클릭 방지 시간 설정 ( 해당 시간 이후에 다시 클릭 가능 )
        private static final long MIN_CLICK_INTERVAL = 5000;
        private long mLastClickTime = 0;

        public abstract void onSingleClick(View v);

        @Override
        public final void onClick(View v) {
            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - mLastClickTime;
            mLastClickTime = currentClickTime;

            // 중복클릭 아닌 경우
            if (elapsedTime > MIN_CLICK_INTERVAL) {
                onSingleClick(v);
                System.out.println("정상 클릭");
            }
        }
    }
}