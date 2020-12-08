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
        button1.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(getApplicationContext(), loading.class);
                startActivity(intent);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                sensor.registerButtonCallback(0, new ButtonCallback() {
                    @Override
                    public void onButtonClick(int value) {
                        if (value == 1){
                            for (int idx = 0; idx < 9; idx++){
                                sensor.showLed(idx);
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        }).start();

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