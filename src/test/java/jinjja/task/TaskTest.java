package jinjja.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Test class for Task hierarchy functionality. Tests the behavior of different task types: Todo, Deadline, and Event.
 */
public class TaskTest {
    private Todo todoTask;
    private Deadline deadlineTask;
    private Event eventTask;
    private LocalDateTime testDeadline;
    private LocalDateTime testEventStart;
    private LocalDateTime testEventEnd;

    @BeforeEach
    public void setUp() {
        todoTask = new Todo("Buy groceries");
        testDeadline = LocalDateTime.of(2025, 12, 31, 23, 59);
        deadlineTask = new Deadline("Submit assignment", testDeadline);
        testEventStart = LocalDateTime.of(2025, 9, 15, 10, 0);
        testEventEnd = LocalDateTime.of(2025, 9, 15, 11, 30);
        eventTask = new Event("Team meeting", testEventStart, testEventEnd);
    }

    @Test
    public void taskCreation_defaultsToNotDone() {
        assertFalse(todoTask.getIsDone());
        assertFalse(deadlineTask.getIsDone());
        assertFalse(eventTask.getIsDone());
    }

    @Test
    public void setDone_changesTaskStatus() {
        // Test marking as done
        assertTrue(todoTask.setDone(true));
        assertTrue(todoTask.getIsDone());

        assertTrue(deadlineTask.setDone(true));
        assertTrue(deadlineTask.getIsDone());

        assertTrue(eventTask.setDone(true));
        assertTrue(eventTask.getIsDone());

        // Test marking as not done
        assertTrue(todoTask.setDone(false));
        assertFalse(todoTask.getIsDone());

        assertTrue(deadlineTask.setDone(false));
        assertFalse(deadlineTask.getIsDone());

        assertTrue(eventTask.setDone(false));
        assertFalse(eventTask.getIsDone());
    }

    @Test
    public void getDescription_returnsCorrectDescription() {
        assertEquals("Buy groceries", todoTask.getDescription());
        assertEquals("Submit assignment", deadlineTask.getDescription());
        assertEquals("Team meeting", eventTask.getDescription());
    }

    @Test
    public void toString_formatsCorrectly() {
        // Test undone tasks
        assertEquals("[T][ ] Buy groceries", todoTask.toString());
        assertEquals("[D][ ] Submit assignment (by: Dec 31 2025, 11:59pm)", deadlineTask.toString());
        assertEquals("[E][ ] Team meeting (from: Sep 15 2025, 10:00am to: Sep 15 2025, 11:30am)", eventTask.toString());

        // Test done tasks
        todoTask.setDone(true);
        deadlineTask.setDone(true);
        eventTask.setDone(true);

        assertEquals("[T][X] Buy groceries", todoTask.toString());
        assertEquals("[D][X] Submit assignment (by: Dec 31 2025, 11:59pm)", deadlineTask.toString());
        assertEquals("[E][X] Team meeting (from: Sep 15 2025, 10:00am to: Sep 15 2025, 11:30am)", eventTask.toString());
    }

    @Test
    public void toFileFormat_formatsCorrectly() {
        // Test undone tasks
        assertEquals("T | 0 | Buy groceries", todoTask.toFileFormat());
        assertEquals("D | 0 | Submit assignment | 2025-12-31 23:59", deadlineTask.toFileFormat());
        assertEquals("E | 0 | Team meeting | 2025-09-15 10:00 | 2025-09-15 11:30", eventTask.toFileFormat());

        // Test done tasks
        todoTask.setDone(true);
        deadlineTask.setDone(true);
        eventTask.setDone(true);

        assertEquals("T | 1 | Buy groceries", todoTask.toFileFormat());
        assertEquals("D | 1 | Submit assignment | 2025-12-31 23:59", deadlineTask.toFileFormat());
        assertEquals("E | 1 | Team meeting | 2025-09-15 10:00 | 2025-09-15 11:30", eventTask.toFileFormat());
    }

    @Test
    public void todoTask_handlesSpecialCharacters() {
        Todo specialTodo = new Todo("Buy milk | bread & eggs");
        assertEquals("Buy milk | bread & eggs", specialTodo.getDescription());
        assertEquals("[T][ ] Buy milk | bread & eggs", specialTodo.toString());
        assertEquals("T | 0 | Buy milk | bread & eggs", specialTodo.toFileFormat());
    }

    @Test
    public void deadlineTask_handlesEdgeDates() {
        // Test with different date/time formats
        LocalDateTime newYear = LocalDateTime.of(2026, 1, 1, 0, 0);
        Deadline newYearDeadline = new Deadline("New Year resolution", newYear);

        assertEquals("New Year resolution", newYearDeadline.getDescription());
        assertTrue(newYearDeadline.toString().contains("Jan 01 2026, 12:00am"));
        assertEquals("D | 0 | New Year resolution | 2026-01-01 00:00", newYearDeadline.toFileFormat());
    }

    @Test
    public void eventTask_handlesLongDuration() {
        // Test event spanning multiple days
        LocalDateTime start = LocalDateTime.of(2025, 12, 24, 18, 0);
        LocalDateTime end = LocalDateTime.of(2025, 12, 26, 10, 0);
        Event longEvent = new Event("Christmas celebration", start, end);

        assertEquals("Christmas celebration", longEvent.getDescription());
        assertTrue(longEvent.toString().contains("Dec 24 2025"));
        assertTrue(longEvent.toString().contains("Dec 26 2025"));
        assertEquals("E | 0 | Christmas celebration | 2025-12-24 18:00 | 2025-12-26 10:00", longEvent.toFileFormat());
    }
}
