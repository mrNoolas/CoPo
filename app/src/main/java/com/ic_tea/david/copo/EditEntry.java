/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.copo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class EditEntry extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    private final String TAG = EditEntry.class.getSimpleName();

    EditText titleEditText, hoursSpentEditText, descriptionEditText, achievementsEditText, // logs
            lessonLearnedEditText, nextTimeEditText; // Portfolio
    Spinner projectSpinner, competenceSpinner;

    ArrayList<Project> projects;

    String dateOfAction;
    int type = 0;
    int projectId = 0;
    DBHelper dbHelper;
    ActionLog workingLog;
    PortfolioItem workingPortfolioItem;
    Button dateSetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);

        dbHelper = DBHelper.getInstance(this);
        int id = getIntent().getIntExtra(DisplayEntries.INTENT_ENTRY_ID_EXTRA, -1);
        type = getIntent().getIntExtra(DisplayEntries.INTENT_TYPE_EXTRA, -1);
        projectId = getIntent().getIntExtra(DisplayEntries.INTENT_PROJECT_ID_EXTRA, -1);

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

        // Log and portfolio
        projectSpinner = (Spinner) findViewById(R.id.aee_project_spinner);
        competenceSpinner = (Spinner) findViewById(R.id.aee_competence_spinner);

        titleEditText = (EditText) findViewById(R.id.aee_title);
        dateSetButton= (Button) findViewById(R.id.aee_date_button);
        hoursSpentEditText = (EditText) findViewById(R.id.aee_hours_spent_edit);
        descriptionEditText = (EditText) findViewById(R.id.aee_description);
        achievementsEditText = (EditText) findViewById(R.id.aee_achievements);

        //portfolio
        lessonLearnedEditText = (EditText) findViewById(R.id.aee_lesson_learned);
        nextTimeEditText = (EditText) findViewById(R.id.aee_next_time);

        // prepare all interfaces
        projects = dbHelper.getMultipleProjects(0);

        prepareProjectSpinner();
        prepareDateSetButton();

        if (type == 0) { // Log
            // hide the things that are not used for a log
            findViewById(R.id.aee_competence_spinner_layout).setVisibility(View.GONE);
            competenceSpinner.setVisibility(View.GONE);
            lessonLearnedEditText.setVisibility(View.GONE);
            nextTimeEditText.setVisibility(View.GONE);

            if (id > 0) {
                // Load the existing entry into the form for the user to see and edit
                workingLog = dbHelper.getSingleLog(id);

                for (int i = 0; i < projects.size(); i++) {
                    if (projects.get(i).id == workingLog.projectId) {
                        projectSpinner.setSelection(i);
                    }
                }
                titleEditText.setText(workingLog.title);
                // Format date from 'DDMMYYYY' to 'DD-MM-YYYY'
                String dateText =
                        getString(R.string.date) + " " + workingLog.date.substring(0, 2) + "-" +
                                workingLog.date.substring(2, 4) + "-" + workingLog.date.substring(4);
                dateSetButton.setText(dateText);
                hoursSpentEditText.setText(Double.toString(workingLog.hoursSpent));
                descriptionEditText.setText(workingLog.descr);
                achievementsEditText.setText(workingLog.achievements);
            }
        } else if (type == 1) { // PortfolioItem
            prepareCompetenceSpinner();

            // Load the existing entry into the form for the user to see and edit
            if (id > 0){
                workingPortfolioItem = dbHelper.getSinglePortfolioEntry(id);

                for (int i = 0; i < projects.size(); i++) {
                    if (projects.get(i).id == workingPortfolioItem.projectId) {
                        projectSpinner.setSelection(i);
                    }
                }
                titleEditText.setText(workingPortfolioItem.title);
                // Format date from 'DDMMYYYY' to 'DD-MM-YYYY'
                String dateText = getString(R.string.date) + " " + workingPortfolioItem.date.substring(0, 2)
                        + "-" + workingPortfolioItem.date.substring(2, 4) + "-" +
                        workingPortfolioItem.date.substring(4);
                dateSetButton.setText(dateText);

                hoursSpentEditText.setText(Double.toString(workingPortfolioItem.hoursSpent));
                descriptionEditText.setText(workingPortfolioItem.descr);
                achievementsEditText.setText(workingPortfolioItem.achievements);
                lessonLearnedEditText.setText(workingPortfolioItem.lessonLearned);
                nextTimeEditText.setText(workingPortfolioItem.nextTime);
            }
        }
    }

    private void prepareCompetenceSpinner() {
        ArrayList<String> competenceSpinnerArray =  new ArrayList<>();
        ArrayList<Competence> competences = dbHelper.getMultipleCompetences(0);
        for (Competence competence : competences) {
            competenceSpinnerArray.add(competence.title);
        }

        ArrayAdapter<String> competenceSpinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, competenceSpinnerArray);

        competenceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        competenceSpinner.setAdapter(competenceSpinnerAdapter);
    }

    private void prepareProjectSpinner() {
        ArrayList<String> projectSpinnerArray =  new ArrayList<>();

        for (Project project : projects) {
            projectSpinnerArray.add(project.title);
        }

        ArrayAdapter<String> projectSpinnerAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, projectSpinnerArray);

        projectSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        projectSpinner.setAdapter(projectSpinnerAdapter);

        if (projectId > 0) {
            for (int i = 0; i < projects.size(); i++) {
                if (projects.get(i).id == projectId) {
                    projectSpinner.setSelection(i);
                }
            }
        }
    }

    private void prepareDateSetButton() {
        // Use the current date as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DATE);
        int month = c.get(Calendar.MONTH) + 1; // Calendar returns 0 for january
        int year = c.get(Calendar.YEAR);

        String text = getString(R.string.date) + " " + day + "-" + month + "-" + year;
        dateSetButton.setText(text);

        // Make sure we don't forget some zeroes.... :'(
        if (day < 10 && month < 10) {
            dateOfAction = "0" + Integer.toString(day) + "0" +
                    Integer.toString(month) + Integer.toString(year);
        } else if (day < 10) {
            dateOfAction = "0" + Integer.toString(day) +
                    Integer.toString(month) + Integer.toString(year);
        } else if (month < 10) {
            dateOfAction = Integer.toString(day) + "0" +
                    Integer.toString(month) + Integer.toString(year);
        } else {
            dateOfAction = Integer.toString(day) + Integer.toString(month) + Integer.toString(year);
        }

    }

    public void save(View view) {
        int projectId, competenceId;
        double hoursSpent;
        String DOLE, title, actionDate, description, achievement, lessonLearned, nextTime;

        projectId = dbHelper.getMultipleProjects(0).get(projectSpinner.getSelectedItemPosition()).id;
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmm");
        Date date = new Date();
        DOLE = dateFormat.format(date);

        title = titleEditText.getText().toString();
        actionDate = dateOfAction;
        hoursSpent = 0;
        try {
            Log.i("tag", hoursSpentEditText.getText().toString());
            hoursSpent = Double.parseDouble(hoursSpentEditText.getText().toString()); //bweghh, I know...
            Log.i("tag", Double.toString(hoursSpent));
        } catch (NumberFormatException e) {
            // too bad :( the edit-text is most likely empty
        }

        description = descriptionEditText.getText().toString();
        achievement = achievementsEditText.getText().toString();

        if (type == 1) {
            competenceId = dbHelper.getMultipleCompetences(0).get(competenceSpinner.getSelectedItemPosition()).id;
            lessonLearned = lessonLearnedEditText.getText().toString();
            nextTime = nextTimeEditText.getText().toString();

            if (workingPortfolioItem == null) {
                workingPortfolioItem = new PortfolioItem(projectId, competenceId, DOLE, title,
                        actionDate, hoursSpent, description, achievement, lessonLearned, nextTime);
            } else {
                // Reuse the id, so that we don't create a (edited) duplicate
                workingPortfolioItem = new PortfolioItem(workingPortfolioItem.id, projectId,
                        competenceId, DOLE, title, actionDate, hoursSpent, description, achievement,
                        lessonLearned, nextTime);
            }

            dbHelper.addOrUpdatePortfolioItem(workingPortfolioItem);
            setResult(RESULT_OK);
            finish();
        } else if (type == 0) {
            if (workingLog == null) {
                workingLog = new ActionLog(projectId, DOLE, title, actionDate, hoursSpent,
                        description, achievement);
            } else {
                // Reuse the id, so that we don't create a (edited) duplicate
                workingLog = new ActionLog(workingLog.id, projectId, DOLE, title, actionDate,
                        hoursSpent, description, achievement);
            }

            dbHelper.addOrUpdateLog(workingLog);
            setResult(RESULT_OK);
            finish();
        }

    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void updateDateButton(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String dateText = getString(R.string.date) + " " + dayOfMonth + "-" + monthOfYear +
                "-" + year;
        dateSetButton.setText(dateText);

        // Make sure we don't forget some zeroes.... :'(
        if (dayOfMonth < 10 && monthOfYear < 10) {
            dateOfAction = "0" + Integer.toString(dayOfMonth) + "0" +
                    Integer.toString(monthOfYear) + Integer.toString(year);
        } else if (dayOfMonth < 10) {
            dateOfAction = "0" + Integer.toString(dayOfMonth) +
                    Integer.toString(monthOfYear) + Integer.toString(year);
        } else if (monthOfYear < 10) {
            dateOfAction = Integer.toString(dayOfMonth) + "0" +
                    Integer.toString(monthOfYear) + Integer.toString(year);
        } else {
            dateOfAction = Integer.toString(dayOfMonth) + Integer.toString(monthOfYear) + Integer.toString(year);
        }
    }
}
