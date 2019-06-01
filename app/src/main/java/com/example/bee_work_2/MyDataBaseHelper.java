package com.example.bee_work_2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.PublicKey;

public class MyDataBaseHelper extends SQLiteOpenHelper {

    private Context mContext;
    private final String CREATE_USER_INFORMATION="create table user_information("
            +"name text,"
            +"sex text,"
            +"age integer,"
            +"note text,"
            +"email text,"
            +"account text,"
            +"password text)";

    public MyDataBaseHelper(Context context,String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        this.mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_INFORMATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
