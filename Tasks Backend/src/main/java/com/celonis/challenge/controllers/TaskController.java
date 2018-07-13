package com.celonis.challenge.controllers;

import com.celonis.challenge.model.ProjectGenerationTask;
import com.celonis.challenge.services.FileService;
import com.celonis.challenge.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    private final FileService fileService;

    public TaskController(TaskService taskService,
                          FileService fileService) {
        this.taskService = taskService;
        this.fileService = fileService;
    }

    @GetMapping("/")
    @CrossOrigin(origins = "*")
    public List<ProjectGenerationTask> listTasks() {
        return taskService.listTasks();
    }

    @PostMapping("/")
    @CrossOrigin(origins = "*")
    public ProjectGenerationTask createTask(@RequestBody @Valid ProjectGenerationTask projectGenerationTask) {
        return taskService.createTask(projectGenerationTask);
    }

    @GetMapping("/{taskId}")
    @CrossOrigin(origins = "*")
    public ProjectGenerationTask getTask(@PathVariable String taskId) {
        return taskService.getTask(taskId);
    }

    @PutMapping("/{taskId}")
    @CrossOrigin(origins = "*")
    public ProjectGenerationTask updateTask(@PathVariable String taskId,
                                            @RequestBody @Valid ProjectGenerationTask projectGenerationTask) {
        return taskService.update(taskId, projectGenerationTask);
    }

    @DeleteMapping("/{taskId}")
    @CrossOrigin(origins = "*")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable String taskId) {
        taskService.delete(taskId);
    }

    @PostMapping("/{taskId}/execute")
    @CrossOrigin(origins = "*")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void executeTask(@PathVariable String taskId) {
        taskService.executeTask(taskId);
    }

    @PostMapping("/{taskId}/cancel")
    @CrossOrigin(origins = "*")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelTask(@PathVariable String taskId) {
        taskService.cancelTask(taskId);
    }

    @GetMapping("/{taskId}/monitor")
    @CrossOrigin(origins = "*")
    public ProjectGenerationTask monitorTask(@PathVariable String taskId) {
        return taskService.monitorTask(taskId);
    }

    @GetMapping("/{taskId}/result")
    @CrossOrigin(origins = "*")
    public ResponseEntity<FileSystemResource> getResult(@PathVariable String taskId) {
        return fileService.getTaskResult(taskId);
    }

}
