package com.example.bee_work_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class UserData implements Parcelable{
    public String mName;
    public String mAge;
    public String mSex;
    public String mNote;
    public String mEmail;
    private String mAccount;
    private String mPassword;
    private MyDataBaseHelper dataBaseHelper;
    private Context mContext;
    private SQLiteDatabase db;

    public UserData(Context context, String age, String name , String sex, String note,
                   String email, String account , String password){
        dataBaseHelper = new MyDataBaseHelper(context,"user_information",null,1);
        db = dataBaseHelper.getWritableDatabase();
        this.mContext =context;
        this.mName = name ;
        this.mAge = age;
        this.mSex = sex;
        this.mNote = note;
        this.mEmail = email;
        this.mAccount = account;
        this.mPassword = password;
    }

    protected UserData(Parcel in) {
        mName = in.readString();
        mAge = in.readString();
        mSex = in.readString();
        mNote = in.readString();
        mEmail = in.readString();
        mAccount = in.readString();
        mPassword = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    //保存至数据库
    public boolean apply(){
        if(!MyQuery(mAccount)){
            Toast.makeText(mContext,"用户已存在",Toast.LENGTH_SHORT).show();
            return false;
        }
        ContentValues values = new ContentValues();
        values.put("name",mName.equals("")?" ":mName);
        values.put("sex",mSex.equals("")?" ":mSex);
        values.put("age",Integer.parseInt(mAge.equals("")?"18":mAge));
        values.put("note",mNote.equals("")?" ":mNote);
        values.put("email",mEmail.equals("")?" ":mEmail);
        values.put("account",mAccount);
        values.put("password",mPassword);
        db.insert("user_information",null,values);
        return true;
    }
    //查询账户是否存在
    public boolean MyQuery(String account){
        Cursor cursor = dataBaseHelper.getWritableDatabase().query("user_information",null,"account=?",new String[] {account},null,null,null);
        if(cursor.moveToFirst()==false){
            return true;
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mSex);
        dest.writeString(mAge);
        dest.writeString(mNote);
        dest.writeString(mEmail);
    }
    public static UserData getUserData(Context context,String account){
        MyDataBaseHelper myDataBaseHelper = new MyDataBaseHelper(context,"user_information",null,1);
        SQLiteDatabase db = myDataBaseHelper.getWritableDatabase();
        Cursor cursor = db.query("user_information",null,"account=?",new String[]{account},null,null,null);
        if(cursor.moveToFirst()==false){
            return null;
        }
        else {
            byte[] name = cursor.getBlob(cursor.getColumnIndex("name"));
            byte[] sex = cursor.getBlob(cursor.getColumnIndex("sex"));
            byte[] note = cursor.getBlob(cursor.getColumnIndex("note"));
            byte[] email = cursor.getBlob(cursor.getColumnIndex("email"));
            byte[] password = cursor.getBlob(cursor.getColumnIndex("password"));

            try {
                String str_name = new String(name,"utf-8").trim();
                String str_sex = new String(sex,"utf-8").trim();
                String str_age = ""+cursor.getInt(cursor.getColumnIndex("age"));
                String str_note = new String(note,"utf-8").trim();
                String str_email = new String(email,"utf-8").trim();
                String str_password = new String(password,"utf-8").trim();
                UserData temp = new UserData(context,str_age,str_name,str_sex,str_note,str_email,account,str_password);
                return temp;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public void dataBaseClose(){
        db.close();
    }
}
