package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.geometry.*;
import java.util.*;
import org.example.model.*;

public class CampusNavigationApp extends Application {
    private final CampusMap campusMap = new CampusMap();
    private final ComboBox<String> startComboBox = new ComboBox<>();
    private final ComboBox<String> endComboBox = new ComboBox<>();
    private final Pane canvas = new Pane();
    private final Label resultLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        initializeMapData();
        setupUI(primaryStage);
    }

    private void initializeMapData() {
        // Add Nodes
        Node library = new Node("Library", 100, 100);
        Node admin = new Node("Admin", 300, 200);
        Node block1 = new Node("block1", 450, 400);
        Node hostel = new Node("hostel", 400, 100);
        Node mainBuild = new Node("mainBuild", 200, 300);

        // Add nodes and edges
        campusMap.addNode(library);
        campusMap.addNode(admin);
        campusMap.addNode(block1);
        campusMap.addNode(hostel);
        campusMap.addNode(mainBuild);

        campusMap.addEdge(new Edge(library, admin, 3));
        campusMap.addEdge(new Edge(admin, library, 3));
        campusMap.addEdge(new Edge(block1, library, 8));
        campusMap.addEdge(new Edge(library, block1, 8));
        campusMap.addEdge(new Edge(hostel, library, 7));
        campusMap.addEdge(new Edge(library, hostel, 7));
        campusMap.addEdge(new Edge(hostel, block1, 4));
        campusMap.addEdge(new Edge(block1, hostel, 4));
        campusMap.addEdge(new Edge(block1, mainBuild, 6));
        campusMap.addEdge(new Edge(mainBuild, block1, 6));
        campusMap.addEdge(new Edge(mainBuild, library, 5));
        campusMap.addEdge(new Edge(library, mainBuild, 5));


        for (Node node : campusMap.getNodes()) {
            startComboBox.getItems().add(node.getName());
            endComboBox.getItems().add(node.getName());
        }

        drawMap();
    }

    private void setupUI(Stage stage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));

        VBox controlContainer = new VBox(10);
        controlContainer.setPadding(new Insets(0, 0, 20, 0));

        HBox controls = new HBox(10);
        controls.getChildren().addAll(
                new Label("Start:"), startComboBox,
                new Label("End:"), endComboBox,
                createFindButton()
        );

        // Style the result label
        resultLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        resultLabel.setPadding(new Insets(0, 0, 0, 0)); // Top padding

        // Add controls and result label to container
        controlContainer.getChildren().addAll(controls, resultLabel);

        canvas.setPrefSize(800, 500);
        canvas.setStyle("-fx-background-color: #add8e6;");

        root.getChildren().addAll(controls, canvas, resultLabel);

        Scene scene = new Scene(root, 850, 650);
        stage.setTitle("Campus Navigation System");
        stage.setScene(scene);
        stage.show();
    }

    private Button createFindButton() {
        Button btn = new Button("Find Path");
        btn.setOnAction(_ -> findPath());
        return btn;
    }

    private void findPath() {
        String start = startComboBox.getValue();
        String end = endComboBox.getValue();

        if (start == null || end == null || start.equals(end)) {
            resultLabel.setText("Please select valid start and end locations");
            return;
        }

        Node startNode = campusMap.getNodeByName(start);
        Node endNode = campusMap.getNodeByName(end);

        CampusMap.PathResult result = campusMap.findShortestPath(startNode, endNode);

        if (result.pathExists()) {
            drawMap();

            drawPath(result.path());
            resultLabel.setText(String.format("Shortest path: %d units\nPath: %s",
                    result.totalDistance(),
                    getPathString(result.path())));
        } else {
            resultLabel.setText("No path found between selected locations");
        }
    }

    private void drawMap() {
        canvas.getChildren().clear();

        // Draw edges
        for (Edge edge : campusMap.getAllEdges()) {
            Line line = new Line(
                    edge.getSource().getX(), edge.getSource().getY(),
                    edge.getDestination().getX(), edge.getDestination().getY()
            );
            line.setStroke(Color.GRAY);
            canvas.getChildren().add(line);
        }

        // Draw nodes
        for (Node node : campusMap.getNodes()) {
            Circle circle = new Circle(node.getX(), node.getY(), 8);
            circle.setFill(Color.BLUE);
            canvas.getChildren().add(circle);

            Label label = new Label(node.getName());
            label.setLayoutX(node.getX() + 10);
            label.setLayoutY(node.getY() - 10);
            canvas.getChildren().add(label);
        }
    }

    private void drawPath(List<Node> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Node from = path.get(i);
            Node to = path.get(i + 1);

            Line pathLine = new Line(
                    from.getX(), from.getY(),
                    to.getX(), to.getY()
            );
            pathLine.setStroke(Color.RED);
            pathLine.setStrokeWidth(2);
            canvas.getChildren().add(pathLine);
        }
    }

    private String getPathString(List<Node> path) {
        StringBuilder sb = new StringBuilder();
        for (Node node : path) {
            sb.append(node.getName()).append(" → ");
        }
        return sb.substring(0, sb.length() - 3);
    }

    //Main Class

    public static void main(String[] args) {
        launch(args);
    }
}