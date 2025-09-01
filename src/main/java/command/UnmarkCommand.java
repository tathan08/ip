/**
 * Command to unmark a task (mark as not done).
 */
class UnmarkCommand extends Command {
    private int taskNumber;
    
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }
    
    @Override
    public void execute(TaskList tasks, Storage storage) {
        try {
            tasks.markTask(false, taskNumber - 1);
            printDivider();
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("  " + tasks.getTask(taskNumber - 1));
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
