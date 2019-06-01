package com.example.bee_work_2;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RegisterListAdapter extends ArrayAdapter<String> {
    private List<String> listItems;
    private String item;
    private List<ViewHolder> viewHolderList = new ArrayList<ViewHolder>();
    private int resource;
    public RegisterListAdapter(Context context, int resource, List<String> listItems) {
        super(context, resource);
        this.listItems = listItems;
        this.resource = resource;
        for(String i : listItems){
            System.out.println(i);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        item= listItems.get(position);
        ViewHolder viewHolder = new ViewHolder();
        View view = view = LayoutInflater.from(getContext()).inflate(resource,null);
 //       View view =View.inflate(getContext(),R.layout.registeritem,null);
        viewHolder.title = (TextView)view.findViewById(R.id.title);
        viewHolder.content = (EditText)view.findViewById(R.id.content) ;
        if(position==2){
            viewHolder.content.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        viewHolder.title.setText(item);
        viewHolderList.add(viewHolder);
        return view;
    }
    @Override
    public int getCount(){
        return listItems.size();
    }
    public String getContent(int position){
        return viewHolderList.get(position).content.getText().toString();
    }
    class ViewHolder{
        TextView title;
        EditText content;
        public ViewHolder() {
        }
    }
}
