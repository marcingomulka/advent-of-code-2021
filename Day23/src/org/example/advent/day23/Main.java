package org.example.advent.day23;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static long cacheHit = 0L;

    static Map<Character, Integer> energyCost = Map.of(
            'A', 1,
            'B', 10,
            'C', 100,
            'D', 1000
    );

    static List<Integer> roomPossibleMoves = List.of(0, 1, 3, 5, 7, 9, 10);

    static Map<Integer, Integer> roomExits = Map.of(
            0, 2,
            1, 4,
            2, 6,
            3, 8
    );

    static Map<Character, Integer> targetRoom = Map.of(
            'A', 0,
            'B', 1,
            'C', 2,
            'D', 3
    );

    public static void main(String[] args) throws IOException {
        char[] hallway = new char[11];
        IntStream.range(0, hallway.length).forEach(i -> hallway[i] = '.');

        List<char[]> roomsPart1 = new ArrayList<>();
        char[] room1 = new char[]{'B', 'D'};
        roomsPart1.add(room1);
        char[] room2 = new char[]{'B', 'C'};
        roomsPart1.add(room2);
        char[] room3 = new char[]{'D', 'A'};
        roomsPart1.add(room3);
        char[] room4 = new char[]{'A', 'C'};
        roomsPart1.add(room4);

        List<char[]> roomsPart2 = new ArrayList<>();
        room1 = new char[]{'B', 'D', 'D', 'D'};
        roomsPart2.add(room1);
        room2 = new char[]{'B', 'C', 'B', 'C'};
        roomsPart2.add(room2);
        room3 = new char[]{'D', 'B', 'A', 'A'};
        roomsPart2.add(room3);
        room4 = new char[]{'A', 'C'};
        roomsPart2.add(room4);


        Instant beginTime = Instant.now();

        PriorityQueue<Long> costs = new PriorityQueue<>();
        costs.add(Long.MAX_VALUE);
        long minCost = trySolve(hallway, roomsPart1, 0L, costs, new HashMap<>());

        System.out.println("Cache hits: " + cacheHit);
        System.out.println("Partial solutions: " + costs);
        System.out.println("Part1: " + minCost);
        System.out.println("Time: " + Duration.between(beginTime, Instant.now()).toSeconds() + " s");
    }

    private static long trySolve(char[] hallway, List<char[]> rooms, long currentCost, PriorityQueue<Long> costs, Map<Key, Long> cache) {
        Key key = new Key(hallway, rooms);
        Long cached = cache.get(key);
        if (cached != null) {
            cacheHit++;
            return cached;
        }
        if (currentCost >= costs.peek()) {
            return Long.MAX_VALUE;
        }
        if (allSet(rooms)) {
            costs.add(currentCost);
            return currentCost;
        }

        long minCost = costs.peek();
        boolean moved = false;
        for (int roomId = 0; roomId < rooms.size(); roomId++) {
            for (int hallwayPos : roomPossibleMoves) {
                for (int turn = 0; turn < 2; turn++) {
                    if (turn == 0) {
                        if (canMoveOut(roomId, hallwayPos, hallway, rooms)) {
                            int cost = moveOut(roomId, hallwayPos, hallway, rooms);
                            //System.out.println("current cost: " + (currentCost + cost));
                            //printBoard(hallway, rooms);

                            long moveCost = trySolve(hallway, rooms, currentCost + cost, costs, cache);
                            if (moveCost < minCost) {
                                minCost = moveCost;
                            }

                            moveIn(roomId, hallwayPos, hallway, rooms);

                            moved = true;
                            //printBoard(hallway, rooms);
                        }
                    } else {
                        if (canMoveIn(roomId, hallwayPos, hallway, rooms)) {
                            int cost = moveIn(roomId, hallwayPos, hallway, rooms);
                            //System.out.println("current cost: " + (currentCost + cost));
                            //printBoard(hallway, rooms);

                            long moveCost = trySolve(hallway, rooms, currentCost + cost, costs, cache);
                            if (moveCost < minCost) {
                                minCost = moveCost;
                            }

                            moveOut(roomId, hallwayPos, hallway, rooms);

                            moved = true;
                            //printBoard(hallway, rooms);
                        }
                    }
                }
            }
        }
        if (moved) {
            if (minCost < costs.peek()) {
                cache.put(key, minCost);
                costs.add(minCost);
            }
            return minCost;
        } else {
            cache.put(key, Long.MAX_VALUE);
            return Long.MAX_VALUE;
        }

    }

    private static void printBoard(char[] hallway, List<char[]> rooms) {
        System.out.println(String.valueOf(hallway));
        for (int i = 0; i < 2; i++) {
            System.out.print("##");
            for (char[] room : rooms) {
                System.out.print(room[i] + "#");
            }
            System.out.println("#");
        }
        System.out.println("----------------------");
    }

    private static boolean allSet(List<char[]> rooms) {
        return rooms.get(0)[0] == 'A' && rooms.get(0)[1] == 'A'
                && rooms.get(1)[0] == 'B' && rooms.get(1)[1] == 'B'
                && rooms.get(2)[0] == 'C' && rooms.get(2)[1] == 'C'
                && rooms.get(3)[0] == 'D' && rooms.get(3)[1] == 'D';
    }

    private static boolean canMoveOut(int roomId, int hallwaypos, char[] hallway, List<char[]> rooms) {
        char[] room = rooms.get(roomId);
        if (room[0] == '.' && room[1] == '.') {
            return false;
        }
        if (hallway[hallwaypos] != '.') {
            return false;
        }
        int roomExit = roomExits.get(roomId);
        if (hallwaypos == roomExit) return false;

        if (isTargetRoom(roomId, rooms.get(roomId))) {
            return false;
        }

        int begin = roomExit < hallwaypos ? roomExit : hallwaypos;
        int end = roomExit > hallwaypos ? roomExit : hallwaypos;

        return IntStream.rangeClosed(begin, end)
                .allMatch(i -> hallway[i] == '.');
    }

    private static boolean isTargetRoom(int roomId, char[] room) {
        char letter = room[0] == '.' ? room[1] : room[0];
        int target = targetRoom.get(letter);

        if (roomId == target) {
            return room[1] == letter && (room[0] == '.' || room[0] == letter);
        }
        return false;
    }

    private static boolean canMoveIn(int roomId, int hallwaypos, char[] hallway, List<char[]> rooms) {
        if (hallway[hallwaypos] == '.') {
            return false;
        }
        char letter = hallway[hallwaypos];
        if (targetRoom.get(letter) != roomId) {
            return false;
        }
        char[] room = rooms.get(roomId);
        if (!isRoomEmpty(room, letter)) {
            return false;
        }
        int roomExit = roomExits.get(roomId);

        int begin = roomExit < hallwaypos ? roomExit : hallwaypos + 1;
        int end = roomExit > hallwaypos ? roomExit : hallwaypos - 1;

        return IntStream.rangeClosed(begin, end)
                .allMatch(i -> hallway[i] == '.');
    }

    private static boolean isRoomEmpty(char[] room, char letter) {
        if (room[1] != '.' && room[1] != letter) {
            return false;
        } else return room[0] == '.';
    }

    private static int moveOut(int roomId, int hallwaypos, char[] hallway, List<char[]> rooms) {
        char[] room = rooms.get(roomId);
        int moves = 0;

        char toMove;
        if (room[0] == '.') {
            toMove = room[1];
            room[1] = '.';
            moves++;
        } else {
            toMove = room[0];
            room[0] = '.';
        }
        moves++;
        int roomExit = roomExits.get(roomId);
        hallway[hallwaypos] = toMove;
        moves += Math.abs(hallwaypos - roomExit);

        int stepCost = energyCost.get(toMove);
        return stepCost * moves;
    }

    private static int moveIn(int roomId, int hallwaypos, char[] hallway, List<char[]> rooms) {
        char[] room = rooms.get(roomId);
        int moves = 0;

        char toMove = hallway[hallwaypos];
        int roomExit = roomExits.get(roomId);
        moves += Math.abs(hallwaypos - roomExit);
        hallway[hallwaypos] = '.';

        if (room[1] == '.') {
            room[1] = toMove;
            moves += 2;
        } else {
            room[0] = toMove;
            moves++;
        }
        int stepCost = energyCost.get(toMove);
        return stepCost * moves;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }

}

class Key {

    private final String hallway;
    private final String room0;
    private final String room1;
    private final String room2;
    private final String room3;


    public Key(char[] hallway, List<char[]> rooms) {
        this.hallway = String.valueOf(hallway);
        this.room0 = String.valueOf(rooms.get(0));
        this.room1 = String.valueOf(rooms.get(1));
        this.room2 = String.valueOf(rooms.get(2));
        this.room3 = String.valueOf(rooms.get(3));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return hallway.equals(key.hallway) &&
                room0.equals(key.room0) &&
                room1.equals(key.room1) &&
                room2.equals(key.room2) &&
                room3.equals(key.room3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hallway, room0, room1, room2, room3);
    }
}

class Pair<X, Y> {
    X first;
    Y second;

    public Pair(X first, Y second) {
        this.first = first;
        this.second = second;
    }

    public X getFirst() {
        return first;
    }

    public Y getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "{" + first +
                ", " + second +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}