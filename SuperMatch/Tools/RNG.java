package SuperMatch.Tools;

public class RNG
{
   private static StatefulRNG random = new StatefulRNG((int)System.currentTimeMillis());
   
   public static void setSeed(long s){random.setSeed((int)s);}
   
   public static double nextDouble(){return random.nextDouble();}
   public static boolean nextBoolean(){return random.nextBoolean();}
   public static int nextInt(){return random.nextInt();}
   public static int nextInt(int bound)
   {
      if(bound == 0)
         return 0;
      return (int)(random.nextDouble() * bound);
   }
   
   public static int getPosition(){return random.getIndex();}
   
   public static void setPosition(int pos){random.setIndex(pos);}
}