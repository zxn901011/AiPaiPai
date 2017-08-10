package com.zxn.aipaipai.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by zxn on 2017-08-09.
 */

public class MyDataBaseOpenHelper extends SQLiteOpenHelper {

    private final Context mContext;
    public static final String CREATE_USERINFO="create table if not exists UserInfo (" +
            "username text," +
            "password text," +
            "phoneNumber text)";

    private static MyDataBaseOpenHelper dbhelper = null;
    public static MyDataBaseOpenHelper getInstance(Context context) {
        if (dbhelper == null) {
            dbhelper = new MyDataBaseOpenHelper(context);
        }
        return dbhelper;
    }
    public MyDataBaseOpenHelper(Context context) {
        super(context, "user_info.db", null, 1);
        this.mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERINFO);
        Toast.makeText(mContext,"Create Success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
