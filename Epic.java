package model;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {
    private Set<Long> subtaskIds;

    public Epic(String name, String description) {
        super(name, description);
        subtaskIds = new HashSet<>();
    }


    public boolean add(Long id) {
        return subtaskIds.add(id);
    }

    public Set<Long> getSubtaskIds() {
        return new HashSet<>(subtaskIds);
    }

    public boolean remove(Long id) {
        return subtaskIds.remove(id);
    }

    public void removeAll() {
        subtaskIds.clear();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + "'" +
                ", subtaskIDs=" + subtaskIds +
                ", status='" + getStatus() + "'" +
                '}';
    }
}
