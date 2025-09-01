import java.io.IOException;
import java.util.Scanner;

/**
 * Jinjja is a personal assistant chatbot that helps users manage their tasks. It supports adding, listing, marking,
 * unmarking, and deleting tasks. Tasks can be of three types: Todo, Deadline, and Event. The bot interacts with users
 * via command-line interface.
 */
public class Jinjja {
    private static final String DATA_FILE_PATH = "ip/data/jinjja.txt";

    private Storage storage;
    private TaskList list;

    public Jinjja() {
        this.storage = new Storage(DATA_FILE_PATH);
        try {
            this.list = new TaskList(this.storage.loadTasksFromFile());
        } catch (IOException e) {
            System.err.println("Error loading tasks from file: " + e.getMessage());
            this.list = new TaskList();
        }
    }

    /**
     * Prints a greeting message to the user.
     */
    public static void printGreeting() {
        System.out.println("Hello! I'm Jinjja");
        System.out.println("What can I do for you?");
    }

    /**
     * Prints a farewell message to the user.
     */
    public static void printFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints a divider line. Used before and after bot replies.
     */
    public static void printDivider() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Runs the main logic of the Jinjja chatbot.
     */
    public void run() {
        // Greet user
        printDivider();
        printGreeting();
        printDivider();

        // Loop to handle user input
        Scanner userInput = new Scanner(System.in);
        boolean canExit = false;
        while (!canExit) {
            String fullCommand = userInput.nextLine();
            Command c = Parser.parse(fullCommand);
            c.execute(this.list, this.storage);
            canExit = c.canExit();
        }
        userInput.close();
        printDivider();
        try {
            storage.saveTasksToFile(this.list.getTasks());
        } catch (IOException e) {
            System.err.println("Error saving tasks to file: " + e.getMessage());
        }
        printFarewell();
        printDivider();
    }

    public static void main(String[] args) {
        new Jinjja().run();
    }
}
