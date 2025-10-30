package org.example;

import org.example.graph.Graph;
import org.example.graph.GraphJsonParser;
import org.example.graph.dagsp.DagLongestPath;
import org.example.graph.dagsp.DagShortestPath;
import org.example.graph.scc.TarjanSCC;
import org.example.graph.topo.TopologicalSort;
import org.example.metrics.Metrics;
import org.example.metrics.MetricsImpl;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            Graph g = GraphJsonParser.parse("data/input/tasks.json");
            writeJsonToFile(new JSONObject().put("vertices", g.V()), "data/output/graph.json");

            Metrics sccMetrics = new MetricsImpl();
            TarjanSCC scc = new TarjanSCC(g, sccMetrics);
            JSONObject sccJson = new JSONObject();
            sccJson.put("metrics", new JSONObject().put("elapsedTimeNs", sccMetrics.getElapsedTime()).put("operationCount", sccMetrics.getOperationCount()));
            sccJson.put("sccCount", scc.count());
            sccJson.put("sccs", new JSONArray(scc.getSCCs()));
            writeJsonToFile(sccJson, "data/output/scc.json");

            Graph condensation = scc.getCondensationGraph();
            JSONObject condensationJson = new JSONObject();
            for (int i = 0; i < condensation.V(); i++) {
                condensationJson.put("SCC " + i, new JSONArray(condensation.adj(i).toString()));
            }
            writeJsonToFile(condensationJson, "data/output/condensationGraph.json");

            String content = new String(Files.readAllBytes(Paths.get("data/input/tasks.json")));
            JSONObject json = new JSONObject(content);
            int source = json.getInt("source");

            Metrics topoMetrics = new MetricsImpl();
            TopologicalSort topological = new TopologicalSort(condensation, scc.id(source), topoMetrics);
            JSONObject topoJson = new JSONObject();
            topoJson.put("metrics", new JSONObject().put("elapsedTimeNs", topoMetrics.getElapsedTime()).put("operationCount", topoMetrics.getOperationCount()));

            if (topological.hasOrder()) {
                topoJson.put("order", new JSONArray(topological.getOrder()));
                writeJsonToFile(topoJson, "data/output/topologicalSort.json");

                Metrics spMetrics = new MetricsImpl();
                DagShortestPath sp = new DagShortestPath(condensation, scc.id(source), spMetrics);
                JSONObject spJson = new JSONObject();
                spJson.put("metrics", new JSONObject().put("elapsedTimeNs", spMetrics.getElapsedTime()).put("operationCount", spMetrics.getOperationCount()));
                spJson.put("sourceScc", scc.id(source));
                JSONObject pathsJson = new JSONObject();
                for (int i = 0; i < condensation.V(); i++) {
                    if (sp.hasPathTo(i)) {
                        pathsJson.put("To " + i, new JSONObject().put("distance", sp.distTo(i)).put("path", new JSONArray(sp.pathTo(i))));
                    }
                }
                spJson.put("paths", pathsJson);
                writeJsonToFile(spJson, "data/output/shortestPaths.json");

                Metrics lpMetrics = new MetricsImpl();
                int maxDist = 0;
                List<Integer> criticalPath = null;
                for (int i = 0; i < condensation.V(); i++) {
                    DagLongestPath lp = new DagLongestPath(condensation, i, lpMetrics);
                    for (int j = 0; j < condensation.V(); j++) {
                        if (lp.hasPathTo(j) && lp.distTo(j) > maxDist) {
                            maxDist = lp.distTo(j);
                            criticalPath = lp.pathTo(j);
                        }
                    }
                }
                JSONObject lpJson = new JSONObject();
                lpJson.put("metrics", new JSONObject().put("elapsedTimeNs", lpMetrics.getElapsedTime()).put("operationCount", lpMetrics.getOperationCount()));
                lpJson.put("criticalPathLength", maxDist);
                lpJson.put("criticalPath", new JSONArray(criticalPath));
                writeJsonToFile(lpJson, "data/output/longestPath.json");

            } else {
                writeJsonToFile(new JSONObject().put("error", "Condensation graph has a cycle, which is impossible."), "data/output/error.json");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeJsonToFile(JSONObject json, String fileName) throws IOException {
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json.toString(4));
            System.out.println("Successfully wrote output to " + fileName);
        }
    }
}
