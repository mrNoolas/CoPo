package com.ic_tea.david.copo.objects;

public class PortfolioItem extends ActionLog {
    public int competenceId;
    public String lessonLearned;
    public String nextTime;

    public PortfolioItem (int id, int projectId, int competenceId, String title, String date,
                         String DOLE, String descr, String achievements, String lessonLearned,
                         String nextTime) {
        super(id, projectId, title, date, DOLE, descr, achievements);
        this.competenceId = competenceId;
        this.lessonLearned = lessonLearned;
        this.nextTime = nextTime;
    }

    public PortfolioItem (int projectId, int competenceId, String title, String date,
                         String DOLE, String descr, String achievements, String lessonLearned,
                         String nextTime) {
        super(projectId, title, date, DOLE, descr, achievements);
        this.competenceId = competenceId;
        this.lessonLearned = lessonLearned;
        this.nextTime = nextTime;
    }

    public PortfolioItem (ActionLog log, int competenceId, String lessonLearned, String nextTime) {
        super(log.id, log.projectId, log.title, log.date, log.DOLE, log.descr, log.achievements);
        this.competenceId = competenceId;
        this.lessonLearned = lessonLearned;
        this.nextTime = nextTime;
    }
}
