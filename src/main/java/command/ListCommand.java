/**
 * Command to list all tasks.
 */
class ListCommand extends Command {

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) {
        ui.showTaskList(tasks);
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
