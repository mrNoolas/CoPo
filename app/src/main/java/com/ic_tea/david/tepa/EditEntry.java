/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.tepa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EditEntry extends AppCompatActivity {
    Spinner spinner;
    EditText nameEditText, descriptionEditText, lessonLearnedEditText;
    int type;

    EntriesDBHelper dbHelper;
    Entry workingEntry;

    private final String TAG = EditEntry.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        spinner = (Spinner) findViewById(R.id.spinner);
        nameEditText = (EditText) findViewById(R.id.name);
        descriptionEditText = (EditText) findViewById(R.id.description);
        lessonLearnedEditText = (EditText) findViewById(R.id.lesson_learned);

        dbHelper = EntriesDBHelper.getInstance(this);
        int id = getIntent().getIntExtra(DisplayEntries.INTENT_ENTRY_ID_EXTRA, -1);
        type = getIntent().getIntExtra(DisplayEntries.INTENT_TYPE_EXTRA, 0);

        spinner.setSelection(type);
        if (id != -1) {
            workingEntry = dbHelper.getSingleEntry(id);
            nameEditText.setText(workingEntry.getName());
            descriptionEditText.setText(workingEntry.getDescription());
            lessonLearnedEditText.setText(workingEntry.getLessonLearned());
        }
    }

    public void save(View view) {
        String type, name, description, lessonLearned;
        String dateStr;

        type = (String) spinner.getSelectedItem();
        name = nameEditText.getText().toString();
        description = descriptionEditText.getText().toString();
        lessonLearned = lessonLearnedEditText.getText().toString();

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date();
        dateStr = dateFormat.format(date);
        if (workingEntry == null) {
            workingEntry = new Entry(-1, type, name, dateStr, description, lessonLearned);
        } else {
            // Reuse the id, so that we don't create a (edited) duplicate
            workingEntry = new Entry(workingEntry.getId(), type, name, dateStr, description, lessonLearned);
        }


        dbHelper.addOrUpdateEntry(workingEntry);
        finish();
    }

    public void cancel(View view) {
        finish();
    }
}
