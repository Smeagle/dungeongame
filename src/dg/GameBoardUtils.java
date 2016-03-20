package dg;

import java.util.LinkedList;

public class GameBoardUtils {

	public static Gameboard boardBuilder(String boardString) {
		String[] fields = boardString.split("\\s+");
		Gameboard board = new Gameboard();

		Integer maxFieldsPerRow = -1;
		LinkedList<LinkedList<String>> rows = new LinkedList<LinkedList<String>>();

		int rowNumber = 0;
		for (String field : fields) {
			if (isDelimiter(field) == false) {
				rows.get(rowNumber).add(field);
			} else {
				int entriesInRow = rows.get(rowNumber).size();
				maxFieldsPerRow = Math.max(entriesInRow, maxFieldsPerRow);
				rowNumber = rowNumber + 1;
			}
		}

		int rowOffset = -1 * rows.size() / 2;
		int columnOffset = 0;
		boolean growing = true;
		// Assumes that center row has most fields and rest is shaped hexagonally.
		for (int r = 0; r < rows.size(); r++) {
			int rowSize = rows.get(r).size();

			for (int q = 0; q < rowSize; q++) {
				Coordinates c = new Coordinates(q + columnOffset, r + rowOffset);
				parseField(board, c, rows.get(r).get(q));

				if (growing) {
					columnOffset = columnOffset - 1;
				}

				if (rowSize == maxFieldsPerRow) {
					growing = false;
				}
				rowOffset = rowOffset + 1;
			}
		}
		return board;
	}

	private static boolean isDelimiter(String field) {
		return field == "$";
	}

	private static void parseField(Gameboard board, Coordinates c, String field) {
		LinkedList<Coordinates> route = new LinkedList<Coordinates>();
		route.add(c);
		if (field == "W") {
			board.addField(c, Terrain.WALL);
		} else if (field == "F") {
			board.addField(c, Terrain.FLOOR);
		} else if (field == "E") {
			board.addField(c, Terrain.EXIT);
		} else if (field == "G") {
			board.addField(c, Terrain.FLOOR);
			board.addGuard(c, route);
		} else if (field == "P") {
			board.addField(c, Terrain.FLOOR);
			board.addPlayer(c, "Player1");
		}
	}
}
