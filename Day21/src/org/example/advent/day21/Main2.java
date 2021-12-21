package org.example.advent.day21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main2 {

    public static final int WINNING_SCORE = 21;
    public static final int BOARD_SIZE = 10;
    public static final int ROLL_COUNT = 3;
    public static final int DIE_SIZE = 3;

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        int player1Start = Integer.valueOf(lines.get(0).replace("Player 1 starting position: ", ""));
        int player2Start = Integer.valueOf(lines.get(1).replace("Player 2 starting position: ", ""));

        Map<Key, Pair> cache = new HashMap<>();

        Pair result = new Pair(0L, 0L);
        for (int i = 1; i <= DIE_SIZE; i++) {
            result.add(turn(i, 1, 1, player1Start, player2Start, 0, 0, cache));
        }
        long winner = result.first > result.second ? result.first : result.second;
        System.out.println("Part2 " + winner);
    }

    private static Pair turn(int die, int player, int roll, int player1Pos, int player2Pos, int player1Score, int player2Score,
                             Map<Key, Pair> cache) {

        Key key = new Key(die, player, roll, player1Pos, player2Pos, player1Score, player2Score);
        Pair result = cache.get(key);
        if (result != null) {
            return result;
        }
        int position;
        int score;
        if (player == 1) {
            position = player1Pos;
            score = player1Score;
        } else {
            position = player2Pos;
            score = player2Score;
        }
        position = move(position, die);

        if (roll == ROLL_COUNT) {
            score += position;
            if (score >= WINNING_SCORE) {
                Pair won = player == 1 ? new Pair(1, 0) : new Pair(0, 1);
                cache.put(key, won);
                return won;
            }
        }
        Pair sum = new Pair(0L, 0L);
        if (player == 1) {
            player1Pos = position;
            player1Score = score;
        } else {
            player2Pos = position;
            player2Score = score;
        }
        if (roll < ROLL_COUNT) {
            roll++;
            for (int i = 1; i <= DIE_SIZE; i++) {
                sum.add(turn(i, player, roll, player1Pos, player2Pos, player1Score, player2Score, cache));
            }
        } else {
            int nextPlayer = player == 1 ? 2 : 1;
            for (int i = 1; i <= DIE_SIZE; i++) {
                sum.add(turn(i, nextPlayer, 1, player1Pos, player2Pos, player1Score, player2Score, cache));
            }
        }
        cache.put(key, sum);
        return sum;
    }

    public static int move(int startPos, int dieResult) {
        int result = (startPos + dieResult) % BOARD_SIZE;
        if (result == 0) {
            return BOARD_SIZE;
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

class Key implements Comparable<Key> {
    private final int die;
    private final int player;
    private final int roll;
    private final int player1Pos;
    private final int player2Pos;
    private final int player1Score;
    private final int player2Score;

    public Key(int die, int player, int roll, int player1Pos, int player2Pos, int player1Score, int player2Score) {
        this.die = die;
        this.player = player;
        this.roll = roll;
        this.player1Pos = player1Pos;
        this.player2Pos = player2Pos;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
    }

    @Override
    public String toString() {
        return "{" +
                "die=" + die +
                ", player=" + player +
                ", roll=" + roll +
                ", player1Pos=" + player1Pos +
                ", player2Pos=" + player2Pos +
                ", player1Score=" + player1Score +
                ", player2Score=" + player2Score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return die == key.die &&
                player == key.player &&
                roll == key.roll &&
                player1Pos == key.player1Pos &&
                player2Pos == key.player2Pos &&
                player1Score == key.player1Score &&
                player2Score == key.player2Score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(die, player, roll, player1Pos, player2Pos, player1Score, player2Score);
    }

    @Override
    public int compareTo(Key o) {
        return o.player1Score - this.player1Score;
    }
}

class Pair {
    long first;
    long second;

    public Pair(long first, long second) {
        this.first = first;
        this.second = second;
    }

    public long getFirst() {
        return first;
    }

    public long getSecond() {
        return second;
    }

    public Pair add(Pair other) {
        this.first += other.first;
        this.second += other.second;

        return this;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return first == pair.first &&
                second == pair.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}