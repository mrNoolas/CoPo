/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.copo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ic_tea.david.copo.objects.ActionLog;
import com.ic_tea.david.copo.objects.PortfolioItem;

import java.util.ArrayList;

public class DisplayEntries extends AppCompatActivity {
    public static final String INTENT_TYPE_EXTRA = "com.ic_tea.david.copo.TYPEFOREDIT";
    public static final String INTENT_ENTRY_ID_EXTRA = "com.ic_tea.david.copo.ENTRY";
    public static final int INTENT_REQUEST_EDIT = 1;
    public static final String INTENT_PROJECT_ID_EXTRA = "com.ic_tea.david.copo.PROJECTID";
    private final String TAG = DisplayEntries.class.getSimpleName();

    ListView listView;
    TextView instructions;
    ArrayList<ActionLog> entries = new ArrayList<>();
    ArrayList<PortfolioItem> portfolioItems = new ArrayList<>();
    DBHelper dbHelper;
    String typeStr;
    private int type, projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DBHelper.getInstance(this); // this may be laggy

        projectId = getIntent().getIntExtra(INTENT_PROJECT_ID_EXTRA, 0);
        type = getIntent().getIntExtra(INTENT_TYPE_EXTRA, 0);

        if (projectId > 0) {
            if (type == 0) {
                setTitle(getString(R.string.log) + " - " + dbHelper.getProject(projectId).title);
            } else if (type == 1) {
                setTitle(getString(R.string.portfolio) + " - " +  dbHelper.getProject(projectId).title);
            }
        } else {
            if (type == 0) {
                setTitle(getString(R.string.log));
            } else if (type == 1) {
                setTitle(getString(R.string.portfolio));
            }
        }

        setContentView(R.layout.activity_display_entries);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        instructions = (TextView) findViewById(R.id.instructions);
        refreshListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startEditEntryActivity();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void refreshListView() {
        switch (type) {
            case 0: // Logs
                entries = dbHelper.getMultipleLogs(projectId);
                if (entries.size() > 0) {
                    instructions.setVisibility(View.GONE);
                } else {
                    instructions.setVisibility(View.VISIBLE);
                }
                setupListView();
                break;

            case 1: // Portfolio
                portfolioItems = dbHelper.getMultiplePortfolioEntries(projectId);
                entries = new ArrayList<>(); // reset in case of earlier use. This is for following conditional statement

                if (portfolioItems.size() > 0) {
                    instructions.setVisibility(View.GONE);
                    for (PortfolioItem portfolioItem : portfolioItems) {
                        entries.add(portfolioItem); // this convert eliminates unnecessary method duplicates to handle both portfolioitems and actionlogs
                    }
                } else {
                    instructions.setVisibility(View.VISIBLE);
                }

                setupListView();
                break;

            default: // Show empty listview and instructions
                setupListView();
                instructions.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_REQUEST_EDIT && resultCode != RESULT_CANCELED) {
            refreshListView();
            String message = getString(R.string.confirm_save_toast);
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void setupListView () {
        listView = (ListView) findViewById(R.id.entries_list_view);
        final EntryAdapter entryAdapter = new EntryAdapter(this, entries);
        listView.setAdapter(entryAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        // Capture listview item click
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                mode.setTitle(listView.getCheckedItemCount() + " " +
                        getString(R.string.selected_shareselect));
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

                                // remove from db
                                if (type == 0) {
                                    dbHelper.deleteLog(entries.get(key));
                                } else if (type == 1) {
                                    dbHelper.deletePortfolioItem(portfolioItems.get(key));
                                    portfolioItems.remove(key);
                                }
                                // remove from adapter
                                ActionLog selectedItem = entryAdapter.getItem(key);
                                entryAdapter.remove(selectedItem);
                                // entries.remove(key);
                            }
                        }

                        // Close CAB (Contextual action bar)
                        mode.finish();
                        return true;

                    case R.id.share_option:
                        // Calls getSelectedIds method from EntryListViewAdapter Class
                        selected = entryAdapter.getSelectedIds();

                        if (type == 1) { // portfolio items
                            // isolate the selected entries
                            ArrayList<PortfolioItem> sharableEntries = new ArrayList<>();
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    sharableEntries.add(portfolioItems.get(selected.keyAt(i)));
                                }
                            }
                            startShareIntent(sharableEntries);
                        } else {
                            // isolate the selected entries
                            ArrayList<ActionLog> sharableEntries = new ArrayList<>();
                            for (int i = (selected.size() - 1); i >= 0; i--) {
                                if (selected.valueAt(i)) {
                                    sharableEntries.add(entries.get(selected.keyAt(i)));
                                }
                            }
                            startShareIntent(sharableEntries, true);
                        }

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
                startEditEntryActivity(entries.get(position).id);
            }
        });
    }

    private void startShareIntent(ArrayList<ActionLog> sharableEntries, Boolean isLog) {
        ShareMessage message = new ShareMessage(this, dbHelper);
        for (ActionLog log : sharableEntries) {
            message.addLog(log);
        }
        if(!message.send())message.showNoContentDialog();
    }
    private void startShareIntent(ArrayList<PortfolioItem> sharableEntries) {
        ShareMessage message = new ShareMessage(this, dbHelper);
        for (PortfolioItem p : sharableEntries) {
            message.addPortfolioItem(p);
        }
        if(!message.send())message.showNoContentDialog();
    }

    private void startEditEntryActivity() { startEditEntryActivity(-1); }
    private void startEditEntryActivity(int id) {
        Intent intent = new Intent(this, EditEntry.class);
        intent.putExtra(INTENT_TYPE_EXTRA, type);
        intent.putExtra(INTENT_ENTRY_ID_EXTRA, id);
        intent.putExtra(INTENT_PROJECT_ID_EXTRA, projectId);
        startActivityForResult(intent, INTENT_REQUEST_EDIT);
    }
}
