package SuperMatch.Board;

import java.awt.*;

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
      fallSpeed = Math.min(fallSpeed + GRAVITY, TERMINAL_VELOCITY);
      vOffset += fallSpeed;
      if(vOffset >= 0.0)
      {
         fallSpeed = 0.0;
         fallSpeed = 0.0;
      }
   }
   
   public boolean isFalling(){return vOffset < 0.0;}
   public Color getColor(){return type.color;}
   public int imageIndex(){return type.ordinal();}
   public boolean matches(BoardTile that){return type.matches(that.type);}
}