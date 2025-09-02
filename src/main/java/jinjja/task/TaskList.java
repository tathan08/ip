package jinjja.task;

import java.util.ArrayList;

/**
 * Manages a list of tasks. Provides operations to add, remove, mark, and query tasks. This class serves as the main
 * data structure for storing and manipulating tasks in the Jinjja application.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with the specified list of tasks.
     *
     * @param tasks The initial list of tasks
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Retrieves a task at the specified index.
     *
     * @param index The index of the task to retrieve
     * @return The task at the specified index
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     */
    public Task getTask(int index) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.getSize()) {
            return this.tasks.get(index);
        } else {
            throw new ArrayIndexOutOfBoundsException("Task number is out of range.");
        }
    }

    /**
     * Returns the underlying list of tasks.
     *
     * @return The ArrayList containing all tasks
     */
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }

    /**
     * Adds a new task to the list.
     *
     * @param task The task to be added
     */
    public void addTask(Task task) {
        this.tasks.add(task);
    }

    /**
     * Removes a task at the specified index.
     *
     * @param index The index of the task to remove
     * @return The removed task
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     */
    public Task removeTask(int index) throws ArrayIndexOutOfBoundsException {
        if (index >= 0 && index < this.getSize()) {
            return this.tasks.remove(index);
        } else {
            throw new ArrayIndexOutOfBoundsException("Task number is out of range.");
        }
    }

    /**
     * Marks or unmarks a task at the specified index.
     *
     * @param isDone true to mark the task as done, false to mark as not done
     * @param index The index of the task to mark/unmark
     * @throws ArrayIndexOutOfBoundsException if the task list is empty
     * @throws NumberFormatException if the index is out of range
     */
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

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the task list
     */
    public int getSize() {
        return this.tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the list contains no tasks, false otherwise
     */
    public boolean isEmpty() {
        return this.tasks.isEmpty();
    }
}
