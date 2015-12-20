package com.example.callum.md_coursework_v1;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayListActivity extends AppCompatActivity {

    //variables
    FragmentManager fragmentManager;
    List<String> stringArray, titleArray, linkArray, pubDateArray;
    String title = null;

    private static LayoutInflater inflater=null;
    TextView newsFeedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        //get the FragmentManager for interacting with fragments associated with this activity.
        fragmentManager = this.getSupportFragmentManager();

        //get other activity data
        Intent intent = getIntent();
        //retrieve selection data from intent
        String getItemSelected = intent.getStringExtra("ItemSelected");

        newsFeedText = (TextView)findViewById(R.id.NewsFeedText); //find our view object and assign
        newsFeedText.setText("News Feed " + getItemSelected ); //set text of view object


        //Check Item Selected & Assign appropriate URL
        ParserRSS parserRSS = new ParserRSS(AssignAppropriateURL(getItemSelected));
        //set number of data to parse/display
        parserRSS.setLimit(10);

        //initialize arrays
        stringArray = new ArrayList<String>();
        titleArray = new ArrayList<String>();
        pubDateArray = new ArrayList<String>();
        linkArray = new ArrayList<String>();

        //get array data
        try {
            stringArray = parserRSS.execute().get();
        }catch(Exception e){}

        ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

        for(int i = 0; i < stringArray.size(); i+= 5){
            titleArray.add(stringArray.get(i)); //find our titles in array and add to new array
        }

        for(int i = 3; i < stringArray.size(); i+= 5) {
            pubDateArray.add(stringArray.get(i)); //find our publication dates in array and add to new array
        }

        List<Bitmap> bmList = new ArrayList<>();

        for(int i = 4; i < stringArray.size(); i+= 5) {
            //Use LinkArray Urls to retrieve image data
            ImageLoaderURL imageLoader = new ImageLoaderURL(stringArray.get(i)); //find our bitmap urls and pass to constructor

            try {
                bmList.add(imageLoader.execute().get()); //add bitmaps to list
            }catch(Exception e){}
        }

        int incrementer = 0;
        while(incrementer < titleArray.size() && incrementer < pubDateArray.size()){ //make sure incrementer doesn't exceed array size

            HashMap<String, String> map = new HashMap<String, String>();
            HashMap<String, Bitmap> bitMap = new HashMap<>();
            map.put("subjecttitle", titleArray.get(incrementer)); //add list object to hashmap with key name
            map.put("pubdate", pubDateArray.get(incrementer));  //add list object to hashmap with key name

            songsList.add(map);  //add map object to arraylist hashmap

            incrementer++;
        }



        final ListView topicsListView = (ListView) findViewById(R.id.topicsListView); //find our view object and assign

        LazyAdapter adapter;
        // Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, songsList);
        //set the bitmap list
        adapter.setMap(bmList);
        //set the adapter
        topicsListView.setAdapter(adapter);

        //Setup click event
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //take to new layout using title, image
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

    //Item Selected from ListView is checked and the appropriate url is assigned
    //Retrieve the information
    public String AssignAppropriateURL(String selectedItem) {

        String selection = "";
        String urlSite = "";
        boolean isTrue = false;

        switch (selectedItem) {

            case "Latest Stories":
                selection = "articles.rss";
                isTrue = true;
                break;
            case "U.S.":
                selection = "ushome";
                break;
            case "Australia":
                selection = "auhome";
                break;
            case "TV and Showbiz":
                selection = "tvshowbiz";
                break;
            case "Science":
                selection = "sciencetech";
                break;
            case "Video":
                selection = "video/videos.rss";
                isTrue = true;
                break;
            default:
                selection = selectedItem;
                break;
        }

        if (isTrue) {
            urlSite = "http://www.dailymail.co.uk/" + selection;
        } else {
            urlSite = "http://www.dailymail.co.uk/" + selection + "/index.rss";
        }

        return urlSite;
    }
}
