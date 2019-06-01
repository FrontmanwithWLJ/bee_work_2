package com.example.bee_work_2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;



public class MyBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"收到广播",Toast.LENGTH_SHORT).show();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        //这个权限加到哪去了我也不知道
        if(networkInfo!=null&&networkInfo.isConnected()){
            Toast.makeText(context,"网络已连接",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context,"网络已断开",Toast.LENGTH_SHORT).show();
        }
    }
}
