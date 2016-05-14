package dg;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class GameBoardUtils {

	public static Gameboard boardGenerator(File file) throws IOException {
		List<String> strings = Files.readAllLines(Paths.get(file.getPath()));
		StringBuilder sb = new StringBuilder();
		for (String s : strings) {
			sb.append(s);
		}
		return boardGenerator(sb.toString());
	}
	
	/**
	 * Expects String of field symbols. Row ends are demarkated with " $ ".
	 * 
	 * @param boardString
	 *            String representation of board.
	 * @return Gameboard generated from String.
	 */
	public static Gameboard boardGenerator(String boardString) {
		String[] fields = boardString.split("\\s+");
		if (fields.length == 0 || fields[0] == "$") {
			throw new IllegalArgumentException();
		}
		Gameboard board = new Gameboard();

		Integer maxFieldsPerRow = -1;
		LinkedList<LinkedList<String>> rows = new LinkedList<LinkedList<String>>();

		int rowNumber = 0;
		for (String field : fields) {
			if (field.trim().isEmpty()) {
				continue;
			}
			if (isDelimiter(field) == false) {
				if (rows.size() < rowNumber + 1) {
					rows.add(new LinkedList<String>());
				}
				rows.get(rowNumber).add(field);
			} else {
				if (rows.get(rowNumber) != null) {
					int entriesInRow = rows.get(rowNumber).size();
					maxFieldsPerRow = Math.max(entriesInRow, maxFieldsPerRow);
					rowNumber = rowNumber + 1;
				}
			}
		}

		int rowOffset = -1 * (rows.size() / 2);
		int globalColumnOffset = -1 * (rows.size() / 4);
		int columnOffset = 0;
		for (int r = 0; r < rows.size(); r++) {
			int rowSize = rows.get(r).size();
			
			if (r > 0) {
				int rowSizeDiff = rowSize - rows.get(r - 1).size();
				columnOffset += (rowSizeDiff >= 0 ? 1 : 0) + rowSizeDiff / 2;
			}
			
			for (int q = 0; q < rowSize; q++) {
				Coordinates c = new Coordinates(q - columnOffset - globalColumnOffset, r + rowOffset);
				System.out.println("q = " + q + ", r = " + r + ", resulting Coordinates = " + c.toString());
				parseField(board, c, rows.get(r).get(q));
			}
		}

		return board;
	}

	private static boolean isDelimiter(String field) {
		return field.equals("$");
	}

	private static void parseField(Gameboard board, Coordinates c, String field) {
		LinkedList<Coordinates> route = new LinkedList<Coordinates>();
		route.add(c);
		if (field.equals("W")) {
			board.addField(c, Terrain.WALL);
		} else if (field.equals("F")) {
			board.addField(c, Terrain.FLOOR);
		} else if (field.equals("E")) {
			board.addField(c, Terrain.EXIT);
		} else if (field.equals("G")) {
			board.addField(c, Terrain.FLOOR);
			board.addGuard(c, route);
		} else if (field.equals("P")) {
			board.addField(c, Terrain.FLOOR);
			board.addPlayer(c, "Player1");
		}
	}
}
