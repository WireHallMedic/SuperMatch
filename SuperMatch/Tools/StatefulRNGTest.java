package SuperMatch.Tools;

import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


public class StatefulRNGTest {


   /** Fixture initialization (common initialization for all tests). **/
   @Before public void setUp() {
   }


   /** Resetting the position causes deterministic output **/
   @Test public void testResettingPosition() 
   {
      StatefulRNG rng = new StatefulRNG((int)System.currentTimeMillis());
      rng.setIndex(107);
      
      // same seed and index result in same sequence
      int[] series = new int[10];
      for(int i = 0; i < series.length; i++)
         series[i] = rng.nextInt();
      rng.setIndex(107);
      for(int i = 0; i < series.length; i++)
         Assert.assertEquals("Generated number matches stored number", series[i], rng.nextInt());
      
      // different seed same index does not
      rng.setSeed((int)System.currentTimeMillis() + 1000);
      rng.setIndex(107);
      for(int i = 0; i < series.length; i++)
         Assert.assertNotEquals("Generated number does not match stored number", series[i], rng.nextInt());
   }
}
