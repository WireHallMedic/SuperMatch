package SuperMatch.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import SuperMatch.*;
import SuperMatch.GUI.*;
import SuperMatch.Tools.*;
import SuperMatch.Encounter.*;

public class GameBoard extends JPanel implements ActionListener, MouseListener, MouseMotionListener, Runnable
{
   public static final int TILES_WIDE = 8;
   public static final int TILES_TALL = 12;
   public static final int TILE_SIZE = 32;    // px, scales to actual size
   public static final int PARTICLE_SIZE = 5; // on a tile of 32 px, scales to actual size
   public static final int EXPLOSION_SHAKE_DURATION = 10;
   public static final int WAITING_FOR_INPUT = 0;
   public static final int RESOLVING_TURN = 1;
   
   protected static BufferedImage[] tileImageArr;
   protected static BufferedImage[] explosionImageArr;
   protected BoardTile[][] tileArr;
   protected Bag bag;
   protected int[] mouseLoc = {-1, -1};
   protected int[] mouseDownLoc = {-1, -1};
   protected int[] markedTile = {-1, -1};
   protected Vector<VisualEffect> visualEffectList;
   protected EncounterState encounterState;
   protected int turnState;
   protected int shakeDuration;
   
   public void setEncounterState(EncounterState es){encounterState = es;}
   public void setShakeDuration(int dur){shakeDuration = dur;}
   
   public EncounterState getEncounterState(){return encounterState;}
   
   public GameBoard(Bag b, boolean removeInitialMatches)
   {
      super();
      
      tileImageArr = FileManager.loadTileImages();
      explosionImageArr = FileManager.loadExplosionImages();
      tileArr = new BoardTile[TILES_WIDE][TILES_TALL];
      visualEffectList = new Vector<VisualEffect>();
      
      bag = b;
      initializeBoardState(bag == null, removeInitialMatches);
      encounterState = null;
      turnState = WAITING_FOR_INPUT; // must be after initializing
      shakeDuration = 0;
      addMouseListener(this);
      addMouseMotionListener(this);
      
      setBackground(Color.BLACK);
      
      setVisible(true);
      
      if(this instanceof GameBoard)
      {
         Thread thread = new Thread(this);
         thread.start();
      }
   }
   
   public GameBoard(Bag b){this(b, true);}
   
   
   public void initializeBoardState(boolean newBag, boolean removeInitialMatches)
   {
      if(newBag)
         bag = new Bag();
         
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL; y++)
         tileArr[x][y] = bag.draw();
      
