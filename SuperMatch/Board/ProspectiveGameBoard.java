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

public class ProspectiveGameBoard extends GameBoard
{
   public ProspectiveGameBoard(GameBoard initialState)
   {
      super(null, false);
      removeMouseListener(this);
      removeMouseMotionListener(this);
      
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL; y++)
         tileArr[x][y] = new BoardTile(initialState.tileArr[x][y].getType());
   }
   
   public MatchResults getMoveResults(int x1, int y1, int x2, int y2)
   {
      MatchResults results = new MatchResults(x1, y1, x2, y2);
      int combo = 1;
      doSwap(x1, y1, x2, y2);
      while(hasMatches())
      {
         Vector<MatchObj> matchList = getMatches();
         for(MatchObj curMatch : matchList)
            results.add(curMatch, combo);
         removeMatches(false);
         combo++;
      }
      return results;
   }
}