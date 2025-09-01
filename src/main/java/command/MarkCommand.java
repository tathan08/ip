/**
 * Command to mark a task as done.
 */
class MarkCommand extends Command {
    private int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) {
        try {
            tasks.markTask(true, taskNumber - 1);
            ui.showTaskMarked(tasks.getTask(taskNumber - 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            ui.showMessageWithDivider(e.getMessage());
        }
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
