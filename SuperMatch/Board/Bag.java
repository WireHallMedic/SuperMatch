package SuperMatch.Board;

import java.util.*;
import SuperMatch.Tools.*;

public class Bag
{
   private Vector<TileType> list;
   private static final int SETS_PER_WILD = 10;
   
   public Bag()
   {
      list = new Vector<TileType>();
      refresh();
   }
   
   public BoardTile draw()
   {
      TileType tt = list.elementAt(list.size() - 1);
      list.removeElementAt(list.size() - 1);
      if(list.size() == 0)
         refresh();
      return new BoardTile(tt);
   }
   
   public void refresh()
   {
      list.clear();
      for(int i = 0; i < SETS_PER_WILD; i++) 
         for(TileType tt : TileType.getNonWild())
            list.add(tt);
      list.add(TileType.WILD);
      shake();
   }
   
   public void shake()
   {
      Vector<TileType> newList = new Vector<TileType>();
      while(list.size() > 0)
      {
         int index = RNG.nextInt(list.size());
         newList.add(list.elementAt(index));
         list.removeElementAt(index);
      }
      list = newList;
   }
}