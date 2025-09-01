/**
 * Command to delete a task.
 */
class DeleteCommand extends Command {
    private int taskNumber;
    
    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }
    
    @Override
    public void execute(TaskList tasks, Storage storage) {
        try {
            Task removedTask = tasks.removeTask(taskNumber - 1);
            printDivider();
            System.out.println("Noted. I've removed this task:");
            System.out.println("  " + removedTask);
            System.out.println("Now you have " + tasks.getSize() + " tasks in the list.");
            printDivider();
        } catch (ArrayIndexOutOfBoundsException e) {
            printDivider();
            System.out.println(e.getMessage());
            printDivider();
        }
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
