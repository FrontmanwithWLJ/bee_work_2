package com.example.bee_work_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

@SuppressLint("ValidFragment")
public class Fragment_video extends Fragment implements View.OnClickListener {

    private List<String[]> listVideo = new ArrayList<String[]>();
    private TextView videoTime;
    private ProgressBar videoProgress;
    private VideoAdapter videoAdapter;
    private ListView listView;
    private Context context;
    private MediaPlayer mediaPlayer;
    private Thread progress = null;

    //控制线程暂停结束
    private boolean wait = false;
    private boolean stop = false;
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            //SurfaceView不可见时 holder 将被销毁
            //但是mediaPlayer还是会继续播放视频音乐
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    };


    public Fragment_video(Context context,MediaPlayer mediaPlayer){
        this.context = context;
        this.mediaPlayer =  mediaPlayer;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoAdapter = new VideoAdapter(context,R.layout.video_list,listVideo);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initVideo();
            }
        }).start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStace){
        View view = inflater.inflate(R.layout.layout_video,null);
        surfaceView = (SurfaceView) view.findViewById(R.id.videoView);
        listView = (ListView)view.findViewById(R.id.video_listView);
        videoTime = (TextView)view.findViewById(R.id.video_time);
        videoProgress = (ProgressBar)view.findViewById(R.id.video_progress);
        Button play = (Button) view.findViewById(R.id.videoPlay);
        play.setOnClickListener(this);
        listView.setAdapter(videoAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoPlay(position);
            }
        });
        return view;
    }

    /**
     * 0 视频名称
     * 1 视频长度
     * 2 视频路径
     * **/
    public void initVideo(){
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null,null,null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        Log.e("Video","查找视频");
        int count = 0;
        while (cursor.moveToNext()){
            String[] temp = new String[]{
                    cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
                    ,cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                    ,cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
            };
            listVideo.add(temp);
            count++;
            if(count>4) {
                count = 0;
                videoAdapter.notifyDataSetChanged();
            }
        }
    }
    public void videoPlay(int position){

        //SurfaceView + MediaPlayer

        if(mediaPlayer==null){
            mediaPlayer = new MediaPlayer();
        }
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(listVideo.get(position)[2]);
            holder = surfaceView.getHolder();
            holder.addCallback(callback);
            mediaPlayer.setDisplay(holder);
            mediaPlayer.prepare();
            mediaPlayer.start();
            videoTime.setText(formatTime(listVideo.get(position)[1]));
            initProgress(listVideo.get(position)[1]);
            progress.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.videoPlay:{
                if (mediaPlayer == null){
                    break;
                }else if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    waitProgress();
                }else {
                    mediaPlayer.start();
                    notifyProgress();
                }
            }
        }
    }
    public String formatTime (String ms){
        int t = Integer.parseInt(ms);
        return ((t/1000/60)>9?(t/1000/60):("0"+t/1000/60))+":"+((t/1000%60)>9?(t/1000%60):("0"+t/1000%60));
    }

    public void initProgress(String time){
        if(progress!=null){
            //有线程存在就先结束
            progress.interrupt();
            //stop = true;
        }
        //确定线程结束再新开线程
        while (stop) {}
        progress = getThread(time);
    }

    public void waitProgress(){
        wait = true;
    }

    public void notifyProgress(){
        wait = false;
    }

    public Thread getThread(String time){

        //更新间隔
        final int t = Integer.parseInt(time)/100;
        return new Thread(new Runnable() {
            @Override
            public void run() {
                int count = 0;
                try {
                    do{
                        if(!wait) {
                            count++;
                            sleep(t);
                            Log.e("pogressBar",count+"");
                            videoProgress.setProgress(count);
                        }
                    }while (count<100);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
