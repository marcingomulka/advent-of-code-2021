package org.example.advent.day13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        Set<Pair<Integer, Integer>> points = new HashSet<>();
        List<Pair<String, Integer>> folds = new ArrayList<>();
        for (String line : lines) {
            if (!line.startsWith("fold") && !line.trim().isEmpty()) {
                String[] chunks = line.split(",");
                Pair<Integer, Integer> point = new Pair<>(Integer.valueOf(chunks[0]), Integer.valueOf(chunks[1]));

                points.add(point);
            } else if (line.startsWith("fold")) {
                String[] chunks = line.replace("fold along ", "").split("=");
                Pair<String, Integer> fold = new Pair<>(chunks[0].trim(), Integer.valueOf(chunks[1].trim()));

                folds.add(fold);
            }
        }

        boolean firstFold = true;
        for (Pair<String, Integer> fold : folds) {
            List<Pair<Integer, Integer>> removed = new ArrayList<>();
            List<Pair<Integer, Integer>> added = new ArrayList<>();
            for (Pair<Integer, Integer> point : points) {
                String direction = fold.getFirst();
                Integer foldCoordinate = fold.getSecond();
                switch (direction) {
                    case "x":
                        if (point.getFirst() > foldCoordinate) {
                            removed.add(point);
                            int newX = foldCoordinate - (point.getFirst() - foldCoordinate);
                            added.add(new Pair<>(newX, point.getSecond()));
                        }
                        break;
                    case "y":
                        if (point.getSecond() > foldCoordinate) {
                            removed.add(point);
                            int newY = foldCoordinate - (point.getSecond() - foldCoordinate);
                            added.add(new Pair<>(point.getFirst(), newY));
                        }
                        break;
                }
            }
            removed.stream()
                    .forEach(point -> points.remove(point));
            added.stream()
                    .forEach(point -> points.add(point));

            if (firstFold) {
                System.out.println("Part1: " + points.size());
                firstFold = false;
            }
        }
        int maxY = points.stream()
                .mapToInt(point -> point.getSecond())
                .max()
                .orElse(0);
        int maxX = points.stream()
                .mapToInt(point -> point.getFirst())
                .max()
                .orElse(0);

        boolean[][] paper = new boolean[maxX + 1][maxY + 1];
        points.forEach(point -> paper[point.getFirst()][point.getSecond()] = true);

        System.out.println("Part2: ");
        for (int i = 0; i < maxY + 1; i++) {
            for (int j = 0; j < maxX + 1; j++) {
                if (paper[j][i]) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
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
    public String toString() {
        return "(" + first +
                "," + second +
                ')';
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