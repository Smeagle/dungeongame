package dg;
import java.util.Hashtable;

public class Grid {
  private Hashtable<Coordinate, Terrain> board;
  
  public void addField(Coordinate c, Terrain t) {
	  if(false == board.containsKey(c)) {
	  	  board.put(c, t);
	  }
  }
  
  public Terrain getTerrain(Coordinate c) {
	  return board.get(c);
  }
  
}
