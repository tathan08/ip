/**
 * Command representing an invalid or erroneous command.
 */
class InvalidCommand extends Command {
    private String errorMessage;

    public InvalidCommand(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void execute(TaskList tasks, Storage storage, Ui ui) {
        ui.showMessageWithDivider(errorMessage);
    }

    @Override
    public boolean canExit() {
        return false;
    }
}
