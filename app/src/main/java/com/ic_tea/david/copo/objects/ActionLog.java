package com.ic_tea.david.copo.objects;

import android.content.Context;
import android.content.Intent;

import com.ic_tea.david.copo.DisplayEntries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/** It should be taken into account that this class doesn't handle correctness of data **/
public class ActionLog {
    public int id;
    public int projectId;
    public String DOLE;
    public String title;
    public String date;
    public double hoursSpent;
    public String descr;
    public String achievements;

    public ActionLog(int id, int projectId, String DOLE, String title, String date,
                     double hoursSpent, String descr, String achievements) {
        this.id = id;
        this.projectId = projectId;
        this.DOLE = DOLE;
        this.title = title;
        this.date = date;
        this.hoursSpent = hoursSpent;
        this.descr = descr;
        this.achievements = achievements;
    }

    public ActionLog(int projectId, String DOLE, String title, String date, double hoursSpent,
                     String descr, String achievements) {
        this.id = -1;
        this.projectId = projectId;
        this.DOLE = DOLE;
        this.title = title;
        this.date = date;
        this.hoursSpent = hoursSpent;
        this.descr = descr;
        this.achievements = achievements;
    }
}
