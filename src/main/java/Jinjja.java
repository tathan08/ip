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
        printDivider();
        printGreeting();
        printDivider();
        printFarewell();
        printDivider();
    }
}
