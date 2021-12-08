package org.example.advent.day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main2 {

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		int position = 0;
		int depth = 0;
		int aim = 0;
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
		for (String line : values) {
			String[] chunks = line.split(" ");
			Integer value = Integer.valueOf(chunks[1]);
			switch(chunks[0]) {
			case "forward":
				position += value;
				depth += aim*value;
				break;
			case "down":
				aim += value;
				break;
			case "up":
				aim -= value;
				break;
			}
			System.out.println("Position: " + position + ", depth: " + depth);
		}
		System.out.println(position*depth);
	}
	
}
