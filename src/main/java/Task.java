class Task {
    private boolean done;
    private String description;

    public Task(String description) {
        this.done = false;
        this.description = description;
    }

    public boolean markDone() {
        this.done = true;
        return true;
    }

    public boolean markNotDone() {
        this.done = false;
        return true;
    }

    @Override
    public String toString() {
        return "[" + (done ? "X" : " ") + "] " + description;
    }
}