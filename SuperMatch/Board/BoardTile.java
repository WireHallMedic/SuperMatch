package SuperMatch.Board;

import java.awt.*;

public class BoardTile
{
	private TileType type;
	private double vOffset;


	public TileType getType(){return type;}
	public double getVOffset(){return vOffset;}


	public void setType(TileType t){type = t;}
	public void setVOffset(double v){vOffset = v;}

   public BoardTile(TileType t)
   {
      type = t;
      vOffset = 0.0;
   }
   
   public void adjustVOffset(double delta)
   {
      vOffset += delta;
   }
   
   public Color getColor(){return type.color;}
   public boolean matches(BoardTile that){return type.matches(that.type);}
}