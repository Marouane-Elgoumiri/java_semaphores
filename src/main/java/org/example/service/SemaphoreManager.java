package org.example.service;

import org.example.model.Resource;
import org.example.model.WorkerThread;
import java.util.List;
import java.util.concurrent.*;

public class SemaphoreManager {
    private final Semaphore semaphore;
    private final List<Resource> resources;
    private final ExecutorService executorService;
    private volatile boolean isRunning = false;

    public SemaphoreManager(int permits, List<Resource> resources) {
        this.semaphore = new Semaphore(permits);
        this.resources = resources;
        this.executorService = Executors.newFixedThreadPool(10);
    }

    public void startThread(WorkerThread thread, Runnable onUpdate) {
        executorService.submit(() -> {
            try {
                thread.startExecution();
                while (!Thread.currentThread().isInterrupted() && isRunning) {
                    thread.setStatus("Waiting");
                    onUpdate.run();

                    if (semaphore.tryAcquire(1, TimeUnit.SECONDS)) {
                        Resource availableResource = acquireResource(thread.getId());
                        if (availableResource != null) {
                            thread.setStatus("Using Resource " + availableResource.getId());
                            onUpdate.run();
                            Thread.sleep(2000); // Simulate work
                            releaseResource(availableResource);
                            semaphore.release();
                            thread.setStatus("Released");
                            onUpdate.run();
                            Thread.sleep(1000);
                        }
                    }
                }
                thread.stopExecution();
                onUpdate.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                thread.stopExecution();
                onUpdate.run();
            }
        });
    }

    private synchronized Resource acquireResource(int threadId) {
        for (Resource resource : resources) {
            if (!resource.isInUse()) {
                resource.setInUse(true, threadId);
                return resource;
            }
        }
        return null;
    }

    private synchronized void releaseResource(Resource resource) {
        resource.setInUse(false, -1);
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    public void shutdown() {
        isRunning = false;
        executorService.shutdownNow();
    }
}