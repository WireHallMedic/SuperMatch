package SuperMatch.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import SuperMatch.GUI.*;

public class GameBoard extends JPanel implements ActionListener
{
   public static final int TILES_WIDE = 8;
   public static final int TILES_TALL = 12;
   public static final int TILE_SIZE = 32;
   
   private static BufferedImage[] tileImageArr;
   private BoardTile[][] tileArr;
   private Bag bag;
   
   public GameBoard(Bag b)
   {
      super();
      
      tileImageArr = FileManager.loadTileImages();
      
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
   
   public boolean hasMatches()
   {
      return getMatches().size() > 0;
   }
   
   public void removeMatches()
   {
      Vector<MatchObj> matchList = getMatches();
      for(MatchObj curMatch : matchList)
      {
         for(int i = 0; i < curMatch.length; i++)
         {
            int x = curMatch.xLoc;
            int y = curMatch.yLoc;
            if(curMatch.horizontal)
               x += i;
            else
               y += i;
            tileArr[x][y] = null;
         }
      }
      doGravity();
      fillGaps();
   }
   
   // bubble sort out nulls for readability; KISS
   public void doGravity()
   {
      for(int x = 0; x < TILES_WIDE; x++)
      {
         boolean tilesInAir = true;
         while(tilesInAir)
         {
            tilesInAir = false;
            for(int y = TILES_TALL - 1; y >= 1; y--)
            {
               if(tileArr[x][y] == null && tileArr[x][y - 1] != null)
               {
                  tileArr[x][y] = tileArr[x][y - 1];
                  tileArr[x][y].adjustVOffset(-1.0);
                  tileArr[x][y - 1] = null;
                  tilesInAir = true;
               }
            }
         }
      }
      if(!holesAtTop())
         System.out.println("Holes not at top");
   }
   
   public void fillGaps()
   {
      for(int x = 0; x < TILES_WIDE; x++)
      {
         int gaps = 0;
         for(int y = TILES_TALL - 1; y >= 0; y--)
            if(tileArr[x][y] == null)
               gaps++;
               
         double startingHeight = -gaps;
         for(int y = TILES_TALL - 1; y >= 0; y--)
         {
            if(tileArr[x][y] == null)
            {
               tileArr[x][y] = bag.draw();
               tileArr[x][y].setVOffset(startingHeight);
            }
         }
      }
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
      
      BufferedImage smallImage = new BufferedImage(TILES_WIDE * TILE_SIZE, TILES_TALL * TILE_SIZE, 
                                                   BufferedImage.TYPE_INT_ARGB);
      Graphics2D smallG2d = smallImage.createGraphics();
      
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL; y++)
      {
         if(tileArr[x][y] != null)
         {
            int yPos = (int)((y + tileArr[x][y].getVOffset()) * TILE_SIZE);
            smallG2d.drawImage(tileImageArr[tileArr[x][y].imageIndex()], x * TILE_SIZE, yPos, null);
         }
      }
      // no tiles falling falling
      if(hasFallingTiles())
         smallG2d.setColor(Color.RED);
      else
         smallG2d.setColor(Color.GREEN);
      smallG2d.fillRect(0, 0, 5, 5);
      
      // nmatches exist
      if(!hasMatches())
         smallG2d.setColor(Color.RED);
      else
         smallG2d.setColor(Color.GREEN);
      smallG2d.fillRect(15, 0, 5, 5);
      
      g2d.drawImage(smallImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), 0, 0 , null);
      smallG2d.dispose();
   }
   
   private void loadTileImages()
   {
      tileImageArr = new BufferedImage[TileType.values().length];
      BufferedImage imageStrip = FileManager.loadImageFile("/SuperMatch/Resources/Tiles.png");
   }
   
   public boolean hasFallingTiles()
   {
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL; y++)
      {
         if(tileArr[x][y] == null)
            return true;
         if(tileArr[x][y].isFalling())
            return true;
      }
      return false;
   }
   
   // kicked by timer
   public void actionPerformed(ActionEvent ae)
   {
      if(hasFallingTiles())
      {
         for(int x = 0; x < TILES_WIDE; x++)
         for(int y = 0; y < TILES_TALL; y++)
            if(tileArr[x][y] != null)
               tileArr[x][y].applyGravity();
      }
      repaint();
   }
   
   private boolean holesAtTop()
   {
      for(int x = 0; x < TILES_WIDE; x++)
      {
         boolean encounteredNonNull = false;
         for(int y = 0; y < TILES_TALL; y++)
         {
            if(tileArr[x][y] == null && encounteredNonNull)
               return false;
            if(tileArr[x][y] != null)
               encounteredNonNull = true;
         }
      }
      return true;
   }
}