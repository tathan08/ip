package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Cli;

/**
 * Command representing an invalid or erroneous command.
 */
public class InvalidCommand extends Command {
    private String errorMessage;

    public InvalidCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Cli cli) {
        cli.showMessageWithDivider(errorMessage);
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
