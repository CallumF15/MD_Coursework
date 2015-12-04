package com.example.callum.md_coursework_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class DisplayListActivity extends AppCompatActivity {

    List<String> stringArray, titleArray, descArray, linkArray;
    String title = null;

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
        parserRSS.setLimit(20);

        //initialize arrays
        stringArray = new ArrayList<String>();
        titleArray = new ArrayList<String>();
        descArray = new ArrayList<String>();
        linkArray = new ArrayList<String>();

        //get array data
        try {
            stringArray = parserRSS.execute().get();
        }catch(Exception e){}

        //Separate the XML data to only get Title text
        for(int i = 0; i < stringArray.size(); i+= 3){
            title = stringArray.get(i);
            titleArray.add(title);
        }

        final ListView topicsListView = (ListView) findViewById(R.id.topicsListView);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, titleArray);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        topicsListView.setAdapter(adapter);

        //Setup click event
        topicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


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
}
