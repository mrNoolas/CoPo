package com.ic_tea.david.copo.objects;

public class EduLevel {
    public int id;
    public String name;
    public String descr;

    public EduLevel(int id, String name, String descr) {
        this.id = id;
        this.name = name;
        this.descr = descr;
    }

    public EduLevel(String name, String descr) {
        this.id = -1;
        this.name = name;
        this.descr = descr;
    }
}
