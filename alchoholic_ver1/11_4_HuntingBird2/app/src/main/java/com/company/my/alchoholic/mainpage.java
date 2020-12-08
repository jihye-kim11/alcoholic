package com.company.my.alchoholic;

import android.content.Intent;
import android.os.SystemClock;
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

    private ButtonCallback switchCallback;

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

        switchCallback = new ButtonCallback() {
            @Override
            public void onButtonClick(int value) {
                if(value == 1) {
                    imageview.setImageResource(R.drawable.sound_on);
                }
                else{
                    imageview.setImageResource(R.drawable.sound_off);
                }
            }
        };

        button1=(ImageButton)findViewById(R.id.imageButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), loading.class);
                startActivity(intent);
            }
        });
        
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: 액티비티 가려졌을 떄 호출되는 건지 확인
        SensorInstance.getInstance().unregisterSwitchCallback(0, switchCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: 액티비티 돌아왓을 때 호출되는건지 확인
        SensorInstance.getInstance().registerSwitchCallback(0, switchCallback);
    }


}