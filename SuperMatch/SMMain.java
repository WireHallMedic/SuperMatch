package SuperMatch;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import SuperMatch.Board.*;
import SuperMatch.Tools.*;
import SuperMatch.Encounter.*;

public class SMMain extends JFrame implements KeyListener
{
   public static final boolean DEBUG_MODE = true;
   
   private GameBoard board;
   private EncounterState es;
   private javax.swing.Timer timer;
   
   public SMMain()
   {
      super();
      setSize(400, 600);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setLayout(new GridLayout(1, 1));
      setTitle("SuperMatch");
      
      board = new GameBoard(null);
      add(board);
      addKeyListener(this);
      
      es = new EncounterState();
      board.setEncounterState(es);
      es.setBoard(board);
      
      timer = new javax.swing.Timer(1000 / 30, null);
      timer.addActionListener(board);
      timer.start();
      
      setVisible(true);
   }
   
   public void keyReleased(KeyEvent ke){}
   public void keyTyped(KeyEvent ke){}
   public void keyPressed(KeyEvent ke)
   {
//       switch(ke.getKeyCode())
//       {
//          case KeyEvent.VK_SPACE : board.addCollateralDamage(10); 
//             System.out.println("Collateral Damage invoked");
//             break;
//       }
   }
   
   public static void main(String[] args)
   {
      SMMain main = new SMMain();
   }
}