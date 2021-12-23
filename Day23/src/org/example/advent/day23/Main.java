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

    private static final Map<Character, Integer> ENERGY_COSTS = Map.of(
            'A', 1,
            'B', 10,
            'C', 100,
            'D', 1000
    );

    private static final List<Integer> HALLWAY_POSITIONS = List.of(0, 1, 3, 5, 7, 9, 10);

    private static Map<Integer, Integer> ROOM_EXITS = Map.of(
            0, 2,
            1, 4,
            2, 6,
            3, 8
    );

    private static Map<Character, Integer> LETTER_TO_ROOM = Map.of(
            'A', 0,
            'B', 1,
            'C', 2,
            'D', 3
    );
    private static Map<Integer, Character> ROOM_TO_LETTER = Map.of(
            0, 'A',
            1, 'B',
            2, 'C',
            3, 'D'
    );

    public static void main(String[] args) throws IOException {
        List<String> lines = readInput();

        int[] hallway = new int[11];
        Arrays.fill(hallway, '.');

        String line1 = lines.get(2).replace("#", "").trim();
        String line2 = lines.get(3).replace("#", "").trim();

        List<int[]> roomsPart1 = new ArrayList<>();
        IntStream.range(0, 4)
                .forEach(roomId -> roomsPart1.add(new int[2]));
        IntStream.range(0, 4)
                .forEach(roomId -> roomsPart1.get(roomId)[0] = line1.charAt(roomId));
        IntStream.range(0, 4)
                .forEach(roomId -> roomsPart1.get(roomId)[1] = line2.charAt(roomId));

        List<String> part2RoomSupplements = List.of(
                "DD", "CB", "BA", "AC"
        );
        List<int[]> roomsPart2 = new ArrayList<>();
        for (int i = 0; i < roomsPart1.size(); i++) {
            String supplement = part2RoomSupplements.get(i);
            int[] room = roomsPart1.get(i);
            int[] newRoom = new int[4];
            newRoom[0] = room[0];
            newRoom[3] = room[1];
            newRoom[1] = supplement.charAt(0);
            newRoom[2] = supplement.charAt(1);
            roomsPart2.add(newRoom);
        }
        //System.out.println(roomsPart1.stream().map(Main::arrayToString).collect(Collectors.joining(", ")));
        //System.out.println(roomsPart2.stream().map(Main::arrayToString).collect(Collectors.joining(", ")));

        Instant beginTime = Instant.now();
        PriorityQueue<Long> costs = new PriorityQueue<>();
        costs.add(Long.MAX_VALUE);
        long minCost = trySolve(hallway, roomsPart1, 0L, costs);

        System.out.println("Partial solutions: " + costs);
        System.out.println("Part1: " + minCost);
        System.out.println("Time: " + Duration.between(beginTime, Instant.now()).toSeconds() + " s");

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        Arrays.fill(hallway, '.');

        beginTime = Instant.now();
        costs = new PriorityQueue<>();
        costs.add(Long.MAX_VALUE);
        long minCostPart2 = trySolve(hallway, roomsPart2, 0L, costs);

        System.out.println("Partial solutions: " + costs);
        System.out.println("Part2: " + minCostPart2);
        System.out.println("Time: " + Duration.between(beginTime, Instant.now()).toSeconds() + " s");

        System.out.println("HURRRAY, IT FINISHED!!!!!!!!!!!!!!!!!11111");
    }

    private static long trySolve(int[] hallway, List<int[]> rooms, long currentCost, PriorityQueue<Long> costs) {
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
            for (int hallwayPos : HALLWAY_POSITIONS) {
                for (int turn = 0; turn < 2; turn++) {
                    if (turn == 0) {
                        if (canMoveOut(roomId, hallwayPos, hallway, rooms)) {
                            int cost = moveOut(roomId, hallwayPos, hallway, rooms);

                            long moveCost = trySolve(hallway, rooms, currentCost + cost, costs);
                            if (moveCost < minCost) {
                                minCost = moveCost;
                            }
                            moveIn(roomId, hallwayPos, hallway, rooms);
                            moved = true;
                        }
                    } else {
                        if (canMoveIn(roomId, hallwayPos, hallway, rooms)) {
                            int cost = moveIn(roomId, hallwayPos, hallway, rooms);
                            long moveCost = trySolve(hallway, rooms, currentCost + cost, costs);
                            if (moveCost < minCost) {
                                minCost = moveCost;
                            }
                            moveOut(roomId, hallwayPos, hallway, rooms);
                            moved = true;
                        }
                    }
                }
            }
        }
        if (moved) {
            if (minCost < costs.peek()) {
                costs.add(minCost);
            }
            return minCost;
        } else {
            return Long.MAX_VALUE;
        }
    }

    private static String arrayToString(int[] array) {
        return Arrays.stream(array)
                .mapToObj(c -> Character.toString((char) c))
                .collect(Collectors.joining(","));
    }

    private static void printBoard(int[] hallway, List<int[]> rooms) {
        String hall = Arrays.stream(hallway).mapToObj(c -> Character.toString(c)).collect(Collectors.joining());
        System.out.println(hall);
        for (int i = 0; i < rooms.get(0).length; i++) {
            System.out.print("##");
            for (int[] room : rooms) {
                System.out.print(room[i] + "#");
            }
            System.out.println("#");
        }
        System.out.println("----------------------");
    }

    private static boolean allSet(List<int[]> rooms) {
        return Arrays.stream(rooms.get(0)).allMatch(c -> c == 'A')
                && Arrays.stream(rooms.get(1)).allMatch(c -> c == 'B')
                && Arrays.stream(rooms.get(2)).allMatch(c -> c == 'C')
                && Arrays.stream(rooms.get(3)).allMatch(c -> c == 'D');
    }

    private static boolean canMoveOut(int roomId, int hallwaypos, int[] hallway, List<int[]> rooms) {
        if (hallway[hallwaypos] != '.') {
            return false;
        }
        int roomExit = ROOM_EXITS.get(roomId);
        if (hallwaypos == roomExit) return false;

        int roomLetter = ROOM_TO_LETTER.get(roomId);
        int[] room = rooms.get(roomId);
        if (isRoomEmptyOrSettled(room, roomLetter)) {
            return false;
        }
        int begin = roomExit < hallwaypos ? roomExit : hallwaypos;
        int end = roomExit > hallwaypos ? roomExit : hallwaypos;

        return isHallwayEmpty(hallway, begin, end);
    }

    private static boolean canMoveIn(int roomId, int hallwaypos, int[] hallway, List<int[]> rooms) {
        if (hallway[hallwaypos] == '.') {
            return false;
        }
        int letter = hallway[hallwaypos];
        if (LETTER_TO_ROOM.get((char) letter) != roomId) {
            return false;
        }
        int[] room = rooms.get(roomId);
        if (!isRoomEmptyOrSettled(room, letter)) {
            return false;
        }
        int roomExit = ROOM_EXITS.get(roomId);

        int begin = roomExit < hallwaypos ? roomExit : hallwaypos + 1;
        int end = roomExit > hallwaypos ? roomExit : hallwaypos - 1;

        return isHallwayEmpty(hallway, begin, end);
    }

    private static int moveOut(int roomId, int hallwaypos, int[] hallway, List<int[]> rooms) {
        int[] room = rooms.get(roomId);

        int roomPos = findFirstLetterInRoom(room);
        int toMove = room[roomPos];
        int moves = roomPos + 1;
        room[roomPos] = '.';

        int roomExit = ROOM_EXITS.get(roomId);
        hallway[hallwaypos] = toMove;
        moves += Math.abs(hallwaypos - roomExit);

        int stepCost = ENERGY_COSTS.get((char) toMove);
        return stepCost * moves;
    }

    private static int moveIn(int roomId, int hallwaypos, int[] hallway, List<int[]> rooms) {
        int[] room = rooms.get(roomId);
        int moves = 0;

        int toMove = hallway[hallwaypos];
        int roomExit = ROOM_EXITS.get(roomId);
        moves += Math.abs(hallwaypos - roomExit);
        hallway[hallwaypos] = '.';

        int roomPos = findFirstLetterInRoom(room) - 1;
        room[roomPos] = toMove;
        moves += roomPos + 1;

        int stepCost = ENERGY_COSTS.get((char) toMove);
        return stepCost * moves;
    }

    private static boolean isHallwayEmpty(int[] hallway, int begin, int end) {
        boolean hallwayEmpty = true;
        for (int i = begin; i <= end; i++) {
            if (hallway[i] != '.') {
                hallwayEmpty = false;
                break;
            }
        }
        return hallwayEmpty;
    }

    private static boolean isRoomEmptyOrSettled(int[] room, int roomLetter) {
        boolean roomEmpty = true;
        for (int letter : room) {
            if (letter != '.' && letter != roomLetter) {
                roomEmpty = false;
                break;
            }
        }
        return roomEmpty;
    }

    private static int findFirstLetterInRoom(int[] room) {
        int roomPos = room.length;
        for (int i = 0; i < room.length; i++) {
            if (room[i] != '.') {
                roomPos = i;
                break;
            }
        }
        return roomPos;
    }

    private static List<String> readInput() throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            lines = reader.lines().collect(Collectors.toList());
        }
        return lines;
    }
}
