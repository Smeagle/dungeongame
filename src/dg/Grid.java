package dg;
import java.util.Hashtable;

public class Grid {
  private Hashtable<Coordinates, Terrain> board;
  
  public void addField(Coordinates c, Terrain t) {
	  if(false == board.containsKey(c)) {
	  	  board.put(c, t);
	  }
  }
  
  public Terrain getTerrain(Coordinates c) {
	  return board.get(c);
  }
  
}
