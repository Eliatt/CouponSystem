package tasks;

import org.junit.Test;

import static org.junit.Assert.*;

public class CleaningTaskTest {

    @Test
    public void run() {
        CleaningTask cleaningTask = new CleaningTask();
        Thread cleaningTaskThread = new Thread(cleaningTask);
        cleaningTaskThread.start();
        try {
            cleaningTaskThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void stop() {
    }
}