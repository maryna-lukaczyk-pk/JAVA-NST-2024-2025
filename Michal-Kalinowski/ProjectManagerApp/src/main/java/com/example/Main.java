package com.example;

import com.example.model.Task;
import com.example.priority.HighPriority;
import com.example.priority.MediumPriority;
import com.example.priority.LowPriority;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Lista do przechowywania zadań
        List<Task> tasks = new ArrayList<>();

        // Tworzenie zadań i dodawanie ich do listy
        Task task1 = new Task();
        task1.setDescription("Finish the report");
        task1.setPriority(new HighPriority());
        tasks.add(task1);

        Task task2 = new Task();
        task2.setDescription("Prepare the presentation");
        task2.setPriority(new MediumPriority());
        tasks.add(task2);

        Task task3 = new Task();
        task3.setDescription("Organize the meeting");
        task3.setPriority(new LowPriority());
        tasks.add(task3);

        // Wyświetlenie wszystkich zadań
        for (Task task : tasks) {
            System.out.println("Task: " + task.getDescription() + " - Priority: " + task.getPriority().getPriority());
        }
    }
}