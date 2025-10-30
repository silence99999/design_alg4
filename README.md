# Smart City / Smart Campus Scheduling

This project implements a solution for scheduling tasks with dependencies, using graph algorithms to handle both cyclic and acyclic dependencies.

## Data Summary

The datasets are generated in JSON format and stored in the `/data/input` directory. Each task has a name, a list of dependencies, and a duration.

- **Weight Model**: The duration of the tasks is used as the weight for the *edges* in the graph.

### Datasets

- **Small**: 6-10 nodes, simple cases, 1-2 cycles or pure DAG.
    - `data/input/small-1.json`: 6 nodes, 1 cycle.
    - `data/input/small-2.json`: 6 nodes, pure DAG.
    - `data/input/small-3.json`: 5 nodes, 1 cycle.
- **Medium**: 10-20 nodes, mixed structures, several SCCs.
    - `data/input/medium-1.json`: 11 nodes, 2 SCCs.
    - `data/input/medium-2.json`: 12 nodes, complex structure.
    - `data/input/medium-3.json`: 12 nodes, dense graph.
- **Large**: 20-50 nodes, performance and timing tests.
    - `data/input/large-1.json`: 20 nodes, sparse graph with one large cycle.
    - `data/input/large-2.json`: 20 nodes, mixed structure.
    - `data/input/large-3.json`: 20 nodes, dense graph.

## Results

### Metrics for `small-1.json` (n=6, Cyclic)
| Algorithm | Time (ns) | Operation Count | n |
|---|---|---|---|
| Tarjan's SCC | 40100 | 6 | 6 |
| Topological Sort | 49000 | 12 | 6 |
| Shortest Path | 11800 | 7 | 6 |
| Longest Path | 2100 | 42 | 6 |

### Metrics for `small-2.json` (n=6, Pure DAG)
| Algorithm | Time (ns) | Operation Count | n |
|---|---|---|---|
| Tarjan's SCC | 35200 | 6 | 6 |
| Topological Sort | 48000 | 12 | 6 |
| Shortest Path | 15400 | 5 | 6 |
| Longest Path | 1700 | 30 | 6 |

### Metrics for `small-3.json` (n=5, Cyclic)
| Algorithm | Time (ns) | Operation Count | n |
|---|---|---|---|
| Tarjan's SCC | 35400 | 5 | 5 |
| Topological Sort | 35500 | 2 | 1 |
| Shortest Path | 9400 | 0 | 1 |
| Longest Path | 7500 | 0 | 1 |

### Metrics for `medium-1.json` (n=11, Multiple SCCs)
| Algorithm | Time (ns) | Operation Count | n |
|---|---|---|---|
| Tarjan's SCC | 65700 | 11 | 11 |
| Topological Sort | 57000 | 14 | 7 |
| Shortest Path | 14100 | 5 | 7 |
| Longest Path | 3900 | 35 | 7 |

### Metrics for `medium-2.json` (n=12, Pure DAG)
| Algorithm | Time (ns) | Operation Count | n |
|---|---|---|---|
| Tarjan's SCC | 59600 | 12 | 12 |
| Topological Sort | 59500 | 24 | 12 |
| Shortest Path | 22600 | 12 | 12 |
| Longest Path | 4600 | 144 | 12 |

### Metrics for `medium-3.json` (n=12, Denser DAG)
| Algorithm | Time (ns) | Operation Count | n |
|---|---|---|---|
| Tarjan's SCC | 37200 | 12 | 12 |
| Topological Sort | 60400 | 24 | 12 |
| Shortest Path | 18700 | 22 | 12 |
| Longest Path | 35600 | 264 | 12 |

### Metrics for `large-1.json` (n=20, Single Large Cycle)
| Algorithm | Time (ns) | Operation Count | n |
|---|---|---|---|
| Tarjan's SCC | 75200 | 20 | 20 |
| Topological Sort | 47000 | 2 | 1 |
| Shortest Path | 12900 | 0 | 1 |
| Longest Path | 9200 | 0 | 1 |

### Metrics for `large-2.json` (n=20, Pure DAG)
| Algorithm | Time (ns) | Operation Count | n |
|---|---|---|---|
| Tarjan's SCC | 63100 | 20 | 20 |
| Topological Sort | 69200 | 40 | 20 |
| Shortest Path | 37000 | 19 | 20 |
| Longest Path | 5900 | 380 | 20 |

### Metrics for `large-3.json` (n=20, Denser Cyclic Graph)
| Algorithm | Time (ns) | Operation Count | n |
|---|---|---|---|
| Tarjan's SCC | 56500 | 20 | 20 |
| Topological Sort | 44000 | 2 | 1 |
| Shortest Path | 9800 | 0 | 1 |
| Longest Path | 8300 | 0 | 1 |

