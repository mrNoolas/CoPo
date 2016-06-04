package com.ic_tea.david.copo.objects;

public class Goal {
    public int id;
    public int parentId;
    public int projectId;
    public String title;
    public String description;
    public String dueDate;
    public boolean isFinished;

    public Goal(int id, int parentId, int projectId, String title, String description,
                String dueDate, boolean isFinished) {
        this.id = id;
        this.parentId = parentId;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isFinished = isFinished;
    }

    public Goal(int id, int parentId, int projectId, String title, String description,
                String dueDate, int isFinished) {
        this.id = id;
        this.parentId = parentId;
        this.projectId = projectId;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        setFinished(isFinished);
    }

    public void setFinished (int isFinished) {
        this.isFinished = isFinished > 0;
    }

    public int getFinishedInt() {
        if (isFinished) {
            return 1;
        } else {
            return 0;
        }
    }
}
