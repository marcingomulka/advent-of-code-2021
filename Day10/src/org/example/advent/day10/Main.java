package org.example.advent.day10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {

    static Map<Character, Character> chunkPairs = Map.of(
            '(', ')',
            '[', ']',
            '{', '}',
            '<', '>'
    );
    static Map<Character, Long> chunkPoints = Map.of(
            ')', 3L,
            ']', 57L,
            '}', 1197L,
            '>', 25137L
    );

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();
        long result = 0L;

        for (String line : lines) {
            LinkedList<Character> stack = new LinkedList<>();

            List<Integer> chars = line.chars().boxed().collect(Collectors.toList());
            for (Integer charValue : chars) {
                char sign = (char) charValue.intValue();
                if (chunkPairs.containsKey(sign)) {
                    stack.push(sign);
                } else {
                    char shouldMatch = chunkPairs.get(stack.pop());
                    if (sign != shouldMatch) {
                        //System.out.println("Wrong character " + sign);
                        long points = chunkPoints.get(sign);
                        result += points;
                    }
                }
            }
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

