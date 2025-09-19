package SuperMatch.Tools;

public class StatefulRNG
{
   private static final int BIT_NOISE_ONE =   0xB5297A4D;
   private static final int BIT_NOISE_TWO =   0x68E31DA4;
   private static final int BIT_NOISE_THREE = 0x1B56C4E9;
   private static final int BIG_PRIME_NUMBER = 198491317;
   private int _seed = 0;
   private int lastIndexed = 0;
   
   public void setIndex(int i){lastIndexed = i;}
   public void setSeed(int s){_seed = s;}
   
   public int getIndex(){return lastIndexed;}
   
   // seeded constructor
   public StatefulRNG(int seed)
   {
      setSeed(seed);
      lastIndexed = 0;
   }
   
   // unseeded constructor (seeds with current time)
   public StatefulRNG()
   {
      this((int)System.currentTimeMillis());
   }
   
   // set seed and get int value at a position
   public int sampleInt(int position, int seed)
   {
      setSeed(seed);
      return sampleInt(position);
   }
   
   // get next int value, based on last value sampled
   public int nextInt()
   {
      return sampleInt(++lastIndexed);
   }
   
   // get value at position offset from existing seed (which may be zero)
   public int sampleInt(int position)
   {
      lastIndexed = position;
      int val = Math.abs(position + _seed);
      val *= BIT_NOISE_ONE;
      val = val ^ (val >>> 8);
      val += BIT_NOISE_TWO;
      val = val ^ (val << 8);
      val *= BIT_NOISE_THREE;
      val = val ^ (val >>> 8);
      val = Math.abs(val);
      return val;
   }
   
   // int to double conversion method
   private static double toDouble(int val)
   {
      return (double)val / (double)Integer.MAX_VALUE;
   }
   
   // get value at position offset from existing seed (which may be zero)
   public double sampleDouble(int position)
   {
      return toDouble(sampleInt(position));
   }
   
   // get next double value, based on last value sampled
   public double nextDouble()
   {
      return sampleDouble(++lastIndexed);
   }
   
   public boolean sampleBoolean(int position)
   {
      return sampleInt(position) % 2 == 0;
   }
   
   public boolean nextBoolean()
   {
      return sampleBoolean(++lastIndexed);
   }

}