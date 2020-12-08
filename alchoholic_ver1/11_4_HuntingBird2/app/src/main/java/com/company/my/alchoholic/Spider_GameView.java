package com.company.my.alchoholic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import com.company.my.alchoholic.sensor.Sensor;
import com.company.my.alchoholic.sensor.SensorInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

//-----------------------------
// GameView
//-----------------------------
public class Spider_GameView extends View {
    // Context, Thread
    private Context context;
    private GameThread mThread;

    // Random, 배경, 화면 크기
    private Random rnd = new Random();
    private Bitmap imgBack;
    private int w, h;

    // 점수, 포획 나비 수, 명중율
    private int score = 0;
    private int killCount = 0;
    private float rate;

    //타이머
    private int inputNumber = 20;

    // 점수 표시용
    private Paint paint = new Paint();

    // 나비, 독액, 거미
    static public List<Spider_Butterfly> mFly;
    static public List<Spider_Poison> mPoison;
    private Spider spider;
    //sqlite
    myDBAdapter dbAdapter;
    //센서
    final Sensor sensor = SensorInstance.getInstance();
    //-----------------------------
    // 생성자
    //-----------------------------
    public Spider_GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        dbAdapter = new myDBAdapter(context);
        dbAdapter.open();
        dbAdapter.clear();
        dbAdapter.insert("0");//첫 시작에서만 0
        dbAdapter.close();
        sensor.showLed(0);//시작시 센서 초기화
        // 리소스 설정
        Spider_CommonResources.set(context);

        // 글자크기, 글자색, 글자속성(Bold체)
        paint.setTextSize(60);
        paint.setColor(Color.BLACK);
        paint.setFakeBoldText(true);

        // 나비, Poison
        mFly = Collections.synchronizedList(new ArrayList<Spider_Butterfly>());
        mPoison = Collections.synchronizedList(new ArrayList<Spider_Poison>());

