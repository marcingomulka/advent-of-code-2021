package org.example.advent.day8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main2 {

	public static void main(String[] args) throws IOException {
		List<String> lines = readInput();
		long result = 0L;
		
		for (String line : lines) {
			String[] chunks = line.split("\\|");
			String pattern = chunks[0].trim();
			String output = chunks[1].trim();
			
			Map<Integer, Set<Integer>> recognized = new HashMap<>();
			List<String> patternChunks = Stream.of(pattern.split("\\s+")).map(String::trim).collect(Collectors.toList());
			
			patternChunks.stream()
				.forEach(value -> recognizeUniques(value, recognized));
			patternChunks.stream()
				.filter(value -> value.length() == 5)
				.forEach(value -> recognizeFiveSegments(value, recognized));
			patternChunks.stream()
				.filter(value -> value.length() == 6)
				.forEach(value -> recognizeSixSegments(value, recognized));
			
			long number = Long.valueOf(Stream.of(output.split("\\s+"))
				.map(String::trim)
				.map(value -> match(value, recognized))
				.collect(Collectors.joining()));
			
			//System.out.println(number);
			result += number;
				
		}
		System.out.println(result);
	}

	
	private static String match(String value, Map<Integer, Set<Integer>> recognized) {
		Set<Integer> segments = value.chars().boxed().collect(Collectors.toSet());
		
		return recognized.entrySet().stream()
			.filter(entry -> entry.getValue().equals(segments))
			.map(entry -> entry.getKey())
			.map(String::valueOf)
			.findFirst()
			.orElse("X");
	}


	private static void recognizeSixSegments(String digit, Map<Integer, Set<Integer>> recognized) {
		Set<Integer> segments = digit.chars().boxed().collect(Collectors.toSet());
		Set<Integer> one = recognized.get(1);
		Set<Integer> four = recognized.get(4);
		
		if (segments.containsAll(one) && segments.containsAll(four)) {
			recognized.put(9, segments);
			
		} else if (segments.containsAll(one) && !segments.containsAll(four)) {
			recognized.put(0, segments);
			
		} else {
			recognized.put(6, segments);
		}
	}


	private static void recognizeFiveSegments(String digit, Map<Integer, Set<Integer>> recognized) {
		Set<Integer> segments = digit.chars().boxed().collect(Collectors.toSet());
		Set<Integer> one = recognized.get(1);
		Set<Integer> fourMinusOne = new HashSet<>(recognized.get(4));
		fourMinusOne.removeAll(one);
		
		if (segments.containsAll(one)) {
			recognized.put(3, segments);
			
		} else if (segments.containsAll(fourMinusOne)) {
			recognized.put(5, segments);
			
		} else {
			recognized.put(2, segments);
		}
	}


	private static void recognizeUniques(String digit, Map<Integer, Set<Integer>> recognized) {
		Set<Integer> segments = digit.chars().boxed().collect(Collectors.toSet());
		if (digit.length() == 2) {
			recognized.put(1, segments);
			
		} else if (digit.length() == 3) {
			recognized.put(7, segments);
			
		} else if (digit.length() == 4) {
			recognized.put(4, segments);
			
		} else if (digit.length() == 7) {
			recognized.put(8, segments);
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

