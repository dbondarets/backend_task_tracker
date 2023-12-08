package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.*;

import static model.Status.*;

public class TaskService {
    private Long nextId;
    private final Map<Long, Task> tasks;
    private final Map<Long, Subtask> subtasks;
    private final Map<Long, Epic> epics;


    public TaskService() {
        this.nextId = 1L;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public Task addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        return task;
    }

    public Task addEpic(Epic task) {
        task.setId(nextId++);
        epics.put(task.getId(), task);
        return task;
    }

    public Task addSubtask(Subtask task) {
        Epic epic = epics.get(task.getEpicId());
        if (epic == null) {
            System.out.printf("epic with id = %d not exist", task.getEpicId());
            return null;
        }
        Long id = nextId++;
        task.setId(id);
        subtasks.put(id, task);
        epic.add(id);
        return task;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Task getTask(Long id) {
        return tasks.get(id);
    }

    public Task getEpic(Long id) {
        return epics.get(id);
    }

    public Task getSubtask(Long id) {
        return subtasks.get(id);
    }

    public void updateTask(Task task) {
        if (task.getId() == null || tasks.get(task.getId()) == null) {
            System.out.println("Task not found");
            return;
        }
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic task) {
        Epic epic = epics.get(task.getId());
        if (epic == null) {
            System.out.println("Epic not found");
            return;
        }
        epic.setDescription(task.getDescription());
        epic.setName(task.getName());
    }

    public void updateSubtask(Subtask task) {
        Subtask currentTask =  subtasks.get(task.getId());
        if (currentTask == null) {
            System.out.println("Subtask not found");
            return;
        }
        Epic epic = epics.get(task.getEpicId());
        if (epic == null) {
            System.out.println("Incorrect epic id");
            return;
        }
        subtasks.put(task.getId(), task);
        if (task.getStatus() != currentTask.getStatus()) {
            updateEpicStatusIfNeed(epic);
        }
    }

    private void updateEpicStatusIfNeed(Epic epic) {
        int countNew = 0;
        int countDone = 0;
        for (Long id : epic.getSubtaskIds()) {
            switch (subtasks.get(id).getStatus()) {
                // если хотя бы один в статусе IN_PROGRESS, то задача эпику ставим такой же
                case IN_PROGRESS:
                    epic.setStatus(IN_PROGRESS);
                    break;
                case NEW:
                    countNew++;
                    break;
                case DONE:
                    countDone++;
                    break;
            }
            if (countNew == epic.getSubtaskIds().size()) {
                epic.setStatus(NEW);
            } else if (countDone == epic.getSubtaskIds().size()) {
                epic.setStatus(DONE);
            }
        }
    }

    public Task removeTask(Long id) {
        return tasks.remove(id);
    }

    public Task removeEpic(Long id) {
        Epic epic = epics.get(id);
        if (epic  == null) {
            System.out.printf("epic with id = %d not found", id);
            return null;
        }
        for (Long subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        return epics.remove(id);
    }

    public Task removeSubtask(Long id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.printf("subtask with id = %d not found", id);
            return null;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.remove(id);
        //могли удалить задачу, которая влияет на статус, поэтому вызываем метод, чтобы сменить статус, если потребуется
        updateEpicStatusIfNeed(epic);
        return subtasks.remove(id);
    }

    public Set<Subtask> getAllEpicSubtasks(Long epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            System.out.printf("epic with id == %d not found", epicId);
            return null;
        }
        Set<Subtask> epicSubtasks = new HashSet<>();
        for (Long id : epic.getSubtaskIds()) {
            epicSubtasks.add(subtasks.get(id));
        }
        return epicSubtasks;
    }


}