        //타이머
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                /**
                 * 넘겨받은 what값을 이용해 실행할 작업을 분류합니다
                 */
                if(msg.what==1){
                    Log.d("What Number : ", "What is 1");
                }else if(msg.what==2){
                    Log.d("What Number : ", "What is 2");
                }
            }
        };
        Runnable task = new Runnable(){
            public void run(){
                while(inputNumber > 0){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}

                    --inputNumber;
                    /**
                     * sendEmptyMessage은 단순한 int형 What을 전달하기 때문에
                     * Message객체의 생성이 필요가 없습니다
                     */
                    handler.sendEmptyMessage(1);

                    /**
                     * sendMessage는 message객체를 넘겨주며,
                     * 이때 what의 값, arg1, arg2등 int형 값을 줄수도 있고
                     * intent등의 객체 전체를 넘길수도 있다 (message.obj = 겍체)
                     */
                    Message message= Message.obtain();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();

    }

    //-----------------------------
    // View의 크기 구하기
    //-----------------------------
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        this.w = w;  // 화면의 폭과 높이
        this.h = h;

        // 배경 이미지
        imgBack = BitmapFactory.decodeResource(getResources(), R.drawable.spider_back);
        imgBack = Bitmap.createScaledBitmap(imgBack, w, h, true);

        spider = new Spider(w, h);

        // 스레드 기동
        if (mThread == null) {
            mThread = new GameThread();
            mThread.start();

        }
    }

    //-----------------------------
    // View의 종료
    //-----------------------------
    @Override
    protected void onDetachedFromWindow() {
        mThread.canRun = false;
        super.onDetachedFromWindow();
    }

    //-----------------------------
    // 화면 그리기 - 동기화
    //-----------------------------
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(imgBack, 0, 0, null);

        // 나비
        synchronized (mFly) {
            for (Spider_Butterfly tmp : mFly) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.w, tmp.y - tmp.h, null);
            }
        }

        // Poison
        synchronized (mPoison) {
            for (Spider_Poison tmp : mPoison) {
                canvas.drawBitmap(tmp.img, tmp.x - tmp.r, tmp.y - tmp.r, null);
            }
        }

        // 거미
        canvas.drawBitmap(spider.img, spider.x - spider.w, spider.y - spider.h, null);

        // 점수
       // paint.setTextAlign(Paint.Align.LEFT);
      //  canvas.drawText("득점 : " + score, 50, 80, paint);

     //   paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("남은 시간 : " + inputNumber, w / 2, 80, paint);
        //canvas.drawText("포획 : " + killCount, w / 2, 80, paint);

      //  rate = killCount / (spider.fireCnt + 0.001f) * 100;
      //  paint.setTextAlign(Paint.Align.RIGHT);
       // canvas.drawText(String.format("명중률 : %4.1f%%", rate), w - 50, 80, paint);
    }

    //-----------------------------
    // 나비 만들기
    //-----------------------------
    private void makeButterfly() {
        synchronized (mFly) {
            if (mFly.size() < 8) {
                mFly.add(new Spider_Butterfly(w, h));
            }
        }
    }

    //-----------------------------
    // 이동
    //-----------------------------
    private void moveObject() {
        // 나비
        synchronized (mFly) {
            for (Spider_Butterfly tmp : mFly) {
                tmp.update();
            }
        }

        // 거미
        spider.update();

        // Poison
        synchronized (mPoison) {
            for (Spider_Poison tmp : mPoison) {
                tmp.update();
            }
        }
    }

    //-----------------------------
    // 제거
    //-----------------------------
    private void removeDead() {
        // 득점 추가 및 나비 초기화
        for (int i = mFly.size() - 1; i >= 0; i--) {
            if (mFly.get(i).isDead) {
                killCount++;
                score += mFly.get(i).score;
                //led 1개씩 켜기
                System.out.println(killCount + "led 켜기");
                // 나비 초기화
                mFly.get(i).init();
                //점수 db에 저장

                if (killCount == 1) {
                    //led 0 켜기
                    sensor.showLed(1);
                    dbAdapter.open();
                    dbAdapter.clear();
                    dbAdapter.insert("0");
                    dbAdapter.close();
                } else if (killCount == 2) {
                    //led 1 켜기
                    sensor.showLed(2);
                    dbAdapter.open();
                    dbAdapter.clear();
                    dbAdapter.insert("0");
                    dbAdapter.close();
                } else if (killCount == 3) {
                    //led 1 켜기
                    sensor.showLed(3);
                    dbAdapter.open();
                    dbAdapter.clear();
                    dbAdapter.insert("0");
                    dbAdapter.close();
                } else if (killCount == 4) {
                    //led 1 켜기
                    sensor.showLed(4);
                    dbAdapter.open();
                    dbAdapter.clear();
                    dbAdapter.insert("0");
                    dbAdapter.close();
                } else if (killCount == 5) {
                    //led 1 켜기
                    sensor.showLed(5);
                    dbAdapter.open();
                    dbAdapter.clear();
                    dbAdapter.insert("0");
                    dbAdapter.close();
                } else if (killCount == 6) {
                    //led 1 켜기
                    sensor.showLed(6);
                    dbAdapter.open();
                    dbAdapter.clear();
                    dbAdapter.insert("0");
                    dbAdapter.close();
                } else if (killCount == 7) {
                    //led 1 켜기
                    sensor.showLed(7);
                    dbAdapter.open();
                    dbAdapter.clear();
                    dbAdapter.insert("0");
                    dbAdapter.close();
                } else if (killCount == 8) {
                    //led 1 켜기
                    sensor.showLed(8);
                    dbAdapter.open();
                    dbAdapter.clear();
                    dbAdapter.insert("1");
                    dbAdapter.close();
                }

            }
        }

        // Poison 제거
        synchronized (mPoison) {
            for (int i = mPoison.size() - 1; i >= 0; i--) {
                if (mPoison.get(i).isDead) {
                    mPoison.remove(i);
                }
            }
        }
    }

    //-----------------------------
    // Touch Event
    //-----------------------------
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            spider.setAction(event.getX(), event.getY());
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            spider.stop();
        }

        return true;
    }

    //-----------------------------
    // Thread
    //-----------------------------
    class GameThread extends Thread {
        public volatile boolean canRun = true;

        @Override
        public void run() {
            while (canRun) {
                try {
                    Time.update();      // deltaTime 계산

                    makeButterfly();
                    moveObject();
                    removeDead();
                    postInvalidate();   // 화면 그리기
                    sleep(10);
                } catch (Exception e) {
                    //
                }
            }
        }
    } // Thread

} // GameView
