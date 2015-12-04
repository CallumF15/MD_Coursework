package com.example.callum.md_coursework_v1;


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
import android.widget.ListView;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<String> listNewSubject;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QueryDatabase queryDatabase = new QueryDatabase(this, "NewsSubjects.s3db", null, 1);

        final ListView subjectListView = (ListView) findViewById(R.id.subjectListView);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, queryDatabase.getAllSubjects());


        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        subjectListView.setAdapter(adapter);

        fragmentManager = this.getSupportFragmentManager();

        //Setup click event
        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //Get item selected from list
                String selectedFromList = (String)(subjectListView.getItemAtPosition(position));
                // Launching new Activity on selecting single List Item
                Intent intent = new Intent(getApplicationContext(), DisplayListActivity.class);
                // sending data to new activity
                intent.putExtra("ItemSelected", selectedFromList);
                startActivity(intent);
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
                startActivity(intent);
                return true;
            case R.id.about:
                DialogFragment dialogFragment = new MainAboutDialogue();
                dialogFragment.show(fragmentManager, "");
                return true;
            case R.id.quit:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
