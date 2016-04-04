package dg;

import java.awt.event.KeyEvent;
import java.util.LinkedList;

import dg.action.Action;
import dg.gui.ImageCache;

public class Player extends Agent {
	public static final Integer MOVES_PER_TURN_PLAYER = 3;
	public static final Integer ACTIONS_PER_TURN_PLAYER = 1;

	private Integer actionsLeft = 0;
	private String identity;

	public Player(Coordinates spawnpoint, Gameboard board, String identity) {
		super(spawnpoint, board);
		this.affiliation = Affiliation.PLAYER;
		this.position = spawnpoint;
		this.movesPerTurn = MOVES_PER_TURN_PLAYER;
		this.movesLeft = 0;
		this.identity = identity;
		this.computerControlled = false;
	}

	@Override
	public void kill() {
		GameState.playerKilled();
		// TODO Do we need to remove Player figure?
	}

	@Override
	public void takeTurn() {
		movesLeft = movesPerTurn;
		actionsLeft = ACTIONS_PER_TURN_PLAYER;

		while (actionsLeft > 0 || movesLeft > 0) {
			LinkedList<Action> actionOptions = new LinkedList<Action>();

			if (actionsLeft > 0) {
				actionOptions.addAll(generateActionOptions());
			}

			if (movesLeft > 0) {
				LinkedList<Coordinates> moveOptions = getMoveOptions(position);
				actionOptions.addAll(generateMoveActions(moveOptions));
			}

			Action action = GameState.getPlayerAction(actionOptions);

			try {
				action.execute();
			} catch (GameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (Agent agent : board.getAgents()) {
				if (agent.getPosition().equals(position) && agent.getAffiliation().equals(Affiliation.DUNGEON)) {
					this.kill();
				}
			}

			if (board.getTerrain(position).equals(Terrain.EXIT)) {
				GameState.levelComplete();
			}
		}
	}

	private LinkedList<Action> generateMoveActions(LinkedList<Coordinates> moveOptions) {
		LinkedList<Action> actionOptions = new LinkedList<Action>();
		
		for(Coordinates move : moveOptions) {
			Direction d = Direction.getDirectionFromCoordinates(position, move);
			Action moveAction = new Action(d.name()) {
				@Override
				public void execute() throws GameException {
					GameState.getActiveAgent().performMoveAction(position);
				}
			};
			actionOptions.add(moveAction);
		}
		return actionOptions;
	}

	private LinkedList<Action> generateActionOptions() {
		LinkedList<Action> actionOptions = new LinkedList<Action>();
		Action endTurnAction = new Action("SPACE: Zug beenden", KeyEvent.VK_SPACE) {
			@Override
			public void execute() {
				GameState.getActiveAgent().endTurn();
			}
		};
		actionOptions.add(endTurnAction);

		Action peek = new Action("Peek") {
			@Override
			public void execute() {
				GameState.getActiveAgent().endMovementInProgress();
				//TODO: Get direction as input, show Vision from field additionally until next action.
				System.out.println("Peek not implemented yet.");
			}
		};
		actionOptions.add(peek);

		Action sprint = new Action("Sprint") {
			@Override
			public void execute() {
				GameState.getActiveAgent().endMovementInProgress();
				GameState.getActiveAgent().addMoves(2); // TODO: Add chance of alert and make random amount.
			}
		};
		actionOptions.add(sprint);
		
		return actionOptions;
	}

	public String getIdentity() {
		return identity;
	}

	@Override
	public String getImage() {
		return ImageCache.ROGUERIGO;
	}

	@Override
	public void endTurn() {
		movesLeft = 0;
		actionsLeft = 0;
	}
}
