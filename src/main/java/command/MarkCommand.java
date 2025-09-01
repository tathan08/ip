/**
 * Command to mark a task as done.
 */
class MarkCommand extends Command {
    private int taskNumber;
    
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }
    
    @Override
    public void execute(TaskList tasks, Storage storage) {
        try {
            tasks.markTask(true, taskNumber - 1);
            printDivider();
            System.out.println("Nice! I've marked this task as done:");
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
