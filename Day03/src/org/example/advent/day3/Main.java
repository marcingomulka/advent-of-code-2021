package org.example.advent.day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws NumberFormatException, IOException {

        List<String> values = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            do {
                line = reader.readLine();
                if (Objects.nonNull(line)) {
                    values.add(line);
                }

            } while (Objects.nonNull(line));
        }
        int[] oneOccurences = new int[12];
        int[] zeroOccurences = new int[12];
        for (String line : values) {
            for (int i = 0; i < line.length(); i++) {
                char c = line.charAt(i);
                if (c == '1') {
                    oneOccurences[i]++;
                } else if (c == '0') {
                    zeroOccurences[i]++;
                }
            }
        }
        StringBuilder gammaBuilder = new StringBuilder();
        StringBuilder epsilonBuilder = new StringBuilder();
        for (int i = 0; i < oneOccurences.length; i++) {
            if (oneOccurences[i] > zeroOccurences[i]) {
                gammaBuilder.append('1');
                epsilonBuilder.append('0');
            } else if (zeroOccurences[i] > oneOccurences[i]) {
                gammaBuilder.append('0');
                epsilonBuilder.append('1');
            } else {
                System.out.println("ERROR - Tie on position " + i);
            }
        }
        int gamma = Integer.parseInt(gammaBuilder.toString(), 2);
        int epsilon = Integer.parseInt(epsilonBuilder.toString(), 2);
        System.out.println("gamma: " + gamma + " epsilon " + epsilon);
        System.out.println(gamma * epsilon);
    }

}
