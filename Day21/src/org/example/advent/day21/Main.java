package org.example.advent.day21;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final int WINNING_SCORE_PART1 = 1000;
    public static final int WINNING_SCORE_PART2 = 21;
    public static final int BOARD_SIZE = 10;
    public static final int ROLL_COUNT = 3;
    public static final int DIRAC_DIE_SIZE = 3;

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        int player1Start = Integer.valueOf(lines.get(0).replace("Player 1 starting position: ", ""));
        int player2Start = Integer.valueOf(lines.get(1).replace("Player 2 starting position: ", ""));

        System.out.println("Part1: " + part1(player1Start, player2Start));
        System.out.println("Part2: " + part2(player1Start, player2Start));
    }

    private static int part1(int player1Start, int player2Start) {
        DeterministicDie die = new DeterministicDie(0, 100);

        Deque<PlayerState> players = new LinkedList<>(Arrays.asList(
                new PlayerState(1, player1Start),
                new PlayerState(2, player2Start)));

        boolean winnerFound = false;
        while (!winnerFound) {

            PlayerState player = players.removeFirst();
            int move = die.roll() + die.roll() + die.roll();

            player.move(move);
            player.addScore(player.getPosition());
            if (player.getScore() >= WINNING_SCORE_PART1) {
                winnerFound = true;
            }
            players.addLast(player);
        }
        int loserScore = players.stream()
                .min(Comparator.comparingInt(PlayerState::getScore))
                .map(PlayerState::getScore)
                .orElse(0);
        return loserScore * die.rollCount();
    }

    private static long part2(int player1Start, int player2Start) {
        Pair result = new Pair(0L, 0L);
        PlayerState player1 = new PlayerState(1, player1Start);
        PlayerState player2 = new PlayerState(2, player2Start);

        for (int i = 1; i <= DIRAC_DIE_SIZE; i++) {
            result.add(turn(i, 1, player1, player1, player2, new HashMap<>()));
        }
        return result.first > result.second ? result.first : result.second;
    }

    private static Pair turn(int die, int roll, PlayerState activePlayer, PlayerState player1, PlayerState player2,
                             Map<Key, Pair> cache) {

        Key key = new Key(die, roll, activePlayer, player1, player2);
        Pair result = cache.get(key);
        if (result != null) {
            return result;
        }
        PlayerState previousState = activePlayer.copy();

        activePlayer.move(die);
        if (roll == ROLL_COUNT) {
            activePlayer.addScore(activePlayer.getPosition());
            if (activePlayer.getScore() >= WINNING_SCORE_PART2) {

                Pair won = activePlayer.getNumber() == 1 ? new Pair(1, 0) : new Pair(0, 1);
                cache.put(key, won);
                activePlayer.reset(previousState);
                return won;
            }
        }
        Pair sum = new Pair(0L, 0L);
        if (roll < ROLL_COUNT) {
            roll++;
            for (int i = 1; i <= DIRAC_DIE_SIZE; i++) {
                sum.add(turn(i, roll, activePlayer, player1, player2, cache));
            }
        } else {
            PlayerState nextPlayer = activePlayer.getNumber() == 1 ? player2 : player1;
            for (int i = 1; i <= DIRAC_DIE_SIZE; i++) {
                sum.add(turn(i, 1, nextPlayer, player1, player2, cache));
            }
        }
        cache.put(key, sum);
        activePlayer.reset(previousState);
        return sum;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}

class DeterministicDie {
    private int die;
    private int rollCount;
    private final int max;

    public DeterministicDie(int die, int max) {
        this.die = die;
        this.max = max;
        this.rollCount = 0;
    }

    public int roll() {
        rollCount++;
        die++;
        if (die > max) {
            die = 1;
        }
        return die;
    }

    public int rollCount() {
        return this.rollCount;
    }
}

class PlayerState {
    private final int number;
    private int position;
    private int score;

    public PlayerState(int number, int position) {
        this.number = number;
        this.position = position;
        this.score = 0;
    }

    public int getNumber() {
        return this.number;
    }

    public int getPosition() {
        return this.position;
    }

    public int getScore() {
        return this.score;
    }

    public void move(int die) {
        int result = (this.position + die) % Main.BOARD_SIZE;
        if (result == 0) {
            result = Main.BOARD_SIZE;
        }
        this.position = result;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public PlayerState copy() {
        var player = new PlayerState(this.number, this.position);
        player.score = this.score;
        return player;
    }

    public void reset(PlayerState previousState) {
        this.position = previousState.position;
        this.score = previousState.score;
    }
}

class Key {
    private final int die;
    private final int player;
    private final int roll;
    private final int player1Pos;
    private final int player2Pos;
    private final int player1Score;
    private final int player2Score;

    public Key(int die, int roll, PlayerState activePlayer, PlayerState player1, PlayerState player2) {
        this.die = die;
        this.player = activePlayer.getNumber();
        this.roll = roll;
        this.player1Pos = player1.getPosition();
        this.player2Pos = player2.getPosition();
        this.player1Score = player1.getScore();
        this.player2Score = player2.getScore();
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
}