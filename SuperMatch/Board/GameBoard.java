package SuperMatch.Board;

import javax.swing.*;
import java.awt.*;

public class GameBoard extends JPanel
{
   public static final int TILES_WIDE = 8;
   public static final int TILES_TALL = 12;
   
   private BoardTile[][] tileArr;
   private Bag bag;
   
   public GameBoard(Bag b)
   {
      super();
      if(b == null)
         bag = new Bag();
      else
         bag = b;
      tileArr = new BoardTile[TILES_WIDE][TILES_TALL];
      
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL; y++)
         tileArr[x][y] = bag.draw();
      
      setVisible(true);
   }
   
   @Override
   public void paint(Graphics g)
   {
      super.paint(g);
      int tileWidth = getWidth() / TILES_WIDE;
      int tileHeight = getHeight() / TILES_TALL;
      Graphics2D g2d = (Graphics2D)g;
      
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL; y++)
      {
         if(tileArr[x][y] != null)
         {
            g2d.setColor(tileArr[x][y].getColor());
            g2d.fillRect(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
         }
      }
   }
}