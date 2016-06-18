package com.ic_tea.david.copo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ProjectsActivity extends AppCompatActivity {
    public static final String INTENT_TYPE_EXTRA = "com.ic_tea.david.copo.TYPE";
    String[] projects;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        occupyListView();
    }

    private void occupyListView() {
        //listView = (ListView) findViewById(R.id.am_main_list_view);
        // Set up categories in listView:
        projects = getResources().getStringArray(R.array.competences);
        ArrayAdapter<String> competenceAdapter =
                new ArrayAdapter<>(this, R.layout.category_item, projects);

        //listView.setAdapter(competenceAdapter);
        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        startCategory(position);
       //     }
        //});
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

    protected void startCategory(int pos) {
        Intent intent = new Intent(this, DisplayEntries.class);
        intent.putExtra(INTENT_TYPE_EXTRA, pos);
        startActivity(intent);
    }

}
