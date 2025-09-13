package jinjja.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jinjja.command.AddCommand;
import jinjja.command.Command;
import jinjja.command.ConfirmCommand;
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
import jinjja.task.Tentative;
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
    private static String buildStringFromParts(List<String> parts, int startIndex, int endIndex) {
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

        // Split input into parts using streams
        List<String> parts = Arrays.stream(input.split(" ")).collect(Collectors.toList());

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
        case TENTATIVE:
            return parseTentativeCommand(parts);
        case CONFIRM:
            return parseConfirmCommand(parts);
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
    private static Command parseUnmarkCommand(List<String> parts) {
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
    private static Command parseTodoCommand(List<String> parts) {
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
    private static Command parseDeadlineCommand(List<String> parts) {
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

        boolean hasDelimiter = fromIndex != -1 && toIndex != -1;
        boolean isValidIndex = fromIndex + 1 < toIndex;
        if (!hasDelimiter || !isValidIndex) {
            return new InvalidCommand("/from, or /to is missing.");
        }

        String taskDescription = buildStringFromParts(parts, 1, fromIndex);
        assert !taskDescription.trim().isEmpty() : "Task description should not be empty";
        if (taskDescription.trim().isEmpty()) {
            return new InvalidCommand("Event description is missing.");
        }

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
    private static Command parseDeleteCommand(List<String> parts) {
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
    private static Command parseFindCommand(List<String> parts) {
        if (parts.size() <= 1) {
            return new InvalidCommand("Search keyword is missing.");
        }

        String keyword = buildStringFromParts(parts, 1, parts.size());
        return new FindCommand(keyword);
    }

    /**
     * Parses a tentative event command from the input parts. Format: tentative DESCRIPTION /slots /from DATE /to DATE
     * [/from DATE /to DATE ...]
     *
     * @param parts The input split into parts
     * @return An AddCommand with a Tentative if valid, InvalidCommand otherwise
     */
    private static Command parseTentativeCommand(List<String> parts) {
        int slotsIndex = findSlotsIndex(parts);
        if (slotsIndex == -1) {
            return new InvalidCommand("Tentative event must include /slots delimiter.");
        }

        if (slotsIndex <= 1) {
            return new InvalidCommand("Tentative event description is missing.");
        }

        String description = buildStringFromParts(parts, 1, slotsIndex);
        List<String> slotParts = parts.subList(slotsIndex + 1, parts.size());
        Tentative tentative = new Tentative(description);

        Command slotsParseResult = parseTimeSlots(slotParts, tentative, slotsIndex);
        if (slotsParseResult != null) {
            return slotsParseResult;
        }

        if (tentative.getSlotCount() == 0) {
            return new InvalidCommand("Tentative event must have at least one time slot.");
        }

        return new AddCommand(tentative);
    }

    /**
     * Finds the index of "/slots" delimiter in the input parts.
     *
     * @param parts The input split into parts
     * @return The index of "/slots" or -1 if not found
     */
    private static int findSlotsIndex(List<String> parts) {
        for (int i = 1; i < parts.size(); i++) {
            if (parts.get(i).equals("/slots")) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Parses time slots from the slot parts and adds them to the tentative event.
     *
     * @param slotParts The parts containing the time slots
     * @param tentative The tentative event to add slots to
     * @param slotsIndex The original index of "/slots" for error messages
     * @return InvalidCommand if parsing fails, null if successful
     */
    private static Command parseTimeSlots(List<String> slotParts, Tentative tentative, int slotsIndex) {
        for (int i = 0; i < slotParts.size();) {
            Command slotParseResult = parseNextTimeSlot(slotParts, tentative, i, slotsIndex);
            if (slotParseResult instanceof InvalidCommand) {
                return slotParseResult;
            }
            i = findNextFromIndex(slotParts, i);
        }
        return null;
    }

    /**
     * Parses the next time slot starting at the given index.
     *
     * @param slotParts The parts containing the time slots
     * @param tentative The tentative event to add the slot to
     * @param startIndex The starting index in slotParts
     * @param slotsIndex The original index of "/slots" for error messages
     * @return InvalidCommand if parsing fails, null if successful
     */
    private static Command parseNextTimeSlot(List<String> slotParts, Tentative tentative, int startIndex,
            int slotsIndex) {
        if (startIndex >= slotParts.size() || !slotParts.get(startIndex).equals("/from")) {
            return new InvalidCommand("Expected /from at position " + (slotsIndex + startIndex + 2));
        }

        int toIndex = findToIndex(slotParts, startIndex);
        if (toIndex == -1) {
            return new InvalidCommand("Missing /to for /from at position " + (slotsIndex + startIndex + 2));
        }

        if (toIndex <= startIndex + 1) {
            return new InvalidCommand("Missing date/time after /from.");
        }

        int nextFromIndex = findNextFromIndex(slotParts, toIndex);
        if (nextFromIndex <= toIndex + 1) {
            return new InvalidCommand("Missing date/time after /to.");
        }

        String fromDateString = buildStringFromParts(slotParts, startIndex + 1, toIndex);
        String toDateString = buildStringFromParts(slotParts, toIndex + 1, nextFromIndex);

        try {
            LocalDateTime fromDateTime = LocalDateTime.parse(fromDateString, DATETIME_FILE);
            LocalDateTime toDateTime = LocalDateTime.parse(toDateString, DATETIME_FILE);
            tentative.addTentativeSlot(fromDateTime, toDateTime);
            return null;
        } catch (DateTimeParseException e) {
            return new InvalidCommand("Invalid date format. Please use yyyy-MM-dd HH:mm.");
        }
    }

    /**
     * Finds the index of the next "/to" delimiter after the given index.
     *
     * @param slotParts The parts containing the time slots
     * @param fromIndex The index to start searching from
     * @return The index of "/to" or -1 if not found before the next "/from"
     */
    private static int findToIndex(List<String> slotParts, int fromIndex) {
        for (int j = fromIndex + 1; j < slotParts.size(); j++) {
            if (slotParts.get(j).equals("/to")) {
                return j;
            } else if (slotParts.get(j).equals("/from")) {
                break; // Found next /from without /to
            }
        }
        return -1;
    }

    /**
     * Finds the index of the next "/from" delimiter or the end of the list.
     *
     * @param slotParts The parts containing the time slots
     * @param toIndex The index to start searching from
     * @return The index of the next "/from" or the size of slotParts
     */
    private static int findNextFromIndex(List<String> slotParts, int toIndex) {
        for (int j = toIndex + 1; j < slotParts.size(); j++) {
            if (slotParts.get(j).equals("/from")) {
                return j;
            }
        }
        return slotParts.size();
    }

    /**
     * Parses a confirm command from the input parts. Format: confirm TASK_NUMBER SLOT_NUMBER
     *
     * @param parts The input split into parts
     * @return A ConfirmCommand if valid, InvalidCommand otherwise
     */
    private static Command parseConfirmCommand(List<String> parts) {
        if (parts.size() < 3) {
            return new InvalidCommand("Usage: confirm TASK_NUMBER SLOT_NUMBER");
        }

        try {
            int taskNumber = Integer.parseInt(parts.get(1));
            int slotNumber = Integer.parseInt(parts.get(2));
            return new ConfirmCommand(taskNumber, slotNumber);
        } catch (NumberFormatException e) {
            return new InvalidCommand("Invalid task number or slot number format.");
        }
    }
}
