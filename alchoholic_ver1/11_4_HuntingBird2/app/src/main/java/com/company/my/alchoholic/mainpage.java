package com.company.my.alchoholic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.company.my.alchoholic.sensor.ButtonCallback;
import com.company.my.alchoholic.sensor.Sensor;
import com.company.my.alchoholic.sensor.SensorInstance;
import com.company.my.alchoholic.sensor.SensorStatus;

public class mainpage extends AppCompatActivity {
    private ImageButton button1;
    ImageView imageview = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        imageview = (ImageView)findViewById(R.id.sound);
        final Sensor sensor = SensorInstance.getInstance();
        sensor.readySensor();
        if (sensor.getSensorStatus() == SensorStatus.FAIL) {
            Toast.makeText(this.getApplicationContext(), "센서 리딩에 실패했습니다. 드라이버가 제대로 설치되었는지 확인해주세요.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this.getApplicationContext(), "센서 리딩에 성공하였습니다.", Toast.LENGTH_LONG).show();
        }

        sensor.registerButtonCallback(0, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("0 번!");
            }
        });

        sensor.registerButtonCallback(1, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("1 번!");
            }
        });

        sensor.registerButtonCallback(2, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("2 번!");
            }
        });

        sensor.registerButtonCallback(3, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("3 번!");
            }
        });

        sensor.registerButtonCallback(4, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("4 번!");
            }
        });

        sensor.registerButtonCallback(5, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("5 번!");
            }
        });

        sensor.registerButtonCallback(6, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("6 번!");
            }
        });

        sensor.registerButtonCallback(7, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("7 번!");
            }
        });

        sensor.registerButtonCallback(8, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if (value==1)System.out.println("8 번!" + value);
            }
        });

        sensor.registerSwitchCallback(0, new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if(value == 1) {
                    imageview.setImageResource(R.drawable.sound_off);
                    System.out.println("0번 딥스위치");}
                else{imageview.setImageResource(R.drawable.sound_on);}

            }
        });

        button1=(ImageButton)findViewById(R.id.imageButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensor.startAnimatedDot(0);
                sensor.runMotor(0, 10, 10);
                Intent intent = new Intent(getApplicationContext(), loading.class);
                startActivity(intent);
            }
        });


    }
}