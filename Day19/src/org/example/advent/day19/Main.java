package org.example.advent.day19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        Map<Integer, List<Triple>> scanners = new HashMap<>();
        int scanner = -1;

        for (String line : lines) {
            if (line.startsWith("---")) {
                scanner++;
            } else if (!line.isEmpty()) {
                String[] coords = line.split(",");
                Triple point = new Triple(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(coords[2]));
                scanners.computeIfAbsent(scanner, k -> new ArrayList<>()).add(point);
            }
        }
        //System.out.println(scanners.keySet().size());

        for (int i = 0; i < scanners.keySet().size(); i++) {
            for (int j = i; j < scanners.keySet().size(); j++) {
                if (i != j) {
                    Triple translation = tryMatchScanners(scanners.get(i), scanners.get(j));
                    System.out.println(translation);
                }
            }
        }
    }

    private static Triple tryMatchScanners(List<Triple> scanner1, List<Triple> scanner2) {
        //Dunno how to solve it!
        return null;
    }

    private static int matchCount(List<Triple> scanner1, List<Triple> scanner2) {
        return (int) scanner2.stream()
                .filter(scanner1::contains)
                .count();
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
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
}