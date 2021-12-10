package org.example.advent.day6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        TreeMap<Integer, Long> populationByTimer = Arrays.stream(lines.get(0).split(","))
                .map(Integer::valueOf)
                .collect(Collectors.groupingBy(value -> value, TreeMap::new, Collectors.counting()));

        for (int i = 0; i < 256; i++) {
            populationByTimer = grow(populationByTimer);
            //System.out.println(populationByTimer);
        }
        long result = populationByTimer.values().stream().mapToLong(value -> value).sum();
        System.out.println(result);
    }

    private static TreeMap<Integer, Long> grow(TreeMap<Integer, Long> populationByTimer) {
        TreeMap<Integer, Long> newPopulation = new TreeMap<>();
        populationByTimer.keySet().forEach(key -> {
            newPopulation.put(key - 1, populationByTimer.get(key));
        });
        if (newPopulation.containsKey(-1)) {
            long multiply = newPopulation.remove(-1);
            long pop6 = newPopulation.getOrDefault(6, 0L);
            newPopulation.put(6, pop6 + multiply);
            newPopulation.put(8, multiply);
        }
        return newPopulation;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
