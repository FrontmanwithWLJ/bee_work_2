package com.example.bee_work_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class Fragment_music extends Fragment implements View.OnClickListener {
    private List<String[]> musicList = new ArrayList<String[]>();       //item
    private Context context;
    private MusicAdapter listAdapter;
    private ListView listView;
    private TextView music_infor;
    private ImageButton up;
    private ImageButton pause;
    private ImageButton down;
    private MediaPlayer mediaPlayer;        //播放音乐
    private static int position = 0;                       //记录播放位置，非时间记录

    public Fragment_music(Context context,MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //提前加载
        listAdapter = new MusicAdapter(context,0,R.layout.music_list,musicList);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initMusicList();
            }
        }).start();
        //initMusicList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStace){
        View view = inflater.inflate(R.layout.layout_music,null);
        listView = (ListView)view.findViewById(R.id.music_list);
        music_infor = (TextView)view.findViewById(R.id.music_content);
        up = (ImageButton)view.findViewById(R.id.music_up);
        pause = (ImageButton)view.findViewById(R.id.music_pause);
        down = (ImageButton)view.findViewById(R.id.music_down);

        //播放控件
        up.setOnClickListener(this);
        pause.setOnClickListener(this);
        down.setOnClickListener(this);

        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
        Log.e("list_adapter",listAdapter.getCount()+"");
        Log.e("ListView",listView.toString());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 musicPlay(position);
            }
        });
        return view;
    }

    public void initMusicList(){
        /**
         * 0 歌曲名
         * 1 歌手名
         * 2 歌曲长度
         * 3 歌曲路径
         * **/
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                ,null,null,null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        int count =0;
        Log.e("music","查找音乐");
        while (cursor.moveToNext()){
            count++;
            String[] temp = new String[]{cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
            ,cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
            ,cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION))
                    ,cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))};
            musicList.add(temp);
            Log.e("music",temp[0]+temp[1]+temp[2]+temp[3]);
            if(count>2) {
                //找到三个音乐就刷新一次列表
                count=0;
                listAdapter.notifyDataSetChanged();
            }
        }
        Log.e("listAdapter","运行至此0");

        if(musicList.size()==0)
            return;
        Log.e("listAdapter","运行至此");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.music_up:
                Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();
                if(mediaPlayer==null){
                    break;
                }else if(position-1<0) {
                    Toast.makeText(getContext(),"往上已经没有歌曲了",Toast.LENGTH_SHORT).show();
                    break;
                }
                musicPlay(--position);
                break;
            case R.id.music_pause:
                Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();
                if(mediaPlayer==null){
                    break;
                }else if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }else{
                    mediaPlayer.start();
                }
                break;
            case R.id.music_down:
                Toast.makeText(getContext(),"hello",Toast.LENGTH_SHORT).show();
                if(mediaPlayer==null){
                    break;
                }else if(position+1>=musicList.size()) {
                    Toast.makeText(getContext(),"往下没有歌曲了",Toast.LENGTH_SHORT).show();
                    break;
                }
                musicPlay(++position);
                break;
        }
    }
    public void musicPlay(int position){
        this.position = position;
        String path = musicList.get(position)[3];
        music_infor.setText(musicList.get(position)[0]);
        if(mediaPlayer==null){      //实例为空则重新获取
            mediaPlayer = new MediaPlayer();
        }else{
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setDataSource(path);    //设置播放源
            mediaPlayer.prepare();              //准备
            mediaPlayer.start();                //开始播放
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
