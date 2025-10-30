package org.example.metrics;

public class MetricsImpl implements Metrics {
    private long startTime;
    private long endTime;
    private long operationCount;

    public MetricsImpl() {
        this.operationCount = 0;
    }

    @Override
    public void start() {
        startTime = System.nanoTime();
    }

    @Override
    public void stop() {
        endTime = System.nanoTime();
    }

    @Override
    public void incrementOperationCount() {
        operationCount++;
    }

    @Override
    public long getElapsedTime() {
        return endTime - startTime;
    }

    @Override
    public long getOperationCount() {
        return operationCount;
    }
}