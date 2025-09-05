package jinjja.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for TaskList functionality. Tests task management operations including adding, removing, marking, and
 * querying tasks.
 */
public class TaskListTest {
    private TaskList taskList;
    private Task todoTask;
    private Task deadlineTask;
    private Task eventTask;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        todoTask = new Todo("Buy groceries");
        deadlineTask = new Deadline("Submit assignment", LocalDateTime.of(2025, 12, 31, 23, 59));
        eventTask = new Event("Team meeting", LocalDateTime.of(2025, 9, 15, 10, 0),
                LocalDateTime.of(2025, 9, 15, 11, 30));
    }

    @Test
    public void addTask_singleTask_success() {
        taskList.addTask(todoTask);
        assertEquals(1, taskList.getSize());
        assertEquals(todoTask, taskList.getTask(0));
        assertFalse(taskList.isEmpty());
    }

    @Test
    public void addTask_multipleTasks_correctOrder() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.addTask(eventTask);

        assertEquals(3, taskList.getSize());
        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(deadlineTask, taskList.getTask(1));
        assertEquals(eventTask, taskList.getTask(2));
    }

    @Test
    public void removeTask_validIndex_success() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.addTask(eventTask);

        Task removedTask = taskList.removeTask(1);

        assertEquals(deadlineTask, removedTask);
        assertEquals(2, taskList.getSize());
        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(eventTask, taskList.getTask(1));
    }

    @Test
    public void removeTask_invalidIndex_throwsException() {
        taskList.addTask(todoTask);

        // Test negative index
        assertThrows(ArrayIndexOutOfBoundsException.class, ()-> {
            taskList.removeTask(-1);
        });

        // Test index too large
        assertThrows(ArrayIndexOutOfBoundsException.class, ()-> {
            taskList.removeTask(1);
        });

        // Test index on empty list
        TaskList emptyList = new TaskList();
        assertThrows(ArrayIndexOutOfBoundsException.class, ()-> {
            emptyList.removeTask(0);
        });
    }

    @Test
    public void removeTask_lastTask_emptyList() {
        taskList.addTask(todoTask);
        Task removedTask = taskList.removeTask(0);

        assertEquals(todoTask, removedTask);
        assertEquals(0, taskList.getSize());
        assertTrue(taskList.isEmpty());
    }

    @Test
    public void markTask_validIndex_success() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);

        assertFalse(todoTask.getIsDone());
        assertFalse(deadlineTask.getIsDone());

        taskList.markTask(true, 0);
        assertTrue(todoTask.getIsDone());
        assertFalse(deadlineTask.getIsDone());

        taskList.markTask(true, 1);
        assertTrue(deadlineTask.getIsDone());

        // Test unmarking
        taskList.markTask(false, 0);
        assertFalse(todoTask.getIsDone());
        assertTrue(deadlineTask.getIsDone());
    }

    @Test
    public void markTask_emptyList_throwsException() {
        assertThrows(ArrayIndexOutOfBoundsException.class, ()-> {
            taskList.markTask(true, 0);
        });
    }

    @Test
    public void markTask_invalidIndex_throwsNumberFormatException() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);

        // Test negative index
        assertThrows(NumberFormatException.class, ()-> {
            taskList.markTask(true, -1);
        });

        // Test index too large
        assertThrows(NumberFormatException.class, ()-> {
            taskList.markTask(true, 2);
        });
    }

    @Test
    public void getTask_validIndex_returnsCorrectTask() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.addTask(eventTask);

        assertEquals(todoTask, taskList.getTask(0));
        assertEquals(deadlineTask, taskList.getTask(1));
        assertEquals(eventTask, taskList.getTask(2));
    }

    @Test
    public void getTask_invalidIndex_throwsException() {
        taskList.addTask(todoTask);

        assertThrows(ArrayIndexOutOfBoundsException.class, ()-> {
            taskList.getTask(-1);
        });

        assertThrows(ArrayIndexOutOfBoundsException.class, ()-> {
            taskList.getTask(1);
        });

        TaskList emptyList = new TaskList();
        assertThrows(ArrayIndexOutOfBoundsException.class, ()-> {
            emptyList.getTask(0);
        });
    }

    @Test
    public void constructor_withExistingTasks_correctInitialization() {
        ArrayList<Task> existingTasks = new ArrayList<>();
        existingTasks.add(todoTask);
        existingTasks.add(deadlineTask);

        TaskList taskListWithTasks = new TaskList(existingTasks);

        assertEquals(2, taskListWithTasks.getSize());
        assertEquals(todoTask, taskListWithTasks.getTask(0));
        assertEquals(deadlineTask, taskListWithTasks.getTask(1));
        assertFalse(taskListWithTasks.isEmpty());
    }

    @Test
    public void getTasks_returnsCorrectList() {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);

        ArrayList<Task> tasks = taskList.getTasks();

        assertEquals(2, tasks.size());
        assertEquals(todoTask, tasks.get(0));
        assertEquals(deadlineTask, tasks.get(1));
    }
}
