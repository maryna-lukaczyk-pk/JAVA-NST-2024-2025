public class Task {
    private int id;
    private String title;
    private String description;
    private PriorityLevel priority;

    // Konstruktor
    public Task(int id, String title, String description, PriorityLevel priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }


    // Gettery i settery
    public PriorityLevel getPriority() {
        return priority;
    }

    public void setPriority(PriorityLevel priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority.getPriority() +
                '}';
    }
}