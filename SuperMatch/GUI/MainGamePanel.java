package SuperMatch.GUI;

import SuperMatch.Board.*;
import javax.swing.*;
import java.awt.*;

public class MainGamePanel extends JPanel
{
	private JPanel playerPanel;
	private JPanel enemyPanel;
	private GameBoard gameBoard;


	public JPanel getPlayerPanel(){return playerPanel;}
	public JPanel getEnemyPanel(){return enemyPanel;}
	public GameBoard getGameBoard(){return gameBoard;}


	public void setPlayerPanel(JPanel p){playerPanel = p;}
	public void setEnemyPanel(JPanel e){enemyPanel = e;}
	public void setGameBoard(GameBoard g){gameBoard = g;}

   
   public MainGamePanel()
   {
      super();
      setLayout(new GridLayout(1, 3));
      
      playerPanel = new JPanel();
      playerPanel.add(new JLabel("Player"));
      add(playerPanel);
      
      gameBoard = new GameBoard(null);
      add(gameBoard);
      
      enemyPanel = new JPanel();
      enemyPanel.add(new JLabel("Enemy"));
      add(enemyPanel);
      
      setVisible(true);
   }
}