package org.example.model;

import java.util.*;

public class DijkstraAlgorithm {

    public static CampusMap.PathResult findShortestPath(Map<Node, List<Edge>> graph, Node start, Node end) {
        Map<Node, Integer> distances = new HashMap<>();
        Map<Node, Node> previous = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialize distances
        for (Node node : graph.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        queue.add(start);

        // Process nodes
        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (distances.get(current) == Integer.MAX_VALUE) break;

            for (Edge edge : graph.getOrDefault(current, Collections.emptyList())) {
                Node neighbor = edge.getDestination();
                int newDist = distances.get(current) + edge.getWeight();

                if (newDist < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return buildPathResult(start, end, distances, previous);
    }

    private static CampusMap.PathResult buildPathResult(Node start, Node end,
                                                        Map<Node, Integer> distances,
                                                        Map<Node, Node> previous) {
        List<Node> path = new LinkedList<>();
        Node current = end;

        // Reconstruct path
        while (current != null && !current.equals(start)) {
            path.addFirst(current);
            current = previous.get(current);
        }

        if (current != null) {
            path.addFirst(start);
        } else {
            path.clear();
        }

        boolean pathExists = distances.containsKey(end) && distances.get(end) != Integer.MAX_VALUE;
        return new CampusMap.PathResult(
                pathExists ? distances.get(end) : 0,
                path,
                pathExists
        );
    }
}
