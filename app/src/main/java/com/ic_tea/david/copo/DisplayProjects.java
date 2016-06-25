/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.copo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ic_tea.david.copo.objects.ActionLog;
import com.ic_tea.david.copo.objects.Goal;
import com.ic_tea.david.copo.objects.PortfolioItem;
import com.ic_tea.david.copo.objects.Project;

import java.util.ArrayList;

public class DisplayProjects extends AppCompatActivity {
    public static final String INTENT_ENTRY_ID_EXTRA = "com.ic_tea.david.copo.ENTRY";
    public static final int INTENT_REQUEST_EDIT = 1;
    private final String TAG = DisplayProjects.class.getSimpleName();

    ExpandableListView listView;
    TextView instructions;
    ArrayList<Project> projects = new ArrayList<>();
    DBHelper dbHelper;
    String typeStr;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_projects);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        listView = (ExpandableListView) findViewById(R.id.cdp_projects_list_view);
        dbHelper = DBHelper.getInstance(this); // this may be laggy
        instructions = (TextView) findViewById(R.id.cdp_instructions);
        refreshListView();
    }

    private void refreshListView() {
        projects = dbHelper.getMultipleProjects(0);
        if (projects.size() > 0) {
            instructions.setVisibility(View.GONE);
        } else {
            instructions.setVisibility(View.VISIBLE);
        }
        final ProjectAdapter projectAdapter = new ProjectAdapter(this, projects);
        listView.setAdapter(projectAdapter);
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

    public void startEditProjectActivity(View view) { startEditProjectActivity(-1); }
    private void startEditProjectActivity(int id) {
        Intent intent = new Intent(this, EditProject.class);
        intent.putExtra(INTENT_ENTRY_ID_EXTRA, id);
        startActivityForResult(intent, INTENT_REQUEST_EDIT);
    }

    public void buttonClick(final View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.dpc_portfolio_button:
                intent = new Intent(this, DisplayEntries.class);
                intent.putExtra(DisplayEntries.INTENT_TYPE_EXTRA, 1); // Portfolio
                intent.putExtra(DisplayEntries.INTENT_PROJECT_ID_EXTRA, (int) view.getTag());
                startActivity(intent);
                break;
            case R.id.dpc_log_button:
                intent = new Intent(this, DisplayEntries.class);
                intent.putExtra(DisplayEntries.INTENT_TYPE_EXTRA, 0); // Log
                intent.putExtra(DisplayEntries.INTENT_PROJECT_ID_EXTRA, (int) view.getTag());
                startActivity(intent);
                break;
            case R.id.dpc_edit_button:
                Log.i("foo", Integer.toString((int) view.getTag()));
                startEditProjectActivity((int) view.getTag());
                break;
            case R.id.dpc_share_button:
                ShareMessage message = new ShareMessage(this, dbHelper);
                message.addProject(projects.get((int) view.getTag()));
                if(!message.send()) message.showNoContentDialog();
                break;
            case R.id.dpc_delete_button:
                new AlertDialog.Builder(this)
                        .setTitle("Project verwijderen?")
                        .setMessage("Als je een project verwijdert, worden ook alle daar bij " +
                                "behorende portfolio en logboek verslagen verwijdert en dat is voor" +
                                " altijd onomkeerbaar... (net als in Minecraft: forever is a " +
                                "very, very long time)")
                        .setPositiveButton("Verwijderen!", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int projectId = (int) view.getTag();

                                // first remove logs:
                                ArrayList<ActionLog> logs = dbHelper.getMultipleLogs(projectId);
                                for (ActionLog log : logs) {
                                    dbHelper.deleteLog(log);
                                }

                                // then remove portfolio:
                                ArrayList<PortfolioItem> portfolioItems =
                                        dbHelper.getMultiplePortfolioEntries(projectId);
                                for (PortfolioItem portfolioItem : portfolioItems) {
                                    dbHelper.deletePortfolioItem(portfolioItem);
                                }

                                // then remove goals:
                                ArrayList<Goal> goals = dbHelper.getMultipleGoals(projectId);
                                for (Goal goal : goals) {
                                    dbHelper.deleteGoal(goal);
                                }

                                // remove from db
                                dbHelper.deleteProject(projectId);
                                recreate();
                            }
                        })
                        .setNegativeButton("Nee, toch niet... Annuleren", null)
                        .show();
                break;
        }
    }

    public void shareAllProjects(View view) {
        ShareMessage message = new ShareMessage(this, dbHelper);
        for (Project p : projects) {
            message.addProject(p);
        }
        if(!message.send()) message.showNoContentDialog();
    }
}
