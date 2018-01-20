package com.xuf.www.gobang.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2018/1/9.
 */

public class HisDao {
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    public void openDb(Context context)
    {
        if(db==null)
        {
            dbHelper = new DbHelper(context);
            db = dbHelper.getWritableDatabase();
        }
    }
    public List<History> getAllHistoryMessage()
    {
        List<History> histories = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from tbl_his", null);
        while (cursor.moveToNext())
        {
            int uid = cursor.getInt(cursor.getColumnIndex("uid"));
            String condition = cursor.getString(cursor.getColumnIndex("condition"));
            String mode = cursor.getString(cursor.getColumnIndex("mode"));
            String mycolor = cursor.getString(cursor.getColumnIndex("mycolor"));
            String hiscolor = cursor.getString(cursor.getColumnIndex("hiscolor"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            History history = new History(uid, condition, mode, mycolor, hiscolor, date,time);
            histories.add(history);
        }
        return histories;
    }
    public void addHistory(History history)
    {
        db.execSQL("insert into tbl_his(condition,mode,mycolor,hiscolor,time,date) values(?,?,?,?,?,?)",new String[]{
                history.getCondition(),history.getMode(),history.getMycolor(),history.getHiscolor(),history.getTime(),history.getDate()
        });
    }
    public void deleteHistory(int uid)
    {
        db.execSQL("delete from tbl_his where uid = ?",new String[]{String.valueOf(uid)});
    }
}
