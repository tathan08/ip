package jinjja.ui;

import jinjja.task.Task;
import jinjja.task.TaskList;

/**
 * Interface for different UI implementations (CLI and GUI).
 */
public interface Ui {
    String showGreeting();

    String showFarewell();

    String showDivider();

    String showMessage(String message);

    String showError(String errorMessage);

    String showMessageWithDivider(String message);

    String showTaskAdded(Task task, int totalTasks);

    String showTaskDeleted(Task task, int totalTasks);

    String showTaskMarked(Task task);

    String showTaskUnmarked(Task task);

    String showTaskList(TaskList tasks);

    String showFindResults(TaskList matchingTasks, String keyword);

    String readCommand();

    void close();
}
