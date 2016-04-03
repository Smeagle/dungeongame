package dg;

import java.util.LinkedList;

import dg.gui.ImageCache;

public class Player extends Agent {
	public static final Integer MOVES_PER_TURN_PLAYER = 3;// TODO magic number
	private String identity;

	public Player(Coordinates spawnpoint, Gameboard board, String identity) {
		super(spawnpoint, board);
		this.affiliation = Affiliation.PLAYER;
		this.position = spawnpoint;
		this.movesPerTurn = MOVES_PER_TURN_PLAYER;
		this.movesLeft = 0;
		this.identity = identity;
	}

	@Override
	public void kill() {
		DungeonGame.playerKilled();
		// TODO Do we need to remove Player figure?
	}

	@Override
	public void takeTurn() {
		movesLeft = movesPerTurn;

		while (movesLeft > 0) {
			LinkedList<Coordinates> moveOptions = getMoveOptions(position);
			Direction move = DungeonGame.getPlayerMove(moveOptions);
			position = new Coordinates(position, move);
			movesLeft = movesLeft - 1;

			for (Agent agent : board.getAgents()) {
				if (agent.getPosition() == position && agent.getAffiliation() == Affiliation.DUNGEON) {
					this.kill();
				}
			}

			if (board.getTerrain(position) == Terrain.EXIT) {
				DungeonGame.levelComplete();
			}
		}
	}

	public String getIdentity() {
		return identity;
	}

	@Override
	public String getImage() {
		return ImageCache.ROGUERIGO;
	}
}
