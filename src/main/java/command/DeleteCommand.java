/**
 * Command to delete a task.
 */
class DeleteCommand extends Command {
    private int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) {
        try {
            Task removedTask = tasks.removeTask(taskNumber - 1);
            ui.showTaskDeleted(removedTask, tasks.getSize());
        } catch (ArrayIndexOutOfBoundsException e) {
            ui.showMessageWithDivider(e.getMessage());
        }
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
