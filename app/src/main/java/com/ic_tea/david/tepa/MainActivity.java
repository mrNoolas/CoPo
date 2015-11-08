/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.tepa;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    String[] competences;
    ListView listView;
    public static final String INTENT_TYPE_EXTRA = "com.ic_tea.david.tepa.TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.main_list_view);
        // Set up categories in listView:
        competences = getResources().getStringArray(R.array.competences);
        ArrayAdapter<String> competenceAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, competences);

        listView.setAdapter(competenceAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startCategory(position);
            }
        });
    }



    protected void startCategory(int pos) {
        Intent intent = new Intent(this, DisplayEntries.class);
        intent.putExtra(INTENT_TYPE_EXTRA, pos);
        startActivity(intent);
    }
}
