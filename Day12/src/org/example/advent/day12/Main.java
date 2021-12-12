package org.example.advent.day12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        Map<String, Node> nodes = new HashMap<>();
        for (String line : lines) {
            String[] chunks = line.split("-");
            String nodeLabel1 = chunks[0].trim();
            String nodeLabel2 = chunks[1].trim();

            Node node1 = nodes.computeIfAbsent(nodeLabel1, k -> new Node(k));
            Node node2 = nodes.computeIfAbsent(nodeLabel2, k -> new Node(k));

            node1.addAjacent(node2);
            node2.addAjacent(node1);
        }
        List<Collection<Node>> paths = new ArrayList<>();
        Deque<Node> path = new LinkedList<>();
        Node start = nodes.get("start");
        searchPaths(start, path, paths);

        System.out.println(paths.size());
    }

    public static void searchPaths(Node visited, Deque<Node> path, List<Collection<Node>> paths) {
        path.push(visited);
        if (!visited.getLabel().equals("end")) {
            visited.getAdjacent().stream()
                    .filter(n -> !n.isSmall() || !path.contains(n))
                    .forEach(n -> searchPaths(n, path, paths));
        } else {
            paths.add(new ArrayList<>(path));
        }
        path.pop();
    }


    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}


class Node {
    final String label;

    final List<Node> adjacent = new ArrayList<>();

    public Node(String label) {
        Objects.requireNonNull(label);
        this.label = label;
    }

    public void addAjacent(Node node) {
        this.adjacent.add(node);
    }

    public String getLabel() {
        return label;
    }

    public List<Node> getAdjacent() {
        return Collections.unmodifiableList(adjacent);
    }

    public boolean isSmall() {
        return this.label.chars()
                .allMatch(character -> character - '0' > 48);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return label.equals(node.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return "Node{" +
                "label='" + label + '\'' +
                '}';
    }
}