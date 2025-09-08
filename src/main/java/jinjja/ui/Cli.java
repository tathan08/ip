package jinjja.ui;

import java.util.Scanner;

import jinjja.task.Task;
import jinjja.task.TaskList;

/**
 * Handles user interface operations including printing messages and reading user input.
 */
public class Cli implements Ui {
    private Scanner scanner;

    public Cli() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a greeting message to the user.
     *
     * @return An empty string.
     */
    @Override
    public String showGreeting() {
        System.out.println("Hello! I'm Jinjja");
        System.out.println("What can I do for you?");
        return "";
    }

    /**
     * Prints a farewell message to the user.
     *
     * @return An empty string.
     */
    @Override
    public String showFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
        return "";
    }

    /**
     * Prints a divider line. Used before and after bot replies.
     *
     * @return An empty string.
     */
    @Override
    public String showDivider() {
        System.out.println("____________________________________________________________");
        return "";
    }

    /**
     * Prints a message to the user.
     *
     * @param message The message to print.
     * @return An empty string.
     */
    @Override
    public String showMessage(String message) {
        System.out.println(message);
        return "";
    }

    /**
     * Prints an error message to the user.
     *
     * @param errorMessage The error message to print.
     * @return An empty string.
     */
    @Override
    public String showError(String errorMessage) {
        System.err.println(errorMessage);
        return "";
    }

    /**
     * Prints a message with dividers around it.
     *
     * @param message The message to print.
     * @return An empty string.
     */
    @Override
    public String showMessageWithDivider(String message) {
        showDivider();
        showMessage(message);
        showDivider();
        return "";
    }

    /**
     * Shows a task addition confirmation message.
     *
     * @param task The task that was added.
     * @param totalTasks The total number of tasks after adding.
     * @return An empty string.
     */
    @Override
    public String showTaskAdded(Task task, int totalTasks) {
        showDivider();
        showMessage("Got it. I've added this task:");
        showMessage("  " + task);
        showMessage("Now you have " + totalTasks + " tasks in the list.");
        showDivider();
        return "";
    }

    /**
     * Shows a task deletion confirmation message.
     *
     * @param task The task that was removed.
     * @param totalTasks The total number of tasks after removal.
     * @return An empty string.
     */
    @Override
    public String showTaskDeleted(Task task, int totalTasks) {
        showDivider();
        showMessage("Noted. I've removed this task:");
        showMessage("  " + task);
        showMessage("Now you have " + totalTasks + " tasks in the list.");
        showDivider();
        return "";
    }

    /**
     * Shows a task marked as done confirmation message.
     *
     * @param task The task that was marked.
     * @return An empty string.
     */
    @Override
    public String showTaskMarked(Task task) {
        showDivider();
        showMessage("Nice! I've marked this task as done:");
        showMessage("  " + task);
        showDivider();
        return "";
    }

    /**
     * Shows a task unmarked confirmation message.
     *
     * @param task The task that was unmarked.
     * @return An empty string.
     */
    @Override
    public String showTaskUnmarked(Task task) {
        showDivider();
        showMessage("OK, I've marked this task as not done yet:");
        showMessage("  " + task);
        showDivider();
        return "";
    }

    /**
     * Shows the list of tasks.
     *
     * @param tasks The task list to display.
     * @return An empty string.
     */
    @Override
    public String showTaskList(TaskList tasks) {
        showDivider();
        for (int i = 0; i < tasks.getSize(); i++) {
            showMessage((i + 1) + "." + tasks.getTask(i));
        }
        showDivider();
        return "";
    }

    /**
     * Shows the results of a find operation.
     *
     * @param matchingTasks The task list containing tasks that match the search criteria
     * @param keyword The keyword that was searched for
     * @return An empty string.
     */
    @Override
    public String showFindResults(TaskList matchingTasks, String keyword) {
        showDivider();
        if (matchingTasks.isEmpty()) {
            showMessage("No matching tasks found for keyword: " + keyword);
        } else {
            showMessage("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.getSize(); i++) {
                showMessage((i + 1) + "." + matchingTasks.getTask(i));
            }
        }
        showDivider();
        return "";
    }

    /**
     * Reads a line of input from the user.
     *
     * @return The user's input as a string.
     */
    @Override
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Closes the scanner to free resources.
     */
    @Override
    public void close() {
        scanner.close();
    }
}
