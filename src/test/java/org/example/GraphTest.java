package org.example;

import org.example.graph.Graph;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    public void testGraph() {
        Graph g = new Graph(4);
        assertEquals(4, g.V());
        g.addEdge(new Graph.Edge(0, 1, 1));
        g.addEdge(new Graph.Edge(0, 2, 1));
        g.addEdge(new Graph.Edge(1, 2, 1));
        g.addEdge(new Graph.Edge(2, 0, 1));
        g.addEdge(new Graph.Edge(2, 3, 1));
        g.addEdge(new Graph.Edge(3, 3, 1));

        assertEquals(2, g.adj(0).size());
        assertEquals(1, g.adj(1).size());
        assertEquals(2, g.adj(2).size());
        assertEquals(1, g.adj(3).size());
    }
}
