/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.tepa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
                new ArrayAdapter<String>(this, R.layout.category_item, competences);

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

        if (sharableEntries.size() > 0) {
            ArrayList<String> headers = DisplayEntries.getInfo(sharableEntries);

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
        } else {
            String noEntriesMessage = "Er zijn geen deelbare verslagen gevonden";
            Toast toast = Toast.makeText(this, noEntriesMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
