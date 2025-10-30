package org.example;

import org.example.graph.Graph;
import org.example.graph.GraphJsonParser;
import org.example.graph.dagsp.DagLongestPath;
import org.example.graph.dagsp.DagShortestPath;
import org.example.graph.scc.TarjanSCC;
import org.example.metrics.MetricsImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DagPathTest {

    @Test
    public void testDagPath() throws IOException {
        Graph g = GraphJsonParser.parse("data/input/small-1.json");
        TarjanSCC scc = new TarjanSCC(g, new MetricsImpl());
        Graph condensation = scc.getCondensationGraph();

        int originalSource = 0;
        int originalTarget = 5;

        DagShortestPath sp = new DagShortestPath(condensation, scc.id(originalSource), new MetricsImpl());
        assertEquals(6, sp.distTo(scc.id(originalTarget)));

        DagLongestPath lp = new DagLongestPath(condensation, scc.id(originalSource), new MetricsImpl());
        assertEquals(8, lp.distTo(scc.id(originalTarget)));
    }
}
