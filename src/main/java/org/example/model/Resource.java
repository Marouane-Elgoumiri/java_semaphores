package org.example.model;

public class Resource {
    private final int id;
    private boolean inUse;
    private int usedByThread;

    public Resource(int id) {
        this.id = id;
        this.inUse = false;
        this.usedByThread = -1;
    }

    public int getId() {
        return id;
    }

    public boolean isInUse() {
        return inUse;
    }

    public int getUsedByThread() {
        return usedByThread;
    }

    public void setInUse(boolean inUse, int threadId) {
        this.inUse = inUse;
        this.usedByThread = threadId;
    }
}