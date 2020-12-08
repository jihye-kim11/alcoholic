package com.company.my.alchoholic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.company.my.alchoholic.sensor.Sensor;
import com.company.my.alchoholic.sensor.SensorInstance;

public class spider_info extends AppCompatActivity {
    private ImageButton button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Sensor sensor = SensorInstance.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spider_info);
        sensor.showLcd(0,"spider");
        button1=(ImageButton)findViewById(R.id.imageButton_spider);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Spider_MainActivity.class);
                startActivity(intent);

            }
        });
    }
}