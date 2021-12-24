package org.example.advent.day24;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        long[] registers = new long[4];

        int[] number = new int[14];
        Arrays.fill(number, 9);

        boolean success = false;
        while (!success) {
            int inputIndex = 0;
            for (String line : lines) {

                //System.out.print(Arrays.stream(registers).mapToObj(String::valueOf).collect(Collectors.joining(",")));
                //System.out.println(" -> " + line);
                String[] chunks = line.split(" ");
                String instruction = chunks[0].trim();
                String arg1 = chunks[1].trim();
                switch (instruction) {
                    case "inp":
                        inp(registers, number, inputIndex, arg1);
                        inputIndex++;
                        break;
                    case "add":
                        add(registers, arg1, chunks[2]);
                        break;
                    case "mul":
                        mul(registers, arg1, chunks[2]);
                        break;
                    case "div":
                        div(registers, arg1, chunks[2]);
                        break;
                    case "mod":
                        mod(registers, arg1, chunks[2]);
                        break;
                    case "eql":
                        eql(registers, arg1, chunks[2]);
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
            //success = true;
            if (registers[2] == 0) {
                success = true;
            } else {
                generateNumber(number);
            }
            //System.out.println(toNumber(number));
        }
        //System.out.println(Arrays.stream(registers).mapToObj(String::valueOf).collect(Collectors.joining(",")));
        System.out.println("Part1: " + toNumber(number));
    }

    private static String toNumber(int[] number) {
        return Arrays.stream(number)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    private static void generateNumber(int[] number) {
        int i = number.length - 1;
        int last = number[i];
        last--;
        number[i] = last;

        while (last <= 0 && i > 0) {
            number[i] = 9;
            i--;
            last = number[i];
            last--;
            number[i] = last;
        }
    }

    private static void eql(long[] registers, String arg1, String arg2) {
        int mode = 0;
        int param1 = decodeRegister(arg1);
        int param2;
        try {
            param2 = Integer.parseInt(arg2);
        } catch (NumberFormatException e) {
            param2 = decodeRegister(arg2);
            mode = 1;
        }
        if (mode == 0) {
            registers[param1] = registers[param1] == param2 ? 1 : 0;
        } else {
            registers[param1] = registers[param1] == registers[param2] ? 1 : 0;
        }
    }

    private static void mod(long[] registers, String arg1, String arg2) {
        int mode = 0;
        int param1 = decodeRegister(arg1);
        int param2;
        try {
            param2 = Integer.parseInt(arg2);
        } catch (NumberFormatException e) {
            param2 = decodeRegister(arg2);
            mode = 1;
        }
        if (mode == 0) {
            registers[param1] %= param2;
        } else {
            registers[param1] %= registers[param2];
        }
    }

    private static void div(long[] registers, String arg1, String arg2) {
        int mode = 0;
        int param1 = decodeRegister(arg1);
        int param2;
        try {
            param2 = Integer.parseInt(arg2);
        } catch (NumberFormatException e) {
            param2 = decodeRegister(arg2);
            mode = 1;
        }
        if (mode == 0) {
            registers[param1] /= param2;
        } else {
            registers[param1] /= registers[param2];
        }
    }

    private static void mul(long[] registers, String arg1, String arg2) {
        int mode = 0;
        int param1 = decodeRegister(arg1);
        int param2;
        try {
            param2 = Integer.parseInt(arg2);
        } catch (NumberFormatException e) {
            param2 = decodeRegister(arg2);
            mode = 1;
        }
        if (mode == 0) {
            registers[param1] *= param2;
        } else {
            registers[param1] *= registers[param2];
        }
    }

    private static void add(long[] registers, String arg1, String arg2) {
        int mode = 0;
        int param1 = decodeRegister(arg1);
        int param2;
        try {
            param2 = Integer.parseInt(arg2);
        } catch (NumberFormatException e) {
            param2 = decodeRegister(arg2);
            mode = 1;
        }
        if (mode == 0) {
            registers[param1] += param2;
        } else {
            registers[param1] += registers[param2];
        }
    }

    private static void inp(long[] registers, int[] number, int inputIndex, String arg1) {
        registers[decodeRegister(arg1)] = number[inputIndex];
    }

    private static int decodeRegister(String param) {
        switch (param) {
            case "x":
                return 0;
            case "y":
                return 1;
            case "z":
                return 2;
            case "w":
                return 3;
            default:
                throw new IllegalArgumentException();
        }
    }


    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}


