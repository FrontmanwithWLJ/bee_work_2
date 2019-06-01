package com.example.bee_work_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

//我的信息列表适配器
public class InformationListAdapter extends ArrayAdapter<String[]> {
    private List<String[]> listItems;
    private String[] item;
    public InformationListAdapter(Context context, int resource, List<String[]> listItems) {
        super(context, resource);
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
        item= listItems.get(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.listitem,null);
        TextView title = (TextView)view.findViewById(R.id.title);
        TextView content = (TextView) view.findViewById(R.id.content);
        title.setText(item[0]);
        content.setText(item[1]);
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }
    @Override
    public int getCount(){
        return listItems.size();
    }
}
