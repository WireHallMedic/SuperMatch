package SuperMatch.Board;

public class MatchObj
{
   public int xLoc;
   public int yLoc;
   public TileType type;
   public boolean horizontal;
   public int length;
   
   public int getValue(){return length - 2;}
   
   public MatchObj()
   {
      this(0, 0, true);
   }
   
   public MatchObj(int x, int y, boolean horiz)
   {
      xLoc = x;
      yLoc = y;
      type = null;
      horizontal = horiz;
      length = 2;
   }
   
   // true if prospect continues match string
   public boolean matches(BoardTile a)
   {
      return type.matches(a.getType());
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
   
   public void incrementLength()
   {
      length++;
   }
   
   public String toString()
   {
      String dir = "Horiz";
      if(!horizontal)
         dir = "Vert";
      return String.format("%s match at [%d, %d] of %d %s", dir, xLoc, yLoc, length, type.name);
   }
   
   public String toShortString()
   {
      return type.name + " " + length;
   }
}