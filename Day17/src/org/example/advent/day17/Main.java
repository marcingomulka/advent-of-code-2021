package org.example.advent.day17;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {

    private static int yMaxPos = 0;

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        String line = lines.get(0);
        String[] coords = line.replace("target area: ", "").split(", ");
        String[] xCoords = coords[0].trim().replace("x=", "").split("\\.\\.");
        String[] yCoords = coords[1].trim().replace("y=", "").split("\\.\\.");

        Pair<Integer, Integer> xRange = new Pair<>(Integer.valueOf(xCoords[0]), Integer.valueOf(xCoords[1]));
        Pair<Integer, Integer> yRange = new Pair<>(Integer.valueOf(yCoords[0]), Integer.valueOf(yCoords[1]));

        List<Integer> velocityXHits = new ArrayList<>();
        List<Integer> velocityYHits = new ArrayList<>();

        int vx = 0;
        int vy = yRange.first - 1;
        while (vx <= xRange.second) {
            vx++;
            if (simulateXPath(vx, xRange)) {
                velocityXHits.add(vx);
            }
        }
        while (vy <= -yRange.first) {
            vy++;
            if (simulateYPath(vy, yRange)) {
                velocityYHits.add(vy);
            }
        }
        int result = 0;
        for (int i = 0; i < velocityXHits.size(); i++) {
            for (int j = 0; j < velocityYHits.size(); j++) {
                if (simulatePath(velocityXHits.get(i), velocityYHits.get(j), xRange, yRange)) {
                    result++;
                }
            }
        }
        System.out.println("Part1: " + Main.yMaxPos);
        System.out.println("Part2: " + result);
    }

    private static boolean simulatePath(int vx, int vy, Pair<Integer, Integer> xRange, Pair<Integer, Integer> yRange) {
        int xPos = 0;
        int yPos = 0;
        while (xPos <= xRange.second && yPos >= yRange.first) {
            xPos += vx;
            yPos += vy;
            if ((xPos >= xRange.first && xPos <= xRange.second) && (yPos >= yRange.first && yPos <= yRange.second)) {
                return true;
            }
            vx--;
            vy--;
            if (vx < 0) {
                vx = 0;
            }
        }
        return false;
    }

    private static boolean simulateXPath(int vx, Pair<Integer, Integer> xRange) {
        int xPos = 0;
        while (xPos < xRange.second && vx >= 0) {
            xPos += vx;
            if (xPos >= xRange.first && xPos <= xRange.second) {
                return true;
            }
            vx--;
        }
        return false;
    }

    private static boolean simulateYPath(int vy, Pair<Integer, Integer> yRange) {
        int yPos = 0;
        int yCurrMax = 0;
        boolean hit = false;

        while (yPos >= yRange.first) {
            yPos += vy;
            if (yPos > yCurrMax) {
                yCurrMax = yPos;
            }
            if (yPos >= yRange.first && yPos <= yRange.second) {
                hit = true;
                break;
            }
            vy--;
        }
        if (hit) {
            if (yCurrMax > Main.yMaxPos) {
                Main.yMaxPos = yCurrMax;
            }
        }
        return hit;
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