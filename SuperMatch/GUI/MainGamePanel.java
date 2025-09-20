package SuperMatch.GUI;

import javax.swing.*;
import java.awt.*;

public class MainGamePanel extends JPanel
{
   private JPanel playerPanel;
   private JPanel enemyPanel;
   private GameBoard gameBoard;
   
   public MainGamePanel()
   {
      super();
      setLayout(new GridLayout(1, 3));
   }
}