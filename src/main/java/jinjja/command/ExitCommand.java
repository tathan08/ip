package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Ui;

/**
 * Command to exit the application.
 */
public class ExitCommand extends Command {

    @Override
    public String execute(TaskList tasks, Storage storage, Ui ui) {
        // No output needed for exit command
        return ui.showFarewell();
    }

    @Override
    public boolean canExit() {
        return true;
    }
}
