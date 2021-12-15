package org.example.advent.day15;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        int maxCols = lines.get(0).length();
        int maxRows = lines.size();

        int[][] map = new int[maxRows][maxCols];
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxCols; j++) {
                String line = lines.get(i);
                map[i][j] = Integer.valueOf(line.substring(j, j + 1));
            }
        }
        int[][] fullMap = new int[maxRows * 5][maxCols * 5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                copyArrayWithIncrement(i, j, fullMap, map, maxRows, maxCols);
            }
        }

        Pair<Integer, Integer> start = new Pair<>(0, 0);

        Instant begin = Instant.now();
        dijkstraShortestDistances(map, start, new Pair<>(map.length - 1, map[0].length - 1));
        System.out.println("Dijkstra time: " + Duration.between(begin, Instant.now()).toMillis() + " ms");

        begin = Instant.now();
        long result1 = aStarShortestDistance(map, start, new Pair<>(map.length - 1, map[0].length - 1));
        System.out.println("A* time: " + Duration.between(begin, Instant.now()).toMillis() + " ms");

        System.out.println("Part1: " + result1);

        begin = Instant.now();
        dijkstraShortestDistances(fullMap, start, new Pair<>(fullMap.length - 1, fullMap[0].length - 1));
        System.out.println("Dijkstra time: " + Duration.between(begin, Instant.now()).toMillis() + " ms");

        begin = Instant.now();
        long result2 = aStarShortestDistance(fullMap, start, new Pair<>(fullMap.length - 1, fullMap[0].length - 1));
        System.out.println("A* time: " + Duration.between(begin, Instant.now()).toMillis() + " ms");

        System.out.println("Part2: " + result2);
    }

    private static void copyArrayWithIncrement(int tileX, int tileY, int[][] fullMap, int[][] map, int sizeX, int sizeY) {
        int beginRow = tileX * sizeX;
        int beginCol = tileY * sizeY;
        int increment = tileX + tileY;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                fullMap[beginRow + i][beginCol + j] = map[i][j] + increment;
                if (fullMap[beginRow + i][beginCol + j] > 9) {
                    fullMap[beginRow + i][beginCol + j] %= 9;
                }
            }
        }
    }

    private static long[][] dijkstraShortestDistances(int[][] map, Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        int maxRows = map.length;
        int maxCols = map[0].length;
        long[][] distances = new long[maxRows][maxCols];
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxCols; j++) {
                distances[i][j] = Long.MAX_VALUE;
            }
        }
        distances[start.first][start.second] = 0L;

        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>(
                (o1, o2) -> {
                    long d1 = distances[o1.first][o1.second];
                    long d2 = distances[o2.first][o2.second];
                    return Long.compare(d1, d2);
                }
        );
        queue.add(start);

        Set<Pair<Integer, Integer>> settled = new HashSet<>();
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> node = queue.poll();
            if (settled.contains(node)) {
                continue;
            }
            if (node.equals(end)) {
                return distances;
            }
            settled.add(node);
            List<Pair<Integer, Integer>> neighbours = getNeighbors(node.first, node.second, map);
            neighbours.stream()
                    .filter(neighbor -> !settled.contains(neighbor))
                    .forEach(neighbor -> {
                        long distance = distances[node.first][node.second] + map[neighbor.first][neighbor.second];
                        if (distance < distances[neighbor.first][neighbor.second]) {
                            distances[neighbor.first][neighbor.second] = distance;
                        }
                        queue.add(neighbor);
                    });
        }
        return distances;
    }

    private static long aStarShortestDistance(int[][] map, Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        int maxRows = map.length;
        int maxCols = map[0].length;
        long[][] distances = new long[maxRows][maxCols];
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxCols; j++) {
                distances[i][j] = Long.MAX_VALUE;
            }
        }
        distances[start.first][start.second] = 0L;
        long[][] estimates = new long[maxRows][maxCols];
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxCols; j++) {
                estimates[i][j] = Long.MAX_VALUE;
            }
        }
        estimates[start.first][start.second] = 0L;

        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>(
                (o1, o2) -> {
                    long d1 = estimates[o1.first][o1.second];
                    long d2 = estimates[o2.first][o2.second];
                    return Long.compare(d1, d2);
                }
        );
        queue.add(start);

        Set<Pair<Integer, Integer>> settled = new HashSet<>();
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> node = queue.poll();
            if (settled.contains(node)) {
                continue;
            }
            settled.add(node);
            if (node.equals(end)) {
                return distances[node.first][node.second];
            }
            List<Pair<Integer, Integer>> neighbours = getNeighbors(node.first, node.second, map);
            neighbours.stream()
                    .filter(neighbor -> !settled.contains(neighbor))
                    .forEach(neighbor -> {
                        long dist = distances[node.first][node.second] + map[neighbor.first][neighbor.second];
                        if (dist < distances[neighbor.first][neighbor.second]) {
                            distances[neighbor.first][neighbor.second] = dist;
                            estimates[neighbor.first][neighbor.second] = dist + taxiDist(neighbor, end);
                        }
                        queue.add(neighbor);
                    });
        }
        return 0L;
    }

    private static long taxiDist(Pair<Integer, Integer> neighbor, Pair<Integer, Integer> end) {
        return Math.abs(end.first - neighbor.first) + Math.abs(end.second - neighbor.second);
    }

    private static List<Pair<Integer, Integer>> getNeighbors(int x, int y, int[][] map) {

        BiPredicate<Integer, Integer> up = (i, j) -> i - 1 >= 0;
        BiPredicate<Integer, Integer> down = (i, j) -> i + 1 < map.length;
        BiPredicate<Integer, Integer> left = (i, j) -> j - 1 >= 0;
        BiPredicate<Integer, Integer> right = (i, j) -> j + 1 < map[i].length;

        List<Pair<Integer, Integer>> neighbors = new ArrayList<>();
        if (up.test(x, y)) {
            neighbors.add(new Pair<>(x - 1, y));
        }
        if (down.test(x, y)) {
            neighbors.add(new Pair<>(x + 1, y));
        }
        if (left.test(x, y)) {
            neighbors.add(new Pair<>(x, y - 1));
        }
        if (right.test(x, y)) {
            neighbors.add(new Pair<>(x, y + 1));
        }
        return neighbors;
    }


    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}


class Pair<X, Y> {
    X first;
    Y second;

    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    public X getFirst() {
        return first;
    }

    public Y getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
