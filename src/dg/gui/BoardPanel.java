package dg.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;

import javax.swing.JPanel;

import dg.Agent;
import dg.Coordinates;
import dg.GameState;
import dg.Terrain;
import dg.action.DebugPathfindingAction;
import dg.gui.input.BoardMouseListener;
import dg.gui.input.Dialog;
import dg.gui.input.DialogMouseListener;
import dg.gui.input.Menu;
import dg.gui.input.MenuMouseListener;
import dg.gui.input.Selection;

public class BoardPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static final boolean DRAW_TEXTS = false;
	private static final boolean DRAW_MOUSE = false;
	
	private static final Stroke MOUSEOVER_STROKE = new BasicStroke(1);
	private static final Stroke GRID_STROKE = new BasicStroke(1);
	private static final Stroke DEBUG_PATHFINDING_STROKE = new BasicStroke(3);
	
	private static BoardPanel instance = null;
	
	public static double translateX = 0;
	public static double translateY = 0;
	public static double scale = 1;

	public static int mouseX = 0;
	public static int mouseY = 0;
	
	public static BoardPanel getInstance() {
		if (instance == null) {
			instance = new BoardPanel();
		}
		return instance;
	}
	
	private BoardPanel() {
		this.setBackground(Colors.BOARD_PANEL_BACKGROUND); // coffee brown!
		
		Dimension fullscreenDim = GUIUtils.getFullScreenBounds();
		translateX = fullscreenDim.getWidth() / 2;
		translateY = fullscreenDim.getHeight() / 2;
		
		addBoardListeners();
	}
	
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
		Dialog.resize(); 
		Menu.getInstance().resizeButtons(); 
	}
	
	public void addBoardListeners() {
		// board
		this.addMouseMotionListener(BoardMouseListener.getInstance());
		this.addMouseListener(BoardMouseListener.getInstance());
		this.addMouseWheelListener(BoardMouseListener.getInstance());
		
		// menu
		this.addMouseMotionListener(MenuMouseListener.getInstance());
		this.addMouseListener(MenuMouseListener.getInstance());
		
		// dialog
		this.addMouseListener(DialogMouseListener.getInstance());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		GUIUtils.setRenderingHints(g2);
	    
		Shape hex = Shapes.getShape(Shapes.HEX);
		
		Hashtable<Coordinates, Terrain> grid = GameState.getBoard().getGrid();
		for (Coordinates c : grid.keySet()) {
			Point2D hexOffset = GUIUtils.getHexOffset(c);
			AffineTransform hexTransform = getHexTransform(c, hexOffset);
			
			// hex texture
			Image image = ImageCache.getImage(c, grid.get(c));
			if (image != null) {
				g2.setTransform(hexTransform);
				g2.setClip(hex);
				double r = Shapes.HEX_RADIUS;
				double h = Shapes.HEX_TRIANGLE_HEIGHT;
				g2.drawImage(image, (int) -r - 1, (int) -h - 1, (int) (2 * r) + 2, (int) (2 * h) + 2, null);
				g2.setTransform(GUIUtils.IDENTITY_TRANSFORM);
				g2.setClip(null);
			}

			if (DRAW_TEXTS) {
				AffineTransform textTransform = getHexTextTransform(hexOffset);
				g2.setTransform(textTransform);
				
				g2.setColor(Color.white);
				String string = c.toString();
				Rectangle2D stringRect = g2.getFontMetrics().getStringBounds(string, null); // ok, it's only for debugging
				g2.drawString(string, (int) (-stringRect.getWidth() / 2), (int) (stringRect.getHeight() / 2));
				
				g2.setTransform(GUIUtils.IDENTITY_TRANSFORM);
			}
		}

		for (Coordinates c : grid.keySet()) {
			Point2D hexOffset = GUIUtils.getHexOffset(c);
			AffineTransform hexTransform = getHexTransform(c, hexOffset);
			
			// grid
			if (GameState.getBoard().getTerrain(c) == Terrain.WALL) {
				g2.setColor(Colors.GRID);
				g2.setStroke(GRID_STROKE);
				g2.draw(hexTransform.createTransformedShape(hex));
			}
		}
		
		// mouseover
		if (Selection.isSelectionMode()) {
			Coordinates c = GameState.getMouseoverCoordinates();
			if (c != null) {
				Point2D hexOffset = GUIUtils.getHexOffset(c);
				AffineTransform hexTransform = getHexTransform(c, hexOffset);
				g2.setColor(Colors.HEX_MOUSEOVER);
				g2.setStroke(MOUSEOVER_STROKE);
				g2.draw(hexTransform.createTransformedShape(hex));
			}
		}
		
		// Wegfindung debuggen
		if (DebugPathfindingAction.isActive()) {
			for (Coordinates c : DebugPathfindingAction.getPath()) {
				Point2D hexOffset = GUIUtils.getHexOffset(c);
				AffineTransform hexTransform = getHexTransform(c, hexOffset);
				g2.setColor(Colors.DEBUG_PATHFINDING_COLOR);
				g2.setStroke(DEBUG_PATHFINDING_STROKE);
				g2.draw(hexTransform.createTransformedShape(hex));
			}
		}
		
		// agents
		for (Agent agent : GameState.getBoard().getAgents()) {
			agent.paintBeforeAgents(g2);
		}
		for (Agent agent : GameState.getBoard().getAgents()) {
			agent.paintAgent(g2);
		}
		for (Agent agent : GameState.getBoard().getAgents()) {
			agent.paintAfterAgents(g2);
		}
		
		// menu
		Menu.paintMenu(g2);
		
		// selection help text
		if (Selection.isSelectionMode() && Selection.getHelpText() != null) {
			g2.setFont(Fonts.getFont(Fonts.SELECTION_HELP));
			g2.drawString(Selection.getHelpText(), 10, 40);
		}
		
		// dialog
		Dialog.paintDialog(g2);
		
		// mouse
		if (DRAW_MOUSE) {
			g2.setColor(Color.white);
			g2.drawLine(mouseX - 10, mouseY, mouseX + 10, mouseY);
			g2.drawLine(mouseX, mouseY - 10, mouseX, mouseY + 10);
		}
	};
	
	public static void updateMouseoverCoordinates() {
		Coordinates old = GameState.getMouseoverCoordinates();
		Coordinates newCoordinates = null;
		
		Shape hex = Shapes.getShape(Shapes.HEX);
		Hashtable<Coordinates, Terrain> grid = GameState.getBoard().getGrid();
		for (Coordinates c : grid.keySet()) {
			if (BoardPanel.getTransformedHex(c, hex).contains(BoardPanel.mouseX, BoardPanel.mouseY)) {
				newCoordinates = c;
				break;
			}
		}
		GameState.setMouseoverCoordinates(newCoordinates);
		
		if (old == null || !old.equals(newCoordinates)) {
			BoardPanel.getInstance().repaint();
		}
	}
	
	private static Shape getTransformedHex(Coordinates c, Shape hex) {
		Point2D hexOffset = GUIUtils.getHexOffset(c);
		AffineTransform hexTransform = getHexTransform(c, hexOffset);
		return hexTransform.createTransformedShape(hex);
	}

	public static AffineTransform getHexTransform(Coordinates c, Point2D hexOffset) {
		AffineTransform hexTransform = new AffineTransform();
		hexTransform.translate(translateX, translateY);
		hexTransform.scale(scale, scale);
		hexTransform.translate(hexOffset.getX(), hexOffset.getY());
		if (GameState.getBoard().getTerrain(c) == Terrain.WALL) {
			hexTransform.rotate((GUIUtils.getWallRotation(c)) * (2 * Math.PI / 6));
		}
		return hexTransform;
	}
	
	private static AffineTransform getHexTextTransform(Point2D hexOffset) {
		AffineTransform textTransform = new AffineTransform();
		textTransform.translate(translateX, translateY);
		textTransform.scale(scale, scale);
		textTransform.translate(hexOffset.getX(), hexOffset.getY());
		textTransform.scale(1 / scale, 1 / scale);
		return textTransform;
	}
	
}
