/** 
 * Culminating Activity - Hero Class
 * By Lauren Smillie
 * 06/17/2022
**/
import java.util.ArrayList;
public class Hero
{
	private int hpCurrent;
	private int hpMax;
	private String name;
	private int defense;
	private int attack;
	private ArrayList<Item> inventory;
	private int coins;
	private ArrayList<Monster> party;
	// default hero
	public Hero()
	{
		name = "Demetro";
		hpMax = 15;
		hpCurrent = hpMax;
		defense = 0;
		attack = 0;
		inventory = new ArrayList<>();
		party = new ArrayList<>();
		coins=0;
	}
	
	public Hero(String name)
	{
		this();
		this.name=name;
	}
	public String getName()
	{
		return name;
	}
	public int getHpMax()
	{
		return hpMax;
	}
	// alters coins by given amount
	public void changeCoins(int coinsToAdd)
	{
		coins=coins+coinsToAdd;	
	}
	
	public int getCoins()
	{
		return coins;
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
	public int getAttack()
	{
		return attack;
	}
	public void setHpCurrent(int num)
	{
		if(num>0)
		{
			hpCurrent=num;
		}
	}
	// alters HP by given amount
	public void changeHpCurrent(int num)
	{
		hpCurrent=hpCurrent+num;
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
	public void setAttack(int num)
	{
		if(num>=0)
		{
			attack=num;
		}
	}
	
	public void setName(String name)
	{
		if(name!=null && name.length()>0)
		{
			this.name=name;
		}
	}
	// returns party of monsters
	public ArrayList<Monster> getParty()
	{
		return party;
	}
	// returns whole inven
	public ArrayList<Item> getWholeInventory()
	{
		return inventory;
	}
	// returns special items that can be used in battle
	public ArrayList<Item> getSpecInventory()
	{
		ArrayList<Item> specInven = new ArrayList<>();
		for(int i=0;i<inventory.size();i++)
		{
			if(inventory.get(i).getType() == Item.SPECIAL)
			{
				specInven.add(inventory.get(i));
			}
		}
		return specInven;
	}
	
	public void addMonster(Monster monster)
	{
		party.add(monster);
	}
	
	public void addItem(Item item)
	{
		inventory.add(item);
	}
	public void removeItem(String item)
	{
		if(inventory.contains(new Item(item,0,0,0,0)))
		{
			int i=0;
			while(inventory.get(i).getName()!=item)
			{
				i++;
			}
			inventory.remove(i);
		}
	}
	// checks if user has a monster in their party
	public boolean hasMonster(String name)
	{
		if(party.contains(new Monster(name,0,0,0,0)))
		{
			return true;
		}
		return false;
	}
	// checks if user has an item in their inventory
	public boolean hasItem(String name)
	{
		if(inventory.contains(new Item(name,0,0,0,0)))
		{
			return true;
		}
		return false;
	}
	// resets fields
	public void reset()
	{
		name = "Demetro";
		hpMax = 15;
		hpCurrent = hpMax;
		defense = 0;
		attack = 0;
		inventory = new ArrayList<>();
		party = new ArrayList<>();
		coins=0;
	}
}
