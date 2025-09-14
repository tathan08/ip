package jinjja;

import java.io.IOException;

import jinjja.command.Command;
import jinjja.parser.Parser;
import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Cli;
import jinjja.ui.Gui;
import jinjja.ui.Ui;

/**
 * Jinjja is a personal assistant chatbot that helps users manage their tasks.
 * It supports adding, listing, marking, unmarking, and deleting tasks.
 * Tasks can be of three types: Todo, Deadline, and Event.
 * The bot can interact with users via GUI or command-line interface.
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
        if (isGui) {
            this.ui = new Gui();
        } else {
            this.ui = new Cli();
        }
    }

    /**
     * Runs the main logic of the Jinjja chatbot for CLI.
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

    public String getGreeting() {
        if (this.ui == null) {
            this.ui = new Cli();
        }
        return this.ui.showGreeting();
    }

    /**
     * Parses and executes a command for the GUI, returning the output string.
     */
    public String getResponse(String input) {
        assert input != null : "Input cannot be null";

        if (this.storage == null) {
            this.storage = new Storage(DATA_FILE_PATH);
        }
        if (this.list == null) {
            try {
                this.list = new TaskList(this.storage.loadTasksFromFile());
            } catch (IOException e) {
                this.list = new TaskList();
            }
        }

        assert this.storage != null : "Storage should be initialized";
        assert this.list != null : "Task list should be initialized";
        assert this.ui != null : "UI should be initialized";

        Command c = Parser.parse(input);
        assert c != null : "Parser should return a non-null command";
        return c.execute(this.list, this.storage, this.ui);
    }
}
