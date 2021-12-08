package org.example.advent.day5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main2 {

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
		
		List<Line> lines = new ArrayList<>();
		for (String value : values) {
			String[] points = value.split("->");
			String[] first = points[0].trim().split(",");
			String[] second = points[1].trim().split(",");
			
			Line line = new Line(Integer.valueOf(first[0]), Integer.valueOf(first[1]), Integer.valueOf(second[0]), Integer.valueOf(second[1]));
			lines.add(line);
			//if (line.isHorizontalOrVertical()) {
			//	lines.add(line);
			//}
		}
		//lines.forEach(System.out::println);
		int maxY = lines.stream()
				.mapToInt(line -> Math.max(line.y1, line.y2))
				.max()
				.orElse(0);
		int maxX = lines.stream()
				.mapToInt(line -> Math.max(line.x1, line.x2))
				.max()
				.orElse(0);
		
		int[][] board = new int[maxX+1][maxY+1];
		
		lines.forEach(line -> drawLine(line, board));
		
		//System.out.println(Arrays.deepToString(board));
		int result = countIntersections(board);
		System.out.println(result);
	}

	private static int countIntersections(int[][] board) {
		int sum = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] >= 2) {
					sum++;
				}
			}
		}
		return sum;
	}

	private static void drawLine(Line line, int[][] board) {
		int firstX = line.x1 < line.x2 ? line.x1 : line.x2;
		int lastX = line.x1 > line.x2 ? line.x1 : line.x2;
		int firstY = line.y1 < line.y2 ? line.y1 : line.y2;
		int lastY = line.y1 > line.y2 ? line.y1 : line.y2;
		if (line.isHorizontal()) {
			for (int i = firstX; i <= lastX; i ++) {
				board[i][line.y1]++;
			}
		} else if (line.isVertical()) {
			for (int i = firstY; i <= lastY; i++) {
				board[line.x1][i]++;
			}
		} else if (line.isDiagonal()) {
			drawDiagonal(line, board);
		}
	}
	
	private static void drawDiagonal(Line line, int[][]board) {
		boolean ascending = true;
		if ((line.x1 - line.x2 > 0) ^ (line.y1 - line.y2 > 0)) {
			ascending = false;
		}
		int firstX = line.x1 < line.x2 ? line.x1 : line.x2;
		int lastX = line.x1 > line.x2 ? line.x1 : line.x2;
		int firstY = line.y1 < line.y2 ? line.y1 : line.y2;
		int lastY = line.y1 > line.y2 ? line.y1 : line.y2;
		if (ascending) {
			for (int i = 0; i <= (lastX - firstX); i++) {
				board[firstX + i][ firstY + i]++;
			}
		} else {
			for (int i = 0; i <= (lastX - firstX); i++) {
				board[firstX + i][ lastY - i]++;
			}
		}
	}
}
