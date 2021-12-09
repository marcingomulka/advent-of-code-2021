package org.example.advent.day9;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main2 {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        int maxCol = lines.get(0).length();
        int maxRow = lines.size();

        int[][] heightmap = new int[maxRow][maxCol];
        for (int i = 0; i < maxRow; i++) {
            for (int j = 0; j < maxCol; j++) {
                String line = lines.get(i);
                heightmap[i][j] = Integer.valueOf(line.substring(j, j + 1));
            }
        }
        //System.out.println(Arrays.deepToString(heightmap));
        List<Pair<Integer, Integer>> lowPoints = new ArrayList<>();
        for (int i = 0; i < heightmap.length; i++) {
            for (int j = 0; j < heightmap[i].length; j++) {
                if (isLowPoint(i, j, heightmap)) {
                    lowPoints.add(new Pair<>(i, j));
                }
            }
        }
        Long result = lowPoints.stream()
                .map(point -> flood(point, heightmap))
                .filter(basinSize -> basinSize > 0)
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce((s1, s2) -> s1 * s2)
                .orElse(0L);
        System.out.println(result);
    }

    private static long flood(Pair<Integer, Integer> lowPoint, int[][] heightmap) {
        if (heightmap[lowPoint.getKey()][lowPoint.getValue()] == 9) {
            return 0;
        }
        int size = 1;
        heightmap[lowPoint.getKey()][lowPoint.getValue()] = 9;

        List<Pair<Integer, Integer>> neighbors = getNeighbors(lowPoint.getKey(), lowPoint.getValue(), heightmap);
        for (Pair<Integer, Integer> neighbor : neighbors) {
            if (heightmap[neighbor.getKey()][neighbor.getValue()] < 9) {
                size += flood(neighbor, heightmap);
            }
        }
        return size;
    }

    private static List<Pair<Integer, Integer>> getNeighbors(int i, int j, int[][] heightmap) {
        int up = i - 1;
        int down = i + 1;
        int left = j - 1;
        int right = j + 1;

        List<Pair<Integer, Integer>> neighbors = new ArrayList<>();
        if (up >= 0) {
            neighbors.add(new Pair<>(up, j));
        }
        if (down < heightmap.length) {
            neighbors.add(new Pair<>(down, j));
        }
        if (left >= 0) {
            neighbors.add(new Pair<>(i, left));
        }
        if (right < heightmap[i].length) {
            neighbors.add(new Pair<>(i, right));
        }
        return neighbors;
    }

    private static boolean isLowPoint(int i, int j, int[][] heightmap) {
        List<Pair<Integer, Integer>> neighbors = getNeighbors(i, j, heightmap);
        return neighbors.stream()
                .allMatch(n -> heightmap[i][j] < heightmap[n.getKey()][n.getValue()]);
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
