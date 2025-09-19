package SuperMatch.Board;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import SuperMatch.Tools.*;


public class GameBoardTest {


   /** Fixture initialization (common initialization for all tests). **/
   @Before public void setUp() {
   }


   // find vertical match that includes wildcard termination: AAWX
   @Test public void verticalMatching() 
   {
      RNG.setSeed(1758309434630L);
      RNG.setPosition(0);
      GameBoard board = new GameBoard(null, false);
      Vector<MatchObj> matchList = board.getMatches();
      Assert.assertTrue("Vertical match found", 
         hasMatch(1, 8, 4, TileType.BANTER, false, matchList));
   }

   // find horizontal match that includes wildcard termination: AAWX
   @Test public void horizontalMatching() 
   {
      RNG.setSeed(1758309678262L);
      RNG.setPosition(0);
      GameBoard board = new GameBoard(null, false);
      Vector<MatchObj> matchList = board.getMatches();
      Assert.assertTrue("Horizontal match found", 
         hasMatch(2, 3, 3, TileType.COLLATERAL, true, matchList));
   }

   // find overlapping matches: AAWBB
   @Test public void overlappingWildMatching() 
   {
      RNG.setSeed(1758309841862L);
      RNG.setPosition(0);
      GameBoard board = new GameBoard(null, false);
      Vector<MatchObj> matchList = board.getMatches();
      Assert.assertTrue("Overlapping coaxial matches on a wild tile are found (first match)", 
         hasMatch(3, 5, 3, TileType.STRENGTH, false, matchList));
      Assert.assertTrue("Overlapping coaxial matches on a wild tile are found (second match)", 
         hasMatch(3, 7, 3, TileType.ATTACK, false, matchList));
   }
   
   public boolean hasMatch(int x, int y, int len, TileType type, boolean horizontal, Vector<MatchObj> list)
   {
      for(MatchObj match : list)
      {
         if(match.xLoc == x &&
            match.yLoc == y &&
            match.length == len &&
            match.type == type &&
            match.horizontal == horizontal)
            return true;
      }
      return false;
   }
}
