package SuperMatch.Board;

public class MatchResults
{
   public int x1, y1, x2, y2;
   public boolean hasFourMatch;
   public int[] typeCount;
   
   public MatchResults(int ax, int ay, int bx, int by)
   {
      x1 = ax;
      y1 = ay;
      x2 = bx;
      y2 = by;
      hasFourMatch = false;
      typeCount = new int[TileType.values().length];
   }
   
   public void add(MatchObj match, int combo)
   {
      if(match.length >= 4)
         hasFourMatch = true;
      typeCount[match.type.ordinal()] += match.getValue() * combo;
   }
   
   public int sumCounts()
   {
      int sum = 0;
      for(int val : typeCount)
         sum += val;
      return sum;
   }
   
   public MatchResults compare(MatchResults that)
   {
      // if one has a four-match and the other does not, return the one that does
      if(this.hasFourMatch && !that.hasFourMatch)
         return this;
      if(that.hasFourMatch && !this.hasFourMatch)
         return that;
      // else return the one with a higher score
      if(this.sumCounts() > that.sumCounts())
         return this;
      return that;
      
   }
}