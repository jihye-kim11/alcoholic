package com.company.my.alchoholic;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.company.my.alchoholic.sensor.Sensor;
import com.company.my.alchoholic.sensor.SensorInstance;

public class loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        final Sensor sensor = SensorInstance.getInstance();
        ImageView gif = (ImageView) findViewById(R.id.gif_image);
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(gif);
        Glide.with(this).load(R.drawable.loading).into(gifImage);

        int randomTime = (int)(Math.random() * 5) + 5;
        System.out.println("radom time " + randomTime);
        
        sensor.runMotor( 1, 10);
        sensor.startAnimatedDot(0);
        sensor.showLcd("", "");
        sensor.show7Seg(0);
        sensor.showLed(8);
       // sensor.show7Seg(1244);
      //  sensor.showLed(3);
       // sensor.showLed(4);
       // sensor.showLed(5);
       // sensor.showLcd(0,"hunting bird");
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                sensor.showLed(0);
                sensor.stopMotor();
                Intent intent = new Intent(getApplicationContext(), select.class);
                startActivity(intent);
                finish();
            }
        }, randomTime * 1000);//5초 딜레이를 준 후 시작

}
}