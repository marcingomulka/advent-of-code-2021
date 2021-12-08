package org.example.advent.day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

	public static void main(String[] args) throws NumberFormatException, IOException {
		
		int position = 0;
		int depth = 0;
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
			switch(chunks[0]) {
			case "forward":
				position += Integer.valueOf(chunks[1]);
				break;
			case "down":
				depth += Integer.valueOf(chunks[1]);
				break;
			case "up":
				depth -= Integer.valueOf(chunks[1]);
				break;
			}
		}
		System.out.println(position*depth);
	}
	
}
