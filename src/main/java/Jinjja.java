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
            switch(command) {
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
