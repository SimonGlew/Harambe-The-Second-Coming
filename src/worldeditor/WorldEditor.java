package worldeditor;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import core.Board;
import core.GameSystem;
import core.GameSystem.Direction;
import core.Location;
import gameobjects.Chest;
import gameobjects.Tree;
import gameobjects.Wall;
import gameobjects.Fence;
import iohandling.BoardParser;
import iohandling.BoardWriter;
import items.Banana;
import items.FloatingDevice;
import items.Key;
import renderer.Renderer;
import tile.GrassTile;
import tile.SandTile;
import tile.StoneTile;
import tile.Tile;
import tile.WaterTile;
import tile.WoodTile;
import util.Position;

public class WorldEditor {

	Board board;
	int currentLocation = 0;
	EditorFrame frame;
	ToolSelectionFrame toolSelect;
	Renderer renderer;

	String tool = "none";
	String floor = "grass";
	private String gameObject = "tree";

	public WorldEditor() {
		renderer = new Renderer();
		 //LOAD BOARD
		board = BoardParser.parseBoardFName("map-new.txt");
		//board = BoardCreator.loadBoard("map.txt");
		//String s = BoardWriter.writeBoardToString(board);
		//board = BoardCreator.loadBoardFromString(s);
		// currentLocation = createBlankLocation();
		currentLocation = 0;

		frame = new EditorFrame(this);
		toolSelect = new ToolSelectionFrame(this);
		update();
	}

	public int createBlankLocation() {
		Location loc = new Location(board.getNextUniqueId(), "", new Tile[10][10], board);
		for (int i = 0; i < loc.getTiles().length; i++) {
			for (int j = 0; j < loc.getTiles()[0].length; j++) {
				loc.getTiles()[i][j] = new GrassTile(new Position(i, j), null);
			}
		}
		board.addLocation(loc.getId(), loc);
		return loc.getId();
	}

	public void update() {
		if (board.getLocationById(currentLocation) != null) {
			frame.setImage(renderer.paintLocation(board.getLocationById(currentLocation), frame.panel.getWidth(),
					frame.panel.getHeight()));
		}
	}

	public static void main(String[] args) {
		new WorldEditor();
	}

	public void processTile(int i, int j) {
		if (i >= 0 && j >= 0 && i < board.getLocationById(currentLocation).getTiles().length
				&& j < board.getLocationById(currentLocation).getTiles()[0].length) {
			Tile tile = board.getLocationById(currentLocation).getTiles()[i][j];
			if (tool.equals("Set Floor Type")) {
				Tile newTile = null;
				switch (floor) {
				case "grass":
					newTile = new GrassTile(tile.getPos(), tile.getGameObject());
					break;
				case "stone":
					newTile = new StoneTile(tile.getPos(), tile.getGameObject());
					break;
				case "water":
					newTile = new WaterTile(tile.getPos(), tile.getGameObject());
					break;
				case "sand":
					newTile = new SandTile(tile.getPos(), tile.getGameObject());
					break;
				case "wood":
					newTile = new WoodTile(tile.getPos(), tile.getGameObject());
					break;
				}
				renderer.selectTile(newTile);
				board.getLocationById(currentLocation).getTiles()[i][j] = newTile;
			}
			if (tool.equals("Add Game Object")) {
				switch (gameObject) {
				case "tree":
					tile.setGameObject(new Tree());
					break;
				case "wall":
					tile.setGameObject(new Wall());
					break;
				case "fence":
					tile.setGameObject(new Fence());
					break;
				case "chest":
					tile.setGameObject(new Chest());
					break;
				case "key":
					tile.setGameObject(new Key("name", 0));
					break;
				case "floaty":
					tile.setGameObject(new FloatingDevice("Floating Device"));
					break;
				case "banana":
					tile.setGameObject(new Banana("Banana"));
					break;
				}

			}
			update();
		}
	}

	public void setFloorType(String string) {
		this.floor = string;
	}

	public void setTool(String string) {
		this.tool = string;
	}

	public void setObjectType(String string) {
		this.gameObject = string;
	}

	public void clearTile(int i, int j) {
		if (i >= 0 && j >= 0 && i < board.getLocationById(currentLocation).getTiles().length
				&& j < board.getLocationById(currentLocation).getTiles()[0].length) {
			board.getLocationById(currentLocation).getTiles()[i][j].setGameObject(null);
		}
		update();
	}

	public void selectTile(Position selected) {
		if (selected != null) {
			renderer.selectTile(new Position((int) selected.getX(), (int) selected.getY()),
					board.getLocationById(currentLocation));
			update();
		}
		selected = null;
	}

	public void selectLocation(GameSystem.Direction dir) {
		renderer.selectLocation(dir);
		update();
	}

	public void clickLocation(Direction dir) {
		if (dir != null) {
			if (board.getLocationById(currentLocation).getNeighbours().get(dir) == null) {
				board.getLocationById(currentLocation).getNeighbours().put(dir, createBlankLocation());
				board.getLocationById(board.getLocationById(currentLocation).getNeighbours().get(dir)).getNeighbours()
						.put(Location.oppositeDir(dir), currentLocation);
				Map<Point, Integer> map = board.mapLocations(currentLocation, 0, 0, new HashMap<Point, Integer>());
				board.linkLocations(map);
			} else {
				currentLocation = board.getLocationById(currentLocation).getNeighbours().get(dir);
			}
		}
		update();
	}

	public Map<Point, Location> mergeMaps(Map<Point, Location> map1, Map<Point, Location> map2) {
		Map<Point, Location> mergedMap = new HashMap<Point, Location>();
		for (Point p : map1.keySet()) {
			mergedMap.put(p, map1.get(p));
		}
		for (Point p : map2.keySet()) {
			mergedMap.put(p, map2.get(p));
		}
		return mergedMap;
	}

	public Location getLocationAt(int startingLoc, Direction[] directions) {
		int finalLoc = startingLoc;
		for (Direction d : directions) {
			if (board.getLocationById(finalLoc).getNeighbours().get(d) != null) {
				finalLoc = board.getLocationById(finalLoc).getNeighbours().get(d);
			} else {
				return null;
			}
		}
		return board.getLocationById(finalLoc);
	}

}