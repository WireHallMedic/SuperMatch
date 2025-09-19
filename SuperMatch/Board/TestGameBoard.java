package SuperMatch.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;
import SuperMatch.*;
import SuperMatch.GUI.*;
import SuperMatch.Tools.*;
import SuperMatch.Encounter.*;

public class TestGameBoard extends GameBoard
{
   public TestGameBoard(GameBoard initialState)
   {
      super(null, false);
      for(int x = 0; x < TILES_WIDE; x++)
      for(int y = 0; y < TILES_TALL; y++)
         tileArr[x][y] = initialState.tileArr[x][y];
   }
}