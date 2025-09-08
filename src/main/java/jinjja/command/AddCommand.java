package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.Task;
import jinjja.task.TaskList;
import jinjja.ui.Cli;

/**
 * Command to add a task to the task list. This command is used for adding Todo, Deadline, and Event tasks.
 */
public class AddCommand extends Command {
    private Task task;

    /**
     * Constructs an AddCommand with the specified task.
     *
     * @param task The task to be added to the task list
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Executes the add command by adding the task to the task list and displaying a confirmation message to the user.
     *
     * @param tasks The task list to add the task to
     * @param storage The storage system (not used in this command)
     * @param cli The user interface for displaying messages
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Cli cli) {
        tasks.addTask(task);
        cli.showTaskAdded(task, tasks.getSize());
    }

    /**
     * Returns whether the application should exit after this command.
     *
     * @return false, as adding a task does not terminate the application
     */
    @Override
    public boolean canExit() {
        return false;
    }
}
