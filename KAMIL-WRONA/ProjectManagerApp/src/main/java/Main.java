public class Main {
    public static void main(String[] args) {
        // Tworzenie zadań z różnymi priorytetami
        Task task1 = new Task(1, "Implement feature X", "Implement new feature X", new HighPriority());
        Task task2 = new Task(2, "Fix bug Y", "Fix critical bug Y", new MediumPriority());
        Task task3 = new Task(3, "Write documentation", "Write project documentation", new LowPriority());

        // Wyświetlanie zadań
        System.out.println(task1);
        System.out.println(task2);
        System.out.println(task3);

        // Zmiana priorytetu zadania
        task1.setPriority(new LowPriority());
        System.out.println("After changing priority: " + task1);

        // Użycie fabryki priorytetów
        PriorityLevel priority = PriorityFactory.createPriority("medium");
        Task task4 = new Task(4, "Refactor code", "Refactor legacy code", priority);
        System.out.println(task4);
    }
}