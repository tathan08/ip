package jinjja;

import java.io.IOException;

import jinjja.command.Command;
import jinjja.parser.Parser;
import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Cli;
import jinjja.ui.Gui;

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
    private Cli cli;
    private Gui gui;

    /**
     * Constructor for the Jinjja chatbot.
     * Separates UI, Storage, and TaskList components.
     * Creates new TaskList file if there are errors loading the current one in
     * Storage.
     *
     * @param isGui true if the UI is graphical, false for command-line interface
     */
    public Jinjja(boolean isGui) {
        if (isGui) {
            this.gui = new Gui();
        } else {
            this.cli = new Cli();
        }
    }

    /**
     * Runs the main logic of the Jinjja chatbot.
     */
    public void run() {
        // Greet user
        this.cli.showDivider();
        this.cli.showGreeting();

        // Initialize storage and load existing tasks
        this.storage = new Storage(DATA_FILE_PATH);
        try {
            this.list = new TaskList(this.storage.loadTasksFromFile());
        } catch (IOException e) {
            this.cli.showError("Error loading tasks from file: " + e.getMessage());
            this.list = new TaskList();
        }
        this.cli.showDivider();

        // Loop to handle user input
        boolean canExit = false;
        while (!canExit) {
            String fullCommand = this.cli.readCommand();
            Command c = Parser.parse(fullCommand);
            c.execute(this.list, this.storage, this.cli);
            canExit = c.canExit();
        }
        this.cli.close();
        this.cli.showDivider();
        try {
            storage.saveTasksToFile(this.list.getTasks());
        } catch (IOException e) {
            this.cli.showError("Error saving tasks to file: " + e.getMessage());
        }
        this.cli.showFarewell();
        this.cli.showDivider();
    }

    public static void main(String[] args) {
        // Set isGui to false for CLI, true for GUI
        new Jinjja(false).run();
    }

    /**
     * Parses and executes a command for the GUI, returning the output string.
     */
    public String getResponse(String input) {
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
        if (this.gui == null) {
            this.gui = new Gui();
        }
        Command c = Parser.parse(input);
        // return c.execute(this.list, this.storage, this.cli);
        return ""; // Placeholder return statement
    }
}
