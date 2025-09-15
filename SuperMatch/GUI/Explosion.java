package SuperMatch.GUI;

import java.awt.*;
import SuperMatch.Tools.*;

public class Explosion implements VisualEffect
{
   public double xLoc;
   public double yLoc;
   public int age;
   public int framesPerFrame;
   public int maxAge;
   
   public Explosion(int x, int y)
   {
      xLoc = (double)x - .5;  // explosion is twice the size of a tile, -.5 wil center it
      yLoc = (double)y - .5;
      
      age = 0;
      framesPerFrame = 6 + RNG.nextInt(4);
      maxAge = 4 * framesPerFrame;
   }
   
   public void increment()
   {
      age++;
   }
   
   public boolean isDead()
   {
      return age >= maxAge;
   }
   
   public int getImageIndex()
   {
      return age / framesPerFrame;
   }
}