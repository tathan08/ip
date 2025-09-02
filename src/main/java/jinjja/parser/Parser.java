package jinjja.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import jinjja.command.AddCommand;
import jinjja.command.Command;
import jinjja.command.DeleteCommand;
import jinjja.command.ExitCommand;
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
     * Parses the user input and returns a Command object.
     *
     * @param input The user input string.
     * @return A Command object that can be executed.
     */
    public static Command parse(String input) {
        // Split input into parts
        ArrayList<String> parts = new ArrayList<>();
        for (String part : input.split(" ")) {
            parts.add(part);
        }

        if (parts.isEmpty()) {
            return new InvalidCommand("Empty command");
        }

        String action = parts.get(0);
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
        case UNKNOWN:
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
        if (parts.size() <= 1) {
            return new InvalidCommand("Task number is missing.");
        }
        try {
            int taskNum = Integer.parseInt(parts.get(1));
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
        if (parts.size() <= 1) {
            return new InvalidCommand("Task number is missing.");
        }
        try {
            int taskNum = Integer.parseInt(parts.get(1));
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
        if (parts.size() <= 1) {
            return new InvalidCommand("Task description is missing.");
        }

        // Build description from remaining parts
        StringBuilder descBuilder = new StringBuilder();
        for (int i = 1; i < parts.size(); i++) {
            descBuilder.append(parts.get(i));
            if (i < parts.size() - 1) {
                descBuilder.append(" ");
            }
        }

        Task task = new Todo(descBuilder.toString());
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

        if (byIndex == -1 || byIndex <= 1 || byIndex >= parts.size() - 1) {
            return new InvalidCommand("Deadline description or /by is missing.");
        }

        // Build description
        StringBuilder descBuilder = new StringBuilder();
        for (int i = 1; i < byIndex; i++) {
            descBuilder.append(parts.get(i));
            if (i < byIndex - 1) {
                descBuilder.append(" ");
            }
        }

        // Build date string
        StringBuilder byBuilder = new StringBuilder();
        for (int i = byIndex + 1; i < parts.size(); i++) {
            byBuilder.append(parts.get(i));
            if (i < parts.size() - 1) {
                byBuilder.append(" ");
            }
        }

        String taskDescription = descBuilder.toString();
        String byDate = byBuilder.toString();

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

        if (fromIndex == -1 || toIndex == -1 || fromIndex + 1 >= toIndex) {
            return new InvalidCommand("Event description, /from, or /to is missing.");
        }

        // Build description
        StringBuilder descBuilder = new StringBuilder();
        for (int i = 1; i < fromIndex; i++) {
            descBuilder.append(parts.get(i));
            if (i < fromIndex - 1) {
                descBuilder.append(" ");
            }
        }

        // Build from date string
        StringBuilder fromBuilder = new StringBuilder();
        for (int i = fromIndex + 1; i < toIndex; i++) {
            fromBuilder.append(parts.get(i));
            if (i < toIndex - 1) {
                fromBuilder.append(" ");
            }
        }

        // Build to date string
        StringBuilder toBuilder = new StringBuilder();
        for (int i = toIndex + 1; i < parts.size(); i++) {
            toBuilder.append(parts.get(i));
            if (i < parts.size() - 1) {
                toBuilder.append(" ");
            }
        }

        String taskDescription = descBuilder.toString();
        String fromDateString = fromBuilder.toString();
        String toDateString = toBuilder.toString();

        try {
            LocalDateTime fromDateTime = LocalDateTime.parse(fromDateString, DATETIME_FILE);
            LocalDateTime toDateTime = LocalDateTime.parse(toDateString, DATETIME_FILE);
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
        if (parts.size() < 2) {
            return new InvalidCommand("Task number is missing.");
        }
        try {
            int taskNum = Integer.parseInt(parts.get(1));
            return new DeleteCommand(taskNum);
        } catch (NumberFormatException e) {
            return new InvalidCommand("Invalid task number format. " + e.getMessage());
        }
    }
}
