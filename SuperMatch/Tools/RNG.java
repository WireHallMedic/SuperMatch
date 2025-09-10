package SuperMatch.Tools;

import java.util.*;

public class RNG
{
   private static java.util.Random random = new java.util.Random(System.currentTimeMillis());
   
   public static void setSeed(long s){random.setSeed(s);}
   
   public static double nextDouble(){return random.nextDouble();}
   public static boolean nextBoolean(){return random.nextBoolean();}
   public static int nextInt(){return random.nextInt();}
   public static int nextInt(int bound)
   {
      if(bound == 0)
         return 0;
      return random.nextInt(bound);
   }
}