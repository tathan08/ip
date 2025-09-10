package jinjja.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import jinjja.command.AddCommand;
import jinjja.command.Command;
import jinjja.command.DeleteCommand;
import jinjja.command.ExitCommand;
import jinjja.command.FindCommand;
import jinjja.command.InvalidCommand;
import jinjja.command.ListCommand;
import jinjja.command.MarkCommand;
import jinjja.command.UnmarkCommand;
import jinjja.task.Deadline;
import jinjja.task.Event;
import jinjja.task.Task;
import jinjja.task.Todo;

/**
 * Parser deals with making sense of the user command. It parses user input and returns appropriate Command objects.
 */
public class Parser {
    private static final DateTimeFormatter DATETIME_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Builds a string from parts of the input, joining them with spaces.
     *
     * @param parts The list of string parts
     * @param startIndex The starting index (inclusive)
     * @param endIndex The ending index (exclusive)
     * @return The concatenated string with spaces between parts
     */
    private static String buildStringFromParts(ArrayList<String> parts, int startIndex, int endIndex) {
        StringBuilder builder = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            builder.append(parts.get(i));
            if (i < endIndex - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    /**
     * Parses the user input and returns a Command object.
     *
     * @param input The user input string.
     * @return A Command object that can be executed.
     */
    public static Command parse(String input) {
        assert input != null : "Input cannot be null";

        // Split input into parts
        ArrayList<String> parts = new ArrayList<>();
        for (String part : input.split(" ")) {
            parts.add(part);
        }

        if (parts.isEmpty()) {
            return new InvalidCommand("Empty command");
        }

        String action = parts.get(0);
        assert action != null : "First part of input should not be null";
        CommandType commandType = CommandType.fromString(action);

        switch (commandType) {
        case BYE:
            return new ExitCommand();
        case LIST:
            return new ListCommand();
        case MARK:
            return parseMarkCommand(parts);
        case UNMARK:
            return parseUnmarkCommand(parts);
        case TODO:
            return parseTodoCommand(parts);
        case DEADLINE:
            return parseDeadlineCommand(parts);
        case EVENT:
            return parseEventCommand(parts);
        case DELETE:
            return parseDeleteCommand(parts);
        case FIND:
            return parseFindCommand(parts);
        case UNKNOWN:
            // Fallthrough
        default:
            return new InvalidCommand("I have no clue what you just said. Please use a command I know.");
        }
    }

    /**
     * Parses a mark command from the input parts.
     *
     * @param parts The input split into parts
     * @return A MarkCommand if valid, InvalidCommand otherwise
     */
    private static Command parseMarkCommand(ArrayList<String> parts) {
        assert parts != null : "Parts list should not be null";
        assert parts.size() > 0 : "Parts list should contain at least the command";

        if (parts.size() <= 1) {
            return new InvalidCommand("Task number is missing.");
        }
        try {
            int taskNum = Integer.parseInt(parts.get(1));
            assert taskNum > 0 : "Task number should be positive";
            return new MarkCommand(taskNum);
        } catch (NumberFormatException e) {
            return new InvalidCommand("Invalid task number format. " + e.getMessage());
        }
    }

    /**
     * Parses an unmark command from the input parts.
     *
     * @param parts The input split into parts
     * @return An UnmarkCommand if valid, InvalidCommand otherwise
     */
    private static Command parseUnmarkCommand(ArrayList<String> parts) {
        assert parts != null : "Parts list should not be null";
        assert parts.size() > 0 : "Parts list should contain at least the command";

        if (parts.size() <= 1) {
            return new InvalidCommand("Task number is missing.");
        }
        try {
            int taskNum = Integer.parseInt(parts.get(1));
            assert taskNum > 0 : "Task number should be positive";
            return new UnmarkCommand(taskNum);
        } catch (NumberFormatException e) {
            return new InvalidCommand("Invalid task number format. " + e.getMessage());
        }
    }

    /**
     * Parses a todo command from the input parts.
     *
     * @param parts The input split into parts
     * @return An AddCommand with a Todo task if valid, InvalidCommand otherwise
     */
    private static Command parseTodoCommand(ArrayList<String> parts) {
        assert parts != null : "Parts list should not be null";
        assert parts.size() > 0 : "Parts list should contain at least the command";

        if (parts.size() <= 1) {
            return new InvalidCommand("Task description is missing.");
        }

        String description = buildStringFromParts(parts, 1, parts.size());
        Task task = new Todo(description);
        return new AddCommand(task);
    }

    /**
     * Parses a deadline command.
     */
    private static Command parseDeadlineCommand(ArrayList<String> parts) {
        // Find the "/by" delimiter
        int byIndex = -1;
        for (int i = 1; i < parts.size(); i++) {
            if (parts.get(i).equals("/by")) {
                byIndex = i;
                break;
            }
        }

        boolean hasDelimiter = byIndex != -1;
        boolean hasDescription = byIndex > 1;
        boolean hasDate = byIndex < parts.size() - 1;
        if (!hasDelimiter || !hasDescription || !hasDate) {
            return new InvalidCommand("Deadline description or /by is missing.");
        }

        String taskDescription = buildStringFromParts(parts, 1, byIndex);
        String byDate = buildStringFromParts(parts, byIndex + 1, parts.size());

        // Parse the byDate string into a LocalDateTime object
        try {
            LocalDateTime byDateTime = LocalDateTime.parse(byDate, DATETIME_FILE);
            Task task = new Deadline(taskDescription, byDateTime);
            return new AddCommand(task);
        } catch (DateTimeParseException e) {
            return new InvalidCommand("Invalid date format. Please use yyyy-MM-dd HH:mm.");
        }
    }

    /**
     * Parses an event command.
     */
    private static Command parseEventCommand(ArrayList<String> parts) {
        // Find the "/from" and "/to" delimiters
        int fromIndex = -1;
        int toIndex = -1;
        for (int i = 1; i < parts.size(); i++) {
            if (parts.get(i).equals("/from")) {
                fromIndex = i;
            } else if (parts.get(i).equals("/to")) {
                toIndex = i;
            }
        }

        boolean hasDelimiter = fromIndex != -1 && toIndex != -1;
        boolean hasDescription = fromIndex > 1;
        boolean isValidIndex = fromIndex + 1 < toIndex;
        if (!hasDelimiter || !hasDescription || !isValidIndex) {
            return new InvalidCommand("Event description, /from, or /to is missing.");
        }

        String taskDescription = buildStringFromParts(parts, 1, fromIndex);
        String fromDateString = buildStringFromParts(parts, fromIndex + 1, toIndex);
        String toDateString = buildStringFromParts(parts, toIndex + 1, parts.size());

        try {
            LocalDateTime fromDateTime = LocalDateTime.parse(fromDateString, DATETIME_FILE);
            LocalDateTime toDateTime = LocalDateTime.parse(toDateString, DATETIME_FILE);
            assert !fromDateTime.isAfter(toDateTime) : "Event start time should not be after end time";
            Task task = new Event(taskDescription, fromDateTime, toDateTime);
            return new AddCommand(task);
        } catch (DateTimeParseException e) {
            return new InvalidCommand("Invalid date format for /from or /to. Please use yyyy-MM-dd HH:mm.");
        }
    }

    /**
     * Parses a delete command.
     */
    private static Command parseDeleteCommand(ArrayList<String> parts) {
        assert parts != null : "Parts list should not be null";
        assert parts.size() > 0 : "Parts list should contain at least the command";

        if (parts.size() < 2) {
            return new InvalidCommand("Task number is missing.");
        }
        try {
            int taskNum = Integer.parseInt(parts.get(1));
            assert taskNum > 0 : "Task number should be positive";
            return new DeleteCommand(taskNum);
        } catch (NumberFormatException e) {
            return new InvalidCommand("Invalid task number format. " + e.getMessage());
        }
    }

    /**
     * Parses a find command from the input parts.
     *
     * @param parts The input split into parts
     * @return A FindCommand if valid, InvalidCommand otherwise
     */
    private static Command parseFindCommand(ArrayList<String> parts) {
        if (parts.size() <= 1) {
            return new InvalidCommand("Search keyword is missing.");
        }

        String keyword = buildStringFromParts(parts, 1, parts.size());
        return new FindCommand(keyword);
    }
}
