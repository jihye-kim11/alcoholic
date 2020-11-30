package com.company.my.alchoholic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.company.my.alchoholic.sensor.Sensor;
import com.company.my.alchoholic.sensor.SensorInstance;
import com.company.my.alchoholic.sensor.SensorStatus;

public class mainpage extends AppCompatActivity {
    private ImageButton button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        button1=(ImageButton)findViewById(R.id.imageButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), loading.class);
                startActivity(intent);
               // Toast.makeText(getApplicationContext(),"메시지 알림.", Toast.LENGTH_SHORT).show(); // 짧게 표시

            }
        });

        Sensor sensor = SensorInstance.getInstance();
        sensor.readySensor();
        if (sensor.getSensorStatus() == SensorStatus.FAIL) {
            Toast.makeText(this.getApplicationContext(), "센서 리딩에 실패했습니다. 드라이버가 제대로 설치되었는지 확인해주세요.", Toast.LENGTH_LONG).show();
            System.out.println("??>?");
        } else {
            Toast.makeText(this.getApplicationContext(), "센서 리딩에 성공하였습니다.", Toast.LENGTH_LONG).show();
        }
    }
}