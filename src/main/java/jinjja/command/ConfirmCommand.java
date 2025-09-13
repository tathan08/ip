package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.Task;
import jinjja.task.TaskList;
import jinjja.task.Tentative;
import jinjja.ui.Ui;

/**
 * Command to confirm a tentative slot for a tentative event.
 */
public class ConfirmCommand extends Command {
    private int taskNumber;
    private int slotNumber;

    /**
     * Constructs a ConfirmCommand with the specified task and slot numbers.
     *
     * @param taskNumber The 1-indexed task number
     * @param slotNumber The 1-indexed slot number to confirm
     */
    public ConfirmCommand(int taskNumber, int slotNumber) {
        assert taskNumber > 0 : "Task number should be positive";
        assert slotNumber > 0 : "Slot number should be positive";
        this.taskNumber = taskNumber;
        this.slotNumber = slotNumber;
    }

    @Override
    public String execute(TaskList tasks, Storage storage, Ui ui) {
        try {
            if (taskNumber > tasks.getSize() || taskNumber <= 0) {
                return ui.showError("Invalid task number. Please provide a valid task number.");
            }

            Task task = tasks.getTask(taskNumber - 1);

            if (!(task instanceof Tentative)) {
                return ui.showError("Task " + taskNumber + " is not a tentative event.");
            }

            Tentative tentative = (Tentative) task;

            if (tentative.confirmSlot(slotNumber)) {
                storage.saveTasksToFile(tasks.getTasks());
                return ui.showMessage("Confirmed slot " + slotNumber + " for: " + tentative.getDescription());
            } else {
                return ui.showError("Invalid slot number. Please provide a valid slot number (1-"
                        + tentative.getSlotCount() + ").");
            }
        } catch (Exception e) {
            return ui.showError("Error confirming tentative event: " + e.getMessage());
        }
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
