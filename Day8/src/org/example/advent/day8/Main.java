package org.example.advent.day8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static Set<Integer> UNIQUE_SIZES = new HashSet<>(Arrays.asList(2, 3, 4, 7));

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();
        int result = 0;
        for (String line : lines) {
            String[] chunks = line.split("\\|");
            String pattern = chunks[0].trim();
            String output = chunks[1].trim();

            result += Stream.of(output.split("\\s+"))
                    .filter(value -> UNIQUE_SIZES.contains(value.trim().length()))
                    .count();
        }
        System.out.println(result);
    }


    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
