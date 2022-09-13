/** 
 * Culminating Activity - Walker Class
 * By Lauren Smillie
 * 06/17/2022
**/
public class Walker extends Monster
{
	public Walker()
	{
		super();
	}
	
	public Walker(String name, int attack, int defense, int hpMax, int coinsDropped)
	{
		super(name,attack,defense,hpMax,coinsDropped);
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
		{
			return true;
		}
		if (!(other instanceof Walker))
		{
			return false;
		}
		Walker s = (Walker) other;
		if(s.getName().equals(this.getName()))
		{
			return true;
		}
		return false;
	}
}
