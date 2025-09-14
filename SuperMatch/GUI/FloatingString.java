package SuperMatch.GUI;

import java.awt.*;

public class FloatingString implements VisualEffect
{
   public String text;
   public Color fillColor;
   public Color outlineColor;
   public double yOffset;        // offset in tiles from center
   public double ySpeed;
   public int age;
   public int maxAge;
   
   public FloatingString(String str)
   {
      text = str;
      fillColor = Color.BLACK;
      outlineColor = Color.WHITE;
      yOffset = 0.0;
      ySpeed = -.05;
      age = 0;
      maxAge = 45;   // 1.5 seconds
   }   
   
   public void increment()
   {
      age++;
      yOffset += ySpeed;
   }
   
   public boolean isDead()
   {
      return age >= maxAge;
   }
}