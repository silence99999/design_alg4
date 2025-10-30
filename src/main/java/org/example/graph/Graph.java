package org.example.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int V;
    private final List<List<Edge>> adj;

    public Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(Edge e) {
        adj.get(e.from()).add(e);
    }

    public List<Edge> adj(int v) {
        return adj.get(v);
    }

    public int V() {
        return V;
    }

    public static class Edge {
        private final int from;
        private final int to;
        private final int weight;

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int from() {
            return from;
        }

        public int to() {
            return to;
        }

        public int weight() {
            return weight;
        }

        @Override
        public String toString() {
            return from + "->" + to + " " + weight;
        }
    }
}
