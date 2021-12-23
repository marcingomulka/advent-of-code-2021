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

    private static long cacheHit = 0L;

    private static Map<Character, Integer> energyCost = Map.of(
            'A', 1,
            'B', 10,
            'C', 100,
            'D', 1000
    );

    private static List<Integer> roomPossibleMoves = List.of(0, 1, 3, 5, 7, 9, 10);

    private static Map<Integer, Integer> roomExits = Map.of(
            0, 2,
            1, 4,
            2, 6,
            3, 8
    );

    private static Map<Character, Integer> targetRoom = Map.of(
            'A', 0,
            'B', 1,
            'C', 2,
            'D', 3
    );

    public static void main(String[] args) throws IOException {
        int[] hallway = new int[11];
        IntStream.range(0, hallway.length).forEach(i -> hallway[i] = '.');

        List<int[]> roomsPart1 = new ArrayList<>();
        int[] room1 = new int[]{'B', 'D'};
        roomsPart1.add(room1);
        int[] room2 = new int[]{'B', 'C'};
        roomsPart1.add(room2);
        int[] room3 = new int[]{'D', 'A'};
        roomsPart1.add(room3);
        int[] room4 = new int[]{'A', 'C'};
        roomsPart1.add(room4);

        List<int[]> roomsPart2 = new ArrayList<>();
        room1 = new int[]{'B', 'D', 'D', 'D'};
        roomsPart2.add(room1);
        room2 = new int[]{'B', 'C', 'B', 'C'};
        roomsPart2.add(room2);
        room3 = new int[]{'D', 'B', 'A', 'A'};
        roomsPart2.add(room3);
        room4 = new int[]{'A', 'A', 'C', 'C'};
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

    private static long trySolve(int[] hallway, List<int[]> rooms, long currentCost, PriorityQueue<Long> costs, Map<Key, Long> cache) {
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

    private static boolean allSet(List<int[]> rooms) {
        return rooms.get(0)[0] == 'A' && rooms.get(0)[1] == 'A'
                && rooms.get(1)[0] == 'B' && rooms.get(1)[1] == 'B'
                && rooms.get(2)[0] == 'C' && rooms.get(2)[1] == 'C'
                && rooms.get(3)[0] == 'D' && rooms.get(3)[1] == 'D';
    }

    private static boolean canMoveOut(int roomId, int hallwaypos, int[] hallway, List<int[]> rooms) {
        int[] room = rooms.get(roomId);
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

    private static boolean isTargetRoom(int roomId, int[] room) {
        int letter = room[0] == '.' ? room[1] : room[0];
        int target = targetRoom.get((char) letter);

        if (roomId == target) {
            return room[1] == letter && (room[0] == '.' || room[0] == letter);
        }
        return false;
    }

    private static boolean canMoveIn(int roomId, int hallwaypos, int[] hallway, List<int[]> rooms) {
        if (hallway[hallwaypos] == '.') {
            return false;
        }
        int letter = hallway[hallwaypos];
        if (targetRoom.get((char) letter) != roomId) {
            return false;
        }
        int[] room = rooms.get(roomId);
        if (!isRoomEmpty(room, letter)) {
            return false;
        }
        int roomExit = roomExits.get(roomId);

        int begin = roomExit < hallwaypos ? roomExit : hallwaypos + 1;
        int end = roomExit > hallwaypos ? roomExit : hallwaypos - 1;

        return IntStream.rangeClosed(begin, end)
                .allMatch(i -> hallway[i] == '.');
    }

    private static boolean isRoomEmpty(int[] room, int letter) {
        if (room[1] != '.' && room[1] != letter) {
            return false;
        } else return room[0] == '.';
    }

    private static int moveOut(int roomId, int hallwaypos, int[] hallway, List<int[]> rooms) {
        int[] room = rooms.get(roomId);
        int moves = 0;

        int toMove;
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

        int stepCost = energyCost.get((char) toMove);
        return stepCost * moves;
    }

    private static int moveIn(int roomId, int hallwaypos, int[] hallway, List<int[]> rooms) {
        int[] room = rooms.get(roomId);
        int moves = 0;

        int toMove = hallway[hallwaypos];
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
        int stepCost = energyCost.get((char) toMove);
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

    private final int[] hallway;
    private final int[] room0;
    private final int[] room1;
    private final int[] room2;
    private final int[] room3;


    Key(int[] hallway, List<int[]> rooms) {
        this.hallway = Arrays.copyOf(hallway, hallway.length);
        this.room0 = Arrays.copyOf(rooms.get(0), rooms.get(0).length);
        this.room1 = Arrays.copyOf(rooms.get(1), rooms.get(1).length);
        this.room2 = Arrays.copyOf(rooms.get(2), rooms.get(2).length);
        this.room3 = Arrays.copyOf(rooms.get(3), rooms.get(3).length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key key = (Key) o;
        return Arrays.equals(hallway, key.hallway) &&
                Arrays.equals(room0, key.room0) &&
                Arrays.equals(room1, key.room1) &&
                Arrays.equals(room2, key.room2) &&
                Arrays.equals(room3, key.room3);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(hallway);
        result = 31 * result + Arrays.hashCode(room0);
        result = 31 * result + Arrays.hashCode(room1);
        result = 31 * result + Arrays.hashCode(room2);
        result = 31 * result + Arrays.hashCode(room3);
        return result;
    }
}
