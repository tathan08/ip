import java.util.Scanner;
import java.util.ArrayList;

public class Jinjja {
    public static void printGreeting() {
        System.out.println("Hello! I'm Jinjja");
        System.out.println("What can I do for you?");
    }
    
    public static void printFarewell() {
        System.out.println("Bye. Hope to see you again soon!");
    }

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
            switch(action) {
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
                    if (parts.size() > 1) {
                        int taskNum = Integer.parseInt(parts.get(1));
                        printDivider();      
                        if (taskNum > 0 && taskNum <= listInputs.size()) {
                            listInputs.get(taskNum - 1).markDone();
                            System.out.println("Nice! I've marked this task as done:");
                            System.out.println("  " + listInputs.get(taskNum - 1));
                        } else {
                            System.out.println("Invalid task number.");
                        }
                        printDivider();
                    }
                    break;
                case "unmark":
                    if (parts.size() > 1) {
                        int taskNum = Integer.parseInt(parts.get(1));
                        printDivider();      
                        if (taskNum > 0 && taskNum <= listInputs.size()) {
                            listInputs.get(taskNum - 1).markNotDone();
                            System.out.println("OK, I've marked this task as not done yet:");
                            System.out.println("  " + listInputs.get(taskNum - 1));
                        } else {
                            System.out.println("Invalid task number.");
                        }
                        printDivider();
                    }
                    break;
                default:
                    Task newTask = new Task(command);
                    listInputs.add(newTask);
                    printDivider();
                    System.out.println("added: " + command);
                    printDivider();
            }
        }
        userInput.close();
        printDivider();
        printFarewell();
        printDivider();
    }
}
