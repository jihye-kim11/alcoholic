package com.company.my.alchoholic;

import android.graphics.Bitmap;

public class Spider_Poison {
    // 속도, 현재 위치, 크기
    private  int speed = 1200;
    public float x, y;
    public int r;

    // 비트맵, 소멸?
    public Bitmap img;
    public boolean isDead;

    //--------------------------
    // 생성자
    //--------------------------
    public Spider_Poison(float tx, float ty) {
        x = tx;
        y = ty;

        img = Spider_CommonResources.imgPoison;
        r = Spider_CommonResources.pr;
    }

    //--------------------------
    // Move & Dead
    //--------------------------
    public void update() {
        y -= speed * Time.deltaTime;
        isDead = (y < -r);

        checkCollision();
    }

    //--------------------------
    // 충돌 체크
    //--------------------------
    private void checkCollision() {
        for (Spider_Butterfly tmp : Spider_GameView.mFly) {
            // 나비와 충돌이 발생하면 소멸한다
            if ( tmp.checkCollision(x, y, r) ) {
                isDead = true;
                break;
            }
        }
    }

} // Poison
