/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.copo;

import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Entry {
    private static final String TAG = Entry.class.getSimpleName();
    private final int id;
    private int type;
    private String name;
    private String dateOfLastEdit;
    private String description;
    private String lessonLearned;

    public Entry(int id, int type, String name,
                 String dateOfLastEdit, String description, String lessonLearned) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.dateOfLastEdit = dateOfLastEdit;
        this.description = description;
        this.lessonLearned = lessonLearned;
    }

    /***
     * * This function generates and executes a share intent.
     * It will compose a message with info and content from sharableEntries.
     *
     * @param context         Context object
     * @param sharableEntries Arraylist of all entry objects to be shared
     */
    public static boolean share(Context context, ArrayList<Entry> sharableEntries) {
        if (sharableEntries.size() > 0) {
            ArrayList<String> headers = DisplayEntries.getInfo(sharableEntries);

            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
            Date date = new Date();
            String dateStr = dateFormat.format(date);

            String body = context.getString(R.string.automatically_generated_email_message) + dateStr;
            for (int i = 0; i < sharableEntries.size(); i++) {
                Entry entry = sharableEntries.get(i);
                String typeStr = context.getResources().getStringArray(R.array.competences)[entry.getType()];

                body = body + "\n\n==================================================\n\n" +
                        headers.get(i) + context.getString(R.string.competence_email) + typeStr
                        + context.getString(R.string.description_situation_email) + entry.getDescription()
                        + context.getString(R.string.lesson_learned_email) + entry.getLessonLearned() + "\"";
            }
            body = body + "\n\n==================================================\n\n" +
                    context.getString(R.string.disclaimer_email);

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, body);
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
            return true;
        } else {
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfLastEdit() {
        return dateOfLastEdit;
    }

    public void setDateOfLastEdit(String dateOfLastEdit) {
        this.dateOfLastEdit = dateOfLastEdit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLessonLearned() {
        return lessonLearned;
    }

    public void setLessonLearned(String lessonLearned) {
        this.lessonLearned = lessonLearned;
    }
}
