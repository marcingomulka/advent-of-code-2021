package org.example.advent.day19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static List<Integer[][]> ROTATIONS = new ArrayList<>();

    static {
        ROTATIONS.add(new Integer[][]{{1, 0, 0}, {0, 1, 0}, {0, 0, 1}});
        ROTATIONS.add(new Integer[][]{{1, 0, 0}, {0, 0, -1}, {0, 1, 0}});
        ROTATIONS.add(new Integer[][]{{1, 0, 0}, {0, -1, 0}, {0, 0, -1}});
        ROTATIONS.add(new Integer[][]{{1, 0, 0}, {0, 0, 1}, {0, -1, 0}});
        ROTATIONS.add(new Integer[][]{{0, 0, 1}, {0, 1, 0}, {-1, 0, 0}});
        ROTATIONS.add(new Integer[][]{{-1, 0, 0}, {0, 1, 0}, {0, 0, -1}});
        ROTATIONS.add(new Integer[][]{{0, 0, -1}, {0, 1, 0}, {1, 0, 0}});
        ROTATIONS.add(new Integer[][]{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}});
        ROTATIONS.add(new Integer[][]{{-1, 0, 0}, {0, -1, 0}, {0, 0, 1}});
        ROTATIONS.add(new Integer[][]{{0, 1, 0}, {-1, 0, 0}, {0, 0, 1}});
        ROTATIONS.add(new Integer[][]{{0, 0, 1}, {1, 0, 0}, {0, 1, 0}});
        ROTATIONS.add(new Integer[][]{{-1, 0, 0}, {0, 0, 1}, {0, 1, 0}});
        ROTATIONS.add(new Integer[][]{{0, 0, -1}, {-1, 0, 0}, {0, 1, 0}});
        ROTATIONS.add(new Integer[][]{{0, -1, 0}, {0, 0, -1}, {1, 0, 0}});
        ROTATIONS.add(new Integer[][]{{-1, 0, 0}, {0, 0, -1}, {0, -1, 0}});
        ROTATIONS.add(new Integer[][]{{0, 1, 0}, {0, 0, -1}, {-1, 0, 0}});
        ROTATIONS.add(new Integer[][]{{0, 0, 1}, {0, -1, 0}, {1, 0, 0}});
        ROTATIONS.add(new Integer[][]{{0, 0, -1}, {0, -1, 0}, {-1, 0, 0}});
        ROTATIONS.add(new Integer[][]{{0, -1, 0}, {-1, 0, 0}, {0, 0, -1}});
        ROTATIONS.add(new Integer[][]{{0, 1, 0}, {1, 0, 0}, {0, 0, -1}});
        ROTATIONS.add(new Integer[][]{{0, 0, 1}, {-1, 0, 0}, {0, -1, 0}});
        ROTATIONS.add(new Integer[][]{{0, 0, -1}, {1, 0, 0}, {0, -1, 0}});
        ROTATIONS.add(new Integer[][]{{0, -1, 0}, {0, 0, 1}, {-1, 0, 0}});
        ROTATIONS.add(new Integer[][]{{0, 1, 0}, {0, 0, 1}, {1, 0, 0}});

    }

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        Map<Integer, List<Triple>> scannerPoints = new HashMap<>();
        int scannerNum = -1;

        for (String line : lines) {
            if (line.startsWith("---")) {
                scannerNum++;
            } else if (!line.isEmpty()) {
                String[] coords = line.split(",");
                Triple point = new Triple(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(coords[2]));
                scannerPoints.computeIfAbsent(scannerNum, k -> new ArrayList<>()).add(point);
            }
        }
        Map<Pair<Integer, Integer>, Transformation> scannersTransformations = new HashMap<>();
        Map<Integer, Set<Integer>> scannerRelations = new HashMap<>();

        for (int i = 0; i < scannerPoints.keySet().size(); i++) {
            for (int j = 0; j < scannerPoints.keySet().size(); j++) {
                if (i == j) {
                    continue;
                }
                Optional<Transformation> transformation = tryMatchScanners(scannerPoints.get(i), scannerPoints.get(j));
                if (transformation.isPresent()) {
                    scannersTransformations.put(new Pair<>(i, j), transformation.get());
                    scannerRelations.computeIfAbsent(i, k -> new HashSet<>()).add(j);
                }
            }
        }
        Set<Triple> beacons = collectBeacons(0, scannerRelations, scannersTransformations, scannerPoints, new HashSet<>());
        System.out.println("Part1: " + beacons.size());

        Set<Triple> scannersPositions = collectScanners(0, scannerRelations, scannersTransformations, new HashSet<>());
        List<Triple> scannerList = new ArrayList<>(scannersPositions);

        long maxDistance = 0L;
        for (int i = 0; i < scannerList.size(); i++) {
            for (int j = i; j < scannerList.size(); j++) {
                long dist = taxiDist(scannerList.get(i), scannerList.get(j));
                if (dist > maxDistance) {
                    maxDistance = dist;
                }
            }
        }
        System.out.println("Part2: " + maxDistance);

    }

    private static long taxiDist(Triple p1, Triple p2) {
        return Math.abs(p2.first - p1.first) + Math.abs(p2.second - p1.second) + Math.abs(p2.third - p1.third);
    }

    private static Set<Triple> collectScanners(int scannerId, Map<Integer, Set<Integer>> relations,
                                               Map<Pair<Integer, Integer>, Transformation> scannersTransformations, HashSet<Object> alreadyProcessed) {

        Triple position = new Triple(0, 0, 0);
        Set<Triple> scanners = new HashSet<>();
        scanners.add(position);

        alreadyProcessed.add(scannerId);
        for (Integer matchedScannerId : relations.get(scannerId)) {
            var transformation = scannersTransformations.get(new Pair<>(scannerId, matchedScannerId));
            Triple matchPosition = transformation.transform(position);
            scanners.add(matchPosition);

            if (relations.containsKey(matchedScannerId) && !alreadyProcessed.contains(matchedScannerId)) {
                Set<Triple> related = collectScanners(matchedScannerId, relations, scannersTransformations, alreadyProcessed)
                        .stream()
                        .map(transformation::transform)
                        .collect(Collectors.toSet());
                scanners.addAll(related);
            }
        }
        return scanners;
    }

    private static Set<Triple> collectBeacons(int scannerId, Map<Integer, Set<Integer>> relations,
                                              Map<Pair<Integer, Integer>, Transformation> scannersTransformations,
                                              Map<Integer, List<Triple>> scannerPoints, Set<Integer> alreadyProcessed) {
        alreadyProcessed.add(scannerId);
        Set<Triple> beaconList = new HashSet<>(scannerPoints.get(scannerId));
        for (Integer matchedScannerId : relations.get(scannerId)) {
            var transformation = scannersTransformations.get(new Pair<>(scannerId, matchedScannerId));

            Set<Triple> transformed = scannerPoints.get(matchedScannerId).stream()
                    .map(transformation::transform)
                    .collect(Collectors.toSet());

            beaconList.addAll(transformed);

            if (relations.containsKey(matchedScannerId) && !alreadyProcessed.contains(matchedScannerId)) {
                Set<Triple> related = collectBeacons(matchedScannerId, relations, scannersTransformations, scannerPoints, alreadyProcessed).stream()
                        .map(transformation::transform)
                        .collect(Collectors.toSet());

                beaconList.addAll(related);
            }
        }
        return beaconList;
    }


    private static Optional<Transformation> tryMatchScanners(List<Triple> scannerList1, List<Triple> scannerList2) {
        List<Triple> rotated;
        for (Integer[][] rotation : ROTATIONS) {
            rotated = scannerList2.stream()
                    .map(point -> rotate(point, rotation))
                    .collect(Collectors.toList());
            for (Triple point1 : scannerList1) {
                for (Triple point2 : rotated) {
                    Triple translation = vector(point2, point1);

                    List<Triple> translated = rotated.stream()
                            .map(point -> translate(point, translation))
                            .collect(Collectors.toList());

                    List<Triple> matched = match(translated, scannerList1);
                    if (matched.size() >= 12) {
                        return Optional.of(new Transformation(rotation, translation));
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static Triple vector(Triple p1, Triple p2) {
        return new Triple(p2.first - p1.first, p2.second - p1.second, p2.third - p1.third);
    }

    static Triple translate(Triple point, Triple vector) {
        return new Triple(point.first + vector.first, point.second + vector.second, point.third + vector.third);
    }

    static Triple rotate(Triple point, Integer[][] matrix) {
        Triple result = new Triple(0, 0, 0);
        for (int i = 0; i < 3; i++) {
            int sum = 0;
            for (int j = 0; j < 3; j++) {
                sum += matrix[i][j] * point.get(j);
            }
            result.set(i, sum);
        }
        return result;
    }

    private static List<Triple> match(List<Triple> scanner1, List<Triple> scanner2) {
        return scanner2.stream()
                .filter(scanner1::contains)
                .collect(Collectors.toList());
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}

class Transformation {
    Integer[][] rotation;
    Triple translation;

    public Transformation(Integer[][] rotation, Triple translation) {
        this.rotation = rotation;
        this.translation = translation;
    }

    public Triple transform(Triple point) {
        return Main.translate(Main.rotate(point, rotation), translation);
    }
}

class Triple {
    int first;
    int second;
    int third;

    public Triple(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getThird() {
        return third;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple triple = (Triple) o;
        return first == triple.first &&
                second == triple.second &&
                third == triple.third;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "{" + first +
                "," + second +
                "," + third +
                '}';
    }

    public Integer get(int j) {
        switch (j) {
            case 0:
                return first;
            case 1:
                return second;
            case 2:
                return third;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void set(int j, int value) {
        switch (j) {
            case 0:
                first = value;
                break;
            case 1:
                second = value;
                break;
            case 2:
                third = value;
                break;
            default:
                throw new IllegalArgumentException();
        }
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