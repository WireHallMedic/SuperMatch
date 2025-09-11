package SuperMatch.Board;

import java.awt.*;

public enum TileType
{
   STRENGTH    ("Strength", new Color(230, 28, 34)),     //red
   AGILITY     ("Agility", new Color(230, 218, 0)),      // yellow
   WILL        ("Will", new Color(39, 204, 88)),         // green
   INTELLECT   ("Intellect", new Color(0, 161, 230)),    // blue
   ATTACK      ("Attack", new Color(230, 112, 34)),      // orange
   COLLATERAL  ("Collateral", new Color(145, 145, 145)), // gray
   BANTER      ("Banter", new Color(230, 230, 230)),     // white
   WILD        ("Wild", Color.BLACK),                    // actually uses all other colors
   SKILL       ("Skill", new Color(194, 53, 204));       // purple
   
   public Color color;
   public String name;
   
   private TileType(String n, Color c)
   {
      name = n;
      color = c;
   }
   
   public static TileType[] getBasicSet()
   {
      int len = TileType.values().length - 2;
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