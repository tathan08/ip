package jinjja.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
     * Parses the user input and returns a Command object.
     *
     * @param input The user input string.
     * @return A Command object that can be executed.
     */
    public static Command parse(String input) {
        // Split input into parts using streams
        List<String> parts = Arrays.stream(input.split(" "))
            .collect(Collectors.toList());

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
    private static Command parseMarkCommand(List<String> parts) {
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
    private static Command parseUnmarkCommand(List<String> parts) {
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
    private static Command parseTodoCommand(List<String> parts) {
        if (parts.size() <= 1) {
            return new InvalidCommand("Task description is missing.");
        }

        // Build description from remaining parts using streams
        String description = parts.stream()
            .skip(1)
            .collect(
                Collectors.joining(" "));

        Task task = new Todo(description);
        return new AddCommand(task);
    }

    /**
     * Parses a deadline command.
     */
    private static Command parseDeadlineCommand(List<String> parts) {
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

        // Build description using streams
        String taskDescription = parts.stream()
            .skip(1)
            .limit(byIndex - 1)
            .collect(
                Collectors.joining(" "));

        // Build date string using streams
        String byDate = parts.stream()
            .skip(byIndex + 1)
            .collect(
                Collectors.joining(" "));

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
    private static Command parseEventCommand(List<String> parts) {
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

        // Build description using streams
        String taskDescription = parts.stream()
            .skip(1)
            .limit(fromIndex - 1)
            .collect(
                Collectors.joining(" "));

        // Build from date string using streams
        String fromDateString = parts.stream()
            .skip(fromIndex + 1)
            .limit(toIndex - fromIndex - 1)
            .collect(
                Collectors.joining(" "));

        // Build to date string using streams
        String toDateString = parts.stream()
            .skip(toIndex + 1)
            .collect(
                Collectors.joining(" "));

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
    private static Command parseDeleteCommand(List<String> parts) {
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

    /**
     * Parses a find command from the input parts.
     *
     * @param parts The input split into parts
     * @return A FindCommand if valid, InvalidCommand otherwise
     */
    private static Command parseFindCommand(List<String> parts) {
        if (parts.size() <= 1) {
            return new InvalidCommand("Search keyword is missing.");
        }

        // Build keyword from remaining parts using streams
        String keyword = parts.stream()
            .skip(1)
            .collect(
                Collectors.joining(" "));

        return new FindCommand(keyword);
    }
}
