/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.tepa;

import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Entry {
    private static final String TAG = Entry.class.getSimpleName();
    private final int id;
    private String type;
    private String name;
    private String dateOfLastEdit;
    private String description;
    private String lessonLearned;

    public Entry(int id, String type, String name,
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

            String body = "Dit is een automatisch gegenereert bericht door de TePa " +
                    "(Technasium Portfolio) app. Dit bericht bevat een selectie van portfolio-items," +
                    "die zijn gedeeld via e-mail. Deze selectie is gegenereert op: " + dateStr;
            for (int i = 0; i < sharableEntries.size(); i++) {
                Entry entry = sharableEntries.get(i);
                String typeStr = entry.getType();

                body = body + "\n\n==================================================\n\n" +
                        headers.get(i) + "\nCompetentie: " + typeStr
                        + "\n\nOmschrijving van de situatie:\n\"" + entry.getDescription()
                        + "\"\n\nWat heb ik hiervan geleerd:\n\"" + entry.getLessonLearned() + "\"";
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
            context.startActivity(sendIntent);
            return true;
        } else {
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
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
