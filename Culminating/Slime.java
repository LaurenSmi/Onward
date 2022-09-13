/** 
 * Culminating Activity - Slime Class
 * By Lauren Smillie
 * 06/17/2022
**/
public class Slime extends Monster
{
	// different types of slimes
	public static final int COMMON =0;
	public static final int FLOWER =1;
	public static final int KING =2;
	private int type;
	public Slime()
	{
		super();
		type = COMMON;
	}
	
	public Slime(String name, int attack, int defense, int hpMax, int type, int coinsDropped)
	{
		super(name,attack,defense,hpMax,coinsDropped);
		this.type=type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public int getType()
	{
		return this.type;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
		{
			return true;
		}
		if (!(other instanceof Slime))
		{
			return false;
		}
		Slime s = (Slime) other;
		if(s.getName().equals(this.getName()))
		{
			return true;
		}
		return false;
	}
}
