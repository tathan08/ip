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
                case "todo":
                    if (parts.size() > 1) {
                        String taskDescription = parts.get(1);
                        Task newTask = new Todo(taskDescription);
                        listInputs.add(newTask);
                        printDivider();
                        System.out.println("Got it. I've added this task:");
                        System.out.println(newTask);
                        System.out.println("Now you have " + listInputs.size() + " tasks in the list.");
                        printDivider();
                    }
                    break;
                case "deadline":
                    // Find the "/by" delimiter
                    int byIndex = -1;
                    for (int i = 1; i < parts.size(); i++) {
                        if (parts.get(i).equals("/by")) {
                            byIndex = i;
                            break;
                        }
                    }
                    if (byIndex != -1 && byIndex > 1 && byIndex < parts.size() - 1) {
                        // use stringbuilder so concat is faster for a longer task name
                        StringBuilder descBuilder = new StringBuilder();
                        for (int i = 1; i < byIndex; i++) {
                            descBuilder.append(parts.get(i));
                            if (i < byIndex - 1) descBuilder.append(" ");
                        }
                        StringBuilder byBuilder = new StringBuilder();
                        for (int i = byIndex + 1; i < parts.size(); i++) {
                            byBuilder.append(parts.get(i));
                            if (i < parts.size() - 1) byBuilder.append(" ");
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
