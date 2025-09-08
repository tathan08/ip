package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.Task;
import jinjja.task.TaskList;
import jinjja.ui.Cli;

/**
 * Command to find tasks that contain a specific keyword in their description. This command searches through all tasks
 * and displays those that match the search criteria.
 */
public class FindCommand extends Command {
    private String keyword;

    /**
     * Constructs a FindCommand with the specified keyword to search for.
     *
     * @param keyword The keyword to search for in task descriptions
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command by searching for tasks that contain the keyword and displaying the matching tasks to
     * the user. Is case-insensitive.
     *
     * @param tasks The task list to search through
     * @param storage The storage system (not used in this command)
     * @param cli The user interface for displaying the search results
     */
    @Override
    public void execute(TaskList tasks, Storage storage, Cli cli) {
        TaskList matchingTasks = new TaskList();

        // Search through all tasks for the keyword
        for (int i = 0; i < tasks.getSize(); i++) {
            Task task = tasks.getTask(i);
            if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.addTask(task);
            }
        }

        // Display the results
        cli.showFindResults(matchingTasks, keyword);
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
