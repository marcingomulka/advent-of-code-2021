package org.example.advent.day7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) throws IOException {
		List<String> lines = readInput();
		List<Integer> positions = Arrays.stream(lines.get(0).split(","))
				.map(Integer::valueOf)
				.collect(Collectors.toList());
		int maxPos = positions.stream().mapToInt(val -> val).max().orElse(0);
		
		long minFuel = Long.MAX_VALUE;
		int minPos = 0;
		for (int i = 0; i < maxPos; i++) {
			int currPos = i;
			long fuel = positions.stream()
					.mapToInt(val -> val)
					.map(val -> Math.abs(val - currPos))
					.sum();
			if (fuel < minFuel) {
				minFuel = fuel;
				minPos = currPos;
			}
		}
		System.out.println(minFuel);
	}

	
	private static List<String> readInput() throws IOException {
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			lines = reader.lines().collect(Collectors.toList());
		}
		return lines;
	}
}
