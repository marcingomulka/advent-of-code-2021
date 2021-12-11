package org.example.advent.day11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        int maxCol = lines.get(0).length();
        int maxRow = lines.size();

        int[][] octopusStatus = new int[maxRow][maxCol];
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                String line = lines.get(i);
                octopusStatus[i][j] = Integer.valueOf(line.substring(j, j + 1));
            }
        }
        //System.out.println(Arrays.deepToString(octopusStatus));
        long total = 0L;
        int cycle = 1;
        boolean flashSynchronized = false;
        while (!flashSynchronized) {
            countUp(maxCol, maxRow, octopusStatus);

            Queue<Pair<Integer, Integer>> queue = collectFlashes(octopusStatus);

            propagateFlashes(octopusStatus, queue);

            long flashCount = flashCountAndCleanUp(octopusStatus);

            total += flashCount;
            if (cycle == 100) {
                System.out.println("Part1: " + total);
            }
            if (flashCount == maxCol * maxRow) {
                System.out.println("Part2: " + cycle);
                flashSynchronized = true;
            }
            cycle++;
        }
    }

    private static void countUp(int maxCol, int maxRow, int[][] octopusStatus) {
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                octopusStatus[i][j]++;
            }
        }
    }

    private static Queue<Pair<Integer, Integer>> collectFlashes(int[][] octopusStatus) {
        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
        for (int i = 0; i < octopusStatus.length; i++) {
            for (int j = 0; j < octopusStatus[i].length; j++) {
                if (octopusStatus[i][j] > 9) {
                    queue.add(new Pair(i, j));
                }
            }
        }
        return queue;
    }

    private static void propagateFlashes(int[][] octopusStatus, Queue<Pair<Integer, Integer>> queue) {
        while (!queue.isEmpty()) {
            var pair = queue.poll();
            List<Pair<Integer, Integer>> neighbors = getNeighbors(pair.first, pair.second, octopusStatus).stream()
                    .filter(n -> octopusStatus[n.first][n.second] <= 9)
                    .collect(Collectors.toList());
            neighbors.stream()
                    .forEach(n -> octopusStatus[n.first][n.second]++);
            neighbors.stream()
                    .filter(n -> octopusStatus[n.first][n.second] > 9)
                    .forEach(n -> queue.add(n));
        }
    }

    private static long flashCountAndCleanUp(int[][] octopusStatus) {
        long flashCount = 0L;
        for (int i = 0; i < octopusStatus.length; i++) {
            for (int j = 0; j < octopusStatus[i].length; j++) {
                if (octopusStatus[i][j] > 9) {
                    octopusStatus[i][j] = 0;
                    flashCount++;
                }
            }
        }
        return flashCount;
    }

    private static List<Pair<Integer, Integer>> getNeighbors(int x, int y, int[][] heightmap) {

        BiPredicate<Integer, Integer> up = (i, j) -> i - 1 >= 0;
        BiPredicate<Integer, Integer> down = (i, j) -> i + 1 < heightmap.length;
        BiPredicate<Integer, Integer> left = (i, j) -> j - 1 >= 0;
        BiPredicate<Integer, Integer> right = (i, j) -> j + 1 < heightmap[i].length;

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
        if (up.test(x, y) && left.test(x, y)) {
            neighbors.add(new Pair<>(x - 1, y - 1));
        }
        if (up.test(x, y) && right.test(x, y)) {
            neighbors.add(new Pair<>(x - 1, y + 1));
        }
        if (down.test(x, y) && left.test(x, y)) {
            neighbors.add(new Pair<>(x + 1, y - 1));
        }
        if (down.test(x, y) && right.test(x, y)) {
            neighbors.add(new Pair<>(x + 1, y + 1));
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
}