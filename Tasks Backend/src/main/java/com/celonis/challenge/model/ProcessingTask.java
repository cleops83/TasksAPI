package com.celonis.challenge.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessingTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private int x;
    private int y;

    private boolean done = false;

    public ProcessingTask(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void run() {
        if (!done) {
            //logger.info("running " + Thread.currentThread().getName());
            //logger.info("x " + x);

            if (x == y) {
                done = true;
            } else {
                x++;
            }
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDone() {
        return done;
    }
}
