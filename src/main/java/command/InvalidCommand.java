/**
 * Command representing an invalid or erroneous command.
 */
class InvalidCommand extends Command {
    private String errorMessage;
    
    public InvalidCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    @Override
    public void execute(TaskList tasks, Storage storage) {
        printDivider();
        System.out.println(errorMessage);
        printDivider();
    }
    
    @Override
    public boolean canExit() {
        return false;
    }
    
    /**
     * Prints a divider line.
     */
    private void printDivider() {
        System.out.println("____________________________________________________________");
    }
}
