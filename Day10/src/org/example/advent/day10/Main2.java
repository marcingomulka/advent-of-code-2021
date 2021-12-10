package org.example.advent.day10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main2 {

    static Map<Character, Character> chunkPairs = Map.of(
            '(', ')',
            '[', ']',
            '{', '}',
            '<', '>'
    );
    static Map<Character, Long> chunkPoints = Map.of(
            ')', 1L,
            ']', 2L,
            '}', 3L,
            '>', 4L
    );

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();
        List<Long> scores = new ArrayList<>();

        for (String line : lines) {
            LinkedList<Character> stack = new LinkedList<>();

            List<Integer> chars = line.chars().boxed().collect(Collectors.toList());
            if (!validateLine(stack, chars)) {
                continue;
            }
            scores.add(calculatesPoints(stack));
        }
        Collections.sort(scores);
        System.out.println(scores.get(scores.size() / 2));
    }

    private static long calculatesPoints(LinkedList<Character> stack) {
        long linePoints = 0L;
        while (!stack.isEmpty()) {
            char missing = chunkPairs.get(stack.pop());
            //System.out.print(missing);
            long points = chunkPoints.get(missing);
            linePoints = linePoints * 5L + points;
        }
        //System.out.println();
        return linePoints;
    }

    private static boolean validateLine(LinkedList<Character> stack, List<Integer> chars) {
        for (Integer charValue : chars) {
            char sign = (char) charValue.intValue();
            if (chunkPairs.containsKey(sign)) {
                stack.push(sign);
            } else {
                char shouldMatch = chunkPairs.get(stack.pop());
                if (sign != shouldMatch) {
                    return false;
                }
            }
        }
        return true;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}

