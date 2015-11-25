/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.tepa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class EditEntry extends AppCompatActivity {
    private final String TAG = EditEntry.class.getSimpleName();
    Spinner spinner;
    EditText nameEditText, descriptionEditText, lessonLearnedEditText;
    int type = 0;
    EntriesDBHelper dbHelper;
    Entry workingEntry;

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

        if (id != -1) {
            workingEntry = dbHelper.getSingleEntry(id);
            ArrayList<String> typeArray = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.competences)));
            type = typeArray.indexOf(workingEntry.getType());
            nameEditText.setText(workingEntry.getName());
            descriptionEditText.setText(workingEntry.getDescription());
            lessonLearnedEditText.setText(workingEntry.getLessonLearned());
        }
        spinner.setSelection(type);
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
        setResult(RESULT_OK);
        finish();
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
