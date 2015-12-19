package com.example.callum.md_coursework_v1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Callum on 18/12/2015.
 */
public class MainActivityAdapter extends ArrayAdapter<NewSubject> {

    private Context context;
    List<NewSubject> newSubjects;
    SavePreferences savedPreference;

    //Constructor
    public MainActivityAdapter(Context context, List<NewSubject> newSubjects){
        super(context, R.layout.product_list_item, newSubjects);

        this.context = context;
        this.newSubjects = newSubjects;
        savedPreference = new SavePreferences();
    }

    private class ViewHolder{
        TextView subjectName;
        ImageView favouriteImg;
    }

    @Override
    public int getCount(){
        //returns size of list
        return newSubjects.size();
    }

    @Override
    public NewSubject getItem(int position){
        //returns list item at specified position
        return newSubjects.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;
        View vi = convertView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.product_list_item, null);

            holder = new ViewHolder();
            holder.subjectName = (TextView) vi.findViewById(R.id.txt_newsubname);
            holder.favouriteImg = (ImageView) vi.findViewById(R.id.img_favourite);

            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        NewSubject subject = (NewSubject) getItem(position);
        holder.subjectName.setText(subject.getSubject_name());


        if (checkFavoriteItem(subject)) {
            holder.favouriteImg.setImageResource(R.drawable.ic_action_favorite);
            holder.favouriteImg.setTag("red");
        } else {
            holder.favouriteImg.setImageResource(R.drawable.ic_action_favorite_light);
            holder.favouriteImg.setTag("grey");
        }
        return vi;
    }

    /*Checks whether a particular subject exists in SharedPreferences*/
    public boolean checkFavoriteItem(NewSubject checkSubject) {
        boolean check = false;
        List<NewSubject> favorites = savedPreference.getFavorites(context);
        if (favorites != null) {
            for (NewSubject subject : favorites) {
                if (subject.getSubject_name().equals(checkSubject.getSubject_name())) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void add(NewSubject subject) {
        super.add(subject);
        newSubjects.add(subject);
        notifyDataSetChanged();
    }

    @Override
    public void remove(NewSubject subject) {
        super.remove(subject);
        newSubjects.remove(subject);
        notifyDataSetChanged();
    }
}
