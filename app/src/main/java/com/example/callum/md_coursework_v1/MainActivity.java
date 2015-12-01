package com.example.callum.md_coursework_v1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView subjectListView = (ListView) findViewById(R.id.subjectListView);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.RSS_Subjects, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        subjectListView.setAdapter(adapter);

        //Setup click event
        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //Get item selected from list
                String selectedFromList =(String) (subjectListView.getItemAtPosition(position));


                // Launching new Activity on selecting single List Item
                Intent intent = new Intent(getApplicationContext(), DisplayListActivity.class);
                // sending data to new activity
                intent.putExtra("ItemSelected", selectedFromList);
                startActivity(intent);
            }
        });

    }
}
