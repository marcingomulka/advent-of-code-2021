package org.example.advent.day16;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    private static long versionSum = 0L;

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        String hexCode = lines.get(0);

        List<Character> binaries = new ArrayList<>();
        for (int i = 0; i < hexCode.length(); i++) {
            String hex = Character.toString(hexCode.charAt(i));
            String binary = Integer.toBinaryString(Integer.valueOf(hex, 16));
            String binaryPad = String.format("%4s", binary).replace(' ', '0');

            binaryPad.chars().forEach(c -> binaries.add((char) c));
        }
        long result = readPacket(binaries.listIterator());

        System.out.println("Part1: " + versionSum);
        System.out.println("Part2: " + result);
    }

    private static int readHeaderParam(ListIterator<Character> pointer) {
        String versionStr = pointer.next() + Character.toString(pointer.next()) + pointer.next();
        return Integer.valueOf(versionStr, 2);
    }

    private static long readPacket(ListIterator<Character> pointer) {
        int version = readHeaderParam(pointer);
        versionSum += (long) version;
        int type = readHeaderParam(pointer);
        if (type == 4) {
            return readLiteralPacket(pointer);
        } else {
            return readOperator(pointer, type);
        }
    }

    private static long readLiteralPacket(ListIterator<Character> pointer) {
        boolean endGroupReached = false;
        StringBuilder numberBits = new StringBuilder();
        while (!endGroupReached) {
            char groupId = pointer.next();
            IntStream.range(0, 4).forEach(i -> numberBits.append(pointer.next()));
            if (groupId == '0') {
                endGroupReached = true;
            }
        }
        return Long.valueOf(numberBits.toString(), 2);
    }

    private static long readOperator(ListIterator<Character> pointer, int type) {
        List<Long> subNumbers;
        char lengthType = pointer.next();
        if (lengthType == '0') {
            subNumbers = readLengthTypeSubPackets(pointer);
        } else {
            subNumbers = readCountTypeSubPackets(pointer);
        }
        switch (type) {
            case 0:
                return sum(subNumbers);
            case 1:
                return product(subNumbers);
            case 2:
                return min(subNumbers);
            case 3:
                return max(subNumbers);
            case 5:
                return gt(subNumbers);
            case 6:
                return lt(subNumbers);
            case 7:
                return eq(subNumbers);
            default:
                throw new IllegalArgumentException();
        }
    }

    private static List<Long> readCountTypeSubPackets(ListIterator<Character> pointer) {
        List<Long> subNumbers = new ArrayList<>();
        StringBuilder subPacketCountBuilder = new StringBuilder();

        IntStream.range(0, 11).forEach(i -> subPacketCountBuilder.append(pointer.next()));

        int subPacketsCount = Integer.valueOf(subPacketCountBuilder.toString(), 2);
        IntStream.range(0, subPacketsCount).forEach(i -> subNumbers.add(readPacket(pointer)));

        return subNumbers;
    }

    private static List<Long> readLengthTypeSubPackets(ListIterator<Character> pointer) {
        List<Long> subNumbers = new ArrayList<>();
        StringBuilder lengthBuilder = new StringBuilder();

        IntStream.range(0, 15).forEach(i -> lengthBuilder.append(pointer.next()));
        int length = Integer.valueOf(lengthBuilder.toString(), 2);

        int begin = pointer.nextIndex();
        do {
            subNumbers.add(readPacket(pointer));
        } while (pointer.nextIndex() < begin + length);

        return subNumbers;
    }

    private static long sum(List<Long> subNumbers) {
        return subNumbers.stream().reduce(0L, Long::sum);
    }

    private static long product(List<Long> subNumbers) {
        return subNumbers.stream().reduce(1L, (v1, v2) -> v1 * v2);
    }

    private static long min(List<Long> subNumbers) {
        return subNumbers.stream().mapToLong(val -> val).min().orElseThrow(IllegalArgumentException::new);
    }

    private static long max(List<Long> subNumbers) {
        return subNumbers.stream().mapToLong(val -> val).max().orElseThrow(IllegalArgumentException::new);
    }

    private static long gt(List<Long> subNumbers) {
        return subNumbers.get(0) > subNumbers.get(1) ? 1 : 0;
    }

    private static long lt(List<Long> subNumbers) {
        return subNumbers.get(0) < subNumbers.get(1) ? 1 : 0;
    }

    private static long eq(List<Long> subNumbers) {
        return subNumbers.get(0).equals(subNumbers.get(1)) ? 1 : 0;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}