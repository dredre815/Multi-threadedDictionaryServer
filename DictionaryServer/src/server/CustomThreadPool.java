/**
 * CustomThreadPool.java
 * Author: Marshall Zhang
 * Student ID: 1160040
 * This file defines a custom thread pool class that manages a pool of worker threads.
 * Key features include:
 * - Allow tasks to be executed in multiple client connections
 * - Use a blocking queue to store tasks
 * - Use worker threads to execute tasks
 */
package server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class CustomThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final Thread[] workerThreads;
    private volatile boolean isShutdownInitiated = false;

    public CustomThreadPool(int numberOfThreads) {
        taskQueue = new LinkedBlockingQueue<>();
        workerThreads = new Thread[numberOfThreads];

        // Initialize and start worker threads
        for (int i = 0; i < numberOfThreads; i++) {
            workerThreads[i] = new Worker("Custom Pool Thread " + (i + 1), taskQueue);
            workerThreads[i].start();
        }
    }

    public void execute(Runnable task) throws Exception {
        if (!isShutdownInitiated) {
            taskQueue.put(task);
        } else {
            throw new Exception("Thread pool is shutdown, cannot execute new tasks.");
        }
    }

    public void shutdown() {
        isShutdownInitiated = true;
        for (Thread workerThread : workerThreads) {
            workerThread.interrupt();
        }
    }

    private static class Worker extends Thread {
        private BlockingQueue<Runnable> taskQueue;

        public Worker(String name, BlockingQueue<Runnable> taskQueue) {
            super(name);
            this.taskQueue = taskQueue;
        }

        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Runnable task = taskQueue.take();
                    task.run();
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " has been interrupted.");
            }
        }
    }
}

