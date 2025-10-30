package org.example.graph.topo;

import org.example.graph.Graph;
import org.example.metrics.Metrics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {
    private List<Integer> order;
    private int[] inDegree;
    private final Metrics metrics;

    public TopologicalSort(Graph G, int s, Metrics metrics) {
        this.metrics = metrics;
        inDegree = new int[G.V()];
        metrics.start();
        for (int v = 0; v < G.V(); v++) {
            for (Graph.Edge edge : G.adj(v)) {
                inDegree[edge.to()]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        if (inDegree[s] == 0) {
            queue.add(s);
            metrics.incrementOperationCount();
        }

        for (int v = 0; v < G.V(); v++) {
            if (v != s && inDegree[v] == 0) {
                queue.add(v);
                metrics.incrementOperationCount();
            }
        }

        order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int v = queue.poll();
            metrics.incrementOperationCount();
            order.add(v);
            for (Graph.Edge edge : G.adj(v)) {
                int w = edge.to();
                inDegree[w]--;
                if (inDegree[w] == 0) {
                    queue.add(w);
                    metrics.incrementOperationCount();
                }
            }
        }
        metrics.stop();

        if (order.size() != G.V()) {
            order = null;
        }
    }

    public List<Integer> getOrder() {
        return order;
    }

    public boolean hasOrder() {
        return order != null;
    }
}