package com.ic_tea.david.copo.objects;

public class Competence {

    public int id;
    public String title;
    public String descr;

    public Competence(int id, String title, String descr) {
        this.id = id;
        this.title = title;
        this.descr = descr;
    }

    public Competence(String title, String descr) {
        this.id = -1;
        this.title = title;
        this.descr = descr;
    }

}
