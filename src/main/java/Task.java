class Task {
    private boolean done;
    private String description;

    public Task(String description) {
        this.done = false;
        this.description = description;
    }

    @Override
    public String toString() {
        return "[" + (done ? "X" : " ") + "] " + description;
    }
}