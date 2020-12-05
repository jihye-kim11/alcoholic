package com.company.my.alchoholic;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class myDBAdapter {
    Context context;
    myHelper myHelper;
    SQLiteDatabase sqlDB;

    public myDBAdapter(Context context) {
        this.context = context;
    }

    //생성된 DB를 반환
    public com.company.my.alchoholic.myDBAdapter open() throws SQLException {
        myHelper = new myHelper(context);
        sqlDB = myHelper.getWritableDatabase();

        return this;
    }

    //초기화
    public void clear() {
        myHelper.onUpgrade(sqlDB, 0, 1);
    }

    //데이터 삽입 함수
    public void insert(String name) {
        sqlDB.execSQL("INSERT INTO groupTBL VALUES('" + name + "')");
    }

    //특정한 행을 수정
    public void update(String name, String num) {
        sqlDB.execSQL("UPDATE groupTBL SET gNumber=" + name + " WHERE gName=" + "\"" + num + "\"");
    }

    //특정한 행을 삭제
    public void delete(String key) {
        sqlDB.execSQL("DELETE FROM groupTBL WHERE gName=" + "\"" + key + "\"");
    }

    public void close(){
        sqlDB.close();
    }

    //커서 반환
    public Cursor getCursor () {
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);
        return cursor;
    }

    public String Open(){
        String sql;
        Cursor cursor = getCursor();
        sqlDB = myHelper.getReadableDatabase();
        sql = "";
        while (cursor.moveToNext()) {
            // 기존 아랫줄에서 수정
            sql += cursor.getString(0);
//          //sqlId += cursor.getString(0) + "\r";
            //System.out.println("sqlite id : " + strId);
        }
        cursor.close();
        return sql;
    }
}
