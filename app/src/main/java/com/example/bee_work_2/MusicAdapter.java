package com.example.bee_work_2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends ArrayAdapter<String[]> {
    private List<String[]> musicList = new ArrayList<String[]>();
    private Context context;
    private int resourceId;

    public MusicAdapter(Context context, int resource, int textViewResourceId,List<String[]> musicList) {
        super(context, resource, textViewResourceId);
        this.context = context;
        this.musicList = musicList;
        this.resourceId = textViewResourceId;
    }

    @Override
    public int getCount(){
        return musicList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        String[] temp = musicList.get(position);
        TextView title = (TextView)view.findViewById(R.id.music_title);
        title.setText(
                "歌曲: "+temp[0]+"\t歌手: "+temp[1]
                +"\n\t\t时间： "+temp[2]
        );
        return view;
    }
}



    /***public ListAdapter(Context context, List<String[]> musicList,int resourceId){
        super(context,resourceId);
        this.context = context;
        this.resourceId = resourceId;
        this.musicList = musicList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view =  LayoutInflater.from(getContext()).inflate(R.layout.music_list,null);
        TextView title = (TextView)view.findViewById(R.id.music_title);
        Log.e("music_list",musicList.get(position)[3]);
        /**title.setText(musicList.get(position)[3]);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点播成功",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
*/