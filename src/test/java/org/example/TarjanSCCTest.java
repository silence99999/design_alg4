package org.example;

import org.example.graph.Graph;
import org.example.graph.GraphJsonParser;
import org.example.graph.scc.TarjanSCC;
import org.example.metrics.MetricsImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TarjanSCCTest {

    @Test
    public void testTarjanSCC() throws IOException {
        Graph g = GraphJsonParser.parse("data/input/small-1.json");
        TarjanSCC scc = new TarjanSCC(g, new MetricsImpl());
        assertEquals(6, scc.count());

        List<List<Integer>> sccs = scc.getSCCs();
        assertEquals(1, sccs.get(0).size());
        assertEquals(1, sccs.get(1).size());
        assertEquals(1, sccs.get(2).size());
        assertEquals(1, sccs.get(3).size());
        assertEquals(1, sccs.get(4).size());
        assertEquals(1, sccs.get(5).size());
    }
}
