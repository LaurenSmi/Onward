/** 
 * Culminating Activity - Monster Class
 * By Lauren Smillie
 * 06/17/2022
**/
public class Monster
{
	private String name;
	private int attack;
	private int defense;
	private int hpCurrent;
	private int hpMax;
	private int coinsDropped;
	
	public Monster()
	{
		name = "unknown";
		defense=0;
		attack=0;
		hpMax=5;
		hpCurrent=hpMax;
		coinsDropped=0;
	}
	public Monster(String name, int attack, int defense, int hpMax,int coinsDropped)
	{
		this();
		this.name = name;
		this.attack = attack;
		this.defense=defense;
		this.hpMax = hpMax;
		hpCurrent=hpMax;
		this.coinsDropped=coinsDropped;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getAttack()
	{
		return attack;
	}
	
	public int getHpMax()
	{
		return hpMax;
	}
	
	public int getCoinsDropped()
	{
		return coinsDropped;
	}
	
	public int getHpCurrent()
	{
		return hpCurrent;
	}
	
	public int getDefense()
	{
		return defense;
	}
	
	public void setHpMax(int num)
	{
		if(num>0)
		{
			hpMax = num;
		}
	}
	public void setHpCurrent(int num)
	{
		if(num>0)
		{
			hpCurrent=num;
		}
	}
	// alters current HP by set amount
	public void changeHpCurrent(int num)
	{
		hpCurrent = hpCurrent+num;
		if(hpCurrent<0)
		{
			hpCurrent=0;
		}
	}
	
	public void setDefense(int num)
	{
		if(num>=0)
		{
			defense=num;
		}
	}
	
	public void setName(String name)
	{
		if(name!=null && name.length()>0)
		{
			this.name=name;
		}
	}
}
