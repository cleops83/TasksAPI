package com.celonis.challenge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ProjectGenerationTask {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private Date creationDate;

    @JsonIgnore
    private String storageLocation;

    @Enumerated(EnumType.STRING)
    private TaskProgress progress;

    @Enumerated(EnumType.STRING)
    private TaskType type;

    @Embedded
    private TaskCounter counter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public TaskProgress getProgress() {
        return progress;
    }

    public void setProgress(TaskProgress progress) {
        this.progress = progress;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public TaskCounter getCounter() {
        return counter;
    }

    public void setCounter(TaskCounter counter) {
        this.counter = counter;
    }
}
