package org.example.advent.day22;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        List<Cuboid> cuboids = new ArrayList<>();
        for (String line : lines) {
            String[] chunks = line.split(" ");
            String[] coords = chunks[1].trim().split(",");

            String[] xrangeChunks = coords[0].replace("x=", "").split("\\.\\.");
            String[] yrangeChunks = coords[1].replace("y=", "").split("\\.\\.");
            String[] zrangeChunks = coords[2].replace("z=", "").split("\\.\\.");

            Pair<Integer, Integer> xRange = new Pair<>(Integer.valueOf(xrangeChunks[0]), Integer.valueOf(xrangeChunks[1]));
            Pair<Integer, Integer> yRange = new Pair<>(Integer.valueOf(yrangeChunks[0]), Integer.valueOf(yrangeChunks[1]));
            Pair<Integer, Integer> zRange = new Pair<>(Integer.valueOf(zrangeChunks[0]), Integer.valueOf(zrangeChunks[1]));

            cuboids.add(new Cuboid(chunks[0], xRange, yRange, zRange));
        }

        List<Cuboid> initialProcessed = new ArrayList<>();
        List<Cuboid> processed = new ArrayList<>();

        for (Cuboid cuboid : cuboids) {
            List<Cuboid> newProcessed = new ArrayList<>();
            for (Cuboid processedCuboid : processed) {
                newProcessed.add(processedCuboid);
                intersection(processedCuboid, cuboid).ifPresent(newProcessed::add);
            }
            if (cuboid.operation.equals("on")) {
                newProcessed.add(cuboid);
            }
            if (inInitialRange(cuboid)) {
                initialProcessed = newProcessed;
            }
            processed = newProcessed;

        }
        long initReactorCubesOnCount = initialProcessed.stream()
                .mapToLong(Main::volume)
                .sum();
        long fullReactorCubesOnCount = processed.stream()
                .mapToLong(Main::volume)
                .sum();

        System.out.println("Part1: " + initReactorCubesOnCount);
        System.out.println("Part2: " + fullReactorCubesOnCount);
    }

    private static long volume(Cuboid cube) {
        long x = cube.first.second - cube.first.first + 1;
        long y = cube.second.second - cube.second.first + 1;
        long z = cube.third.second - cube.third.first + 1;
        long sign = cube.operation.equals("on") ? 1L : -1L;

        return sign * x * y * z;
    }

    private static Optional<Cuboid> intersection(Cuboid cube1, Cuboid cube2) {
        var x = intersectDimension(cube1.first, cube2.first);
        var y = intersectDimension(cube1.second, cube2.second);
        var z = intersectDimension(cube1.third, cube2.third);

        var operation = cube1.operation.equals("on") ? "off" : "on";

        if (x != null && y != null && z != null) {
            return Optional.of(new Cuboid(operation, x, y, z));
        } else {
            return Optional.empty();
        }
    }

    private static Pair<Integer, Integer> intersectDimension(Pair<Integer, Integer> dim1, Pair<Integer, Integer> dim2) {
        int xL = Math.max(dim2.first, dim1.first);
        int xR = Math.min(dim2.second, dim1.second);
        if (xR < xL) {
            return null;
        } else {
            return new Pair<>(xL, xR);
        }
    }

    private static boolean inInitialRange(Cuboid range) {
        return range.first.first >= -50 && range.first.second <= 50
                && range.second.first >= -50 && range.second.second <= 50
                && range.third.first >= -50 && range.third.second <= 50;

    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}

class Cuboid {
    String operation;
    Pair<Integer, Integer> first;
    Pair<Integer, Integer> second;
    Pair<Integer, Integer> third;

    public Cuboid(String operation, Pair<Integer, Integer> first, Pair<Integer, Integer> second, Pair<Integer, Integer> third) {
        this.operation = operation;
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuboid cuboid = (Cuboid) o;
        return first == cuboid.first &&
                second == cuboid.second &&
                third == cuboid.third;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "{" + operation + " | " + first +
                "," + second +
                "," + third +
                '}';
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
        return "{" + first +
                ", " + second +
                '}';
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