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

        this.context = context; //assign variable
        this.newSubjects = newSubjects;
        savedPreference = new SavePreferences(); //initialize variable
    }

    //Holds our View Objects
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

            holder = new ViewHolder(); //initialize ViewHolder
            holder.subjectName = (TextView) vi.findViewById(R.id.txt_newsubname); //find our view objects
            holder.favouriteImg = (ImageView) vi.findViewById(R.id.img_favourite);

            vi.setTag(holder);  //save our holder objects into memory
        } else {
            holder = (ViewHolder) vi.getTag();  //get our holder objects from memory
        }
        NewSubject subject = (NewSubject) getItem(position);  //get subject at specified posiiton
        holder.subjectName.setText(subject.getSubject_name()); //set text of TextView to subject name


        if (checkFavoriteItem(subject)) { //check if item is favourited
            holder.favouriteImg.setImageResource(R.drawable.ic_action_favorite); //changes image if topic is favourited
            holder.favouriteImg.setTag("red"); //sets tag text red for reference purposes
        } else {
            holder.favouriteImg.setImageResource(R.drawable.ic_action_favorite_light); //changes image if topic is un-favourited
            holder.favouriteImg.setTag("grey"); //sets tag text grey for reference purposes
        }
        return vi;
    }

    /*Checks whether a particular subject exists in SharedPreferences*/
    public boolean checkFavoriteItem(NewSubject checkSubject) {
        boolean check = false;
        List<NewSubject> favorites = savedPreference.getFavorites(context); //get saved shared preferences
        if (favorites != null) {
            for (NewSubject subject : favorites) { ///check each object in list
                if (subject.getSubject_name().equals(checkSubject.getSubject_name())) { //check if object already in list
                    check = true; //return true if object is in list.
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void add(NewSubject subject) {
        super.add(subject);  //adds object to super
        newSubjects.add(subject); //adds object to list
        notifyDataSetChanged();  //notifies if any change to object
    }

    @Override
    public void remove(NewSubject subject) {
        super.remove(subject); //removes object from super
        newSubjects.remove(subject); //removes object from list
        notifyDataSetChanged();  //notifies if any change to object
    }
}
