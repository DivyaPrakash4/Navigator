package org.example.model;

public class Node {
    private String name;
    private double x;
    private double y;

    public Node(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    // Add getters
    public String getName() { return name; }
    public double getX() { return x; }
    public double getY() { return y; }
}
