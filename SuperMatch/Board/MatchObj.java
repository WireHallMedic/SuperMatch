package SuperMatch.Board;

public class MatchObj
{
   public int xLoc;
   public int yLoc;
   public TileType type;
   public boolean horizontal;
   public int length;
   
   public MatchObj()
   {
      xLoc = 0;
      yLoc = 0;
      type = null;
      horizontal = true;
      length = 3;
   }
}