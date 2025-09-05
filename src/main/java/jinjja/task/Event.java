package jinjja.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a specific start and end date/time.
 */
public class Event extends Task {
    private static final DateTimeFormatter DATETIME_OUTPUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    private static final DateTimeFormatter DATETIME_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private LocalDateTime from;
    private LocalDateTime to;

    /**
     * Constructs a new Event task with a start and end time.
     *
     * @param description
     * @param from
     * @param to
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + this.from.format(DATETIME_FILE) + " | "
                + this.to.format(DATETIME_FILE);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(DATETIME_OUTPUT) + " to: "
                + this.to.format(DATETIME_OUTPUT) + ")";
    }
}
