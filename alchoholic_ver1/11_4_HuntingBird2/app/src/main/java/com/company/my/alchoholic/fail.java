package com.company.my.alchoholic;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class fail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fail);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(getApplicationContext(), mainpage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 1500);//1.5초동안 진행
    }
}