/**
 * Command to add a task.
 */
class AddCommand extends Command {
    private Task task;
    
    public AddCommand(Task task) {
        this.task = task;
    }
    
    @Override
    public void execute(TaskList tasks, Storage storage) {
        tasks.addTask(task);
        printDivider();
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + tasks.getSize() + " tasks in the list.");
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
