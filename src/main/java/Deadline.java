import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class Deadline extends Task {
    private static final DateTimeFormatter DATETIME_OUTPUT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    private static final DateTimeFormatter DATETIME_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + this.by.format(DATETIME_FILE);
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.by.format(DATETIME_OUTPUT) + ")";
    }
}