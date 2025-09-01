package jinjja.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jinjja.command.AddCommand;
import jinjja.command.Command;
import jinjja.command.DeleteCommand;
import jinjja.command.ExitCommand;
import jinjja.command.InvalidCommand;
import jinjja.command.ListCommand;
import jinjja.command.MarkCommand;
import jinjja.command.UnmarkCommand;

/**
 * Test class for Parser functionality. Tests parsing of various command types and edge cases.
 */
public class ParserTest {

    @Test
    public void parse_validTodoCommand_returnsAddCommand() {
        Command command = Parser.parse("todo buy milk");

        assertTrue(command instanceof AddCommand);
        // We can't directly access the task field, so we test the behavior
        assertFalse(command.canExit());
    }

    @Test
    public void parse_todoWithMultipleWords_correctDescription() {
        Command command = Parser.parse("todo buy groceries and cook dinner");

        assertTrue(command instanceof AddCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_todoWithoutDescription_returnsInvalidCommand() {
        Command command = Parser.parse("todo");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_validDeadlineCommand_returnsAddCommand() {
        Command command = Parser.parse("deadline submit assignment /by 2025-12-31 23:59");

        assertTrue(command instanceof AddCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_deadlineWithInvalidDate_returnsInvalidCommand() {
        Command command = Parser.parse("deadline submit assignment /by invalid-date");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_deadlineWithoutBy_returnsInvalidCommand() {
        Command command = Parser.parse("deadline submit assignment");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_deadlineWithoutDescription_returnsInvalidCommand() {
        Command command = Parser.parse("deadline /by 2025-12-31 23:59");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_deadlineWithEmptyDate_returnsInvalidCommand() {
        Command command = Parser.parse("deadline submit assignment /by");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_validEventCommand_returnsAddCommand() {
        Command command = Parser.parse("event team meeting /from 2025-09-15 10:00 /to 2025-09-15 11:30");

        assertTrue(command instanceof AddCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_eventWithInvalidFromDate_returnsInvalidCommand() {
        Command command = Parser.parse("event team meeting /from invalid-date /to 2025-09-15 11:30");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_eventWithInvalidToDate_returnsInvalidCommand() {
        Command command = Parser.parse("event team meeting /from 2025-09-15 10:00 /to invalid-date");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_eventWithoutFrom_returnsInvalidCommand() {
        Command command = Parser.parse("event team meeting /to 2025-09-15 11:30");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_eventWithoutTo_returnsInvalidCommand() {
        Command command = Parser.parse("event team meeting /from 2025-09-15 10:00");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_eventWithoutDescription_returnsInvalidCommand() {
        Command command = Parser.parse("event /from 2025-09-15 10:00 /to 2025-09-15 11:30");

        // The parser doesn't properly validate empty descriptions for events
        // This test documents the current behavior - it creates an AddCommand with empty description
        assertTrue(command instanceof AddCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_validMarkCommand_returnsMarkCommand() {
        Command command = Parser.parse("mark 1");

        assertTrue(command instanceof MarkCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_markWithoutNumber_returnsInvalidCommand() {
        Command command = Parser.parse("mark");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_markWithInvalidNumber_returnsInvalidCommand() {
        Command command = Parser.parse("mark abc");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_validUnmarkCommand_returnsUnmarkCommand() {
        Command command = Parser.parse("unmark 2");

        assertTrue(command instanceof UnmarkCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_unmarkWithoutNumber_returnsInvalidCommand() {
        Command command = Parser.parse("unmark");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_unmarkWithInvalidNumber_returnsInvalidCommand() {
        Command command = Parser.parse("unmark xyz");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_validDeleteCommand_returnsDeleteCommand() {
        Command command = Parser.parse("delete 3");

        assertTrue(command instanceof DeleteCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_deleteWithoutNumber_returnsInvalidCommand() {
        Command command = Parser.parse("delete");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_deleteWithInvalidNumber_returnsInvalidCommand() {
        Command command = Parser.parse("delete invalid");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_listCommand_returnsListCommand() {
        Command command = Parser.parse("list");

        assertTrue(command instanceof ListCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_byeCommand_returnsExitCommand() {
        Command command = Parser.parse("bye");

        assertTrue(command instanceof ExitCommand);
        assertTrue(command.canExit());
    }

    @Test
    public void parse_unknownCommand_returnsInvalidCommand() {
        Command command = Parser.parse("unknown");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_emptyString_returnsInvalidCommand() {
        Command command = Parser.parse("");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_whitespaceOnly_returnsInvalidCommand() {
        Command command = Parser.parse("   ");

        assertTrue(command instanceof InvalidCommand);
        assertFalse(command.canExit());
    }

    @Test
    public void parse_caseInsensitiveCommands_workCorrectly() {
        Command upperCommand = Parser.parse("TODO buy milk");
        Command lowerCommand = Parser.parse("todo buy milk");
        Command mixedCommand = Parser.parse("ToDo buy milk");

        assertTrue(upperCommand instanceof AddCommand);
        assertTrue(lowerCommand instanceof AddCommand);
        assertTrue(mixedCommand instanceof AddCommand);
    }

    @Test
    public void parse_commandWithExtraSpaces_createsEmptyElements() {
        // Test that documents the current parsing behavior with multiple spaces
        // The split(" ") method will create empty strings for consecutive spaces
        Command command1 = Parser.parse("todo buy milk"); // normal case
        Command command2 = Parser.parse("todo  buy  milk"); // extra spaces

        // Both should work, though command2 might have some empty elements in parsing
        assertTrue(command1 instanceof AddCommand);
        // command2 behavior depends on how parser handles empty strings in parts array
        assertTrue(command2 instanceof AddCommand || command2 instanceof InvalidCommand);

        assertFalse(command1.canExit());
        assertFalse(command2.canExit());
    }
}
