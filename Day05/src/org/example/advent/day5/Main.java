package org.example.advent.day5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws NumberFormatException, IOException {

        List<String> values = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            do {
                line = reader.readLine();
                if (Objects.nonNull(line)) {
                    values.add(line);
                }

            } while (Objects.nonNull(line));
        }

        List<Line> lines = new ArrayList<>();
        for (String value : values) {
            String[] points = value.split("->");
            String[] first = points[0].trim().split(",");
            String[] second = points[1].trim().split(",");

            Line line = new Line(Integer.valueOf(first[0]), Integer.valueOf(first[1]), Integer.valueOf(second[0]), Integer.valueOf(second[1]));
            lines.add(line);
            //if (line.isHorizontalOrVertical()) {
            //	lines.add(line);
            //}
        }
        //lines.forEach(System.out::println);
        int maxY = lines.stream()
                .mapToInt(line -> Math.max(line.y1, line.y2))
                .max()
                .orElse(0);
        int maxX = lines.stream()
                .mapToInt(line -> Math.max(line.x1, line.x2))
                .max()
                .orElse(0);

        int[][] board = new int[maxX + 1][maxY + 1];

        lines.forEach(line -> drawLine(line, board));
        int result = countIntersections(board);
        System.out.println(result);
    }

    private static int countIntersections(int[][] board) {
        int sum = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] >= 2) {
                    sum++;
                }
            }
        }
        return sum;
    }

    private static void drawLine(Line line, int[][] board) {
        if (line.isHorizontal()) {
            int firstX = line.x1 < line.x2 ? line.x1 : line.x2;
            int lastX = line.x1 > line.x2 ? line.x1 : line.x2;
            for (int i = firstX; i <= lastX; i++) {
                board[i][line.y1]++;
            }
        } else if (line.isVertical()) {
            int firstY = line.y1 < line.y2 ? line.y1 : line.y2;
            int lastY = line.y1 > line.y2 ? line.y1 : line.y2;
            for (int i = firstY; i <= lastY; i++) {
                board[line.x1][i]++;
            }
        }
    }
}

class Line {
    int x1;
    int y1;
    int x2;
    int y2;

    public Line(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    boolean isHorizontal() {
        return y1 == y2;
    }

    boolean isVertical() {
        return x1 == x2;
    }

    boolean isDiagonal() {
        return Math.abs(x1 - x2) == Math.abs(y1 - y2);
    }

    boolean isHorizontalOrVertical() {
        return isHorizontal() || isVertical();
    }

    public String toString() {
        return x1 + "," + y1 + " -> " + x2 + "," + y2;
    }
}
