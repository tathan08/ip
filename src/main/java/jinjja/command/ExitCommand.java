package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Cli;

/**
 * Command to exit the application.
 */
public class ExitCommand extends Command {

    @Override
    public void execute(TaskList tasks, Storage storage, Cli cli) {
        // No action needed for exit command
    }

    @Override
    public boolean canExit() {
        return true;
    }
}
