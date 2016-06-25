package com.ic_tea.david.copo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ic_tea.david.copo.objects.ActionLog;
import com.ic_tea.david.copo.objects.PortfolioItem;
import com.ic_tea.david.copo.objects.Project;

/**
 * Created by david on 6/24/16.
 */
public class ShareMessage {
    Context context;
    DBHelper dbHelper;
    private boolean hasContent = false;
    private String start, end, subject, message;

    private final String NL = "\n";
    private final String DIV = "==================================================";
    private final String div = "--------------------------------------------------";
    private final String PRSTART = "Project:";
    private final String POSTART = "Portfolio:";
    private final String LSTART = "Logboek:";

    private final String NAME = "Naam: ";
    private final String DESCR = "Omschrijving: ";
    private final String ID = "IdentificatieNummer: ";


    /**
     * Initiate empty default message
     * @param context the context to form the message in (any context is fine)
     * @param dbHelper Database to querry the additional info from
     *
     */
    public ShareMessage (Context context, DBHelper dbHelper) {
        this.dbHelper = dbHelper;
        this.context = context;

        start = context.getString(R.string.message_start_1) + context.getString(R.string.app_name) + context.getString(R.string.message_start_2);
        end = context.getString(R.string.message_end);

        message = start + NL + NL;
    }

    public void showNoContentDialog () {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Niets om te delen :(")
                .setMessage("Als je iets wilt delen, dan moet je eerst zorgen dat er iets is om te" +
                        " delen.... Dit doe je door te beginnen aan je portfolio (bij dit project)" +
                        " of door een logboek te maken.")
                .setNeutralButton("Jammer... Ik ga mijn best doen!", null)
                .create();
        alertDialog.show();
    }

    public boolean send() {
        if (hasContent) {
            message += DIV + NL + NL + end;

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.setType("text/plain");
            context.startActivity(sendIntent);
            return true;
        }
        return false;
    }

    public void addProject (Project project) {
        Log.i("tag", project.title);
        message +=
                DIV + NL + NL +
                        PRSTART + NL +
                        //ID + project.id + NL +
                        NAME + project.title + NL +
                        DESCR + project.descr + NL + NL;

        for (ActionLog log : dbHelper.getMultipleLogs(project.id)){
            addLog(log);
        }
        for (PortfolioItem portfolioItem : dbHelper.getMultiplePortfolioEntries(project.id)) {
            addPortfolioItem(portfolioItem);
        }
    }

    public void addPortfolioItem (PortfolioItem portfolioItem) {
        message +=
                div + NL + NL +
                        POSTART + NL +
                        //ID + portfolioItem.id + NL +
                        NAME + portfolioItem.title + NL + NL +
                        "Project: " + dbHelper.getProject(portfolioItem.projectId).title + NL +
                        "Competentie: " + dbHelper.getCompetence(portfolioItem.competenceId).title + NL + NL +

                        "Voor het laatst bijgewerkt op: " + // DDMMYYYYHHMM
                        portfolioItem.DOLE.substring(0, 2) + "-" +
                        portfolioItem.DOLE.substring(2, 4) + "-" +
                        portfolioItem.DOLE.substring(4, 8) +
                        " om " + portfolioItem.DOLE.substring(8, 10) + ":" +
                        portfolioItem.DOLE.substring(10) + " uur" + NL +
                        "Datum uitvoering: " + portfolioItem.date.substring(0, 2) + "-" +
                        portfolioItem.date.substring(2, 4) + "-" +
                        portfolioItem.date.substring(4) + NL + NL + NL +

                        DESCR + NL + portfolioItem.descr + NL + NL +
                        "Dit heb ik bereikt:" + NL + portfolioItem.achievements + NL + NL +
                        "Dit heb ik geleerd:" + NL + portfolioItem.lessonLearned + NL + NL +
                        "Dit doe ik de volgende keer:" + NL + portfolioItem.nextTime + NL + NL;
        hasContent = true;
    }

    public void addLog (ActionLog log) {
        message +=
                div + NL + NL +
                        LSTART + NL +
                        //ID + log.id + NL +
                        NAME + log.title + NL + NL +
                        "Project: " + dbHelper.getProject(log.projectId).title + NL + NL +

                        "Voor het laatst bijgewerkt op: " + // DDMMYYYYHHMM
                        log.DOLE.substring(0, 2) + "-" +
                        log.DOLE.substring(2, 4) + "-" +
                        log.DOLE.substring(4, 8) +
                        " om " + log.DOLE.substring(8, 10) + ":" +
                        log.DOLE.substring(10) + " uur" + NL +
                        "Datum uitvoering: " + log.date.substring(0, 2) + "-" +
                        log.date.substring(2, 4) + "-" +
                        log.date.substring(4) + NL + NL + NL +

                        DESCR + NL + log.descr + NL + NL +
                        "Dit heb ik bereikt:" + NL + log.achievements + NL + NL;
        hasContent = true;
    }
}
