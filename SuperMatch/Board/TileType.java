package SuperMatch.Board;

import java.awt.*;

public enum TileType
{
   STRENGTH    ("Strength", Color.RED),
   AGILITY     ("Agility", Color.YELLOW),
   WILL        ("Will", new Color(157, 0, 255)),
   INTELLECT   ("Intellect", Color.BLUE),
   ATTACK      ("Attack", Color.ORANGE),
   COLLATERAL  ("Collateral", Color.GRAY),
   BANTER      ("Banter", Color.WHITE),
   WILD        ("Wild", Color.BLACK);
   
   public Color color;
   public String name;
   
   private TileType(String n, Color c)
   {
      name = n;
      color = c;
   }
   
   public static TileType[] getNonWild()
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