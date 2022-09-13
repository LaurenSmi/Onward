/** 
 * Culminating Activity - Item Class
 * By Lauren Smillie
 * 06/17/2022
**/
public class Item
{
	private String name;
	private int defense;
	private int attack;
	private int speed;
	private int type;
	// fields to see if item can be used in battle
	public static final int BATTLE =1;
	public static final int SPECIAL =2;
	
	public Item()
	{
		name ="Unknown";
		attack=0;
		defense=0;
		speed =0;
		type=0;
	}
	
	public Item(String name, int attack, int defense, int speed,int type)
	{
		this.name = name;
		this.attack = attack;
		this.defense=defense;
		this.speed = speed;
		this.type=type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getAttack()
	{
		return attack;
	}
	
	public int getDefense()
	{
		return defense;
	}
	
	public int getSpeed()
	{
		return speed;
	}
	
	public int getType()
	{
		return type;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
		{
			return true;
		}
		if (!(other instanceof Item))
		{
			return false;
		}
		Item i = (Item) other;
		if(i.getName().equals(this.getName()))
		{
			return true;
		}
		return false;
	}
}
