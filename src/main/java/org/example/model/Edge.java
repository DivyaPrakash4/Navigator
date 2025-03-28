package org.example.model;

public class Edge {
    private Node source;
    private Node destination;
    private int weight;

    public Edge(Node source, Node destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    // Getters
    public Node getSource() { return source; }
    public Node getDestination() { return destination; }
    public int getWeight() { return weight; }
}
