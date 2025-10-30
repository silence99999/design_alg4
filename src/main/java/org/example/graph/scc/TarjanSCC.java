package org.example.graph.scc;

import org.example.graph.Graph;
import org.example.metrics.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TarjanSCC {
    private final Graph G;
    private boolean[] marked;
    private int[] id;
    private int[] low;
    private int[] disc;
    private int pre;
    private int count;
    private Stack<Integer> stack;
    private boolean[] onStack;
    private final Metrics metrics;

    public TarjanSCC(Graph G, Metrics metrics) {
        this.G = G;
        this.metrics = metrics;
        marked = new boolean[G.V()];
        onStack = new boolean[G.V()];
        id = new int[G.V()];
        low = new int[G.V()];
        disc = new int[G.V()];
        pre = 0;
        count = 0;
        stack = new Stack<>();
        metrics.start();
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
        metrics.stop();
    }

    private void dfs(Graph G, int v) {
        metrics.incrementOperationCount();
        marked[v] = true;
        disc[v] = low[v] = pre++;
        stack.push(v);
        onStack[v] = true;
        for (Graph.Edge edge : G.adj(v)) {
            int w = edge.to();
            if (!marked[w]) {
                dfs(G, w);
                low[v] = Math.min(low[v], low[w]);
            } else if (onStack[w]) {
                low[v] = Math.min(low[v], disc[w]);
            }
        }

        if (low[v] == disc[v]) {
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                id[w] = count;
            } while (w != v);
            count++;
        }
    }

    public int count() {
        return count;
    }

    public boolean stronglyConnected(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        return id[v] == id[w];
    }

    public int id(int v) {
        validateVertex(v);
        return id[v];
    }

    public List<List<Integer>> getSCCs() {
        List<List<Integer>> sccs = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            sccs.add(new ArrayList<>());
        }
        for (int v = 0; v < id.length; v++) {
            sccs.get(id[v]).add(v);
        }
        return sccs;
    }

    public Graph getCondensationGraph() {
        Graph condensation = new Graph(count);
        java.util.Map<String, Integer> sccEdges = new java.util.HashMap<>();

        for (int v = 0; v < G.V(); v++) {
            for (Graph.Edge edge : G.adj(v)) {
                int w = edge.to();
                if (id[v] != id[w]) {
                    String key = id[v] + "->" + id[w];
                    sccEdges.put(key, sccEdges.getOrDefault(key, 0) + edge.weight());
                }
            }
        }

        for (java.util.Map.Entry<String, Integer> entry : sccEdges.entrySet()) {
            String[] vertices = entry.getKey().split("->");
            int u = Integer.parseInt(vertices[0]);
            int v = Integer.parseInt(vertices[1]);
            condensation.addEdge(new Graph.Edge(u, v, entry.getValue()));
        }

        return condensation;
    }

    private void validateVertex(int v) {
        int V = G.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }
}