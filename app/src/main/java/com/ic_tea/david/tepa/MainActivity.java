/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.tepa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String INTENT_TYPE_EXTRA = "com.ic_tea.david.tepa.TYPE";
    String[] competences;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.main_list_view);
        // Set up categories in listView:
        competences = getResources().getStringArray(R.array.competences);
        ArrayAdapter<String> competenceAdapter =
                new ArrayAdapter<>(this, R.layout.category_item, competences);

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

    public void shareAll(View view) {
        EntriesDBHelper dbHelper = EntriesDBHelper.getInstance(this);
        ArrayList<Entry> sharableEntries = dbHelper.getMultipleEntries("Alles");

        // No sharable entries found
        if (!Entry.share(this, sharableEntries)) {
            String noEntriesMessage = "Er zijn geen deelbare verslagen gevonden";
            Toast toast = Toast.makeText(this, noEntriesMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
