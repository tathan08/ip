package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Ui;

/**
 * Command to mark a task as done.
 */
public class MarkCommand extends Command {
    private int taskNumber;

    /**
     * Constructs a MarkCommand with the specified task number.
     *
     * @param taskNumber The 1-indexed task number to mark as done
     */
    public MarkCommand(int taskNumber) {
        assert taskNumber > 0 : "Task number should be positive (1-indexed)";
        this.taskNumber = taskNumber;
    }

    @Override
    public String execute(TaskList tasks, Storage storage, Ui ui) {
        assert tasks != null : "Task list cannot be null";
        assert ui != null : "UI cannot be null";
        assert this.taskNumber > 0 : "Task number should be positive";

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
