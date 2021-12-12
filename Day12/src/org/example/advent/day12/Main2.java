package org.example.advent.day12;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main2 {


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
                    .filter(n -> applySelectionFilter(n, path))
                    .forEach(n -> searchPaths(n, path, paths));

        } else {
            paths.add(new ArrayList<>(path));
        }
        path.pop();
    }

    private static boolean applySelectionFilter(Node n, Deque<Node> path) {
        Map<String, Long> visitedSmalls = path.stream()
                .filter(Node::isSmall)
                .collect(Collectors.groupingBy(Node::getLabel, HashMap::new, Collectors.counting()));

        long doubleVisitCount = visitedSmalls.values().stream().filter(val -> val == 2L).count();
        long nodeVisitCount = visitedSmalls.getOrDefault(n.getLabel(), 0L);

        boolean smallNodeNoVisited = nodeVisitCount == 0L;
        boolean smallNodeVisitedOnce = nodeVisitCount == 1L;
        boolean noDoubleVisit = doubleVisitCount == 0L;
        boolean doubleVisitExists = doubleVisitCount == 1L;

        return !n.getLabel().equals("start")
                && (!n.isSmall() || testSmallOccurences(smallNodeVisitedOnce, smallNodeNoVisited, noDoubleVisit, doubleVisitExists));
    }

    private static boolean testSmallOccurences(boolean smallNodeVisitedOnce, boolean smallNodeNoVisited, boolean noDoubleVisit, boolean doubleVisitExists) {
        return (smallNodeVisitedOnce && !doubleVisitExists) || noDoubleVisit || smallNodeNoVisited;
    }


    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
