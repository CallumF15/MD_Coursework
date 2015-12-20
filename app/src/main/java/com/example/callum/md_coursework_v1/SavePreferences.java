package com.example.callum.md_coursework_v1;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Callum on 18/12/2015.
 */
public class SavePreferences {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SavePreferences(){
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<NewSubject> favorites)
    {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        //get our data from file for modifying
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        //allow edit
        editor = settings.edit();

        //initialize object
        Gson gson = new Gson();
        //serializes favourites
        String jsonFavorites = gson.toJson(favorites);
        //Set a String value in the preferences editor
        editor.putString(FAVORITES, jsonFavorites);

        //commit changes
        editor.commit();
    }

    //adds an item that has been favourited to memory
    public void addFavorite(Context context, NewSubject subject) {
        //get favourites from memory
        List<NewSubject> favorites = getFavorites(context);

        if (favorites == null)
            favorites = new ArrayList<NewSubject>(); //initialize new arraylist

        //add favourited item to list
        favorites.add(subject);
        //save favourited item to memory
        saveFavorites(context, favorites);
    }

    //removes an item that has been favourited from memory
    public void removeFavorite(Context context, NewSubject subject) {
        ArrayList<NewSubject> favorites = getFavorites(context); //get favourites from memory
        if (favorites != null) {
            for(int i = 0; i < favorites.size(); i++){
                if(favorites.get(i).getSubject_id() == subject.getSubject_id()) //check if the id of subject match
                    //remove the item from favourites
                   favorites.remove(i);
            }
            //save changes to favourites
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<NewSubject> getFavorites(Context context) {
        SharedPreferences settings;
        List<NewSubject> favorites = null;

        //Retrieve and hold the contents of the preferences
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) { //if settings contains string value

            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson(); //initialize class object
            //deserializes favourited items and puts into array
            NewSubject[] favoriteItems = gson.fromJson(jsonFavorites, NewSubject[].class);
            //convert array to list
            favorites = Arrays.asList(favoriteItems);
            //initialize list as ArrayList
            favorites = new ArrayList<NewSubject>(favorites);

        } else
            return null;

        return (ArrayList<NewSubject>) favorites;
    }
}

