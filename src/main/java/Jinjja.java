import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Jinjja is a personal assistant chatbot that helps users manage their tasks. It supports adding, listing, marking,
 * unmarking, and deleting tasks. Tasks can be of three types: Todo, Deadline, and Event. The bot interacts with users
 * via command-line interface.
 */
public class Jinjja {
    private static final String DATA_FILE_PATH = "ip/data/jinjja.txt";

    private static final DateTimeFormatter DATETIME_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
        boolean exitState = false;
        while (!exitState) {
            String command = userInput.nextLine();
            // List to store part of commands
            ArrayList<String> parts = new ArrayList<>();
            for (String part : command.split(" ")) {
                parts.add(part);
            }
            String action = parts.get(0);
            switch (action) {
            case "bye":
                exitState = true;
                break;
            case "list":
                printDivider();
                this.list.printTasks();
                printDivider();
                break;
            case "mark":
                try {
                    if (parts.size() > 1) {
                        int taskNum = Integer.parseInt(parts.get(1));
                        this.list.markTask(true, taskNum - 1);
                        printDivider();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + this.list.getTask(taskNum - 1));
                        printDivider();
                    } else {
                        throw new MissingParameterException("Task number is missing.");
                    }
                } catch (MissingParameterException e) {
                    printDivider();
                    System.out.println(e.getMessage());
                    printDivider();
                } catch (NumberFormatException e) {
                    printDivider();
                    System.out.println("Invalid task number format. " + e.getMessage());
                    printDivider();
                } catch (ArrayIndexOutOfBoundsException e) {
                    printDivider();
                    System.out.println(e.getMessage());
                    printDivider();
                }
                break;
            case "unmark":
                try {
                    if (parts.size() > 1) {
                        int taskNum = Integer.parseInt(parts.get(1));
                        this.list.markTask(false, taskNum - 1);
                        printDivider();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + this.list.getTask(taskNum - 1));
                        printDivider();
                    } else {
                        throw new MissingParameterException("Task number is missing.");
                    }
                } catch (MissingParameterException e) {
                    printDivider();
                    System.out.println(e.getMessage());
                    printDivider();
                } catch (NumberFormatException e) {
                    printDivider();
                    System.out.println("Invalid task number format. " + e.getMessage());
                    printDivider();
                }
                break;
            case "todo":
                try {
                    if (parts.size() > 1) {
                        // use stringbuilder so concat is faster for a longer
                        // task name
                        StringBuilder descBuilder = new StringBuilder();
                        for (int i = 1; i < parts.size(); i++) {
                            descBuilder.append(parts.get(i));
                            if (i < parts.size() - 1) {
                                descBuilder.append(" ");
                            }
                        }
                        Task newTask = new Todo(descBuilder.toString());
                        this.list.addTask(newTask);
                        printDivider();
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + newTask);
                        System.out.println("Now you have " + this.list.getSize() + " tasks in the list.");
                        printDivider();
                    } else {
                        throw new MissingParameterException("Task description is missing.");
                    }
                } catch (MissingParameterException e) {
                    printDivider();
                    System.out.println(e.getMessage());
                    printDivider();
                }
                break;
            case "deadline":
                try {
                    // Find the "/by" delimiter
                    int byIndex = -1;
                    for (int i = 1; i < parts.size(); i++) {
                        if (parts.get(i).equals("/by")) {
                            byIndex = i;
                            break;
                        }
                    }
                    if (byIndex != -1 && byIndex > 1 && byIndex < parts.size() - 1) {
                        // use stringbuilder so concat is faster for a longer
                        // task name
                        StringBuilder descBuilder = new StringBuilder();
                        for (int i = 1; i < byIndex; i++) {
                            descBuilder.append(parts.get(i));
                            if (i < byIndex - 1) {
                                descBuilder.append(" ");
                            }
                        }
                        StringBuilder byBuilder = new StringBuilder();
                        for (int i = byIndex + 1; i < parts.size(); i++) {
                            byBuilder.append(parts.get(i));
                            if (i < parts.size() - 1) {
                                byBuilder.append(" ");
                            }
                        }
                        String taskDescription = descBuilder.toString();
                        String byDate = byBuilder.toString();

                        // Parse the byDate string into a LocalDateTime object
                        LocalDateTime byDateTime;
                        try {
                            byDateTime = LocalDateTime.parse(byDate, DATETIME_FILE);
                        } catch (DateTimeParseException e) {
                            // prompt user again for properly formatted date
                            throw new MissingParameterException("Invalid date format. Please use yyyy-MM-dd HH:mm.");
                        }

                        Task newTask = new Deadline(taskDescription, byDateTime);
                        this.list.addTask(newTask);
                        printDivider();
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + newTask);
                        System.out.println("Now you have " + this.list.getSize() + " tasks in the list.");
                        printDivider();
                    } else {
                        throw new MissingParameterException("Deadline description or /by is missing.");
                    }
                } catch (MissingParameterException e) {
                    printDivider();
                    System.out.println(e.getMessage());
                    printDivider();
                }
                break;
            case "event":
                try {
                    // Find the "/from" and "/to" delimiter
                    int fromIndex = -1;
                    int toIndex = -1;
                    for (int i = 1; i < parts.size(); i++) {
                        if (parts.get(i).equals("/from")) {
                            fromIndex = i;
                        } else if (parts.get(i).equals("/to")) {
                            toIndex = i;
                        }
                    }
                    if (fromIndex != -1 && toIndex != -1 && fromIndex + 1 < toIndex) {
                        // use stringbuilder so concat is faster for a longer
                        // task name
                        StringBuilder descBuilder = new StringBuilder();
                        for (int i = 1; i < fromIndex; i++) {
                            descBuilder.append(parts.get(i));
                            if (i < fromIndex - 1) {
                                descBuilder.append(" ");
                            }
                        }
                        StringBuilder fromBuilder = new StringBuilder();
                        for (int i = fromIndex + 1; i < toIndex; i++) {
                            fromBuilder.append(parts.get(i));
                            if (i < toIndex - 1) {
                                fromBuilder.append(" ");
                            }
                        }
                        StringBuilder toBuilder = new StringBuilder();
                        for (int i = toIndex + 1; i < parts.size(); i++) {
                            toBuilder.append(parts.get(i));
                            if (i < parts.size() - 1) {
                                toBuilder.append(" ");
                            }
                        }
                        String taskDescription = descBuilder.toString();
                        String fromDateString = fromBuilder.toString();
                        String toDateString = toBuilder.toString();

                        // Parse the fromDate string into a LocalDateTime object
                        LocalDateTime fromDateTime;
                        try {
                            fromDateTime = LocalDateTime.parse(fromDateString, DATETIME_FILE);
                        } catch (DateTimeParseException e) {
                            // prompt user again for properly formatted date
                            throw new MissingParameterException(
                                    "Invalid date format for /from. Please use yyyy-MM-dd HH:mm.");
                        }

                        // Parse the toDate string into a LocalDateTime object
                        LocalDateTime toDateTime;
                        try {
                            toDateTime = LocalDateTime.parse(toDateString, DATETIME_FILE);
                        } catch (DateTimeParseException e) {
                            // prompt user again for properly formatted date
                            throw new MissingParameterException(
                                    "Invalid date format for /to. Please use yyyy-MM-dd HH:mm.");
                        }

                        Task newTask = new Event(taskDescription, fromDateTime, toDateTime);
                        this.list.addTask(newTask);
                        printDivider();
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + newTask);
                        System.out.println("Now you have " + this.list.getSize() + " tasks in the list.");
                        printDivider();
                    } else {
                        throw new MissingParameterException("Event description, /from, or /to is missing.");
                    }
                } catch (MissingParameterException e) {
                    printDivider();
                    System.out.println(e.getMessage());
                    printDivider();
                }
                break;
            case "delete":
                try {
                    if (parts.size() < 2) {
                        throw new MissingParameterException("Task number is missing.");
                    }
                    int taskNum = Integer.parseInt(parts.get(1));
                    Task removedTask = this.list.removeTask(taskNum - 1);
                    printDivider();
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + this.list.getSize() + " tasks in the list.");
                    printDivider();
                } catch (MissingParameterException e) {
                    printDivider();
                    System.out.println(e.getMessage());
                    printDivider();
                } catch (NumberFormatException e) {
                    printDivider();
                    System.out.println("Invalid task number format. " + e.getMessage());
                    printDivider();
                } catch (ArrayIndexOutOfBoundsException e) {
                    printDivider();
                    System.out.println(e.getMessage());
                    printDivider();
                }
                break;
            default:
                printDivider();
                System.out.println("I have no clue what you just said. Please use a command I know.");
                printDivider();
                break;
            }
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
