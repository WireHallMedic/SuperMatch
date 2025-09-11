package SuperMatch;

import java.awt.*;
import javax.swing.*;
import SuperMatch.Board.*;
import SuperMatch.Tools.*;

public class SMMain extends JFrame
{
   public SMMain()
   {
      super();
      setSize(400, 600);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLayout(new GridLayout(1, 1));
      
      long time = System.currentTimeMillis();
      System.out.println("Seed: " + time);
      RNG.setSeed(time);
      GameBoard board = new GameBoard(null);
      add(board);
      
      setVisible(true);
   }
   
   public static void main(String[] args)
   {
      SMMain main = new SMMain();
      main.repaint();
   }
}