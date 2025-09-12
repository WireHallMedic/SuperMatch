package SuperMatch.Board;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GameBoard extends JPanel
{
   public static final int TILES_WIDE = 8;
   public static final int TILES_TALL = 12;
   private static BufferedImage[] tileImageArr;
   
   private BoardTile[][] tileArr;
   private Bag bag;
   
   public GameBoard(Bag b)
   {
      super();
      loadTileImages();
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
   
   public Vector<MatchObj> getMatches()
   {
      Vector<MatchObj> matchList = new Vector<MatchObj>();
      MatchObj curMatch = null; // also serves as flag for being in match
      // horizontal matches
      for(int y = 0; y < TILES_TALL; y++)
      {
         for(int x = 0; x < TILES_WIDE - 1; x++)
         {
            BoardTile a = tileArr[x][y];
            BoardTile b = tileArr[x + 1][y];
            
            // already in a match
            if(curMatch != null)
            {
               // match continues
               if(curMatch.matches(b))
               {
                  curMatch.incrementLength();
                  curMatch.updateType(b);
               }
               else
               // match ends
               {
                  if(curMatch.length > 2)
                     matchList.add(curMatch);
                  curMatch = null;
                  // wild tiles can be part of multiple matches
                  if(a.getType() == TileType.WILD)
                     x--;
               }
            }
            // not in a match
            else
            {
               // match starts
               if(a.matches(b))
               {
                  curMatch = new MatchObj(x, y, true);
                  curMatch.setType(a, b);
               }
               // nothing required for continuing non-match
            }
         }
         // end of row
         if(curMatch != null)
         {
            if(curMatch.length > 2)
               matchList.add(curMatch);
            curMatch = null;
         }
      }
      // vertical matches
      for(int x = 0; x < TILES_WIDE; x++)
      {
         for(int y = 0; y < TILES_TALL - 1; y++)
         {
            BoardTile a = tileArr[x][y];
            BoardTile b = tileArr[x][y + 1];
            
            // already in a match
            if(curMatch != null)
            {
               // match continues
               if(curMatch.matches(b))
               {
                  curMatch.incrementLength();
                  curMatch.updateType(b);
               }
               else
               // match ends
               {
                  if(curMatch.length > 2)
                     matchList.add(curMatch);
                  curMatch = null;
                  // wild tiles can be part of multiple matches
                  if(a.getType() == TileType.WILD)
                     y--;
               }
            }
            // not in a match
            else
            {
               // match starts
               if(a.matches(b))
               {
                  curMatch = new MatchObj(x, y, false);
                  curMatch.setType(a, b);
               }
               // nothing required for continuing non-match
            }
         }
         // end of column
         if(curMatch != null)
         {
            if(curMatch.length > 2)
               matchList.add(curMatch);
            curMatch = null;
         }
      }
      return matchList;
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
   
   private void loadTileImages()
   {
   
   }
}