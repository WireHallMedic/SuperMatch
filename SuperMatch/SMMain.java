package SuperMatch;

import java.awt.*;
import javax.swing.*;
import SuperMatch.Board.*;

public class SMMain extends JFrame
{
   public SMMain()
   {
      super();
      setSize(800, 800);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLayout(new GridLayout(1, 1));
      
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