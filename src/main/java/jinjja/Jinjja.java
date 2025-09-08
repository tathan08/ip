package jinjja;

import java.io.IOException;

import jinjja.command.Command;
import jinjja.parser.Parser;
import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Ui;

/**
 * Jinjja is a personal assistant chatbot that helps users manage their tasks. It supports adding, listing, marking,
 * unmarking, and deleting tasks. Tasks can be of three types: Todo, Deadline, and Event. The bot interacts with users
 * via command-line interface.
 */
public class Jinjja {
    private static final String DATA_FILE_PATH = "ip/data/jinjja.txt";

    private Storage storage;
    private TaskList list;
    private Ui ui;

    /**
     * Constructor for the Jinjja chatbot.
     * Separates UI, Storage, and TaskList components.
     * Creates new TaskList file if there are errors loading the current one in Storage.
     *
     * @param isGui true if the UI is graphical, false for command-line interface
     */
    public Jinjja(boolean isGui) {
        if (!isGui) {
            this.ui = new Ui();
        }
    }

    /**
     * Runs the main logic of the Jinjja chatbot.
     */
    public void run() {
        // Greet user
        this.ui.showDivider();
        this.ui.showGreeting();

        // Initialize storage and load existing tasks
        this.storage = new Storage(DATA_FILE_PATH);
        try {
            this.list = new TaskList(this.storage.loadTasksFromFile());
        } catch (IOException e) {
            this.ui.showError("Error loading tasks from file: " + e.getMessage());
            this.list = new TaskList();
        }
        this.ui.showDivider();

        // Loop to handle user input
        boolean canExit = false;
        while (!canExit) {
            String fullCommand = this.ui.readCommand();
            Command c = Parser.parse(fullCommand);
            c.execute(this.list, this.storage, this.ui);
            canExit = c.canExit();
        }
        this.ui.close();
        this.ui.showDivider();
        try {
            storage.saveTasksToFile(this.list.getTasks());
        } catch (IOException e) {
            this.ui.showError("Error saving tasks to file: " + e.getMessage());
        }
        this.ui.showFarewell();
        this.ui.showDivider();
    }

    public static void main(String[] args) {
        // Set isGui to false for CLI, true for GUI
        new Jinjja(false).run();
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String input) {
        return "Jinjja heard: " + input;
    }
}