      if(removeInitialMatches)
         while(hasMatches())
            removeMatches(false);
   }
   
   public void run()
   {
      while(true)
      {
         if(turnState == RESOLVING_TURN)
         {
            if(!hasFallingTiles())
            {
               if(hasMatches())
               {
                  if(encounterState != null)
                     encounterState.incrementCombo();
                  removeMatches();
               }
               else if(encounterState != null && encounterState.shouldExplode())
               {
                  resolveCollateralDamage(encounterState.popExplosionCount());
                  doGravity(true);
                  fillGaps(true);
               }
               else
               {
                  if(encounterState != null)
                  {
                     encounterState.incrementRound();
                     turnState = WAITING_FOR_INPUT;
                  }
               }
            }
         }
         Thread.yield();
      }
   }
   
   public void attemptSwap(int x1, int y1, int x2, int y2)
   {
      if(isLegalMove(x1, y1, x2, y2))
      {
         doSwap(x1, y1, x2, y2);
      }
      else
      {
         FloatingString fStr = new FloatingString("Not a Legal Move");
         addVisualEffect(fStr);
      }
   }
   
   public void attemptSwap(int[] a, int[] b)
   {
      attemptSwap(a[0], a[1], b[0], b[1]);
   }
   
   public void doSwap(int x1, int y1, int x2, int y2)
   {
      BoardTile temp = tileArr[x1][y1];
      tileArr[x1][y1] = tileArr[x2][y2];
      tileArr[x2][y2] = temp;
      turnState = RESOLVING_TURN;
   }
   
   public void doSwap(int[] a, int[] b)
   {
      doSwap(a[0], a[1], b[0], b[1]);
   }
   
   public void addVisualEffects(VisualEffect[] veArr)
   {
      for(VisualEffect ve : veArr)
         visualEffectList.add(ve);
   }
   
   public void addVisualEffect(VisualEffect ve)
   {
      visualEffectList.add(ve);
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
         if(encounterState != null)
            encounterState.registerMatch(curMatch);
         for(int i = 0; i < curMatch.length; i++)
         {
            int x = curMatch.xLoc;
            int y = curMatch.yLoc;
            if(curMatch.horizontal)
               x += i;
            else
               y += i;
            if(doAnimation && tileArr[x][y] != null)
               addVisualEffects(tileArr[x][y].getParticles(x, y));
            tileArr[x][y] = null;
         }
      }
      doGravity(doAnimation);
      fillGaps(doAnimation);
   }
   public void removeMatches(){removeMatches(true);}
   
   public void resolveCollateralDamage(int count)
   {
      for(int i = 0; i < count; i++)
      {
         int x = RNG.nextInt(TILES_WIDE);
         int y = RNG.nextInt(TILES_TALL);
         while(tileArr[x][y] == null)
         {
            x = RNG.nextInt(TILES_WIDE);
            y = RNG.nextInt(TILES_TALL);
         }
         addVisualEffects(tileArr[x][y].getParticles(x, y));
         addVisualEffect(new Explosion(x, y));
         tileArr[x][y] = null;
      }
      FloatingString fStr = new FloatingString("Collateral Damaage!");
      addVisualEffect(fStr);
      shakeDuration = EXPLOSION_SHAKE_DURATION;
   }
   
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
      
      // visual effects
      for(int i = 0; i < visualEffectList.size(); i++)
      {
         VisualEffect ve = visualEffectList.elementAt(i);
         if(ve instanceof Particle)
         {
            Particle p = (Particle)ve;
            smallG2d.setColor(p.color);
            smallG2d.fillRect((int)(TILE_SIZE * p.xLoc), (int)(TILE_SIZE * p.yLoc), PARTICLE_SIZE, PARTICLE_SIZE);
         }
         else if(ve instanceof Explosion)
         {
            Explosion ex = (Explosion)ve;
            int x = (int)(ex.xLoc * TILE_SIZE);
            int y = (int)(ex.yLoc * TILE_SIZE);
            smallG2d.drawImage(explosionImageArr[ex.getImageIndex()], null, x, y);
         }
         else if(ve instanceof FloatingString)
         {
            FloatingString fs = (FloatingString)ve;
            int yOffset = (int)(fs.yOffset * TILE_SIZE);
            drawTextWithOutline(fs.text, fs.fillColor, fs.outlineColor, yOffset, smallG2d);
         }
      }
      
      if(SMMain.DEBUG_MODE)
      {
         // no tiles falling falling
         if(hasFallingTiles())
            smallG2d.setColor(Color.RED);
         else
            smallG2d.setColor(Color.GREEN);
         smallG2d.fillRect(0, 0, 5, 5);
      }
      
      // scale to actual size
      int shakeX = 0;
      int shakeY = 0;
      if(shakeDuration > 0)
      {
         shakeX = 2 - RNG.nextInt(6);
         shakeY = 2 - RNG.nextInt(6);
      }
      g2d.drawImage(smallImage.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH), shakeX, shakeY, null);
      smallG2d.dispose();
   }
   
   private void drawTextWithOutline(String text, Color fill, Color outline, int yOffset, Graphics2D g2d)
   {
      Font oldFont = g2d.getFont();
      Font font = new Font(oldFont.getName(), Font.BOLD, 24);
      g2d.setFont(font);
      GlyphVector gv = font.createGlyphVector(g2d.getFontRenderContext(), text);
      Shape textOutline = gv.getOutline();
      
      FontMetrics fm = g2d.getFontMetrics(font);
      int x = ((TILES_WIDE * TILE_SIZE) - fm.stringWidth(text)) / 2;
      int y = ((TILES_TALL * TILE_SIZE) - fm.getHeight()) / 2 + fm.getAscent() + yOffset;
      
      // Translate the shape to the desired position
      g2d.translate(x, y); // draw(shape) doesn't take x, y args, need to translate
      //g2d.setStroke(new BasicStroke(2.0f)); // outline thickness
      g2d.setColor(fill);
      g2d.fill(textOutline);
      g2d.setColor(outline);
      g2d.draw(textOutline);
      
      g2d.translate(-x, -y); // translate back
      g2d.setFont(oldFont);
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
      for(int i = 0; i < visualEffectList.size(); i++)
      {
         visualEffectList.elementAt(i).increment();
         if(visualEffectList.elementAt(i).isDead())
         {
            visualEffectList.removeElementAt(i);
            i--;
         }
      }
      if(shakeDuration > 0)
         shakeDuration--;
      repaint();
   }
   
   public void mouseClicked(MouseEvent me){}
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
   
   
   public void mousePressed(MouseEvent me)
   {
      mouseDownLoc = getTilePosition(me.getX(), me.getY());
   }
   
   public void mouseReleased(MouseEvent me)
   {
      int[] newTile = getTilePosition(me.getX(), me.getY());
      if(newTile[0] < 0 || newTile[0] >= TILES_WIDE ||
         newTile[1] < 0 || newTile[1] >= TILES_TALL)
      {
         newTile[0] = -1;
         newTile[1] = -1;
      }
      
      // mouse up adjacent to mouse down
      if(isAdjacent(mouseDownLoc, newTile) && 
         turnState == WAITING_FOR_INPUT)
      {
         attemptSwap(mouseDownLoc, newTile);
         unmarkTile();
      }
      // clicked marked tile
      else if(matchingTiles(newTile, markedTile))
         unmarkTile();
      // clicked adjacent tile while tile is marked
      else if(markedTile[0] != -1 && isAdjacent(markedTile, newTile) && turnState == WAITING_FOR_INPUT)
      {
         attemptSwap(markedTile, newTile);
         unmarkTile();
      }
      // clicked non-adjacent tile
      else
         markedTile = newTile;
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
   
   public boolean matchingTiles(int[] a, int[] b)
   {
      return a[0] == b[0] && a[1] == b[1];
   }
   
   public boolean isLegalMove(int x1, int y1, int x2, int y2)
   {
      TestGameBoard tgm = new TestGameBoard(this);
      tgm.doSwap(x1, y1, x2, y2);
      return tgm.hasMatches();
   }
   
   public boolean hasLegalMove()
   {
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL - 1; y++)
      {
         if(isLegalMove(x, y, x, y + 1))
            return true;
      }
      for(int x = 0; x < TILES_WIDE - 1; x++)
      for(int y = 0; y < TILES_TALL; y++)
      {
         if(isLegalMove(x, y, x + 1, y))
            return true;
      }
      return false;
   }
}