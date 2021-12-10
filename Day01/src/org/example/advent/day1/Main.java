package org.example.advent.day1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) throws NumberFormatException, IOException {

        int counter = 0;
        List<Integer> values = new ArrayList<>();
        List<Integer> sums = new ArrayList<Integer>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            do {
                line = reader.readLine();
                if (Objects.nonNull(line)) {
                    values.add(Integer.valueOf(line));
                }

            } while (Objects.nonNull(line));
        }
        int sum = 0;
        for (int i = 0; i < values.size(); i++) {
            if (i < 2) {
                continue;
            }
            sum = values.get(i) + values.get(i - 1) + values.get(i - 2);
            sums.add(sum);
        }
        int prevSum = -1;
        for (Integer sumValue : sums) {
            if (prevSum == -1) {
                prevSum = sumValue;
            } else {
                if (sumValue > prevSum) {
                    counter++;
                }
                prevSum = sumValue;
            }

        }
        System.out.println(counter);
    }

}
