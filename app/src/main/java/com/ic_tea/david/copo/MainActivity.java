/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.copo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String[] competences;
    ListView listView;
    TextView textView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = DBHelper.getInstance(this);

        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.am_points_counter);
        textView.setText(Integer.toString(dbHelper.getPoints(3)));

        /*listView = (ListView) findViewById(R.id.am_main_list_view);
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
        });*/
    }

    public void shareAllClick(View view) {
        DBHelper dbHelper = DBHelper.getInstance(this);
        /*ArrayList<Entry> sharableEntries = dbHelper.getMultipleEntries(0);

        // No sharable entries found
        if (!Entry.share(this, sharableEntries)) {
            String noEntriesMessage = getString(R.string.no_sharable_entries_message);
            Toast toast = Toast.makeText(this, noEntriesMessage, Toast.LENGTH_SHORT);
            toast.show();
        }*/
    }

    public void startCategory(View view) {
        Intent intent;
        int id = view.getId();

        Log.i("main", Integer.toString(dbHelper.getMultipleProjects(0).size()) + " hi");

        if (id == R.id.am_projects) {
            intent = new Intent(this, DisplayProjects.class);
            startActivity(intent);
        } else if (dbHelper.getMultipleProjects(0).size() == 0) {
            AlertDialog a = new AlertDialog.Builder(this)
                    .setTitle("Geen projecten")
                    .setMessage("Er zijn nog geen projecten aangemaakt...\nZonder projecten kun je" +
                            " niet beginnen aan je logboek of portfolio. Begin dus met het aanmaken" +
                            " van een project.")
                    .setPositiveButton("Geen probleem!", null)
                    .setNegativeButton("He, wat vervelend :(", null)
                    .create();
            a.show();

        } else {
            if (id == R.id.am_logs) {
                intent = new Intent(this, DisplayEntries.class);
                intent.putExtra(DisplayEntries.INTENT_TYPE_EXTRA, 0);
                startActivity(intent);
            } else if (id == R.id.am_portfolio) {
                intent = new Intent(this, DisplayEntries.class);
                intent.putExtra(DisplayEntries.INTENT_TYPE_EXTRA, 1);
                startActivity(intent);
            }
        }

    }
}
