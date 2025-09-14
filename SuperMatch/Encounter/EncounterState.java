package SuperMatch.Encounter;

import SuperMatch.Board.*;
import SuperMatch.GUI.*;
import java.util.*;

public class EncounterState
{
   private int round;
   private int combo;
   private Vector<MatchObj> matchList;
   private GameBoard board;
   
   private int collateralCount;
   
   public int getRound(){return round;}
   public int getCombo(){return combo;}
   
   public EncounterState()
   {
      round = -1;
      matchList = new Vector<MatchObj>();
      board = null;
      incrementRound();
   }
   
   public void setBoard(GameBoard b)
   {
      board = b;
   }
   
   public void incrementRound()
   {
      round++;
      System.out.println("Round " + round);
      combo = 0;
      matchList.clear();
   }
   
   public void registerMatch(MatchObj mo)
   {
      matchList.add(mo);
      System.out.println("  " + mo.type.name + " +" + (combo * mo.getValue()));
      if(mo.type == TileType.COLLATERAL)
      {
         collateralCount += mo.getValue() * combo;
         if(collateralCount >= 5)
         {
            if(board != null)
               board.addCollateralDamage(collateralCount / 2);
            collateralCount = 0;
         }
      }
   }
   
   public void incrementCombo()
   {
      combo++;
      matchList.clear();
      if(combo > 1)
      {
         System.out.println("  Combo x" + combo);
         FloatingString fStr = new FloatingString("x" + combo + " Combo!");
         if(board != null)
            board.addVisualEffect(fStr);
      }
   }
   
   
}