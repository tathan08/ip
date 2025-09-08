package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Ui;

/**
 * Command to unmark a task (mark as not done).
 */
public class UnmarkCommand extends Command {
    private int taskNumber;

    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public String execute(TaskList tasks, Storage storage, Ui ui) {
        try {
            tasks.markTask(false, taskNumber - 1);
            return ui.showTaskUnmarked(tasks.getTask(taskNumber - 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            return ui.showMessageWithDivider(e.getMessage());
        }
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
