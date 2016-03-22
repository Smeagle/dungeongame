package dg;

import java.util.LinkedList;

public class GameBoardUtils {

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
		int columnOffset = 0;
		boolean growing = true;
		// Assumes that center row has most fields and rest is shaped hexagonally.
		for (int r = 0; r < rows.size(); r++) {
			int rowSize = rows.get(r).size();

			for (int q = 0; q < rowSize; q++) {
				Coordinates c = new Coordinates(q + columnOffset, r + rowOffset);
				System.out.println("q = " + q + ", r = " + r + ", resulting Coordinates = " + c.toString());
				parseField(board, c, rows.get(r).get(q));
			}
			
			if (rowSize == maxFieldsPerRow) {
				growing = false;
			}
			if (growing) {
				columnOffset = columnOffset - 1;
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
