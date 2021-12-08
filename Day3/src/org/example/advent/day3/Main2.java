package org.example.advent.day3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
		
		List<String> oxygenList = new ArrayList<>(values);
		List<String> co2List = new ArrayList<>(values);
		int position = 0;
		while (oxygenList.size() > 1 && position < 12) {
			oxygenList = filterMost(oxygenList, position);
			position++;
		}
		position = 0;
		while (co2List.size() > 1 && position < 12) {
			co2List = filterLeast(co2List, position);
			position++;
		}
		
		System.out.println("oxygen size " + oxygenList.size());
		System.out.println("CO2 size " + co2List.size());
		
		int oxygen = Integer.valueOf(oxygenList.get(0), 2);
		int co2 = Integer.valueOf(co2List.get(0), 2);
		
		System.out.println(co2 * oxygen);
	}
	
	static List<String> filterMost(List<String> input, int position) {
		long oneOccurences = input.stream()
			.filter(line -> line.charAt(position) == '1')
			.count();
		long zeroOccurences = input.size() - oneOccurences;
		if (oneOccurences >=  zeroOccurences) {
			return input.stream()
					.filter(line -> line.charAt(position) == '1')
					.collect(Collectors.toList());
		} else {
			return input.stream()
					.filter(line -> line.charAt(position) == '0')
					.collect(Collectors.toList());
		}
	}
	
	static List<String> filterLeast(List<String> input, int position) {
		long oneOccurences = input.stream()
			.filter(line -> line.charAt(position) == '1')
			.count();
		long zeroOccurences = input.size() - oneOccurences;
		if (oneOccurences >=  zeroOccurences) {
			return input.stream()
					.filter(line -> line.charAt(position) == '0')
					.collect(Collectors.toList());
		} else {
			return input.stream()
					.filter(line -> line.charAt(position) == '1')
					.collect(Collectors.toList());
		}
	}
	
}
