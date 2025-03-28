package org.example.model;

import java.util.*;
import java.util.stream.Collectors;

public class CampusMap {
    private final Map<Node, List<Edge>> graph = new HashMap<>();

    // Node and Edge management
    public void addNode(Node node) {
        graph.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Edge edge) {
        addNode(edge.getSource());
        graph.get(edge.getSource()).add(edge);
    }

    // Shortest path delegation
    public PathResult findShortestPath(Node start, Node end) {
        return DijkstraAlgorithm.findShortestPath(graph, start, end);
    }

    // Result record
    public record PathResult(int totalDistance, List<Node> path, boolean pathExists) {}

    // Utility methods
    public Collection<Node> getNodes() {
        return graph.keySet();
    }

    public List<Edge> getAllEdges() {
        return graph.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public Node getNodeByName(String name) {
        return graph.keySet().stream()
                .filter(node -> node.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}