package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.Task;
import jinjja.task.TaskList;
import jinjja.ui.Ui;

/**
 * Command to delete a task.
 */
public class DeleteCommand extends Command {
    private int taskNumber;

    public DeleteCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public String execute(TaskList tasks, Storage storage, Ui ui) {
        try {
            Task removedTask = tasks.removeTask(taskNumber - 1);
            return ui.showTaskDeleted(removedTask, tasks.getSize());
        } catch (ArrayIndexOutOfBoundsException e) {
            return ui.showMessageWithDivider(e.getMessage());
        }
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
