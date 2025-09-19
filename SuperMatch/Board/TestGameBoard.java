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

public class TestGameBoard extends GameBoard
{
   public static final int TILES_WIDE = 8;
   public static final int TILES_TALL = 12;
   public static final int TILE_SIZE = 32;    // px, scales to actual size
   public static final int PARTICLE_SIZE = 5; // on a tile of 32 px, scales to actual size
   public static final int EXPLOSION_SHAKE_DURATION = 10;
   public static final int WAITING_FOR_INPUT = 0;
   public static final int RESOLVING_TURN = 1;
   
   private static BufferedImage[] tileImageArr;
   private static BufferedImage[] explosionImageArr;
   private BoardTile[][] tileArr;
   private Bag bag;
   private int[] mouseLoc = {-1, -1};
   private int[] mouseDownLoc = {-1, -1};
   private int[] markedTile = {-1, -1};
   private Vector<VisualEffect> visualEffectList;
   private EncounterState encounterState;
   private int turnState;
   private int shakeDuration;
   
   public void setEncounterState(EncounterState es){encounterState = es;}
   public void setShakeDuration(int dur){shakeDuration = dur;}
   
   public EncounterState getEncounterState(){return encounterState;}
   
   public TestGameBoard(GameBoard initialState)
   {
      super(null, false);
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL; y++)
         tileArr[x][y] = initialState.tileArr[x][y];
   }
}