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
    public void saveFavorites(Context context, List<NewSubject> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, NewSubject subject) {
        List<NewSubject> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<NewSubject>();
        favorites.add(subject);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, NewSubject subject) {
        ArrayList<NewSubject> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(subject);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<NewSubject> getFavorites(Context context) {
        SharedPreferences settings;
        List<NewSubject> favorites = null;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String favorite = settings.getString(FAVORITES, null);

            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            NewSubject[] favoriteItems = gson.fromJson(jsonFavorites, NewSubject[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<NewSubject>(favorites);

        } else
            return null;

        return (ArrayList<NewSubject>) favorites;
    }
}

