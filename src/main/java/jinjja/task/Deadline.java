package jinjja.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a specific date and time to be completed by. This task type includes a deadline that
 * indicates when the task should be finished.
 */
public class Deadline extends Task {
    private static final DateTimeFormatter DATETIME_OUTPUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    private static final DateTimeFormatter DATETIME_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private LocalDateTime by;

    /**
     * Constructs a Deadline task with the specified description and deadline.
     *
     * @param description The description of the deadline task
     * @param by The date and time by which the task should be completed
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Converts the deadline task to a format suitable for saving to a file. The format is "D | [completion status] |
     * [description] | [deadline]".
     *
     * @return A string representation of the deadline task in file format
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + this.by.format(DATETIME_FILE);
    }

    /**
     * Returns a string representation of the deadline task for display. The format is "[D][completion status]
     * [description] (by: [formatted deadline])".
     *
     * @return A string representation of the deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by.format(DATETIME_OUTPUT) + ")";
    }
}
