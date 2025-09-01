package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.Task;
import jinjja.task.TaskList;
import jinjja.ui.Ui;

/**
 * Command to add a task.
 */
public class AddCommand extends Command {
    private Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) {
        tasks.addTask(task);
        ui.showTaskAdded(task, tasks.getSize());
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
