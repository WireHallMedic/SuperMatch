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
   
   // set type from two tiles
   public void setType(BoardTile a, BoardTile b)
   {
      if(a.getType() != TileType.WILD)
         type = a.getType();
      else if(b.getType() != TileType.WILD)
         type = b.getType();
      else
         type = TileType.WILD;
   }
   
   // update in case first two or more were wild
   public void updateType(BoardTile a)
   {
      if(type == TileType.WILD)
         type = a.getType();
   }
   
   public String toString()
   {
      String dir = "Horiz";
      if(!horizontal)
         dir = "Vert";
      return String.format("%s match at %d, %d of %d %s", dir, length, xLoc, yLoc, type.name);
   }
}