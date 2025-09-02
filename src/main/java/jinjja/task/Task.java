package jinjja.task;

/**
 * Represents a task with a description and completion status. This is the base class for all task types in the Jinjja
 * application.
 */
public class Task {
    private boolean isDone;
    private String description;

    /**
     * Constructs a new Task with the specified description. The task is initially marked as not done.
     *
     * @param description The description of the task
     */
    public Task(String description) {
        this.isDone = false;
        this.description = description;
    }

    /**
     * Sets the completion status of the task.
     *
     * @param isDone true if the task is completed, false otherwise
     * @return true indicating the status was successfully changed
     */
    public boolean setDone(boolean isDone) {
        this.isDone = isDone;
        return true; // Indicate that the status was changed
    }

    /**
     * Returns the completion status of the task.
     *
     * @return true if the task is completed, false otherwise
     */
    public boolean getIsDone() {
        return this.isDone;
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Converts the task to a format suitable for saving to a file.
     * 
     * @return A string representation of the task in file format.
     */
    public String toFileFormat() {
        return (this.isDone ? 1 : 0) + " | " + this.description;
    }

    @Override
    public String toString() {
        return "[" + (this.isDone ? "X" : " ") + "] " + this.description;
    }
}
