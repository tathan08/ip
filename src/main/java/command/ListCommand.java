/**
 * Command to list all tasks.
 */
class ListCommand extends Command {
    
    @Override
    public void execute(TaskList tasks, Storage storage) {
        printDivider();
        tasks.printTasks();
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
