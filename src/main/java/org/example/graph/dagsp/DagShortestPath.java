package org.example.graph.dagsp;

import org.example.graph.Graph;
import org.example.graph.topo.TopologicalSort;
import org.example.metrics.Metrics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DagShortestPath {
    private final int[] distTo;
    private final int[] edgeTo;
    private final Graph G;
    private final int s;
    private final Metrics metrics;

    public DagShortestPath(Graph G, int s, Metrics metrics) {
        this.G = G;
        this.s = s;
        this.metrics = metrics;
        distTo = new int[G.V()];
        edgeTo = new int[G.V()];
        Arrays.fill(edgeTo, -1);
        Arrays.fill(distTo, Integer.MAX_VALUE);
        distTo[s] = 0;

        TopologicalSort topological = new TopologicalSort(G, s, new org.example.metrics.MetricsImpl());
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException("Graph is not a DAG");
        }

        metrics.start();
        for (int v : topological.getOrder()) {
            for (Graph.Edge edge : G.adj(v)) {
                relax(edge);
            }
        }
        metrics.stop();
    }

    private void relax(Graph.Edge e) {
        metrics.incrementOperationCount();
        int v = e.from(), w = e.to();
        if (distTo[v] != Integer.MAX_VALUE && distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = v;
        }
    }

    public int distTo(int v) {
        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        return distTo[v] < Integer.MAX_VALUE;
    }

    public List<Integer> pathTo(int v) {
        if (!hasPathTo(v)) {
            return null;
        }
        List<Integer> path = new java.util.ArrayList<>();
        for (int x = v; x != -1; x = edgeTo[x]) {
            path.add(x);
            if (x == s) break;
        }
        Collections.reverse(path);
        return path;
    }
}