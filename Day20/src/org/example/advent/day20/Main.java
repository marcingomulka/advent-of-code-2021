package org.example.advent.day20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();
        String enhanceStr = lines.get(0);

        List<Character> enhanceList = enhanceStr.chars()
                .mapToObj(ch -> Character.toString((char) ch).charAt(0))
                .collect(Collectors.toList());

        int maxRows = lines.size() - 2;
        int maxCols = lines.get(2).length();
        maxRows += 2;
        maxCols += 2;

        Character[][] map = new Character[maxRows][maxCols];
        int row = 1;
        int skip = 0;
        for (String line : lines) {
            if (skip < 2) {
                skip++;
                continue;
            }
            for (int j = 1; j < line.length() + 1; j++) {
                map[row][j] = line.charAt(j - 1);
            }
            row++;
        }
        Character[][] newMap;
        fillBorders(map, '.');
        for (int step = 1; step <= 50; step++) {
            maxRows += 2;
            maxCols += 2;

            newMap = new Character[maxRows][maxCols];
            char voidChar = getVoidCharacter(step, enhanceList);
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[i].length; j++) {
                    Character value = pixelValue(i, j, map, enhanceList, voidChar);
                    newMap[i + 1][j + 1] = value;
                }
            }
            fillBorders(newMap, getVoidCharacter(step + 1, enhanceList));
            map = newMap;
            if (step == 2) {
                long result = countLights(map);
                System.out.println("Part1: " + result);
            }
        }
        long result = countLights(map);
        System.out.println("Part2: " + result);
    }

    private static long countLights(Character[][] map) {
        long result = 0L;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == '#') {
                    result++;
                }
            }
        }
        return result;
    }

    private static char getVoidCharacter(int step, List<Character> enhanceList) {
        char toFill;
        if (enhanceList.get(0) == '#') {
            if (step % 2 == 1) {
                toFill = '.';
            } else {
                toFill = '#';
            }
        } else {
            toFill = '.';
        }
        return toFill;
    }

    private static void fillBorders(Character[][] map, char toFill) {
        for (int i = 0; i < map.length; i++) {
            map[0][i] = toFill;
            map[i][0] = toFill;
            map[map.length - 1][i] = toFill;
            map[i][map.length - 1] = toFill;
        }
    }

    private static Character pixelValue(int i, int j, Character[][] map, List<Character> enhanceList, char voidChar) {
        int index = Integer.valueOf(collectCode(i, j, map, voidChar), 2);
        return enhanceList.get(index);
    }
    private static String collectCode(int x, int y, Character[][] map, char voidChar) {
        StringBuilder buffer = new StringBuilder();
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i < 0 || i >= map.length) {
                    buffer.append(voidChar);
                } else if (j < 0 || j >= map[0].length) {
                    buffer.append(voidChar);
                } else {
                    buffer.append(map[i][j]);
                }
            }
        }
        String result = buffer.toString().replace(".", "0");
        return result.replace("#", "1");
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}