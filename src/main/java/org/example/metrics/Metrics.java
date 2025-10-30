package org.example.metrics;

public interface Metrics {
    void start();
    void stop();
    void incrementOperationCount();
    long getElapsedTime();
    long getOperationCount();
}