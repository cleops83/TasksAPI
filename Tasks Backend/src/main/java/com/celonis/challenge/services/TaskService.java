package com.celonis.challenge.services;

import com.celonis.challenge.exceptions.InternalException;
import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.model.*;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {

    private final ProjectGenerationTaskRepository projectGenerationTaskRepository;

    private final FileService fileService;

    private final CounterService counterService;


    public TaskService(ProjectGenerationTaskRepository projectGenerationTaskRepository,
                       FileService fileService, CounterService counterService) {
        this.projectGenerationTaskRepository = projectGenerationTaskRepository;
        this.fileService = fileService;
        this.counterService = counterService;
    }


    public List<ProjectGenerationTask> listTasks() {
        List<ProjectGenerationTask> tasks = projectGenerationTaskRepository.findAll();
        for (ProjectGenerationTask task:tasks) this.counterService.monitor(task.getId());
        Collections.sort(tasks,Comparator.comparing(ProjectGenerationTask::getId));
        return tasks;
    }

    public ProjectGenerationTask createTask(ProjectGenerationTask projectGenerationTask) {
        projectGenerationTask.setId(null);
        projectGenerationTask.setCreationDate(new Date());
        projectGenerationTask.setType(TaskType.COUNTER);
        projectGenerationTask.setCounter(new TaskCounter(1, 30));
        return projectGenerationTaskRepository.save(projectGenerationTask);
    }

    public ProjectGenerationTask getTask(String taskId) {
        return get(taskId);
    }

    public ProjectGenerationTask update(String taskId, ProjectGenerationTask projectGenerationTask) {
        ProjectGenerationTask existing = get(taskId);
        existing.setCreationDate(projectGenerationTask.getCreationDate());
        existing.setName(projectGenerationTask.getName());
        return projectGenerationTaskRepository.save(existing);
    }

    public void delete(String taskId) {
        ProjectGenerationTask task = get(taskId);
        if (task.getCounter() != null) {
            counterService.delete(taskId);
        }

        projectGenerationTaskRepository.delete(taskId);

    }

    public void executeTask(String taskId) {
        /*URL url = Thread.currentThread().getContextClassLoader().getResource("challenge.zip");
        if (url == null) {
            throw new InternalException("Zip file not found");
        }*/
        try {
            ProjectGenerationTask task = get(taskId);
            if (task.getType() != null && task.getType().equals(TaskType.COUNTER)
                    && task.getCounter() != null) {
                counterService.execute(taskId);
            }

            //fileService.storeResult(taskId, url);
        } catch (Exception e) {
            throw new InternalException(e);
        }
    }

    public void cancelTask(String taskId) {
        counterService.cancel(taskId);

    }

    private ProjectGenerationTask get(String taskId) {
        ProjectGenerationTask projectGenerationTask = projectGenerationTaskRepository.findOne(taskId);
        if (projectGenerationTask == null) {
            throw new NotFoundException();
        }
        return projectGenerationTask;
    }

    public ProjectGenerationTask monitorTask(String taskId) {
        counterService.monitor(taskId);
        return get(taskId);
    }
}
