package com.celonis.challenge.services;

import com.celonis.challenge.exceptions.NotFoundException;
import com.celonis.challenge.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.*;


@Service
public class CounterService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ProjectGenerationTaskRepository projectGenerationTaskRepository;

    private TaskScheduler taskScheduler;

    private HashMap<String, ScheduledFuture> scheduledFutureMap = new HashMap<>();

    private HashMap<String, ProcessingTask> processingTaskHashMap = new HashMap<>();

    public CounterService(ProjectGenerationTaskRepository projectGenerationTaskRepository,
                          TaskScheduler taskScheduler) {
        this.projectGenerationTaskRepository = projectGenerationTaskRepository;
        this.taskScheduler = taskScheduler;
    }

    public void execute(String taskId) {
        ProjectGenerationTask task = get(taskId);
        saveProgress(task, TaskProgress.EXECUTING);
        
        ProcessingTask processingTask = new ProcessingTask(task.getCounter().getX(), task.getCounter().getY());
        ScheduledFuture scheduledFuture = taskScheduler.scheduleAtFixedRate(processingTask, 1000L);
        scheduledFutureMap.put(taskId, scheduledFuture);
        processingTaskHashMap.put(taskId, processingTask);

    }

    public void cancel(String taskId) {
        ScheduledFuture scheduledFuture = scheduledFutureMap.get(taskId);
        ProjectGenerationTask task = get(taskId);

        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(true);
            saveProgress(task, TaskProgress.CANCELLED);
        }

    }

    public void monitor(String taskId) {
        ScheduledFuture scheduledFuture = scheduledFutureMap.get(taskId);
        ProcessingTask processingTask = processingTaskHashMap.get(taskId);
        ProjectGenerationTask task = get(taskId);

        if (scheduledFuture != null) {
            logger.info("processing task " + taskId + " -  " + + processingTask.getX() + " from " + processingTask.getY());

            if(!scheduledFuture.isCancelled() && processingTask.isDone()){
                saveProgress(task, TaskProgress.FINISHED);
            }
        }
    }

    private ProjectGenerationTask get(String taskId) {
        ProjectGenerationTask projectGenerationTask = projectGenerationTaskRepository.findOne(taskId);
        if (projectGenerationTask == null) {
            throw new NotFoundException();
        }
        return projectGenerationTask;
    }

    private void saveProgress(ProjectGenerationTask task, TaskProgress progress) {
        task.setProgress(progress);
        projectGenerationTaskRepository.save(task);
    }


    public void delete(String taskId) {
        ScheduledFuture scheduledFuture = scheduledFutureMap.get(taskId);
        if(scheduledFuture != null) {
            scheduledFutureMap.remove(taskId);
        }

        ProcessingTask processingTask = processingTaskHashMap.get(taskId);
        if(processingTask !=null) {
            processingTaskHashMap.remove(taskId);
        }
    }
}
