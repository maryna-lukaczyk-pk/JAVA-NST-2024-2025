public class PriorityFactory {
    public static PriorityLevel createPriority(String priorityType) {
        switch (priorityType.toLowerCase()) {
            case "high":
                return new HighPriority();
            case "medium":
                return new MediumPriority();
            case "low":
                return new LowPriority();
            default:
                throw new IllegalArgumentException("Unknown priority type: " + priorityType);
        }
    }
}