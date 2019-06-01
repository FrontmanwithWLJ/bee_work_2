package com.example.bee_work_2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
    //用来存放真实路径，，，图片、视频、音频
    private List<String> list = new ArrayList<String>();
    private String item;
    private ViewHolder viewHolder;
    private List<Bitmap> bitmapList = new ArrayList<Bitmap>();
    private ImageView imageViews;
    private Context context;

    public PictureAdapter(Context context, List<String> list, List<Bitmap> bitmapList, ImageView imageView) {
        this.bitmapList = bitmapList;
        this.list = list;
        this.imageViews = imageView;
        this.context = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        public ViewHolder(View v){
            super(v);
            imageView = (ImageView)v.findViewById(R.id.picture_list_item);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,final int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.picture_list,null);
        /**View v = LayoutInflater.from(context).
                inflate(R.layout.layout_picture,null);
        imageViews = (ImageView)v.findViewById(R.id.layout_p_picture);**/
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position){
        if(position==0)
            return;
        item = list.get(position);
        Log.e("bindViewHolder",item);
        Log.e("bitmapsize", bitmapList.size()+"");
        holder.imageView.setImageBitmap(bitmapList.get(position));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"点击",Toast.LENGTH_SHORT).show();
                imageViews.setImageBitmap(BitmapFactory.decodeFile(list.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
