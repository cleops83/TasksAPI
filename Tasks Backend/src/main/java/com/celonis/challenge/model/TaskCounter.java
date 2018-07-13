package com.celonis.challenge.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * Counter class for task
 * x = start value
 * y = max value
 */
@Embeddable
public class TaskCounter {

    @NotNull
    private int x;

    @NotNull
    private int y;

    public TaskCounter(){}

    public TaskCounter(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
