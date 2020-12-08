package com.company.my.alchoholic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.company.my.alchoholic.sensor.Sensor;
import com.company.my.alchoholic.sensor.SensorInstance;

public class bubble_info extends AppCompatActivity {
    private ImageButton button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Sensor sensor = SensorInstance.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubble_info);
        sensor.showLcd(0,"shooting bubble");
        button1=(ImageButton)findViewById(R.id.imageButton_bubble);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Bubble_MainActivity.class);
                startActivity(intent);

            }
        });
    }
}