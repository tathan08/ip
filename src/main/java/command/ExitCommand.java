/**
 * Command to exit the application.
 */
class ExitCommand extends Command {
    
    @Override
    public void execute(TaskList tasks, Storage storage) {
        // No action needed for exit command
    }
    
    @Override
    public boolean canExit() {
        return true;
    }
}
