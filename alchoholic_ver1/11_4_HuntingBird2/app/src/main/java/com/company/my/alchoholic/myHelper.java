package com.company.my.alchoholic;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class myHelper extends SQLiteOpenHelper {
    public myHelper(Context context) {
        super(context, "groupDB", null, 1);
    }

    //테이블 생성(테이블에 없을 때, 호출될 때)
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE groupTBL ( gName CHAR(20) PRIMARY KEY);");
    }

    //table 삭제, 생성
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS groupTBL");
        onCreate(db);
    }
}

