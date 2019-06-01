package com.example.bee_work_2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("ValidFragment")
public class Fragment_picture extends Fragment {
    private Context context;
    private List<String> listImage = new ArrayList<String>();
    private PictureAdapter recyclerAdapter;
    private List<Bitmap> bitmapList= new ArrayList<Bitmap>();
    private RecyclerView recyclerView;
    private Home.CallBack callBack;

    public Fragment_picture(Context context, Home.CallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        /**
         RecyclerAdapter recyclerAdapter = new RecyclerAdapter(context,listImage,bitmapList,callBack);
         RecyclerView recyclerView = getActivity().findViewById(R.id.recycler_view);
         LinearLayoutManager layoutManager = new LinearLayoutManager(context);
         recyclerView.setAdapter(recyclerAdapter);
         **/
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                initPhoto();
            }
        }).start();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceStace){
        View view = inflater.inflate(R.layout.layout_picture,null);
        ImageView imageView = (ImageView)view.findViewById(R.id.layout_p_picture);
        View loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        recyclerAdapter = new PictureAdapter(context,listImage,bitmapList,imageView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

    /**
     * Activity中菜单创建
     *public boolean onCreateOptionsMenu(Menu menu);
     * 细微差别
     *
     * Fragment里面菜单创建方式
     * 必须调用setHasOptionsMenu(true);
     * 目前还没解决，菜单在viewpager页面显示不出来
     * **/
    /**@Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater){
        inflater.inflate(R.menu.optionmenu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }*/

    public void initPhoto() {

        final String str[] = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATA
        };

        /**
         * 去找图片
         * 本来以为这个可能会大量占用时间
         * 现在知道直接访问了数据库
         * 拿到了所有图片的数据
         * 就是个遍历的事
         * **/
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                str, null, null, null);
        int count = 0;  //记录单次加载数据数量
        int sumCount = 0;//记录总共加载数量         不超过30张
        while (cursor.moveToNext()) {
            /**
             * 0 图片id
             * 1 图片文件名
             * 2 图片路径
             * **/
            Log.e("picture","查找图片");
            count++;
            sumCount++;
            listImage.add(cursor.getString(2));
            Bitmap bit_temp = BitmapFactory.decodeFile(cursor.getString(2));
            //获取图片之后，在重新创建，缩放到我要的宽高
            Bitmap picture = (Bitmap) Bitmap.createScaledBitmap(bit_temp, 400, 300, true);
            bitmapList.add(picture);
            if (count > 5) {
                count = 0;
                if(sumCount==6) {
                    callBack.init(recyclerView,recyclerAdapter);
                   // LayoutInflater.from(context).inflate(R.layout.layout_picture,null).findViewById(R.id.loading).findViewById(R.id.)
                }
                callBack.onRefresh(recyclerAdapter);
                Log.e("bitmap",bitmapList.size()+"");

               // handler.sendMessage(msg);
            }
            if (sumCount > 28) {
                return;
            }

        }
    }
    //预加载
    public void initBitmap(){
        for(String temp : listImage){
            Bitmap bit_temp = BitmapFactory.decodeFile(temp);
            //获取图片之后，在重新创建，缩放到我要的宽高
            Bitmap picture = (Bitmap) Bitmap.createScaledBitmap(bit_temp,400,300,true);
            bitmapList.add(picture);
        }
    }

}
