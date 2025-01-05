import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    // Define a counter for task IDs
    private static long ID = 1;
    // Define a JSON object to store the tasks
    static JSONObject taskObject;


    // Define constant for the tasks file location
    private static final String TASKS_FILE_PATH = "src/main/resources/daily-task.json";


    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the Task Manager!");
        System.out.println("Enter a command (add, update, delete, description, status, exit):");

        Scanner scanner = new Scanner(System.in);
        String inputs;
        while (true) {
            System.out.print("Enter a command: ");
            inputs = scanner.nextLine();
            //add
            if (inputs.contains("add")) {
                String task = inputs.substring(4);
                taskObject = new JSONObject();
                taskObject.put("id", ID++);
                taskObject.put("task", task);
                saveTaskToJson(taskObject);

                System.out.println("Task added: " + task + " with ID: " + (ID -1));
            }

            //update
            if (inputs.contains("update")) {
                String[] parts = inputs.substring(7).split(" ", 2);
                if (parts[0].length() > 2) {
                    System.out.println("Invalid update command. Use format: update <id> <new task>");
                    continue;
                }
                long searchId;
                try {
                    searchId = Long.parseLong(parts[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID. Please enter a number.");
                    continue;
                }
                String updatedTask = parts[1];

                File tasksFile = new File(TASKS_FILE_PATH);

                if (tasksFile.exists()) {
                    String content = new String(Files.readAllBytes(tasksFile.toPath()));
                    JSONArray tasksArray = new JSONArray(content);
                    boolean found = false;

                    for (int i = 0; i < tasksArray.length(); i++) {
                        JSONObject taskObj = tasksArray.getJSONObject(i);
                        if (taskObj.getLong("id") == searchId) {
                            taskObj.put("task", updatedTask);
                            found = true;
                            break;
                        }
                    }

                    if (found) {
                        Files.write(tasksFile.toPath(), tasksArray.toString(2).getBytes());
                        System.out.println("Task updated successfully.");
                    } else {
                        System.out.println("Task with ID " + searchId + " not found.");
                    }
                } else {
                    System.out.println("No tasks found.");
                }
            }

            //delete
            //description
            //Status

            if (inputs.equals("exit")) {
                System.out.println("Goodbye!");
                break;
            }
        }
    }
        // Method to save task to JSON file
        private static void saveTaskToJson(JSONObject taskObject) {
            try {
                File tasksFile = new File(TASKS_FILE_PATH);
                JSONArray tasksArray;

                // Create parent directories if they don't exist
                tasksFile.getParentFile()
                        .mkdirs();

                // If file exists, read existing tasks
                if (tasksFile.exists()) {
                    String content = new String(Files.readAllBytes(tasksFile.toPath()));
                    tasksArray = new JSONArray(content);
                } else {
                    tasksArray = new JSONArray();
                }

                // Add new task
                tasksArray.put(taskObject);

                // Write updated JSON to file
                Files.write(tasksFile.toPath(), tasksArray.toString(2)
                        .getBytes());
            } catch (IOException e) {
                System.out.println("Error saving task: " + e.getMessage());
            }

        }
    }
