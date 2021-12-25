package org.example.advent.day25;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();
        int maxX = lines.size();
        int maxY = lines.get(0).length();
        char[][] map = new char[maxX][maxY];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            for (int j = 0; j < line.length(); j++) {
                map[i][j] = line.charAt(j);
            }
        }
        int step = 0;
        boolean stopped = false;
        while (!stopped) {
            char[][] newMap = new char[maxX][maxY];
            stopped = !simulateMovement(map, newMap);
            map = newMap;
            step++;
        }
        System.out.println("Part1: " + step);
    }

    private static void printMap(char[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }

    private static boolean simulateMovement(char[][] map, char[][] newMap) {
        boolean movedEast = moveEast(map, newMap);

        map = newMap;
        newMap = new char[map.length][map[0].length];

        boolean movedSouth = moveSouth(map, newMap);

        for (int i = 0; i < newMap.length; i++) {
            map[i] = Arrays.copyOf(newMap[i], newMap[i].length);
        }
        return movedEast || movedSouth;
    }

    private static boolean moveSouth(char[][] map, char[][] newMap) {
        boolean moved = false;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 'v') {
                    if (freeToMove(i + 1, j, map)) {
                        int xPos = i + 1;
                        if (xPos >= map.length) {
                            xPos = 0;
                        }
                        newMap[i][j] = '.';
                        newMap[xPos][j] = 'v';
                        moved = true;
                    } else {
                        newMap[i][j] = 'v';
                    }

                } else {
                    if (newMap[i][j] != 'v') {
                        newMap[i][j] = map[i][j];
                    }
                }
            }
        }
        return moved;
    }

    private static boolean moveEast(char[][] map, char[][] newMap) {
        boolean moved = false;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '>') {
                    if (freeToMove(i, j + 1, map)) {
                        int yPos = j + 1;
                        if (yPos >= map[0].length) {
                            yPos = 0;
                        }
                        newMap[i][j] = '.';
                        newMap[i][yPos] = '>';
                        moved = true;
                    } else {
                        newMap[i][j] = '>';
                    }
                } else {
                    if (newMap[i][j] != '>') {
                        newMap[i][j] = map[i][j];
                    }
                }

            }
        }
        return moved;
    }

    private static boolean freeToMove(int i, int j, char[][] map) {
        if (i >= map.length) {
            i = 0;
        }
        if (j >= map[0].length) {
            j = 0;
        }
        return map[i][j] != '>' && map[i][j] != 'v';
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
