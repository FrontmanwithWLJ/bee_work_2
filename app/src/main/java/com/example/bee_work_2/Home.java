package com.example.bee_work_2;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DirectedAcyclicGraph;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Home extends FragmentActivity implements View.OnClickListener {

    private MyBroadCastReceiver myBroadCastReceiver;
    private IntentFilter intentFilter;
    private List<Fragment> list;
    private MyFragmentAdapter myFragmentAdapter;
    private ViewPager viewPager;
    private TextView picture_text, video_text, music_text, setting_text;
    private ImageView line;
    private int moveOne;
    private boolean isScrolling = false,isBackScrolling = false;
    private UserData userData;
    private MediaPlayer musicPlayer = new MediaPlayer() ,
            videoPlayer = new MediaPlayer();
    private CallBack callBack = new CallBack() {
        @Override
        public void init(final RecyclerView recyclerView,final PictureAdapter recyclerAdapter) {
            Home.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setAdapter(recyclerAdapter);
                }
            });
        }

        @Override
        public void onRefresh(final PictureAdapter recyclerAdapter) {
            Home.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void mediaStop(MediaPlayer mediaPlayer) {
            mediaPlayer.stop();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        init();

        //注册广播
        myBroadCastReceiver = new MyBroadCastReceiver();
        NetworkChanged changed = new NetworkChanged();
        intentFilter = new IntentFilter("android.conn.CONNECTIVITY_CHANGE");

//        intentFilter.addAction("android.conn.CONNECTIVITY_CHANGE");
        registerReceiver(changed,intentFilter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(myBroadCastReceiver);
        super.onDestroy();
    }
    public class NetworkChanged extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"broadcase",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @Override public boolean onCreateOptionsMenu (Menu menu){
     * getMenuInflater().inflate(R.menu.optionmenu,menu);
     * return false;
     * }
     */

    public void init(){
        Intent intent = getIntent();
        Fragment_music fragment_music = new Fragment_music(getApplicationContext(),musicPlayer);
        Fragment_picture fragment_picture = new Fragment_picture(getApplicationContext(),callBack);
        final Fragment_video fragment_video = new Fragment_video(getApplicationContext(),videoPlayer);
        Fragment_information fragment_information = new Fragment_information(getApplicationContext(),intent.getStringExtra("user"));
        viewPager = (ViewPager)findViewById(R.id.viewPager);
        findViewById(R.id.picture_layout).setOnClickListener(this);
        findViewById(R.id.music_layout).setOnClickListener(this);
        findViewById(R.id.video_layout).setOnClickListener(this);
        findViewById(R.id.information_layout).setOnClickListener(this);
        picture_text = (TextView) findViewById(R.id.picture_text);
        video_text = (TextView)findViewById(R.id.video_text);
        music_text = (TextView)findViewById(R.id.music_text);
        setting_text = (TextView)findViewById(R.id.setting_text);
        line = (ImageView)findViewById(R.id.line);
        list = new ArrayList<Fragment>();

        Log.e("Sssssssssss",intent.getStringExtra("user"));
        userData = UserData.getUserData(getApplicationContext(),intent.getStringExtra("user"));
        /**
         * 0 picture
         * 1 video
         * 2 music
         * 3 setting
         * **/
        list.add(fragment_picture);
        list.add(fragment_video);
        list.add(fragment_music);
        list.add(fragment_information);

        myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(myFragmentAdapter);

        initLine();
        final long[] startTime = {System.currentTimeMillis()};
        //初始页面再第一个    position = 0；
        changeTab(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                long currentTime = System.currentTimeMillis();
                if(isScrolling && currentTime- startTime[0] >100){
                    Log.e("Scroll","offset");
                    startTime[0] = currentTime;
                    moveLine(i,v*moveOne,10);
                }
                if(isBackScrolling){
                    moveLine(i,0,150);
                }

            }

            @Override
            public void onPageSelected(int i) {

                //moveLine(i,0);
                if(musicPlayer.isPlaying()){
                    if(i!=2 && musicPlayer.isPlaying()){
                        musicPlayer.pause();
                    }
                }
                if(videoPlayer.isPlaying()){
                    if(i!=1 && videoPlayer.isPlaying()){
                        videoPlayer.pause();
                        fragment_video.waitProgress();
                    }
                }
                changeTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

                switch (i) {
                    case 1:
                        Log.e("state",i+"");
                        isScrolling = true;
                        isBackScrolling = false;
                        break;
                    case 2:
                        Log.e("state",i+"");

                        isScrolling = false;
                        isBackScrolling = true;
                        break;
                    default:
                        Log.e("state",i+"");

                        isScrolling = false;
                        isBackScrolling = false;
                        break;
                }
            }
        });
    }

    public void initLine(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        ViewGroup.LayoutParams lp = line.getLayoutParams();
        lp.width = screenW/4;
        line.setLayoutParams(lp);
        moveOne = lp.width;
    }

    public void moveLine(int position,float offset,int ms){
        float curTranslationX = line.getTranslationX();
        float toPositionX = moveOne*position + offset;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(line,"translationX",curTranslationX,toPositionX);
        objectAnimator.setDuration(ms);
        objectAnimator.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.picture_layout:
                viewPager.setCurrentItem(0);
                changeTab(0);
                break;
            case R.id.video_layout:
                viewPager.setCurrentItem(1);
                changeTab(1);
                break;
            case R.id.music_layout:
                viewPager.setCurrentItem(2);
                changeTab(2);
                break;
            case R.id.information_layout:
                viewPager.setCurrentItem(3);
                changeTab(3);
                break;
            default:
                break;
        }
    }

    public void changeTab(int position){
        for(int i=0;i<4;i++){
            if(i!=position){
                changeColor(i,Color.BLACK);
            }else{
                changeColor(i,Color.RED);
            }
        }
    }
    public void changeColor(int position,int color){
        switch (position){
            case 0:
                picture_text.setTextColor(color);
                break;
            case 1:
                video_text.setTextColor(color);
                break;
            case 2:
                music_text.setTextColor(color);
                break;
            case 3:
                setting_text.setTextColor(color);
                break;
        }
    }
    interface CallBack{
        void init(RecyclerView recyclerView,PictureAdapter recyclerAdapter);
        void onRefresh(PictureAdapter recyclerAdapter);
        void mediaStop(MediaPlayer mediaPlayer);
    }
}