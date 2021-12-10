package org.example.advent.day4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) throws NumberFormatException, IOException {

        List<String> values = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            do {
                line = reader.readLine();
                if (Objects.nonNull(line)) {
                    values.add(line);
                }

            } while (Objects.nonNull(line));
        }
        List<Integer> drawNumbers = Stream.of(values.get(0).split(",")).map(Integer::valueOf).collect(Collectors.toList());

        List<Integer[][]> boards = new ArrayList<>();
        Integer[][] board = new Integer[5][5];
        int boardRowIndex = 0;
        for (int i = 1; i < values.size(); i++) {
            if (values.get(i).isEmpty()) {
                continue;
            }
            List<Integer> row = Stream.of(values.get(i).trim().split("\\s+")).map(Integer::valueOf)
                    .collect(Collectors.toList());
            for (int j = 0; j < row.size(); j++) {
                board[boardRowIndex][j] = row.get(j);
            }
            boardRowIndex++;
            if (boardRowIndex == 5) {
                //System.out.println(Arrays.deepToString(board));
                boards.add(board);
                board = new Integer[5][5];
                boardRowIndex = 0;
            }
        }

        List<boolean[][]> marks = new ArrayList<>();
        boards.forEach(b -> marks.add(new boolean[5][5]));

        Iterator<Integer> drawIt = drawNumbers.iterator();

        while (drawIt.hasNext()) {
            int draw = drawIt.next();
            for (int i = 0; i < boards.size(); i++) {
                Integer[][] currBoard = boards.get(i);
                boolean[][] mark = marks.get(i);
                mark(currBoard, mark, draw);
                if (isWinner(mark)) {
                    int result = calculateResult(currBoard, mark);
                    System.out.println(result * draw);
                    return;
                }
            }

        }
        //System.out.println(gamma*epsilon);
    }

    private static void mark(Integer[][] currBoard, boolean[][] mark, int draw) {
        for (int i = 0; i < currBoard.length; i++) {
            for (int j = 0; j < currBoard[i].length; j++) {
                if (currBoard[i][j] == draw) {
                    mark[i][j] = true;
                }
            }
        }
    }

    private static boolean isWinner(boolean[][] mark) {
        for (int i = 0; i < mark.length; i++) {
            boolean rowMatch = true;
            boolean colMatch = true;
            for (int j = 0; j < mark[i].length; j++) {
                if (!mark[i][j]) {
                    rowMatch = false;
                }
                if (!mark[j][i]) {
                    colMatch = false;
                }
            }
            if (rowMatch || colMatch) {
                return true;
            }
        }
        return false;
    }

    private static int calculateResult(Integer[][] currBoard, boolean[][] mark) {
        int sum = 0;
        for (int i = 0; i < currBoard.length; i++) {
            for (int j = 0; j < currBoard[i].length; j++) {
                if (!mark[i][j]) {
                    sum += currBoard[i][j];
                }
            }
        }
        return sum;
    }

}
