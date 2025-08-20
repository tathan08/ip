import java.util.Scanner;

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
        // Loop to handle user input
        Scanner userInput = new Scanner(System.in);
        boolean exitState = false;
        while (!exitState) {
            String command = userInput.nextLine();
            switch(command) {
                case "bye":
                    exitState = true;
                    break;
                default:
                    printDivider();
                    System.out.println(command);
                    printDivider();
            }
        }
        userInput.close();
        printDivider();
        printFarewell();
        printDivider();
    }
}
