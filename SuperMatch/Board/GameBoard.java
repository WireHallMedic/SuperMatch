package SuperMatch.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import SuperMatch.*;
import SuperMatch.GUI.*;

public class GameBoard extends JPanel implements ActionListener, MouseListener, MouseMotionListener, Runnable
{
   public static final int TILES_WIDE = 8;
   public static final int TILES_TALL = 12;
   public static final int TILE_SIZE = 32;
   
   private static BufferedImage[] tileImageArr;
   private BoardTile[][] tileArr;
   private Bag bag;
   private int[] mouseLoc = {-1, -1};
   private int[] markedTile = {-1, -1};
   
   public GameBoard(Bag b)
   {
      super();
      
      tileImageArr = FileManager.loadTileImages();
      tileArr = new BoardTile[TILES_WIDE][TILES_TALL];
      
      bag = b;
      initializeBoardState(bag == null);
      addMouseListener(this);
      addMouseMotionListener(this);
      
      setVisible(true);
      
      
      Thread thread = new Thread(this);
      thread.start();
   }
   
   public void initializeBoardState(boolean newBag)
   {
      if(newBag)
         bag = new Bag();
         
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL; y++)
         tileArr[x][y] = bag.draw();
      
      while(hasMatches())
         removeMatches(false);
   }
   
   public void run()
   {
      while(true)
      {
         if(hasMatches() && !hasFallingTiles())
            removeMatches();
         Thread.yield();
      }
   }
   
   public void doSwap(int x1, int y1, int x2, int y2)
   {
      BoardTile temp = tileArr[x1][y1];
      tileArr[x1][y1] = tileArr[x2][y2];
      tileArr[x2][y2] = temp;
   }
   
   public void doSwap(int[] a, int[] b)
   {
      doSwap(a[0], a[1], b[0], b[1]);
   }
   
   public boolean hasMatches()
   {
      return getMatches().size() > 0;
   }
   
   public void removeMatches(boolean doAnimation)
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
      doGravity(doAnimation);
      fillGaps(doAnimation);
   }
   public void removeMatches(){removeMatches(true);}
   
   // bubble sort out nulls for readability; KISS
   public void doGravity(boolean doAnimation)
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
                  if(doAnimation)
                     tileArr[x][y].adjustVOffset(-1.0);
                  tileArr[x][y - 1] = null;
                  tilesInAir = true;
               }
            }
         }
      }
   }
   
   public void fillGaps(boolean doAnimation)
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
               if(doAnimation)
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
      
      // targeting box
      if(mouseLoc[0] != -1 && !hasFallingTiles())
      {
         smallG2d.setColor(Color.CYAN);
         smallG2d.drawRect(mouseLoc[0] * TILE_SIZE, mouseLoc[1] * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
      }
      
      // marked box
      if(markedTile[0] != -1)
      {
         smallG2d.setColor(Color.ORANGE);
         smallG2d.drawRect(markedTile[0] * TILE_SIZE, markedTile[1] * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
      }
      
      if(SMMain.DEBUG_MODE)
      {
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
      }
      
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
   
   public int[] getTilePosition(int x, int y)
   {
      int[] tilePos = new int[2];
      tilePos[0] = x / (getWidth() / TILES_WIDE);
      tilePos[1] = y / (getHeight() / TILES_TALL);
      return tilePos;
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
   
   public void mouseClicked(MouseEvent me){}
   public void mousePressed(MouseEvent me){}
   public void mouseEntered(MouseEvent me){}
   public void mouseExited(MouseEvent me){mouseLoc[0] = -1; mouseLoc[1] = -1;}
   public void mouseDragged(MouseEvent me){mouseMoved(me);}
   
   public void mouseMoved(MouseEvent me)
   {
      mouseLoc = getTilePosition(me.getX(), me.getY());
      if(mouseLoc[0] < 0 || mouseLoc[0] >= TILES_WIDE ||
         mouseLoc[1] < 0 || mouseLoc[1] >= TILES_TALL)
      {
         mouseLoc[0] = -1;
         mouseLoc[1] = -1;
      }
   }
   
   public void mouseReleased(MouseEvent me)
   {
      int[] newTile = getTilePosition(me.getX(), me.getY());
      // clicked marked tile
      if(newTile[0] == markedTile[0] && newTile[1] == markedTile[1])
         unmarkTile();
      // clicked adjacent tile while tile is marked
      else if(markedTile[0] != -1 && isAdjacent(markedTile, newTile))
      {
         doSwap(markedTile, newTile);
         unmarkTile();
      }
      // clicked non-adjacent tile
      else
         markedTile = newTile;
      
      if(markedTile[0] < 0 || markedTile[0] >= TILES_WIDE ||
         markedTile[1] < 0 || markedTile[1] >= TILES_TALL)
      {
         markedTile[0] = -1;
         markedTile[1] = -1;
      }
   }
   
   public void unmarkTile()
   {
      markedTile[0] = -1;
      markedTile[1] = -1;
   }
   
   public boolean isAdjacent(int[] a, int[] b)
   {
      int diff = Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]);
      return diff == 1;
   }
}