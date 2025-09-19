package SuperMatch.Board;

public class MatchResults
{
   public boolean hasFourMatch;
   public int[] typeCount;
   
   public MatchResults()
   {
      hasFourMatch = false;
      typeCount = new int[TileType.values().length];
   }
   
   public void add(MatchObj match, int combo)
   {
      if(match.length >= 4)
         hasFourMatch = true;
      typeCount[match.type.ordinal()] += match.getValue() * combo;
   }
}