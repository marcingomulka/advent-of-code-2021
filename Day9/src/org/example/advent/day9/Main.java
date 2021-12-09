package org.example.advent.day9;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static Set<Integer> UNIQUE_SIZES = new HashSet<>(Arrays.asList(2, 3, 4, 7));

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();
        long result = 0;

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

        for (int i = 0; i < heightmap.length; i++) {
            for (int j = 0; j < heightmap[i].length; j++) {
                if (isLowPoint(i, j, heightmap)) {
                    int riskLevel = heightmap[i][j] + 1;
                    //System.out.println(riskLevel);
                    result += riskLevel;
                }
            }
        }

        System.out.println(result);
    }

    private static boolean isLowPoint(int i, int j, int[][] heightmap) {
        int up = i - 1;
        int down = i + 1;
        int left = j - 1;
        int right = j + 1;

        List<Integer> neighbors = new ArrayList<>();
        if (up >= 0) {
            neighbors.add(heightmap[up][j]);
        }
        if (down < heightmap.length) {
            neighbors.add(heightmap[down][j]);
        }
        if (left >= 0) {
            neighbors.add(heightmap[i][left]);
        }
        if (right < heightmap[i].length) {
            neighbors.add((heightmap[i][right]));
        }
        return neighbors.stream()
                .allMatch(n -> heightmap[i][j] < n);
    }


    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
