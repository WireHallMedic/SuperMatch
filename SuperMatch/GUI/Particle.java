package SuperMatch.GUI;

import java.awt.*;
import SuperMatch.Tools.*;

public class Particle
{
   public double xLoc;
   public double yLoc;
   public Color color;
   public double xSpeed;
   public double ySpeed;
   public int age;
   public int maxAge;
   
   public Particle(Color c, int x, int y)
   {
      color = c;
      xLoc = x + .5;
      yLoc = y + .5;
      
      // random angle, and speed in range of [1.0, 1.5)
      double angle = RNG.nextDouble() * 2 * 3.14;
      double speed = (RNG.nextDouble() * .05) + 0.1;
      
      xSpeed =  Math.cos(angle) * speed;
      ySpeed = Math.sin(angle) * speed;
      
      age = 0;
      maxAge = 10 + RNG.nextInt(6);
   }
   
   public void increment()
   {
      age++;
      xLoc += xSpeed;
      yLoc += ySpeed;
   }
   
   public boolean isDead()
   {
      return age >= maxAge;
   }
   
}