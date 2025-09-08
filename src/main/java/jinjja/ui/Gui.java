package jinjja.ui;

import jinjja.task.Task;
import jinjja.task.TaskList;

/**
 * Handles GUI interface operations for Jinjja chatbot, returning messages for display in the JavaFX window.
 */
public class Gui implements Ui {
    public Gui() {
        // No scanner needed for GUI
    }

    /**
     * Returns a greeting message to the user.
     *
     * @return The greeting message.
     */
    public String showGreeting() {
        return "Hello! I'm Jinjja\nWhat can I do for you?";
    }

    /**
     * Returns a farewell message to the user.
     *
     * @return The farewell message.
     */
    public String showFarewell() {
        return "Bye. Hope to see you again soon!";
    }

    // showDivider(), no divider needed for GUI

    /**
     * Returns a message to the user.
     *
     * @param message The message to return.
     * @return The message string.
     */
    public String showMessage(String message) {
        return message;
    }

    /**
     * Returns an error message to the user.
     *
     * @param errorMessage The error message to return.
     * @return The error message string.
     */
    public String showError(String errorMessage) {
        return "Error: " + errorMessage;
    }

    // showMessageWithDivider(), no divider needed for GUI

    /**
     * Returns a task addition confirmation message.
     *
     * @param task The task that was added.
     * @param totalTasks The total number of tasks after adding.
     * @return The confirmation message.
     */
    public String showTaskAdded(Task task, int totalTasks) {
        return "Got it. I've added this task:\n  " + task + "\nNow you have " + totalTasks + " tasks in the list.";
    }

    /**
     * Returns a task deletion confirmation message.
     *
     * @param task The task that was removed.
     * @param totalTasks The total number of tasks after removal.
     * @return The confirmation message.
     */
    public String showTaskDeleted(Task task, int totalTasks) {
        return "Noted. I've removed this task:\n  " + task + "\nNow you have " + totalTasks + " tasks in the list.";
    }

    /**
     * Returns a task marked as done confirmation message.
     *
     * @param task The task that was marked.
     * @return The confirmation message.
     */
    public String showTaskMarked(Task task) {
        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Returns a task unmarked confirmation message.
     *
     * @param task The task that was unmarked.
     * @return The confirmation message.
     */
    public String showTaskUnmarked(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Returns the list of tasks as a string.
     *
     * @param tasks The task list to display.
     * @return The formatted task list string.
     */
    public String showTaskList(TaskList tasks) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tasks.getSize(); i++) {
            sb.append((i + 1)).append(".").append(tasks.getTask(i)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Returns the results of a find operation as a string.
     *
     * @param matchingTasks The task list containing tasks that match the search criteria.
     * @param keyword The keyword that was searched for.
     * @return The formatted find results string.
     */
    public String showFindResults(TaskList matchingTasks, String keyword) {
        StringBuilder sb = new StringBuilder();
        if (matchingTasks.isEmpty()) {
            sb.append("No matching tasks found for keyword: ").append(keyword);
        } else {
            sb.append("Here are the matching tasks in your list:\n");
            for (int i = 0; i < matchingTasks.getSize(); i++) {
                sb.append((i + 1)).append(".").append(matchingTasks.getTask(i)).append("\n");
            }
        }
        return sb.toString().trim();
    }
}
