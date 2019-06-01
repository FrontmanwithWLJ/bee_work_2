package com.example.bee_work_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class Login extends AppCompatActivity {
    private CheckBox remember;
    private Button login;
    private EditText user,password;
    private SharedPreferences temp,data;
    private SharedPreferences.Editor temp_editor;
    private MyDataBaseHelper myDataBaseHelper;
    private SQLiteDatabase db ;
    private String u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_login);
       // getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_withback);

        init();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.optionmenu,menu);
        return true;
    }
    public void init(){
        myDataBaseHelper = new MyDataBaseHelper(getApplicationContext(),"user_information",null,1);
        db = myDataBaseHelper.getWritableDatabase();
        user = (EditText)findViewById(R.id.user);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.login);
        remember = (CheckBox)findViewById(R.id.remember);
        temp = getSharedPreferences("temp_data",MODE_PRIVATE);
        temp_editor = temp.edit();
        Intent intent = getIntent();
        u = intent.getStringExtra("user");
        String p = intent.getStringExtra("password");
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkRight(user.getText().toString(),password.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Loading",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    if(!remember.isChecked())
                        temp.edit().putString("password","");
                    intent.putExtra("user",user.getText().toString());
                    db.close();
                    startActivity(intent);
                    setResult(RESULT_OK);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        });

       // Log.e("dsssssssssssssssssssss",u);
        //Log.e("dsssssssssssssssssssss",p);
        if(u!=null&&p!=null){
            user.setText(u);
            password.setText(p);
            login.callOnClick();
        }
        else {
            user.setText(temp.getString("name", ""));
            password.setText(temp.getString("password", ""));
        }

        /**findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
         */

    }
    public boolean checkRight(String name,String password) {
        Cursor cursor = db.query("user_information", null, "account=?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst() == false) {
            return false;
        } else {
            //解决可能出现的乱码
            byte[] val = cursor.getBlob(cursor.getColumnIndex("password"));
            String pwd = null;
            try {
                pwd = new String(val, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            pwd = pwd.trim();//去掉多余空格
            if (password.equals(pwd)) {
                if (remember.isChecked()) {
                    //记住密码
                    temp_editor.putString("name", name);
                    temp_editor.putString("password", password);
                    temp_editor.apply();
                }
                return true;
            } else {
                return false;
            }
        }
    }
}
