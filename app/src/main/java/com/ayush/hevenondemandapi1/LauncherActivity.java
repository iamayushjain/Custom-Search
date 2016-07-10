package com.ayush.hevenondemandapi1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by user on 6/30/2016.
 */
public class LauncherActivity extends Activity {
    ListView listView;
    String[] models = {
            "Url To Text"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(LauncherActivity.this, android.R.layout.simple_list_item_1, models);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent i = new Intent(LauncherActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
    }
}
