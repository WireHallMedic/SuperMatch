package SuperMatch.Board;

import java.awt.*;

public enum TileType
{
   STRENGTH    (Color.RED),
   AGILITY     (Color.YELLOW),
   WILL        (new Color(157, 0, 255)),
   INTELLECT   (Color.BLUE),
   ATTACK      (Color.ORANGE),
   COLLATERAL  (Color.GRAY),
   BANTER      (Color.WHITE),
   WILD        (Color.BLACK);
   
   public Color color;
   
   private TileType(Color c)
   {
      color = c;
   }
   
   public TileType[] getNonWild()
   {
      int len = TileType.values().length - 1;
      TileType[] array = new TileType[len];
      for(int i = 0; i < len; i++)
         array[i] = TileType.values()[i];
      return array;
   }
   
   public boolean matches(TileType that)
   {
      return (this == that) || (this == WILD) || (that == WILD);
   }
}