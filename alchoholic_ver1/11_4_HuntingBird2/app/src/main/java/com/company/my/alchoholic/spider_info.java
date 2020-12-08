package com.company.my.alchoholic;

import android.content.Intent;
import android.os.SystemClock;
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
        sensor.showLed(0);//led 다 끄고 시작
        button1=(ImageButton)findViewById(R.id.imageButton_spider);
        button1.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                Intent intent = new Intent(getApplicationContext(), Spider_MainActivity.class);
                startActivity(intent);

            }
        });
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