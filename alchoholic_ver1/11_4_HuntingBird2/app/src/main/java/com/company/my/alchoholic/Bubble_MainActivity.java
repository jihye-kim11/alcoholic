package com.company.my.alchoholic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

public class Bubble_MainActivity extends AppCompatActivity {
    myDBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//스크린 회전
        setContentView(R.layout.bubble_activity_main);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);

        // Statusbar 감추기
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 타이틀 감추기
        // getSupportActionBar().hide();
        setTitle("비눗방울 터뜨리기 - Thread");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ((GameView) findViewById(R.id.gameView)).mThread = null;
                finish();
                // android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        dbAdapter = new myDBAdapter(this);
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("점수");

                dbAdapter.open();
                String score = dbAdapter.Open();
                dbAdapter.close();
                if(score.equals("1")){
                    Intent intent = new Intent(getApplicationContext(), success.class);
                    startActivity(intent);
                    finish();}
                else{
                    Intent intent = new Intent(getApplicationContext(), fail.class);
                    startActivity(intent);
                    finish();}

            }
        }, 10000);//10초동안 진행

    } // onCreate

    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

} // Activity
