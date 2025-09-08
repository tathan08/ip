package jinjja.command;

import jinjja.storage.Storage;
import jinjja.task.TaskList;
import jinjja.ui.Cli;

/**
 * Represents an abstract command that can be executed. All specific command types inherit from this class.
 */
public abstract class Command {

    /**
     * Executes the command with the given dependencies.
     *
     * @param tasks The task list to operate on.
     * @param storage The storage for saving tasks.
     * @param cli The user interface for displaying messages.
     */
    public abstract void execute(TaskList tasks, Storage storage, Cli cli);

    /**
     * Returns whether the application should exit after executing this command.
     *
     * @return true if the application should exit, false otherwise.
     */
    public abstract boolean canExit();
}
