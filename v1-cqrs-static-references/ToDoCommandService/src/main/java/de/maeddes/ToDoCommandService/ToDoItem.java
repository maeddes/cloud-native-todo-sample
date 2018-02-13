package de.maeddes.ToDoCommandService;

public class ToDoItem {

    String description;
    boolean done = false;

    public ToDoItem() {
    }

    public ToDoItem(String description) {
        this.description = description;
    }

    public ToDoItem(String description, boolean done) {
        this.description = description;
        this.done = done;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return "ToDoItem{" +
                "description='" + description + '\'' +
                ", done=" + done +
                '}';
    }
}