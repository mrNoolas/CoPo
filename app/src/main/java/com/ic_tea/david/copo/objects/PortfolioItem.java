package com.ic_tea.david.copo.objects;

import android.content.Context;

import java.util.ArrayList;

public class PortfolioItem extends ActionLog {
    public int competenceId;
    public String lessonLearned;
    public String nextTime;

    public PortfolioItem (int id, int projectId, int competenceId, String DOLE, String title,
                          String date, int hoursSpent, String descr, String achievements,
                          String lessonLearned, String nextTime) {
        super(id, projectId, DOLE, title, date, hoursSpent, descr, achievements);
        this.competenceId = competenceId;
        this.lessonLearned = lessonLearned;
        this.nextTime = nextTime;
    }

    public PortfolioItem (int projectId, int competenceId, String DOLE, String title,
                          String date, int hoursSpent, String descr, String achievements,
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

    /***
     * * This function generates and executes a share intent.
     * It will compose a message with info and content from sharableEntries.
     *
     * @param context         Context object
     * @param sharableEntries Arraylist of all entry objects to be shared
     */
    public static boolean sharePortfolio(Context context, ArrayList<PortfolioItem> sharableEntries) {
        // TODO: share stuff
        /*
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
        }*/
        return true;
    }
}
