import java.util.ArrayList;
import java.util.Scanner;

/**
 * Jinjja is a personal assistant chatbot that helps users manage their tasks.
 * It supports adding, listing, marking, unmarking, and deleting tasks. Tasks
 * can be of three types: Todo, Deadline, and Event. The bot interacts with
 * users via command-line interface.
 */
public class Jinjja {

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

    public static void main(String[] args) {
        // Greet user
        printDivider();
        printGreeting();
        printDivider();
        // List to store user input
        ArrayList<Task> listInputs = new ArrayList<>();
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
                for (int i = 0; i < listInputs.size(); i++) {
                    System.out.println((i + 1) + "." + listInputs.get(i));
                }
                printDivider();
                break;
            case "mark":
                try {
                    if (parts.size() > 1) {
                        int taskNum = Integer.parseInt(parts.get(1));
                        if (taskNum > 0 && taskNum <= listInputs.size()) {
                            listInputs.get(taskNum - 1).markDone();
                            printDivider();
                            System.out.println("Nice! I've marked this task as done:");
                            System.out.println("  " + listInputs.get(taskNum - 1));
                        } else if (listInputs.size() == 0) {
                            throw new ArrayIndexOutOfBoundsException("No tasks available.");
                        } else {
                            throw new NumberFormatException("Task number is out of range. \n"
                                                            + "Please enter a number between 1 and " + listInputs.size()
                                                            + ".");
                        }
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
                        if (taskNum > 0 && taskNum <= listInputs.size()) {
                            listInputs.get(taskNum - 1).markNotDone();
                            printDivider();
                            System.out.println("OK, I've marked this task as not done yet:");
                            System.out.println("  " + listInputs.get(taskNum - 1));
                        } else {
                            throw new NumberFormatException("Task number is out of range. \n"
                                                            + "Please enter a number between 1 and " + listInputs.size()
                                                            + ".");
                        }
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
                        listInputs.add(newTask);
                        printDivider();
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + newTask);
                        System.out.println("Now you have " + listInputs.size() + " tasks in the list.");
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
                        Task newTask = new Deadline(taskDescription, byDate);
                        listInputs.add(newTask);
                        printDivider();
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + newTask);
                        System.out.println("Now you have " + listInputs.size() + " tasks in the list.");
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
                        String fromDate = fromBuilder.toString();
                        String toDate = toBuilder.toString();
                        Task newTask = new Event(taskDescription, fromDate, toDate);
                        listInputs.add(newTask);
                        printDivider();
                        System.out.println("Got it. I've added this task:");
                        System.out.println("  " + newTask);
                        System.out.println("Now you have " + listInputs.size() + " tasks in the list.");
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
                    int taskNumber = Integer.parseInt(parts.get(1));
                    if (taskNumber < 1 || taskNumber > listInputs.size()) {
                        throw new ArrayIndexOutOfBoundsException("Task number is out of range.");
                    }
                    Task removedTask = listInputs.remove(taskNumber - 1);
                    printDivider();
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask);
                    System.out.println("Now you have " + listInputs.size() + " tasks in the list.");
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
        printFarewell();
        printDivider();
    }
}