Output files are generated in the `data/output/` directory.


![img.png](img.png)

![img_1.png](img_1.png)

## Analysis



This section analyzes the performance of the implemented graph algorithms based on the collected metrics, considering their theoretical time complexities.



-   **Time Complexity Overview:**

    *   **Tarjan's SCC Algorithm:** The theoretical time complexity is O(V + E), where V is the number of vertices and E is the number of edges in the graph. This is because the algorithm performs a single DFS traversal, visiting each vertex and edge at most a constant number of times.

    *   **Topological Sort (Kahn's Algorithm):** The theoretical time complexity is also O(V + E). This involves calculating in-degrees for all vertices (O(V + E)) and then processing each vertex and its outgoing edges once (O(V + E)). For the condensation graph, V and E refer to the number of SCCs and the edges between them, respectively.

    *   **Shortest/Longest Paths in DAGs:** These algorithms rely on a topological sort and then relax each edge exactly once. Therefore, their theoretical time complexity is also O(V + E) on the DAG (condensation graph).



-   **Observed Metrics vs. Theoretical Complexity:**

    *   **Operation Count:** The `Operation Count` metric generally aligns with the `V` and `E` components of the time complexity. For Tarjan's SCC, it represents DFS visits, which directly scales with V. For Topological Sort, it counts queue operations and in-degree decrements, reflecting V and E. For pathfinding, it counts relaxations, directly related to E.

    *   **Elapsed Time:** The `elapsedTimeNs` generally increases with `n` (number of vertices) and `E` (number of edges, implicitly higher in denser graphs). For cyclic graphs, the `n` for Topological Sort, Shortest Path, and Longest Path refers to the number of SCCs, which can be significantly smaller than the original graph's `n`, leading to faster execution times for these algorithms on the condensation graph.



-   **Effect of Graph Structure (Density, Cycles, SCCs):**

    *   **Cyclic vs. Acyclic:** For purely acyclic graphs (e.g., `small-2.json`, `medium-2.json`, `large-2.json`), the SCC algorithm identifies each node as its own SCC, resulting in a condensation graph isomorphic to the original. For cyclic graphs (e.g., `small-1.json`, `small-3.json`, `large-1.json`, `large-3.json`), SCC compression significantly reduces the number of effective vertices (`n` for Topo/SP/LP becomes `sccCount`), which can drastically improve the performance of subsequent topological sort and pathfinding algorithms.

    *   **Density:** Denser graphs (higher E for a given V) generally lead to higher `Operation Count` and `elapsedTimeNs` for all algorithms, as they all have an E component in their time complexity. This is evident when comparing sparse DAGs to denser DAGs of similar `n`.

    *   **Multiple SCCs:** Graphs with multiple SCCs (e.g., `medium-1.json`) demonstrate the effectiveness of SCC compression. The `n` for topological sort and pathfinding is reduced to the number of SCCs, allowing these algorithms to operate on a smaller, acyclic graph.



-   **Bottlenecks:** The primary bottleneck often lies in the initial SCC decomposition for very large or dense graphs, as it processes the entire original graph. However, once the condensation graph is built, subsequent DAG-specific algorithms are highly efficient.



## Conclusions



Based on the analysis, here are key takeaways and practical recommendations:



-   **Prioritize Cycle Detection and Compression:** For task scheduling or dependency management in systems that might contain cycles, it is crucial to first apply an SCC algorithm. This step effectively transforms a potentially cyclic graph into a Directed Acyclic Graph (DAG) of components, simplifying subsequent planning.

-   **Efficiency of DAG Algorithms:** Once a graph is reduced to a DAG (either naturally or through SCC compression), algorithms like Topological Sort and DAG Shortest/Longest Paths become extremely efficient (O(V+E)). This makes them ideal for optimal planning and critical path analysis in complex systems.

-   **Scalability:** The O(V+E) complexity ensures that these algorithms scale well with increasing graph sizes, provided the graph remains relatively sparse. For very dense graphs, the 'E' component can dominate, but the linear relationship generally holds.

-   **Practical Recommendations:**

    *   Always preprocess dependency graphs with SCC to handle cycles gracefully.

    *   Leverage topological sort for ordering tasks where dependencies must be respected.

    *   Utilize DAG shortest/longest path algorithms for identifying critical paths (longest duration) or most efficient sequences (shortest duration) in project management or resource allocation scenarios.

    *   Consider the actual number of vertices and edges (V and E) when estimating performance, especially for the initial SCC step on the original graph and subsequent steps on the condensation graph.

To run the project, you need to have Java and Maven installed.

1. Clone the repository.
2. Run `mvn compile exec:java` to execute the main class.
3. Run `mvn test` to run the tests.
