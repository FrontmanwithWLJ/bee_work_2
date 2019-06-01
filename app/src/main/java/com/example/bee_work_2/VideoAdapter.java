package com.example.bee_work_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends ArrayAdapter<String[]> {
    private ListView listView;
    private Context context;
    private int resourceId;
    private List<String[]> videoList = new ArrayList<String[]>();
    public VideoAdapter(Context context,int resourceId,List<String[]> videoList){
        super(context,resourceId,videoList);
        this.context = context;
        this.videoList = videoList;
        this.resourceId = resourceId;
    }
    @Override
    public View getView(int position, View convertView , ViewGroup viewGroup){
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView content = (TextView)view.findViewById(R.id.video_list_item);
        content.setText("视频: "+videoList.get(position)[0]+
                "\n\t长度: "+videoList.get(position)[1]);
        return view;
    }
    @Override
    public int getCount(){
        return videoList.size();
    }
}
