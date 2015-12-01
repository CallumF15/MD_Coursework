package com.example.callum.md_coursework_v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class DisplayListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_list);

        //get other activity data
        Intent intent = getIntent();
        //retrieve selection data from intent
        String getItemSelected = intent.getStringExtra("ItemSelected");

        //Check Item Selected & Assign appropriate URL
        new ParserRSS(AssignAppropriateURL(getItemSelected)).execute();

        //Get XML data and put into ArrayAdapter


        final ListView topicsListView = (ListView) findViewById(R.id.topicsListView);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.RSS_Subjects, android.R.layout.simple_spinner_item);
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
                selection = "video" + "/video.rss";
                break;

            default:
                break;

        }

        if(isTrue) {
            urlSite = "http://www.dailymail.co.uk/" + selection;
        }else{
            urlSite = "http://www.dailymail.co.uk/" + selection + "/index.rss";
        }

        return urlSite;
    }
}
