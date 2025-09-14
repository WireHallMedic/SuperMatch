package SuperMatch.Encounter;

import SuperMatch.Board.*;
import java.util.*;

public class EncounterState
{
   private int round;
   private int combo;
   private Vector<MatchObj> matchList;
   
   public int getRound(){return round;}
   
   public void setRound(int r){round = r;}
   
   public EncounterState()
   {
      round = -1;
      matchList = new Vector<MatchObj>();
      incrementRound();
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
      System.out.println("  " + mo.toShortString() + " x" + combo);
   }
   
   public void incrementCombo()
   {
      combo++;
      matchList.clear();
      if(combo > 1)
         System.out.println("  Combo x" + combo);
   }
   
   
}