package com.example.bee_work_2;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    private ListView listView;
    private RegisterListAdapter registerListAdapter;
    private List<String> list = new ArrayList<String>();
    private MyDataBaseHelper dataBaseHelper;

   /**
    private EditText user;
    private EditText password;
    private SharedPreferences data;
    private SharedPreferences.Editor editor;
    **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_register);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_withback);

       // init();
        initData();
    }
    /**
     * SharedPreferences记住密码
     * Database 保存用户数据**/
    /** public void init(){
        user = (EditText)findViewById(R.id.user);
        password = (EditText)findViewById(R.id.password);
        data = getSharedPreferences("user_data",MODE_PRIVATE);
        editor = data.edit();

        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_text = user.getText().toString();
                final String password_text = password.getText().toString();
                Log.e("1111111111",user_text);
                Log.e("1111111111",password_text);
                if(user_text.equals("")||password_text.equals("")){
                    Toast.makeText(getApplicationContext(),"账号密码不能为空!",Toast.LENGTH_SHORT).show();
                    user.setText("");
                    password.setText("");
                }
                else {
                    Toast.makeText(getApplicationContext(), "注册成功!\n即将自动登录", Toast.LENGTH_SHORT).show();
                    editor.putString(user_text, password_text);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    intent.putExtra("name", user_text);
                    intent.putExtra("password", password_text);
                    setResult(RESULT_OK);
                    startActivity(intent);
                    finish();
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
**/

    //弹出消息框还有问题
    /**
    public void MyAlert(final String user_text,final String password_text){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
        alertDialog.setTitle("用户："+user_text+"已存在").setMessage("是否更改密码?").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.putString(user_text,password_text);
                editor.apply();
                Toast.makeText(getApplicationContext(),"修改成功!",Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).create().show();
    }
     **/

    public void initData(){
        dataBaseHelper = new MyDataBaseHelper(this,"user_information",null,1);

        listView = (ListView)findViewById(R.id.list_view);
        list.add("Name");
        list.add("Sex");
        list.add("Age");
        list.add("Note");
        list.add("Email");
        list.add("Account");
        list.add("Password");
        registerListAdapter = new RegisterListAdapter(getApplicationContext(),R.layout.registeritem,list);
        registerListAdapter.notifyDataSetChanged();
        listView.setAdapter(registerListAdapter);

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserData userData = new UserData(getApplicationContext(),registerListAdapter.getContent(2),registerListAdapter.getContent(0),
                        registerListAdapter.getContent(1),registerListAdapter.getContent(3),registerListAdapter.getContent(4),
                        registerListAdapter.getContent(5),registerListAdapter.getContent(6));
                SQLiteDatabase db = dataBaseHelper.getWritableDatabase();
                String account = registerListAdapter.getContent(5),
                        password = registerListAdapter.getContent(6);
                if(account.equals("")||password.equals("")){
                    Toast.makeText(getApplicationContext(),"账号或密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(userData.apply()){
                    Toast.makeText(getApplicationContext(),"注册成功\n即将跳转登录",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    intent.putExtra("user",account);
                    intent.putExtra("password",password);
                    setResult(RESULT_OK);
                    startActivity(intent);
                    finish();
                }
                userData.dataBaseClose();
            }
        });


        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    //查询账户是否存在
    public boolean MyQuery(String account){
        Cursor cursor = dataBaseHelper.getWritableDatabase().query("user_information",null,"account=?",new String[] {account},null,null,null);
        if(cursor.moveToFirst()==false){
            return true;
        }
        return false;
    }
}
