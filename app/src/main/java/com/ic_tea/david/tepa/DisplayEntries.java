/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.tepa;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DisplayEntries extends AppCompatActivity {
    private int type;
    ArrayList<Entry> entries;
    ListView listView;
    EntriesDBHelper dbHelper;
    String typeStr;
    private final String TAG = DisplayEntries.class.getSimpleName();

    public static final String INTENT_TYPE_EXTRA = "com.ic_tea.david.tepa.TYPEFOREDIT";
    public static final String INTENT_ENTRY_ID_EXTRA = "com.ic_tea.david.tepa.ENTRY";
    public static final int INTENT_REQUEST_EDIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getIntExtra(MainActivity.INTENT_TYPE_EXTRA, 0);
        typeStr = getResources().getStringArray(R.array.competences)[type];
        setTitle(typeStr);

        setContentView(R.layout.activity_display_entries);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        dbHelper = EntriesDBHelper.getInstance(this); // this may be laggy
        refreshListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddEntryActivity();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void refreshListView() {
        entries = dbHelper.getMultipleEntries(typeStr);Log.i(TAG, "here");
        setupListView(getInfo(entries));
    }

    private void startAddEntryActivity() {
        startAddEntryActivity(-1);
    }

    private void startAddEntryActivity(int id) {
        Intent intent = new Intent(this, EditEntry.class);
        intent.putExtra(INTENT_TYPE_EXTRA, type);
        intent.putExtra(INTENT_ENTRY_ID_EXTRA, id);
        startActivityForResult(intent, INTENT_REQUEST_EDIT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_REQUEST_EDIT) {
            refreshListView();
        }
    }

    private void setupListView (ArrayList<String> info) {
        listView = (ListView) findViewById(R.id.entriesListView);
        final EntryAdapter entryAdapter =
                new EntryAdapter(this, R.layout.entry_item, info);

        listView.setAdapter(entryAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        // Capture listview item click
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                mode.setTitle(listView.getCheckedItemCount() + " Selected");
                entryAdapter.toggleSelection(position);
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_display_entries, menu);
                getSupportActionBar().hide();
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                SparseBooleanArray selected;
                switch (item.getItemId()) {
                    case R.id.delete_option:
                        // Calls getSelectedIds method from EntryListViewAdapter Class
                        selected = entryAdapter.getSelectedIds();

                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                int key = selected.keyAt(i);

                                // remove from adapter
                                String selectedItem = entryAdapter.getItem(key);
                                entryAdapter.remove(selectedItem);

                                // remove from db
                                dbHelper.deleteEntry(entries.get(key));
                                entries.remove(key);
                            }
                        }

                        // Close CAB (Contextual action bar)
                        mode.finish();
                        return true;

                    case R.id.share_option:
                        // Calls getSelectedIds method from EntryListViewAdapter Class
                        selected = entryAdapter.getSelectedIds();

                        // isolate the selected entries
                        ArrayList<Entry> sharableEntries = new ArrayList<Entry>();
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                sharableEntries.add(entries.get(selected.keyAt(i)));
                            }
                        }
                        startShareIntent(sharableEntries);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                entryAdapter.removeSelection();
                getSupportActionBar().show();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startAddEntryActivity(entries.get(position).getId());
            }
        });
    }

    private void startShareIntent(ArrayList<Entry> sharableEntries) {
        if (sharableEntries.size() > 0) {
            ArrayList<String> headers = getInfo(sharableEntries);

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
            Date date = new Date();
            String dateStr = dateFormat.format(date);

            String body = "Dit is een automatisch gegenereert bericht door de TePa " +
                    "(Technasium Portfolio) app. Dit bericht bevat een selectie van portfolio-items," +
                    "die zijn gedeeld via e-mail. Deze selectie is gegenereert op: " + dateStr;
            for (int i = 0; i < sharableEntries.size(); i++) {
                Entry entry = sharableEntries.get(i);
                body = body + "\n\n==================================================\n\n" +
                        headers.get(i) + "\n\nOmschrijving van de situatie:\n\"" +
                        entry.getDescription() + "\"\n\nWat heb ik hiervan geleerd:\n\"" +
                        entry.getLessonLearned() + "\"";
            }
            body = body + "\n\n==================================================\n\n" +
                    "Wanneer u vragen en/of opmerkingen heeft over dit bericht kunt u mailen" +
                    " naar: noolasproductions@gmail.com. \n" +
                    "De gebruiker die deze e-mail verstuurd heeft, is zelf verantwoordelijk voor de" +
                    "inhoud van deze e-mail. De auteur van de app kan dus niet aansprakelijk worden" +
                    "gesteld voor ongepaste (of andere gelijksoortige) inhoud.";

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, body);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    }

    private ArrayList<String> getInfo(ArrayList<Entry> entries) {
        ArrayList<String> info = new ArrayList<String>();

        for (Entry entry : entries) {
            String name = entry.getName();

            // Format date from 'YYYYMMDDHHMM' to 'DD-MM-YYYY HH:MM'
            String date = entry.getDateOfLastEdit(); // YYYYMMDDHHMM
            date = date.substring(6, 8) + "-" + date.substring(4, 6) + "-" + date.substring(0, 4) +
                    " " + date.substring(8, 10) + ":" + date.substring(10);

            info.add(entry.getId() + " " + name + "  |  " + date);
        }
        return info;
    }

}
