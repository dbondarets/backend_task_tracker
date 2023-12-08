package model;

public class Subtask extends Task {
    private final Long epicId;

    public Subtask(String name, String description, Long epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Long getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", epicID=" + epicId +
                ", status='" + getStatus() +
                "'}";
    }
}
