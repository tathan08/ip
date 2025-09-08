package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Ui;

/**
 * Command to mark a task as done.
 */
public class MarkCommand extends Command {
    private int taskNumber;

    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public String execute(TaskList tasks, Storage storage, Ui ui) {
        try {
            tasks.markTask(true, taskNumber - 1);
            return ui.showTaskMarked(tasks.getTask(taskNumber - 1));
        } catch (ArrayIndexOutOfBoundsException e) {
            return ui.showMessageWithDivider(e.getMessage());
        }
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
