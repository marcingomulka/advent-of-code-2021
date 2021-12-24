package org.example.advent.day24;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws IOException {
        //All that pseudo assembler input code reduces to the formula() code
        // with the following params lists per input value:
        int[] param1 = {1, 1, 1, 1, 26, 1, 1, 26, 1, 26, 26, 26, 26, 26};
        int[] param2 = {14, 13, 15, 13, -2, 10, 13, -15, 11, -9, -9, -7, -4, -6};
        int[] param3 = {0, 12, 14, 0, 3, 15, 11, 12, 1, 12, 3, 10, 14, 12};

        List<Integer> descendingInput = IntStream.rangeClosed(1, 9)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        List<Integer> solution = new ArrayList<>();
        Set<Key> pruned = new HashSet<>();
        trySolve(descendingInput, 0, solution, 0, param1, param2, param3, pruned);

        Collections.reverse(solution);
        System.out.println("Part1: " + solution.stream().map(String::valueOf).collect(Collectors.joining()));

        List<Integer> ascendingInput = IntStream.rangeClosed(1, 9)
                .boxed()
                .collect(Collectors.toList());

        List<Integer> solutionPart2 = new ArrayList<>();
        pruned = new HashSet<>();
        trySolve(ascendingInput, 0, solutionPart2, 0, param1, param2, param3, pruned);

        Collections.reverse(solutionPart2);
        System.out.println("Part2: " + solutionPart2.stream().map(String::valueOf).collect(Collectors.joining()));
    }

    private static long formula(long z, int w, int param1, int param2, int param3) {
        boolean xtest = (z % 26 + param2) != w;
        int x = xtest ? 1 : 0;
        return x * (w + param3) + (25 * x + 1) * (z / param1);
    }

    private static boolean trySolve(List<Integer> inputValues, long z, List<Integer> solution, int step,
                                    int[] param1, int[] param2, int[] param3, Set<Key> pruned) {
        Key key = new Key(step, z);
        if (pruned.contains(key)) {
            return false;
        }
        if (step == 14) {
            if (z == 0) {
                return true;
            }
        } else {
            for (int w : inputValues) {
                long prevZ = z;
                z = formula(z, w, param1[step], param2[step], param3[step]);
                if (trySolve(inputValues, z, solution, step + 1, param1, param2, param3, pruned)) {
                    solution.add(w);
                    return true;
                }
                z = prevZ;
            }
        }
        pruned.add(key);
        return false;
    }
}

class Key {
    private final int step;
    private final long z;

    public Key(int step, long z) {
        this.step = step;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return step == key.step &&
                z == key.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(step, z);
    }
}