package com.example.bee_work_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class Fragment_information extends Fragment {
    private  UserData userData;
    private List<String[]> list = new ArrayList<String[]>();
    private ListView listView;
    private InformationListAdapter listAdapter;
    private View view;
    private Context context;
    private boolean first = true;

    public Fragment_information(Context context, String account){
        this.userData = UserData.getUserData(context,account);
        this.context = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStace){
        view = this.getLayoutInflater().inflate(R.layout.layout_information,null);
                //inflater.inflate(R.layout.layout_information,null);
        init();
        listAdapter.notifyDataSetChanged();
        return view;
    }
    public void init(){
        listView = (ListView)view.findViewById(R.id.information) ;
        listAdapter = new InformationListAdapter(context,R.layout.listitem,list);
        listView.setAdapter(listAdapter);
        if(first) {
            addItem();
            first = false;
        }
        listAdapter.notifyDataSetChanged();
        for(String t[] : list){
            Log.e(t[0],t[1]);
        }
    }
    public void addItem(){
        list.add(new String[]{"name",userData.mName});
        list.add(new String[]{"sex",userData.mSex});
        list.add(new String[]{"age",userData.mAge});
        list.add(new String[]{"note",userData.mNote});
        list.add(new String[]{"email",userData.mEmail});
    }
}
