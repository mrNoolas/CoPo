package com.ic_tea.david.copo.objects;

/**
 * Created by david on 6/4/16.
 */
public class Project {
    public int id;
    public String title;
    public String descr;

    public Project(int id, String title, String descr) {
        this.id = id;
        this.title = title;
        this.descr = descr;
    }

    public Project(String title, String descr) {
        this.id = -1;
        this.title = title;
        this.descr = descr;
    }
}
