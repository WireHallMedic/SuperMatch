package SuperMatch.Encounter;


public class EncounterState
{
   private int round;
   
   public int getRound(){return round;}
   
   public void setRound(int r){round = r;}
   
   public EncounterState()
   {
      round = 0;
   }
   
   public void incrementRound()
   {
      round++;
      System.out.println("Round " + round);
   }
}