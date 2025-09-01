package jinjja.task;

import java.util.ArrayList;

/**
 * Manages a list of tasks. Provides operations to add, remove, mark, and query tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public Task getTask(int index) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.getSize()) {
            return this.tasks.get(index);
        } else {
            throw new ArrayIndexOutOfBoundsException("Task number is out of range.");
        }
    }

    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    public void addTask(Task task) {
        this.tasks.add(task);
    }

    public Task removeTask(int index) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.getSize()) {
            return this.tasks.remove(index);
        } else {
            throw new ArrayIndexOutOfBoundsException("Task number is out of range.");
        }
    }

    public void markTask(boolean isDone, int index) throws ArrayIndexOutOfBoundsException, NumberFormatException {
        if (this.isEmpty()) {
            throw new ArrayIndexOutOfBoundsException("No tasks available.");
        }
        if (index >= 0 && index < this.getSize()) {
            Task task = this.tasks.get(index);
            task.setDone(isDone);
        } else {
            throw new NumberFormatException(
                    "Task number is out of range. \n" + "Please enter a number between 1 and " + this.getSize() + ".");
        }
    }

    public int getSize() {
        return this.tasks.size();
    }

    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }
}
