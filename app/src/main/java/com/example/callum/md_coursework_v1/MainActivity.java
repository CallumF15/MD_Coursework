package com.example.callum.md_coursework_v1;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    Activity activity;
    FragmentManager fragmentManager;
    List<Boolean> favouriteList;
    SavePreferences savePreference;
    MainActivityAdapter mainActivityAdapter;
    List<NewSubject> listofSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        savePreference = new SavePreferences();

        QueryDatabase queryDatabase = new QueryDatabase(this, "NewsSubjects.s3db", null, 1);


        final ListView subjectListView = (ListView) findViewById(R.id.subjectListView);

        fragmentManager = this.getSupportFragmentManager();

        listofSubjects = queryDatabase.getAllNewSubjects();
        final MainActivityAdapter productListAdapter = new MainActivityAdapter(this, listofSubjects);
        subjectListView.setAdapter(productListAdapter);


        //Setup click event
        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.beep);
                mp.start();

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                        mp.release();
                    }
                });

                //Get item selected from list
                NewSubject getSubject = (NewSubject)subjectListView.getItemAtPosition(position);
                String selectedFromList = (String)getSubject.getSubject_name();
                // Launching new Activity on selecting single List Item
                Intent intent = new Intent(getApplicationContext(), DisplayListActivity.class);
                // sending data to new activity
                intent.putExtra("ItemSelected", selectedFromList);
                startActivity(intent);
            }
        });

        subjectListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int pos, long arg3) {

                ImageView button = (ImageView) view.findViewById(R.id.img_favourite);

                //Determine if listview item selected is favourited, if not, favourite, if is, un-favourite.
                String tag = button.getTag().toString();
                if (tag.equalsIgnoreCase("grey")) {
                    savePreference.addFavorite(activity, listofSubjects.get(pos));
                    Toast.makeText(activity, "favourite added", Toast.LENGTH_SHORT).show();

                    button.setTag("red");
                    button.setImageResource(R.drawable.ic_action_favorite);
                } else {
                    savePreference.removeFavorite(activity, listofSubjects.get(pos));
                    button.setTag("grey");
                    button.setImageResource(R.drawable.ic_action_favorite_light);
                    //productListAdapter.remove(listofSubjects.get(pos));
                    Toast.makeText(activity, "favourite removed", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.map:
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                //Start new activity
                startActivity(intent);
                return true;
            case R.id.about:
                //create instance of dialog fragment
                DialogFragment dialogFragment = new MainAboutDialogue();
                //display text box for about
                dialogFragment.show(fragmentManager, "");
                return true;
            case R.id.quit:
                //exit/end application
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
