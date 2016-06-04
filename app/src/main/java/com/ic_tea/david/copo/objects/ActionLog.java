package com.ic_tea.david.copo.objects;

/** It should be taken into account that this class doesn't handle correctness of data **/
public class ActionLog {
    public int id;
    public int projectId;
    public String title;
    public String date;
    public String DOLE;
    public String descr;
    public String achievements;

    public ActionLog(int id, int projectId, String title, String date, String DOLE, String descr,
                     String achievements) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.date = date;
        this.DOLE = DOLE;
        this.descr = descr;
        this.achievements = achievements;
    }

    public ActionLog(int projectId, String title, String date, String DOLE, String descr,
                     String achievements) {
        this.id = -1;
        this.projectId = projectId;
        this.title = title;
        this.date = date;
        this.DOLE = DOLE;
        this.descr = descr;
        this.achievements = achievements;
    }
}
