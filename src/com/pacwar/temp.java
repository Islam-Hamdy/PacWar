package com.pacwar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class temp {
	static boolean[][] map = new boolean[53][41];
	static BufferedReader buff;

	public static void main(String[] args) throws NumberFormatException,
			IOException {
		buff = new BufferedReader(new FileReader("Columns.txt"));
		addCols();

		buff = new BufferedReader(new FileReader("Rows.txt"));
		addRows();

		writeToFile(map);
	}

	private static void addCols() throws NumberFormatException, IOException {
		String in = "";
		StringTokenizer st;
		while ((in = buff.readLine()) != null) {
			st = new StringTokenizer(in);
			int col = Integer.parseInt(st.nextToken());
			int row1 = Integer.parseInt(st.nextToken()), row2 = Integer
					.parseInt(st.nextToken());
			openCells(row1 / 3, col / 3, row2 / 3, col / 3);
		}
	}

	private static void addRows() throws NumberFormatException, IOException {
		String in = "";
		StringTokenizer st;
		while ((in = buff.readLine()) != null) {
			st = new StringTokenizer(in);
			int row = Integer.parseInt(st.nextToken());
			int col1 = Integer.parseInt(st.nextToken()), col2 = Integer
					.parseInt(st.nextToken());
			openCells(row / 3, col1 / 3, row / 3, col2 / 3);
		}

	}

	private static void openCells(int x1, int y1, int x2, int y2) {
		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				map[i][j] = true;
			}
		}
	}

	private static void writeToFile(boolean[][] map2)
			throws FileNotFoundException {
		PrintWriter out = new PrintWriter(new File("initial_map.txt"));
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[i].length; j++) {
				out.print(map[i][j] ? '.' : 'x');
			}
			out.println();
		}
		out.close();
	}

}
