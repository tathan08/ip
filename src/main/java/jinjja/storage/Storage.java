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
import jinjja.task.Todo;

/**
 * Handles the storage and retrieval of tasks from a file.
 */
public class Storage {
    private static final DateTimeFormatter DATETIME_FILE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves the list of tasks to a file.
     *
     * @param tasks The list of tasks to save.
     * @throws IOException If an error occurs while saving tasks to the file.
     */
    public void saveTasksToFile(ArrayList<Task> tasks) throws IOException {
        // Create directory if it doesn't exist
        File dataDir = new File("ip/data");
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }

        // Write all current tasks into the file
        // Will override existing data if file already exists
        FileWriter writer = new FileWriter(this.filePath);
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            writer.write(task.toFileFormat() + "\n");
        }
        writer.close();
        System.out.println("Tasks saved to " + this.filePath);
    }

    /**
     * Loads tasks in-place from the specified DATA_FILE_PATH.
     *
     * @return tasks The list of tasks that was loaded.
     * @throws IOException If an error occurs while reading the file.
     */
    public ArrayList<Task> loadTasksFromFile() throws IOException {
        File dataFile = new File(this.filePath);
        ArrayList<Task> tasks = new ArrayList<>();
        if (!dataFile.exists()) {
            System.out.println("No existing task list found. Starting a new list.");
            return tasks; // No file to load from
        }

        Scanner fileScanner = new Scanner(dataFile);
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(" \\| ");
            String taskType = parts[0];
            boolean isDone = parts[1].equals("1");
            String description = parts[2];

            Task task = null;
            switch (taskType) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                String byDateString = parts[3];
                LocalDateTime byDate = LocalDateTime.parse(byDateString, DATETIME_FILE);
                task = new Deadline(description, byDate);
                break;
            case "E":
                String fromDateString = parts[3];
                String toDateString = parts[4];
                LocalDateTime fromDate = LocalDateTime.parse(fromDateString, DATETIME_FILE);
                LocalDateTime toDate = LocalDateTime.parse(toDateString, DATETIME_FILE);
                task = new Event(description, fromDate, toDate);
                break;
            default:
                System.out.println("Unknown task type in file: " + taskType);
                break; // task is still null
            }

            if (task != null) {
                task.setDone(isDone);
                tasks.add(task);
            }
        }
        fileScanner.close();
        System.out.println("Tasks loaded from " + this.filePath);
        return tasks;
    }
}
