package com.example.callum.md_coursework_v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    List<Bitmap> bitmapList;
    int incrementer = 0;

    public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setMap (List<Bitmap> bmList){
        bitmapList = bmList; //assign the list object
    }

    public int getCount() {
        return data.size(); //return size of collection
    }

    public Object getItem(int position) {
        return position; //get the item at specified position
    }

    public long getItemId(int position) {
        return position; //get item at specified position
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null); //create your View from your XML file.

        TextView title = (TextView)vi.findViewById(R.id.subjecttitle); // find our view and assign
        TextView pubDate = (TextView)vi.findViewById(R.id.pubDate); // find our view and assign
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // find our view and assign

        HashMap<String, String> song = new HashMap<String, String>(); //initialize hashmap
        song = data.get(position); //get data at specified position in arraylist hashmap

        //gets title data from hashmap array and sets text
        title.setText(song.get("subjecttitle"));
        pubDate.setText(song.get("pubdate"));

        //update imageview based on position in listview
        thumb_image.setImageBitmap(bitmapList.get(position));

        return vi;
    }
}