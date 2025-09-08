package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Cli;

/**
 * Command to display all tasks in the task list. This command shows the user all their current tasks with their
 * completion status.
 */
public class ListCommand extends Command {

    /**
     * Executes the list command by displaying all tasks to the user.
     *
     * @param tasks The task list to display
     * @param storage The storage system (not used in this command)
     * @param cli The user interface for displaying the task list
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Cli cli) {
        cli.showTaskList(tasks);
    }

    /**
     * Returns whether the application should exit after this command.
     *
     * @return false, as listing tasks does not terminate the application
     */
    @Override
    public boolean canExit() {
        return false;
    }
}
