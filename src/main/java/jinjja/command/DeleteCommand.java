package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.Task;
import jinjja.task.TaskList;
import jinjja.ui.Cli;

/**
 * Command to delete a task.
 */
public class DeleteCommand extends Command {
    private int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Cli cli) {
        try {
            Task removedTask = tasks.removeTask(taskNumber - 1);
            cli.showTaskDeleted(removedTask, tasks.getSize());
        } catch (ArrayIndexOutOfBoundsException e) {
            cli.showMessageWithDivider(e.getMessage());
        }
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
