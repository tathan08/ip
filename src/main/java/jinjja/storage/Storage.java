package jinjja.storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import jinjja.task.Deadline;
import jinjja.task.Event;
import jinjja.task.Task;
import jinjja.task.Tentative;
import jinjja.task.Todo;

/**
 * Handles the storage and retrieval of tasks from a file. This class provides functionality to save tasks to a file and
 * load them back, maintaining data persistence across application sessions.
 */
public class Storage {
    private static final DateTimeFormatter DATETIME_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String filePath;

    /**
     * Constructs a Storage object with the specified file path.
     *
     * @param filePath The path to the file where tasks will be stored
     */
    public Storage(String filePath) {
        assert filePath != null : "File path cannot be null";
        assert !filePath.trim().isEmpty() : "File path cannot be empty";
        this.filePath = filePath;
    }

    /**
     * Saves the list of tasks to a file in a specific format. Creates the data directory if it doesn't exist.
     * Overwrites existing file content.
     *
     * @param tasks The list of tasks to save
     * @throws IOException If an error occurs while saving tasks to the file
     */
    public void saveTasksToFile(ArrayList<Task> tasks) throws IOException {
        assert tasks != null : "Task list cannot be null when saving";
        assert this.filePath != null : "File path should be initialized";

        // Create directory if it doesn't exist
        File dataDir = new File("ip/data");
        if (!dataDir.exists()) {
            boolean dirCreated = dataDir.mkdirs();
            assert dirCreated || dataDir.exists() : "Data directory should be created or already exist";
        }

        // Write all current tasks into the file
        // Will override existing data if file already exists
        FileWriter writer = new FileWriter(this.filePath);
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            assert task != null : "Individual task in list should not be null";
            String fileFormat = task.toFileFormat();
            assert fileFormat != null && !fileFormat.trim().isEmpty() : "Task file format should not be null or empty";
            writer.write(fileFormat + "\n");
        }
        writer.close();
        System.out.println("Tasks saved to " + this.filePath);
    }

    /**
     * Loads tasks from the specified file path. If the file doesn't exist, returns an empty list. Parses each line to
     * recreate Todo, Deadline, and Event tasks.
     *
     * @return The list of tasks loaded from the file
     * @throws IOException If an error occurs while reading the file
     */
    public ArrayList<Task> loadTasksFromFile() throws IOException {
        File dataFile = new File(this.filePath);
        ArrayList<Task> tasks = new ArrayList<>();

        if (!dataFile.exists()) {
            System.out.println("No existing task list found. Starting a new list.");
            return tasks;
        }

        Scanner fileScanner = new Scanner(dataFile);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            Task task = parseTaskFromLine(line);
            if (task != null) {
                tasks.add(task);
            }
        }
        fileScanner.close();
        System.out.println("Tasks loaded from " + this.filePath);
        return tasks;
    }

    /**
     * Parses a single line from the file and creates the corresponding Task object.
     *
     * @param line The line from the file to parse
     * @return The Task object created from the line, or null if parsing fails
     */
    private Task parseTaskFromLine(String line) {
        assert line != null : "File line should not be null";
        String[] parts = line.split(" \\| ");
        assert parts.length >= 3 : "File format should have at least 3 parts";

        String taskType = parts[0];
        assert taskType != null && !taskType.trim().isEmpty() : "Task type should not be null or empty";
        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        assert description != null : "Task description should not be null";

        Task task = createTaskByType(taskType, description, parts);
        if (task != null) {
            task.setDone(isDone);
        }
        return task;
    }

    /**
     * Creates a Task object based on the task type and parsed parts.
     *
     * @param taskType The type of task (T, D, E, TE)
     * @param description The task description
     * @param parts The complete parsed parts from the file line
     * @return The created Task object, or null if task type is unknown
     */
    private Task createTaskByType(String taskType, String description, String[] parts) {
        switch (taskType) {
        case "T":
            return new Todo(description);
        case "D":
            return createDeadlineTask(description, parts);
        case "E":
            return createEventTask(description, parts);
        case "TE":
            return createTentativeTask(description, parts);
        default:
            System.out.println("Unknown task type in file: " + taskType);
            return null;
        }
    }

    /**
     * Creates a Deadline task from the parsed parts.
     *
     * @param description The task description
     * @param parts The parsed parts from the file line
     * @return The created Deadline task
     */
    private Task createDeadlineTask(String description, String[] parts) {
        assert parts.length >= 4 : "Deadline task should have at least 4 parts";
        String byDateString = parts[3];
        assert byDateString != null && !byDateString.trim().isEmpty() : "Deadline date should not be null or empty";
        LocalDateTime byDate = LocalDateTime.parse(byDateString, DATETIME_FILE);
        return new Deadline(description, byDate);
    }

    /**
     * Creates an Event task from the parsed parts.
     *
     * @param description The task description
     * @param parts The parsed parts from the file line
     * @return The created Event task
     */
    private Task createEventTask(String description, String[] parts) {
        assert parts.length >= 5 : "Event task should have at least 5 parts";
        String fromDateString = parts[3];
        String toDateString = parts[4];
        assert fromDateString != null
                && !fromDateString.trim().isEmpty() : "Event from date should not be null or empty";
        assert toDateString != null && !toDateString.trim().isEmpty() : "Event to date should not be null or empty";

        LocalDateTime fromDate = LocalDateTime.parse(fromDateString, DATETIME_FILE);
        LocalDateTime toDate = LocalDateTime.parse(toDateString, DATETIME_FILE);
        assert !fromDate.isAfter(toDate) : "Event start time should not be after end time";
        return new Event(description, fromDate, toDate);
    }

    /**
     * Creates a Tentative task from the parsed parts.
     *
     * @param description The task description
     * @param parts The parsed parts from the file line
     * @return The created Tentative task
     */
    private Task createTentativeTask(String description, String[] parts) {
        assert parts.length >= 5 : "Tentative task should have at least 5 parts";
        Tentative tentative = new Tentative(description);

        parseConfirmedSlot(tentative, parts[3]);
        parseTentativeSlots(tentative, parts);

        return tentative;
    }

    /**
     * Parses and adds the confirmed slot to a Tentative task if it exists.
     *
     * @param tentative The Tentative task to add the confirmed slot to
     * @param confirmedSlotString The confirmed slot string from the file
     */
    private void parseConfirmedSlot(Tentative tentative, String confirmedSlotString) {
        if (!confirmedSlotString.isEmpty()) {
            String[] confirmedSlotParts = confirmedSlotString.split("\\|");
            if (confirmedSlotParts.length == 2) {
                LocalDateTime confirmedFrom = LocalDateTime.parse(confirmedSlotParts[0], DATETIME_FILE);
                LocalDateTime confirmedTo = LocalDateTime.parse(confirmedSlotParts[1], DATETIME_FILE);
                tentative.addTentativeSlot(confirmedFrom, confirmedTo);
                tentative.confirmSlot(1);
            }
        }
    }

    /**
     * Parses and adds tentative slots to a Tentative task.
     *
     * @param tentative The Tentative task to add slots to
     * @param parts The parsed parts from the file line
     */
    private void parseTentativeSlots(Tentative tentative, String[] parts) {
        int slotCount = Integer.parseInt(parts[4]);
        assert parts.length >= 5 + slotCount : "Tentative should have all slots data";

        for (int i = 0; i < slotCount; i++) {
            String slotString = parts[5 + i];
            String[] slotParts = slotString.split("\\|");
            if (slotParts.length == 2) {
                LocalDateTime slotFrom = LocalDateTime.parse(slotParts[0], DATETIME_FILE);
                LocalDateTime slotTo = LocalDateTime.parse(slotParts[1], DATETIME_FILE);
                tentative.addTentativeSlot(slotFrom, slotTo);
            }
        }
    }
}
