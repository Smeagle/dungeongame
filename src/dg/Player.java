package dg;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import dg.action.Action;
import dg.gui.ImageCache;
import dg.gui.input.Button;

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
			List<Button> actionOptions = new LinkedList<Button>();

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

	private List<Button> generateMoveActions(List<Coordinates> moveOptions) {
		List<Button> buttons = new LinkedList<Button>();
		
		for(Coordinates move : moveOptions) {
			Direction d = Direction.getDirectionFromCoordinates(position, move);
			Button moveButton = new Button(d.name(), new Action() {
				@Override
				public void execute() throws GameException {
					GameState.getActiveAgent().performMoveAction(position);
				}
			});
			buttons.add(moveButton);
		}
		return buttons;
	}

	private List<Button> generateActionOptions() {
		List<Button> buttons = new LinkedList<Button>();
		
		Button endTurnButton = new Button("SPACE: Zug beenden", new Action() {
			@Override
			public void execute() {
				GameState.getActiveAgent().endTurn();
			}
		}, KeyEvent.VK_SPACE);
		buttons.add(endTurnButton);

		Button peek = new Button("Peek", new Action() {
			@Override
			public void execute() {
				GameState.getActiveAgent().endMovementInProgress();
				//TODO: Get direction as input, show Vision from field additionally until next action.
				System.out.println("Peek not implemented yet.");
			}
		});
		buttons.add(peek);

		Button sprint = new Button("Sprint", new Action() {
			@Override
			public void execute() {
				GameState.getActiveAgent().endMovementInProgress();
				GameState.getActiveAgent().addMoves(2); // TODO: Add chance of alert and make random amount.
			}
		});
		buttons.add(sprint);
		
		return buttons;
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
