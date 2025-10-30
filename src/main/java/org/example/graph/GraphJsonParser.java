package org.example.graph;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphJsonParser {

    public static Graph parse(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject json = new JSONObject(content);

        int n = json.getInt("n");
        Graph g = new Graph(n);

        JSONArray edges = json.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject edgeJson = edges.getJSONObject(i);
            int u = edgeJson.getInt("u");
            int v = edgeJson.getInt("v");
            int w = edgeJson.getInt("w");
            g.addEdge(new Graph.Edge(u, v, w));
        }
        return g;
    }
}
