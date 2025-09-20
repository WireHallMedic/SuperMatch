package SuperMatch.Actor;

import SuperMatch.Board.*;

public class Actor
{
	private String name;
	private int[] energy;
	private Object[] ability;
	private int maxHealth;
	private int curHealth;


	public String getName(){return name;}
	public int[] getEnergy(){return energy;}
	public Object[] getAbility(){return ability;}
	public int getMaxHealth(){return maxHealth;}
	public int getCurHealth(){return curHealth;}


	public void setName(String n){name = n;}
	public void setEnergy(int[] e){energy = e;}
	public void setAbility(Object[] a){ability = a;}
	public void setMaxHealth(int m){maxHealth = m;}
	public void setCurHealth(int c){curHealth = c;}

   public Actor(String n)
   {
      name = n;
      energy = new int[TileType.values().length - 2];
      ability = new Object[4];
      maxHealth = 20;
      curHealth = 20;
   }
   
   public boolean isDead()
   {
      return curHealth <= 0;
   }
   
   public void fullRefresh()
   {
      for(int i = 0; i < energy.length; i++)
         energy[i] = 0;
      curHealth = maxHealth;
   }
}