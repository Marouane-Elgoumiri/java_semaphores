package org.example.model;

public class WorkerThread {
    private final int id;
    private String status;
    private boolean running;
    private long startTime;
    private long totalExecutionTime;
    private volatile boolean isExecuting;

    public WorkerThread(int id) {
        this.id = id;
        this.status = "Ready";
        this.running = false;
        this.totalExecutionTime = 0;
        this.isExecuting = false;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isExecuting() {
        return isExecuting;
    }

    public void startExecution() {
        if (!isExecuting) {
            isExecuting = true;
            startTime = System.currentTimeMillis();
            running = true;
        }
    }

    public void stopExecution() {
        if (isExecuting) {
            isExecuting = false;
            totalExecutionTime += System.currentTimeMillis() - startTime;
            running = false;
            setStatus("Stopped");
        }
    }

    public long getExecutionTime() {
        if (isExecuting) {
            return totalExecutionTime + (System.currentTimeMillis() - startTime);
        }
        return totalExecutionTime;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
