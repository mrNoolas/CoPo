/*
 * Copyright (c) 2015. This was programmed by IC-Tea (@sirNoolas - David Vonk): Do not copy!
 */

package com.ic_tea.david.tepa;

public class Entry {
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
