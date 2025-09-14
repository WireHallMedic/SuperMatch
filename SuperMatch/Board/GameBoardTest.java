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
      RNG.setSeed(1757609330155L);
      GameBoard board = new GameBoard(null, false);
      Vector<MatchObj> matchList = board.getMatches();
      Assert.assertTrue("Vertical match found", 
         hasMatch(3, 5, 3, TileType.STRENGTH, false, matchList));
   }

   // find horizontal match that includes wildcard termination: AAWX
   @Test public void horizontalMatching() 
   {
      RNG.setSeed(1757609595308L);
      GameBoard board = new GameBoard(null, false);
      Vector<MatchObj> matchList = board.getMatches();
      Assert.assertTrue("Horizontal match found", 
         hasMatch(5, 6, 3, TileType.AGILITY, true, matchList));
   }

   // find overlapping matches: AAWBB
   @Test public void overlappingWildMatching() 
   {
      RNG.setSeed(1757608365544L);
      GameBoard board = new GameBoard(null, false);
      Vector<MatchObj> matchList = board.getMatches();
      Assert.assertTrue("Overlapping coaxial matches on a wild tile are found (first match)", 
         hasMatch(1, 2, 3, TileType.BANTER, false, matchList));
      Assert.assertTrue("Overlapping coaxial matches on a wild tile are found (second match)", 
         hasMatch(1, 4, 3, TileType.ATTACK, false, matchList));
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
