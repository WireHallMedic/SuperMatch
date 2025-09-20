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
   private boolean playersTurn;
   private boolean extraTurn;
   private Actor player;
   private Actor enemy;
   
   public int getRound(){return round;}
   public int getCombo(){return combo;}
   public Actor getPlayer(){return player;}
   public Actor getEnemy(){return enemy;}
   
   public void setCollateralCount(int cc){collateralCount = cc;}
   public void setPlayer(Actor p){player = p;}
   public void setEnemy(Actor e){enemy = e;}
   
   public EncounterState()
   {
      round = -1;
      matchList = new Vector<MatchObj>();
      board = null;
      playersTurn = false;
      player = null;
      enemy = null;
      incrementRound();
   }
   
   public void setBoard(GameBoard b)
   {
      board = b;
   }
   
   public void incrementRound()
   {
      round++;
      combo = 0;
      if(!extraTurn)
         playersTurn = !playersTurn;
      extraTurn = false;
      matchList.clear();
      String str = "Round " + round + " ";
      if(playersTurn)
         str += "[P]";
      else
         str += "[E]";
      System.out.println(str);
   }
   
   public void registerMatch(MatchObj mo)
   {
      matchList.add(mo);
      if(mo.length > 3 && !extraTurn)
      {
         extraTurn = true;
         FloatingString fStr = new FloatingString("Extra Turn!");
         if(board != null)
            board.addVisualEffect(fStr);
      }
      System.out.println("  " + mo.type.name + " +" + (combo * mo.getValue()));
      if(mo.type == TileType.COLLATERAL)
      {
         collateralCount += mo.getValue() * combo;
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
   
   public boolean shouldExplode()
   {
      return collateralCount >= 6;
   }
   
   public int popExplosionCount()
   {
      int explosions = collateralCount / 2;
      collateralCount = 0;
      return explosions;
   }
}