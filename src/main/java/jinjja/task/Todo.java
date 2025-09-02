package jinjja.task;

/**
 * Represents a todo task without any date/time attached to it. This is the simplest type of task that only requires a
 * description.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     *
     * @param description The description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Converts the todo task to a format suitable for saving to a file. The format is "T | [completion status] |
     * [description]".
     *
     * @return A string representation of the todo task in file format
     */
    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }

    /**
     * Returns a string representation of the todo task for display. The format is "[T][completion status]
     * [description]".
     *
     * @return A string representation of the todo task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
