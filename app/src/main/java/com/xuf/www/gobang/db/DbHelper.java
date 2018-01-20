package com.xuf.www.gobang.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Admin on 2018/1/9.
 */

public class DbHelper extends SQLiteOpenHelper {
    public static final String HIS= "create table tbl_his( " +
            "uid integer primary key autoincrement," +
            "condition text," +
            "mode text," +
            "mycolor text," +
            "hiscolor text," +
            "time text," +
            "date text);";
    public DbHelper(Context context)
    {
        super(context,"history.db",null,1);
    }
    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HIS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
