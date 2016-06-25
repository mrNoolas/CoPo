package com.ic_tea.david.copo.objects;

import android.content.Context;

import java.util.ArrayList;

public class PortfolioItem extends ActionLog {
    public int competenceId;
    public String lessonLearned;
    public String nextTime;

    public PortfolioItem (int id, int projectId, int competenceId, String DOLE, String title,
                          String date, double hoursSpent, String descr, String achievements,
                          String lessonLearned, String nextTime) {
        super(id, projectId, DOLE, title, date, hoursSpent, descr, achievements);
        this.competenceId = competenceId;
        this.lessonLearned = lessonLearned;
        this.nextTime = nextTime;
    }

    public PortfolioItem (int projectId, int competenceId, String DOLE, String title,
                          String date, double hoursSpent, String descr, String achievements,
                          String lessonLearned, String nextTime) {
        super(projectId, DOLE, title, date, hoursSpent, descr, achievements);
        this.competenceId = competenceId;
        this.lessonLearned = lessonLearned;
        this.nextTime = nextTime;
    }

    public PortfolioItem (ActionLog log, int competenceId, String lessonLearned, String nextTime) {
        super(log.id, log.projectId, log.DOLE, log.title, log.date, log.hoursSpent, log.descr,
                log.achievements);
        this.competenceId = competenceId;
        this.lessonLearned = lessonLearned;
        this.nextTime = nextTime;
    }

}
