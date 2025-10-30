package org.example;

import org.example.graph.Graph;
import org.example.graph.GraphJsonParser;
import org.example.graph.scc.TarjanSCC;
import org.example.graph.topo.TopologicalSort;
import org.example.metrics.MetricsImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TopologicalSortTest {

    @Test
    public void testTopologicalSort() throws IOException {
        Graph g = GraphJsonParser.parse("data/input/small-1.json");
        TarjanSCC scc = new TarjanSCC(g, new MetricsImpl());
        Graph condensation = scc.getCondensationGraph();
        int originalSource = 0;
        TopologicalSort topological = new TopologicalSort(condensation, scc.id(originalSource), new MetricsImpl());
        assertTrue(topological.hasOrder());
        List<Integer> order = topological.getOrder();
        assertEquals(6, topological.getOrder().size());
        assertEquals(scc.id(originalSource), (int) order.get(0));
    }
}
