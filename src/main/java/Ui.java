import java.util.Scanner;

/**
 * Handles user interface operations including printing messages and reading user input.
 */
class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints a greeting message to the user.
     */
    public void showGreeting() {
        System.out.println("Hello! I'm Jinjja");
        System.out.println("What can I do for you?");
    }

    /**
     * Prints a farewell message to the user.
     */
    public void showFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints a divider line. Used before and after bot replies.
     */
    public void showDivider() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Prints a message to the user.
     *
     * @param message The message to print.
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prints an error message to the user.
     *
     * @param errorMessage The error message to print.
     */
    public void showError(String errorMessage) {
        System.err.println(errorMessage);
    }

    /**
     * Prints a message with dividers around it.
     *
     * @param message The message to print.
     */
    public void showMessageWithDivider(String message) {
        showDivider();
        showMessage(message);
        showDivider();
    }

    /**
     * Shows a task addition confirmation message.
     *
     * @param task The task that was added.
     * @param totalTasks The total number of tasks after adding.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        showDivider();
        showMessage("Got it. I've added this task:");
        showMessage("  " + task);
        showMessage("Now you have " + totalTasks + " tasks in the list.");
        showDivider();
    }

    /**
     * Shows a task deletion confirmation message.
     *
     * @param task The task that was removed.
     * @param totalTasks The total number of tasks after removal.
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        showDivider();
        showMessage("Noted. I've removed this task:");
        showMessage("  " + task);
        showMessage("Now you have " + totalTasks + " tasks in the list.");
        showDivider();
    }

    /**
     * Shows a task marked as done confirmation message.
     *
     * @param task The task that was marked.
     */
    public void showTaskMarked(Task task) {
        showDivider();
        showMessage("Nice! I've marked this task as done:");
        showMessage("  " + task);
        showDivider();
    }

    /**
     * Shows a task unmarked confirmation message.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        showDivider();
        showMessage("OK, I've marked this task as not done yet:");
        showMessage("  " + task);
        showDivider();
    }

    /**
     * Shows the list of tasks.
     *
     * @param tasks The task list to display.
     */
    public void showTaskList(TaskList tasks) {
        showDivider();
        for (int i = 0; i < tasks.getSize(); i++) {
            showMessage((i + 1) + "." + tasks.getTask(i));
        }
        showDivider();
    }

    /**
     * Reads a line of input from the user.
     *
     * @return The user's input as a string.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Closes the scanner to free resources.
     */
    public void close() {
        scanner.close();
    }
}
