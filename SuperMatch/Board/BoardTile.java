package SuperMatch.Board;

import java.awt.*;
import SuperMatch.GUI.*;

public class BoardTile
{
   public static final double GRAVITY = 0.01;
   public static final double TERMINAL_VELOCITY = 0.1;
   
	private TileType type;
	private double vOffset;
   private double fallSpeed;


	public TileType getType(){return type;}
	public double getVOffset(){return vOffset;}


	public void setType(TileType t){type = t;}
	public void setVOffset(double v){vOffset = v;}

   public BoardTile(TileType t)
   {
      type = t;
      vOffset = 0.0;
      fallSpeed = 0.0;
   }
   
   public void adjustVOffset(double delta)
   {
      vOffset += delta;
   }
   
   public void applyGravity()
   {
      if(isFalling())
      {
         fallSpeed = Math.min(fallSpeed + GRAVITY, TERMINAL_VELOCITY);
         vOffset += fallSpeed;
         if(vOffset >= 0.0)
         {
            vOffset = 0.0;
            fallSpeed = 0.0;
         }
      }
   }
   
   public boolean isFalling(){return vOffset < 0.0;}
   public Color getColor(){return type.color;}
   public int imageIndex(){return type.ordinal();}
   public boolean matches(BoardTile that){return type.matches(that.type);}
   
   public Particle[] getParticles(int x, int y)
   {
      Particle[] list = new Particle[7];
      if(type == TileType.WILD)
      {
         for(int i = 0; i < list.length; i++)
            list[i] = new Particle(TileType.values()[i].color, x, y);
      }
      else
      {
         for(int i = 0; i < list.length; i++)
            list[i] = new Particle(type.color, x, y);
      }
      return list;
   }
}