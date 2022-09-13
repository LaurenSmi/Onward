/** 
 * Culminating Activity - Wizard Class
 * By Lauren Smillie
 * 06/17/2022
**/
public class Wizard extends Monster
{
	// spells were not implemented in this version
	// these fields could be added to extend final battle (wizard heals himself)
	private int spell;
	public static final int NONE =0;
	public static final int HEAL =1;
	public static final int CROWN =2;
	
	public Wizard()
	{
		super();
		spell = NONE;
	}
	
	public Wizard(String name, int attack, int defense, int hpMax, int spell, int coinsDropped)
	{
		super(name,attack,defense,hpMax,coinsDropped);
		this.spell=spell;
	}
	public void setSpell(int spell)
	{
		this.spell = spell;
	}
	public int getSpell()
	{
		return this.spell;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(this == other)
		{
			return true;
		}
		if (!(other instanceof Wizard))
		{
			return false;
		}
		Wizard s = (Wizard) other;
		if(s.getName().equals(this.getName()))
		{
			return true;
		}
		return false;
	}
}
