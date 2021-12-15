package org.example.advent.day14;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();
        String template = lines.get(0).trim();

        Map<String, Long> chunkOccurrences = new HashMap<>();
        for (int i = 0; i < template.length() - 1; i++) {
            String chunk = template.substring(i, i + 2);

            chunkOccurrences.compute(chunk, (k, v) -> v != null ? v + 1 : 1);
        }
        Map<String, Long> letterOccurrences = template.chars()
                .boxed()
                .collect(Collectors.groupingBy(Character::toString, Collectors.counting()));

        Map<String, String> rules = new HashMap<>();
        for (String line : lines) {
            if (line.contains("->")) {
                String[] chunks = line.split("->");
                String key = chunks[0].trim();
                String value = chunks[1].trim();

                rules.put(key, value);
            }
        }

        for (int cycle = 0; cycle < 40; cycle++) {
            Map<String, Long> previous = chunkOccurrences.entrySet().stream()
                    .filter(entry -> entry.getValue() > 0)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            for (String chunk : previous.keySet()) {
                String toInsert = rules.get(chunk);
                if (toInsert != null) {
                    String newChunk = chunk.substring(0, 1) + toInsert;
                    String newChunk2 = toInsert + chunk.substring(1, 2);

                    long multiplyFactor = previous.get(chunk);
                    letterOccurrences.compute(toInsert, (k, v) -> v != null ? v + multiplyFactor : multiplyFactor);
                    chunkOccurrences.compute(newChunk, (k, v) -> v != null ? v + multiplyFactor : multiplyFactor);
                    chunkOccurrences.compute(newChunk2, (k, v) -> v != null ? v + multiplyFactor : multiplyFactor);
                    chunkOccurrences.compute(chunk, (k, v) -> v != null ? v - multiplyFactor : 0L);
                }
            }
            if (cycle == 9) {
                long max = letterOccurrences.values().stream().mapToLong(val -> val).max().orElse(0L);
                long min = letterOccurrences.values().stream().mapToLong(val -> val).min().orElse(0L);

                System.out.println("Part1: " + (max - min));
            }
        }
        long max = letterOccurrences.values().stream().mapToLong(val -> val).max().orElse(0L);
        long min = letterOccurrences.values().stream().mapToLong(val -> val).min().orElse(0L);

        System.out.println("Part2: " + (max - min));
    }


    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
