/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.copo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.ic_tea.david.copo.objects.ActionLog;
import com.ic_tea.david.copo.objects.Competence;
import com.ic_tea.david.copo.objects.PortfolioItem;
import com.ic_tea.david.copo.objects.Project;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EditProject extends AppCompatActivity {
    EditText titleEditText, descriptionEditText;

    int type = 0;
    DBHelper dbHelper;
    Project workingProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        dbHelper = DBHelper.getInstance(this);
        int id = getIntent().getIntExtra(DisplayEntries.INTENT_ENTRY_ID_EXTRA, -1);

        titleEditText = (EditText) findViewById(R.id.aep_title);
        descriptionEditText = (EditText) findViewById(R.id.aep_description);

        if (id > 0) {
            // Load the existing entry into the form for the user to see and edit
            workingProject = dbHelper.getProject(id);

            titleEditText.setText(workingProject.title);
            descriptionEditText.setText(workingProject.descr);
        }
    }

    public void save(View view) {
        String title, description;

        title = titleEditText.getText().toString();
        description = descriptionEditText.getText().toString();

        if (workingProject == null) {
            workingProject = new Project(title, description);
        } else {
            // Reuse the id, so that we don't create a (edited) duplicate
            workingProject = new Project(workingProject.id, title, description);
        }

        dbHelper.addOrUpdateProject(workingProject);
        setResult(RESULT_OK);
        finish();

    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}
