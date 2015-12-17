package com.example.callum.md_coursework_v1;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DisplayListActivity extends AppCompatActivity {

    List<String> stringArray, titleArray, linkArray, pubDateArray;
    String title = null;

    private static LayoutInflater inflater=null;

    TextView newsFeedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);



        //get other activity data
        Intent intent = getIntent();
        //retrieve selection data from intent
        String getItemSelected = intent.getStringExtra("ItemSelected");

        newsFeedText = (TextView)findViewById(R.id.NewsFeedText);
        newsFeedText.setText("News Feed " + getItemSelected );


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
            titleArray.add(stringArray.get(i));
        }

        for(int i = 3; i < stringArray.size(); i+= 5) {
            pubDateArray.add(stringArray.get(i));
        }



        List<Bitmap> bmList = new ArrayList<>();

        for(int i = 4; i < stringArray.size(); i+= 5) {
            //Use LinkArray Urls to retrieve image data
            ImageLoaderURL imageLoader = new ImageLoaderURL(stringArray.get(i));

            try {
                bmList.add(imageLoader.execute().get());
            }catch(Exception e){}
        }

        int incrementer = 0;
        while(incrementer < titleArray.size() && incrementer < pubDateArray.size()){

            HashMap<String, String> map = new HashMap<String, String>();
            HashMap<String, Bitmap> bitMap = new HashMap<>();
            map.put("subjecttitle", titleArray.get(incrementer));
            map.put("pubdate", pubDateArray.get(incrementer));

            songsList.add(map);

            incrementer++;
        }



        final ListView topicsListView = (ListView) findViewById(R.id.topicsListView);

        LazyAdapter adapter;
        // Getting adapter by passing xml data ArrayList
        adapter=new LazyAdapter(this, songsList);
        adapter.setMap(bmList);
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

    public String blahhhh() {

        String textHolder = "";

        for(int i = 0; i < stringArray.size(); i+= 5) {
            textHolder = stringArray.get(i);
            return textHolder;
        }
        return textHolder;
    }
}
