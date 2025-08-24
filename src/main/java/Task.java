class Task {
    private boolean isDone;
    private String description;

    public Task(String description) {
        this.isDone = false;
        this.description = description;
    }

    public boolean setDone(boolean isDone) {
        this.isDone = isDone;
        return true; // Indicate that the status was changed
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public String getDescription() {
        return this.description;
    }

    /**
     * Converts the task to a format suitable for saving to a file.
     * 
     * @return A string representation of the task in file format.
     */
    public String toFileFormat() {
        return (this.isDone ? 1 : 0) + " | " + this.description;
    }

    @Override
    public String toString() {
        return "[" + (this.isDone ? "X" : " ") + "] " + this.description;
    }
}