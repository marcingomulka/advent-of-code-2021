package org.example.advent.day21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        int player1Start = Integer.valueOf(lines.get(0).replace("Player 1 starting position: ", ""));
        int player2Start = Integer.valueOf(lines.get(1).replace("Player 2 starting position: ", ""));

        int die = 0;

        boolean winnerFound = false;
        int player1Pos = player1Start;
        int player2Pos = player2Start;
        int player1Score = 0;
        int player2Score = 0;

        int rollCount = 0;
        while (!winnerFound) {

            int move = 0;
            die = roll(die);
            move += die;
            die = roll(die);
            move += die;
            die = roll(die);
            move += die;
            rollCount += 3;

            player1Pos = move(player1Pos, move);
            player1Score += player1Pos;
            if (player1Score >= 1000) {
                break;
            }

            move = 0;
            die = roll(die);
            move += die;
            die = roll(die);
            move += die;
            die = roll(die);
            move += die;
            rollCount += 3;

            player2Pos = move(player2Pos, move);
            player2Score += player2Pos;
            if (player2Score >= 1000) {
                break;
            }
        }

        int loserScore;
        if (player1Score > player2Score) {
            loserScore = player2Score;
        } else {
            loserScore = player1Score;
        }
        System.out.println("Part1 : " + loserScore * rollCount);

    }

    public static int roll(int die) {
        int result = ++die;
        if (result > 100) {
            return 1;
        }
        return result;
    }


    public static int move(int startPos, int dieResult) {
        int result = (startPos + dieResult) % 10;
        if (result == 0) {
            return 10;
        }
        return result;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}