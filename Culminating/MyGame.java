	/** 
 * Culminating Activity - Main Program
 * By Lauren Smillie
 * 06/17/2022
**/

import java.awt.*;  
import javax.swing.*; 
import java.util.Scanner;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import javax.swing.text.*;

public class MyGame extends JFrame implements ActionListener
{
	private Scanner sc;
	private JPanel userBarPanel;
	private JPanel monBarPanel;
	private JLabel temp;
	private JButton[] battleSpaces,storySpaces,inven;
	private JTextPane story;
	private Font msgFont,buttonFont;
	private JButton startButton,enter,contin,contin2;
	private JPanel battlePanel,storyPanel,buttonPanel,invenButtonPanel,storyButtonPanel,miniPanel,battleButtonPanel,tempPanel,bpanel,healthBarPanel,monsterPanel;
	private JTextField userText,battleText,monLabel,userLabel;
	private Hero user;
	private int turn,counter,state;
	private ArrayList<Item> allInventory;
	private Monster currentMonster;
	private JProgressBar monHealthBar,userHealthBar;
	private int extraDef,partyCounter;
	private boolean given;
	
	// Different game states
	private static final int START =0;
	private static final int BATTLE =1;
	private static final int STORY =2;
	private static final int FAINTED =3;
	private static final int STOPPED=4;
	private static final int ITEM_MENU =5;
	private static final Color pink = new Color(255,128,171);
	private static final Color green = new Color(119,201,29);
	private static final Color grey = new Color(77,77,77);
	private static final Color lightGrey = new Color(191,191,191);
	public MyGame() 
	{
		//counts the attack turn of the user's party
		partyCounter=0;
		// if crown was given to wizard
		given=false;
		// extra defense added onto user's stats if they decide to defend
		extraDef=0;
		bpanel = new JPanel();
		setLayout(new BorderLayout());
		// the current monster the user is fighting
		currentMonster = new Monster();
		// makes complete game inventory
		allInventory = new ArrayList<Item>();
		inventoryMaker();
		// label used to add photos
		temp = new JLabel();
		tempPanel = new JPanel();
		msgFont = new Font("Arial",Font.BOLD,24);
		state = START;
		user = new Hero();
		// counter for counting dialogue lines
		counter =0;
		// an array of buttons for the uesr's inventory
		inven = new JButton[1];
		inven[0]= new JButton("BLANK");
		storySpaces= new JButton[2];
		battleSpaces= new JButton[3];
		startButton = new JButton("Start Game");
		buttonFont = new Font("Arial",Font.BOLD,18);
		invenButtonPanel = new JPanel();
		// Continue button for battle layout
		contin = new JButton("Continue");
		contin.setFont(buttonFont);
		contin.addActionListener(this);
		contin.setPreferredSize(new Dimension(200,80));
		contin.setBackground(grey);
		contin.setForeground(lightGrey);
		// start button at beginning of game
		startButton.setFont(buttonFont);
		startButton.addActionListener(this);
		startButton.setPreferredSize(new Dimension(200,50));
		startButton.setBackground(pink);
		// enter button for entering user input
		enter = new JButton("Enter");
		enter.setFont(buttonFont);
		enter.setBackground(pink);
		enter.addActionListener(this);
		enter.setPreferredSize(new Dimension(339,60));
		// for typing user input (names)
		userText = new JTextField(40);
		userText.setFont(buttonFont);
		userText.setPreferredSize(new Dimension(40,40));
		userText.setHorizontalAlignment(JTextField.CENTER);

		miniPanel = new JPanel(new BorderLayout());
		miniPanel.add(userText, BorderLayout.WEST);
		miniPanel.add(enter, BorderLayout.EAST);
		// JTextPane for displaying story in a wrapped format
		story=new JTextPane();
		StyledDocument doc = story.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		story.setFont(msgFont);
		story.setForeground(Color.WHITE);
		story.setBackground(Color.BLACK);
		story.setMargin(new Insets(60,20,15,20));
		story.setPreferredSize(new Dimension(1000,600));
		story.setEditable(false);
		// yes and no buttons when the story requires the user's choice
		storyButtonPanel = new JPanel(new GridLayout(0,1));
		storyButtonPanel.setBackground(pink);
		storySpaces[0]=new JButton("Yes");
		storySpaces[1]=new JButton("No");
		
		storyPanel = new JPanel(new BorderLayout());
		storyPanel.setBackground(Color.BLACK);
		storyPanel.setForeground(Color.WHITE);
		// another continue button to progress the story (no choice)
		contin2=new JButton("Continue");
		contin2.setPreferredSize(new Dimension(200,120));
		contin2.setFont(new Font("Arial",Font.BOLD,21));
		contin2.addActionListener(this);
		contin2.setBackground(pink);	
		for(int i =0;i<2;i++)
		{
			storySpaces[i].setPreferredSize(new Dimension(200,60));
			storySpaces[i].setFont(buttonFont);
			storySpaces[i].addActionListener(this);
			storySpaces[i].setBackground(pink);	
			storyButtonPanel.add(storySpaces[i]);
		}
		// Displays battle text
		battleText = new JTextField();
		battleText.setFont(new Font("Arial",Font.BOLD,22));
		battleText.setForeground(Color.WHITE);
		battleText.setBackground(Color.BLACK);
		battleText.setMargin(new Insets(10,20,10,20));
		battleText.setEditable(false);
		// Battle fight options
		battleSpaces[0]=new JButton("Attack");
		battleSpaces[1]=new JButton("Defend");
		battleSpaces[2]=new JButton("Use Item");
		
		battlePanel = new JPanel(new GridLayout(0,1));
		battlePanel.setBackground(Color.BLACK);
		battlePanel.setPreferredSize(new Dimension(200,155));
		battleButtonPanel = new JPanel(new GridLayout(0,1));
		for(int i =0;i<3;i++)
		{
			battleSpaces[i].setPreferredSize(new Dimension(200,50));
			battleSpaces[i].setFont(buttonFont);
			battleSpaces[i].addActionListener(this);
			battleSpaces[i].setBackground(pink);	
			battleButtonPanel.add(battleSpaces[i]);
		}		
		// diplays monster image
		monsterPanel=new JPanel(new BorderLayout());
		monsterPanel.setBackground(Color.BLACK);
		// monster health bar
		monHealthBar=new JProgressBar(0,currentMonster.getHpMax());
		monHealthBar.setBackground(lightGrey);
		monHealthBar.setForeground(green);
		monHealthBar.setPreferredSize(new Dimension(500,35));
		monHealthBar.setAlignmentX(JProgressBar.RIGHT_ALIGNMENT);
		monBarPanel= new JPanel();
		monBarPanel.setPreferredSize(new Dimension(500,35));
		monBarPanel.setBackground(Color.BLACK);
		monBarPanel.add(monHealthBar);
		// label for monster health bar
		monLabel = new JTextField("Mon Label");
		monLabel.setBorder(BorderFactory.createMatteBorder(0,10,0,10,Color.BLACK));
		monLabel.setFont(new Font("Arial",Font.BOLD,16));
		monLabel.setForeground(lightGrey);
		monLabel.setBackground(Color.BLACK);
		monLabel.setEditable(false);
		monLabel.setHorizontalAlignment(JTextField.RIGHT);
		//user health bar
		userHealthBar= new JProgressBar(0,user.getHpMax());
		userHealthBar.setPreferredSize(new Dimension(500,35));
		userHealthBar.setBackground(lightGrey);
		userHealthBar.setForeground(pink);
		
		userBarPanel= new JPanel();
		userBarPanel.setPreferredSize(new Dimension(500,35));
		userBarPanel.setBackground(Color.BLACK);
		userBarPanel.add(userHealthBar);
		// label for user health bar
		userLabel = new JTextField("User Label");
		userLabel.setBorder(BorderFactory.createMatteBorder(0,10,0,10,Color.BLACK));
		userLabel.setFont(new Font("Arial",Font.BOLD,16));
		userLabel.setForeground(lightGrey);
		userLabel.setBackground(Color.BLACK);
		userLabel.setEditable(false);
		// gridLayout for displaying health bars with a gap
		GridLayout grid = new GridLayout(2,1);
		grid.setHgap(30);
		healthBarPanel=new JPanel(grid);
		healthBarPanel.add(userBarPanel);
		healthBarPanel.add(monBarPanel);
		healthBarPanel.add(userLabel);
		healthBarPanel.add(monLabel);
		healthBarPanel.setBackground(Color.BLACK);
		// sets layout to main menu
		mainMenuLayout();
		
		setSize(1000,600);
        setResizable(false);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent e)
	{
		System.out.println("COUNTER: "+counter);
		System.out.println("STATE: "+state);
		// if game over and user wants to restart
		if(e.getSource()==startButton&&state==FAINTED)
		{
			revalidate();
			user.reset();
			state=0;
			counter=0;
			mainMenuLayout();
		}
		// if game is won
		else if(counter==71)
		{
			revalidate();
			System.out.println("GOOD END");
			temp.setIcon(null);
			revalidate();
			user.reset();
			state=0;
			counter=0;
			mainMenuLayout();
		}
		// if game over
		else if(state==STOPPED||state==FAINTED)
		{
			revalidate();
			System.out.println("GAME OVER");
			gameOverLayout();
			temp.setIcon(null);
			revalidate();
		}
		else if(e.getSource() == startButton)
		{
			startLayout();
		}	
		// accepts and stores the names that the user enters
		else if(e.getSource() == enter)
		{
			// capitalizes names
			String name = capitalizer(userText.getText());
			// user name
			if(counter==1)
			{
				user.setName(name);
				story.setText("Hello "+user.getName()+", "+text());
				remove(miniPanel);
				revalidate();
				storyPanel.add(story);
				add(storyButtonPanel,BorderLayout.SOUTH);
				revalidate();
			}
			// Flower Slime name
			else if(counter==20)
			{
				user.getParty().get(0).setName(name);
				story.setText(user.getParty().get(0).getName()+" was added to your party!");
				storyPanel.add(story);
				remove(miniPanel);
				revalidate();
				add(contin2,BorderLayout.SOUTH);
				revalidate();
			}
			// Walker name
			else if(counter==48)
			{
				for(int i=0;i<user.getParty().size();i++)
				{
					if(user.getParty().get(i).getName().equals(currentMonster.getName()))
					{
						user.getParty().get(i).setName(name);
						story.setText(user.getParty().get(i).getName()+" was added to your party!");
					}
				}
				storyPanel.add(story);
				remove(miniPanel);
				revalidate();
				add(contin2,BorderLayout.SOUTH);
				revalidate();
			}
			setVisible(true);
		}
		// when the user decides "yes"
		else if(e.getSource() == storySpaces[0]&&state == STORY)
		{
			// to reset pictures to null
			if(counter ==4|counter==10||counter==12||counter==17||counter==16||counter==65||counter==18||counter==20||counter==68||counter==28||counter==35||counter==66||counter==44||counter==45||counter==48||counter==47||counter==50||counter==48||counter==56||counter==67)
			{
				temp.setIcon(null);
				temp.revalidate();
			}
			decision(true);
		}
		// when the user does not get a choice and must progress the story
		else if(e.getSource()==contin2 &&state==STORY)
		{
			// to reset pictures to null
			if(counter==4||counter==10||counter==12||counter==16||counter==17||counter==18||counter==65||counter==20||counter==28||counter==68||counter==33||counter==67||counter==35||counter==44||counter==45||counter==50||counter==48||counter==56||counter==47||counter==66)
			{
				temp.setIcon(null);
				temp.revalidate();
			}
			decision(true);
		}
		// when the user decides no
		else if(e.getSource() == storySpaces[1]&&state == STORY)
		{
			// to reset pictures to null
			if(counter==4||counter==10||counter==12||counter==16||counter==17||counter==67||counter==18||counter==65||counter==68||counter==20||counter==28||counter==66||counter==44||counter==35||counter==45||counter==50||counter==47||counter==48||counter==56)
			{
				temp.setIcon(null);
				temp.revalidate();
			}
			decision(false);
		}
		// if the user clicked a battle button
		else if(e.getSource() instanceof JButton && state == BATTLE && turn ==0)
		{
			if(e.getSource() == battleSpaces[0])
			{
				// ATTACKED 
				System.out.println("attacked");
				attack();
			}
			else if(e.getSource() == battleSpaces[1])
			{
				// DEFENDED
				System.out.println("defended");
				defend();
			}
			else if(e.getSource() == battleSpaces[2])
			{
				System.out.println("use item");
				// ITEM MENU
				itemMenuLayout();
			}
			partyCounter=0;
		}
		// to display user's special inventory (items that can be used in battle)
		else if(e.getSource() instanceof JButton && state ==ITEM_MENU)
		{
			// back button
			if(e.getSource()==inven[user.getSpecInventory().size()])
			{
				state=BATTLE;
				battleLayout(currentMonster,false);
			}
			else
			{
				for(int i =0;i<user.getSpecInventory().size();i++)
				{
					if(e.getSource() == inven[i])
					{
						Item currentItem = user.getSpecInventory().get(i);	
						useItem(currentItem);
					}
				}
			}
		}
		// attack for the user's party, each member will have a turn to attack the current monster
		else if(e.getSource()==contin && turn==3&&state==BATTLE)
		{
			if(partyCounter<user.getParty().size()&&!wonBattle())
			{
				if(partyCounter==user.getParty().size()-1)
				{
					turn =1;
				}
				partyAttack(partyCounter);
				partyCounter++;
			}
		}
		// to check if battle is won, or if it is the current monster's turn
		else if(e.getSource()==contin&&turn==1&&state==BATTLE)
		{
			if(!wonBattle())
			{
				monsterAttack();
			}
		}
		// if the user befriended the current monster
		else if(e.getSource()==contin &&state==STORY&&user.getParty().contains(currentMonster))
		{
			if(currentMonster.getName().equals("Flower Slime"))
			{
				counter=17;
				storyLayout();
				continLayout();
				revalidate();
				counter=18;
			}
			else if(currentMonster.getName().equals("Walker"))
			{
				storyLayout();
				continLayout();
				temp.setIcon(null);
				revalidate();
			}
		}
		// if the user defeated the current monster
		else if(e.getSource()==contin&&state==STORY)
		{
			temp.setIcon(null);
			revalidate();
			if(currentMonster.getName().equals("Flower Slime")&&state!=FAINTED)
			{
				counter=20;
				storyLayout();
			}
			else if(currentMonster.getName().equals("Common Slime")&&state!=FAINTED)
			{
				counter=34;
				storyLayout();
				continLayout();
			}
			else if(currentMonster.getName().equals("Walker")&&state!=FAINTED)
			{
				counter=48;
				storyLayout();
				continLayout();
			}
			else if(currentMonster.getName().equals("King Slime")&&state!=FAINTED)
			{
				counter=55;
				storyLayout();
				continLayout();
				user.addItem(getItem("Golden Crown"));
				try
				{
					BufferedImage crownPic = ImageIO.read(new File("Crown.png"));
					temp = new JLabel(new ImageIcon(crownPic));
					storyPanel.add(temp,BorderLayout.SOUTH);
				}
				catch(IOException ex)
				{
					state=STOPPED;
					System.out.println("Image not found");
				}
				revalidate();
			}
			else if(currentMonster.getName().equals("Allistar")&&state!=FAINTED)
			{
				counter=68;
				storyLayout();
				continLayout();
				try
				{
					BufferedImage endPic = ImageIO.read(new File("EndGame.png"));
					temp = new JLabel(new ImageIcon(endPic));
					storyPanel.add(temp,BorderLayout.SOUTH);
				}
				catch(IOException ex)
				{
					state=STOPPED;
					System.out.println("Image not found");
				}
				revalidate();
			}
		}
		// repurposing of inven button panel to display item choices in the market
		else if(e.getSource()instanceof JButton && counter==27)
		{
			if(e.getSource()==inven[0])
			{
				marketChoice(0);
			}
			else if(e.getSource()==inven[1])
			{
				marketChoice(1);
			}
			else if(e.getSource()==inven[2])
			{
				marketChoice(2);
			}
			else
			{
				marketChoice(3);
			}
		}
	}
	// adds text to JTextPane
	private void story()
	{
		story.setText(text());
		storyPanel.add(story,BorderLayout.CENTER);
	}
	// reads story from the story.txt file
	private String text()
	{
		try
		{
			sc = new Scanner(new File("Story.txt"));
			String line = sc.nextLine();
			for(int i=1;i<=counter;i++)
			{
				line = sc.nextLine();
			}
			counter++;
			return line;
		}
		catch(Exception v)
		{
			return"Whoops. txt file not found";
		}	
	}
	// attack method for user
	private void attack()
	{
		extraDef=0;
		// attack is a random number from 1-5
		int attack = randomInt(1,5) + user.getAttack()-currentMonster.getDefense();
		// if the user has a sword
		if(user.getAttack()>0)
		{
			battleText.setText("You attacked "+currentMonster.getName()+" with your sword. It took "+attack+" damage.");
		}
		// if the user has the shield
		else
		{
			battleText.setText("You kicked "+currentMonster.getName()+". It took "+attack+" damage.");
		}
		// updates current monster's stats
		currentMonster.changeHpCurrent(-attack);
		System.out.println(currentMonster.getHpCurrent());
		monHealthBar.setValue(currentMonster.getHpCurrent());
		contin.setVisible(true);
		if(user.getParty().size()==0)
		{
			turn =1;
		}
		// time for the party to attack
		else
		{
			turn=3;
		}
		revalidate();
	}
	// the monster called will have a turn attacking the current monster
	private void partyAttack(int num)
	{
		Monster m = user.getParty().get(num);
		int attack = m.getAttack()-currentMonster.getDefense();
		battleText.setText(m.getName()+" attacked "+currentMonster.getName()+". It took "+attack+" damage.");
		currentMonster.changeHpCurrent(-attack);
		System.out.println(currentMonster.getHpCurrent());
		monHealthBar.setValue(currentMonster.getHpCurrent());
		contin.setVisible(true);
		revalidate();
	}
	// if the user decides to defend in battle
	private void defend()
	{
		// if the user has a shield
		if(user.getDefense()>0)
		{
			battleText.setText("You braced yourself against "+currentMonster.getName()+" with your shield.");
			extraDef=randomInt(2,4);
		}
		// if the user has the sword
		else
		{
			battleText.setText("You braced yourself against "+currentMonster.getName()+".");
			extraDef=randomInt(1,3);
		}
		if(user.getParty().size()==0)
		{
			turn =1;
		}
		else
		{
			turn=3;
		}
		contin.setVisible(true);
		revalidate();
	}
	// method for changing layouts, adding pictures, altering plot based on user input
	private void decision(boolean ans)
	{
		if(counter==1)
		{
			decisionLayout();
		}
		else if (counter ==2)
		{
			if(ans)
			{
				System.out.println("Went outside");
				story();
			}
			else
			{
				System.out.println("Didn't go outside");
				counter=6;
				story();
				continLayout();
			}
		}
		else if(counter ==3)
		{
			if(ans)
			{
				System.out.println("Picked up watering can");
				
				try
				{
					BufferedImage wateringCan = ImageIO.read(new File("WateringCan.png"));
					temp= new JLabel(new ImageIcon(wateringCan));
					temp.setBorder(BorderFactory.createEmptyBorder(0,0,20,0));
					storyPanel.add(temp,BorderLayout.SOUTH);
					story.setText(text());
					storyPanel.add(story,BorderLayout.NORTH);
					user.addItem(getItem("Watering Can"));
				}
				catch(IOException ex)
				{
					System.out.println("Picture not found");
					state=STOPPED;
				}
			}
			else
			{
				System.out.println("Didn't pick up can");
				counter=5;
				story();
			}
			continLayout();
		}
		else if(counter==7||counter ==6||counter ==5)
		{
			continLayout();
			counter=7;
			story();
			revalidate();
		}
		else if(counter ==8)
		{
			int starter = randomInt(1,2);
			if (starter ==1)
			{
				user.addItem(getItem("Old Sword"));
				user.setAttack(getItem("Old Sword").getAttack());
				counter=8;
				try
				{
					BufferedImage sword = ImageIO.read(new File("Sword.png"));
					temp= new JLabel(new ImageIcon(sword));
					storyPanel.add(temp,BorderLayout.SOUTH);
				}
				
				catch(IOException ex)
				{
					System.out.println("Picture not found");
					state=STOPPED;
				}
				story();
				System.out.println("Found sword");
			}
			else
			{
				user.setDefense(2);
				user.addItem(getItem("Shield"));
				counter=10;
				try
				{
					BufferedImage shield = ImageIO.read(new File("Shield.png"));
					temp= new JLabel(new ImageIcon(shield));
					temp.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
					storyPanel.add(temp,BorderLayout.SOUTH);
				}
				catch(IOException ex)
				{
					System.out.println("Picture not found");
					state=STOPPED;
				}
				story();
				System.out.println("Found shield");
			}
		}
		else if (counter==9)
		{
			story();
			counter =12;
		}
		else if(counter==12||counter==14)
		{
			story();
			decisionLayout();
		}
		else if(counter==13)
		{
			if(ans)
			{
				story();
				counter =15;
			}
			else
			{
				counter=14;
				story();
			}
			continLayout();
		}
		else if (counter ==17)
		{
			decisionLayout();
			
			Slime flwrSlime = new Slime("Flower Slime", 4,0,10,Slime.FLOWER,3);
			try
			{
				BufferedImage flwrSlimePic = ImageIO.read(new File("FlowerSlime.png"));
				temp = new JLabel(new ImageIcon(flwrSlimePic));
			}
			catch(IOException ex)
			{
				state=STOPPED;
			}
			remove(story);
			battleLayout(flwrSlime,true);
			monsterPanel.add(temp,BorderLayout.NORTH);
			revalidate();
		}
		else if (counter==18)
		{
			story();
			decisionLayout();
			revalidate();
			try
			{
				BufferedImage flwrSlimePic = ImageIO.read(new File("FlowerSlime.png"));
				temp = new JLabel(new ImageIcon(flwrSlimePic));
				temp.setBorder(BorderFactory.createEmptyBorder(0,0,40,0));
			}
			catch(IOException ex)
			{
				state=STOPPED;
				System.out.println("Cannot find image");
			}
		}
		// if flower slime joined party, change name?
		else if(counter==19)
		{
			if(ans)
			{
				startLayout();
				remove(storyButtonPanel);
				revalidate();
				storyPanel.add(temp,BorderLayout.SOUTH);
				story.setText("Enter the new name of your Flower Slime: ");
			}
			else
			{
				story.setText("Okay, Flower Slime was added to your party!");
				storyPanel.add(temp,BorderLayout.SOUTH);
				continLayout();
			}
			counter=20;
		}
		else if (counter==21)
		{
			story();
			user.changeCoins(5);
			System.out.println("Coins: "+user.getCoins());
		}
		else if(counter==22)
		{
			story();
			decisionLayout();
		}
		// going to market?
		else if(counter==23)
		{
			user.setHpCurrent(user.getHpMax());
			if(ans)
			{
				story();	
			}
			else
			{
				counter=32;
				story();
			}
			continLayout();
		}
		// if not going to market
		else if(counter==34)
		{
			decisionLayout();
			sideBattle();
		}
		else if(counter==25)
		{
			story.setText(text()+"\n\nThe first is a small obsidian dagger that has a price of 3 coins. \n\nThe second is a potion with a price of 6 coins. \n\nThe third is a bag containing dried meat that costs 2 coins.");
			storyPanel.add(story,BorderLayout.CENTER);
		}
		// which item?
		else if(counter==26)
		{
			story.setText(text()+"\n\nYou have "+user.getCoins()+" coins.");
			storyPanel.add(story,BorderLayout.CENTER);
			remove(contin2);
			revalidate();
			marketLayout();
		}
		else if(counter==28)
		{
			continLayout();
			decisionLayout();
			story();
		}
		// Steal the item?
		else if(counter==29)
		{
			continLayout();
			if(ans)
			{
				System.out.println("Thief");
				story();
				user.changeCoins(-user.getCoins());
			}
			else
			{
				System.out.println("Good karma");
				counter=30;
				user.changeCoins(user.getCoins());
				story.setText(text()+"\n\nYou now have "+user.getCoins()+" coins.");
				storyPanel.add(story);
				revalidate();
			}
		}
		else if (counter==30||counter==31)
		{
			counter=34;
			story();
		}	
		else if(counter==35)
		{
			story();
			decisionLayout();
		}
		// approach wizard?
		else if(counter==36)
		{
			continLayout();
			if(!ans)
			{
				counter=37;
				story();
			}
			else
			{
				story();
				counter=38;
			}
		}
		else if(counter==38)
		{
			story();
			try
			{
				BufferedImage wizardPic = ImageIO.read(new File("Wizard.png"));
				temp = new JLabel(new ImageIcon(wizardPic));
			}
			catch(IOException ex)
			{
				state=STOPPED;
			}
			storyPanel.add(temp,BorderLayout.SOUTH);
		}
		else if(counter==41)
		{
			story();
			decisionLayout();
		}
		// accept wizard's mission?
		else if(counter==42)
		{
			if(ans)
			{
				story.setText("\"As thanks, I will increase your stamina for the long journey ahead.\"");
				user.setHpMax(25);
			}
			else
			{
				story.setText("\"Very well, I will pay you 4 coins to complete this mission.\"");
				user.changeCoins(4);
			}
			user.setHpCurrent(user.getHpMax());
			continLayout();
			counter=43;
		}
		else if(counter==45)
		{
			decisionLayout();
			Walker walker = new Walker("Walker",6,0,20,5);
			try
			{
				temp.setIcon(null);
				temp.revalidate();
				BufferedImage walkerPic = ImageIO.read(new File("Walker.png"));
				temp = new JLabel(new ImageIcon(walkerPic));
			}
			catch(IOException ex)
			{
				state=STOPPED;
			}
			remove(story);
			battleLayout(walker,true);
			monsterPanel.add(temp,BorderLayout.NORTH);
			revalidate();
		}
		else if(counter==46)
		{
			story();
			decisionLayout();
		}
		// if walker joined party
		else if(counter==47)
		{
			try
			{
				BufferedImage walkerPic = ImageIO.read(new File("Walker.png"));
				temp = new JLabel(new ImageIcon(walkerPic));
			}
			catch(IOException ex)
			{
				state=STOPPED;
			}
			if(ans)
			{
				startLayout();
				remove(storyButtonPanel);
				revalidate();
				storyPanel.add(temp,BorderLayout.SOUTH);
				story.setText("Enter the new name of your Walker: ");
			}
			else
			{
				story.setText("Okay, Walker was added to your party!");
				storyPanel.add(temp,BorderLayout.SOUTH);
				continLayout();
			}
			counter=48;
		}

		else if(counter==52)
		{
			story();
			decisionLayout();
		}
		// wait to battle King Slime?
		else if (counter==53)
		{
			continLayout();
			if(ans)
			{
				story.setText("You run into the cave to battle!");
			}
			else
			{
				story.setText("You rest until dark before attacking.");
				user.setHpCurrent(user.getHpMax());
			}
			counter=54;
		}
		else if(counter==54)
		{
			decisionLayout();
			Slime kingSlime = new Slime("King Slime", 7,0,40,Slime.KING,12);
			try
			{
				BufferedImage kingSlimePic = ImageIO.read(new File("KingSlime.png"));
				temp = new JLabel(new ImageIcon(kingSlimePic));
			}
			catch(IOException ex)
			{
				state=STOPPED;
			}
			remove(story);
			battleLayout(kingSlime,true);
			monsterPanel.add(temp,BorderLayout.NORTH);
			revalidate();
		}
		else if(counter==57)
		{
			story();
			try
			{
				BufferedImage wizardPic = ImageIO.read(new File("Wizard.png"));
				temp = new JLabel(new ImageIcon(wizardPic));
			}
			catch(IOException ex)
			{
				state=STOPPED;
			}
			storyPanel.add(temp,BorderLayout.SOUTH);
		}
		else if(counter==60)
		{
			story();
			decisionLayout();
		}
		// Give wizard crown?
		else if(counter==61)
		{
			continLayout();
			if(ans)
			{
				given=true;
				user.removeItem("Golden Crown");
				counter=66;
				story();
			}
			else
			{
				story();
			}
		}
		else if(counter==62)
		{
			story();
			decisionLayout();
		}
		// Give wizard crown?
		else if(counter==63)
		{
			continLayout();
			if(ans)
			{
				given=true;
				user.removeItem("Golden Crown");
				counter=66;
				story();
			}
			else
			{
				story();
			}
		}
		else if(counter==64)
		{
			story();
			decisionLayout();
		}
		// Give wizard crown?
		else if(counter==65)
		{
			if(ans)
			{
				given=true;
				user.removeItem("Golden Crown");
				continLayout();
				counter=66;
				story();
				try
				{
					BufferedImage wizardPic = ImageIO.read(new File("Wizard.png"));
					temp = new JLabel(new ImageIcon(wizardPic));
				}
				catch(IOException ex)
				{
					state=STOPPED;
				}
				storyPanel.add(temp,BorderLayout.SOUTH);
			}
			else
			{
				story.setText("HOW DARE YOU, YOU PETULANT FOOL. YOU WILL FACE MY WRATH.");
				continLayout();
				try
				{
					BufferedImage angryPic = ImageIO.read(new File("AngryWizard.png"));
					temp = new JLabel(new ImageIcon(angryPic));
					storyPanel.add(temp,BorderLayout.SOUTH);
				}
				catch(IOException ex)
				{
					state=STOPPED;
				}
				counter=68;
			}
		}
		else if(counter==67)
		{
			story();
			try
				{
					BufferedImage angryPic = ImageIO.read(new File("AngryWizard.png"));
					temp = new JLabel(new ImageIcon(angryPic));
					storyPanel.add(temp,BorderLayout.SOUTH);
				}
				catch(IOException ex)
				{
					state=STOPPED;
				}
			revalidate();
		}
		else if(counter==68)
		{
			decisionLayout();
			Wizard allistar = new Wizard("Allistar", 7,0,50,Wizard.NONE,40);
			if(given)
			{
				allistar.setSpell(Wizard.CROWN);
			}
			try
			{
				BufferedImage angryPic = ImageIO.read(new File("AngryWizard.png"));
				temp = new JLabel(new ImageIcon(angryPic));
			}
			catch(IOException ex)
			{
				state=STOPPED;
			}
			remove(story);
			battleLayout(allistar,true);
			monsterPanel.add(temp,BorderLayout.NORTH);
			revalidate();
			counter=69;
		}
		// final text
		else if(counter==69)
		{
			if(user.getAttack()>0)
			{
				story.setText(text()+"sword has only caused you trouble.");
			}
			else
			{
				story.setText(text()+"shield has only caused you trouble.");
			}
		}
		else
		{
			story();
			continLayout();
		}
	}
	// checks if current monster fainted
	private boolean wonBattle()
	{
		if(currentMonster.getHpCurrent()==0)
		{
			battleText.setText("You defeated "+currentMonster.getName()+". It dropped "+currentMonster.getCoinsDropped()+" coins!");
			user.changeCoins(currentMonster.getCoinsDropped());
			contin.setVisible(true);
			state=STORY;
			return true;
		}
		return false;
	}
	// displays the available items to buy
	private void marketLayout()
	{
		remove(storyButtonPanel);
		revalidate();
		invenButtonPanel=new JPanel();
		inven = new JButton[4];
		inven[0]=new JButton("Obsidian Dagger - 3 coins");
		inven[1]=new JButton("Mysterious Potion - 6 coins");
		inven[2]=new JButton("Dried Meat - 2 coins");
		inven[3]=new JButton("None");
		inven[user.getSpecInventory().size()].addActionListener(this);
		inven[user.getSpecInventory().size()].setFont(buttonFont);
		inven[user.getSpecInventory().size()].setBackground(pink);
		invenButtonPanel.setLayout(new GridLayout(0,1));		
		for(int i=0;i<4;i++)
		{
			inven[i].addActionListener(this);
			inven[i].setFont(buttonFont);
			inven[i].setBackground(pink);	
			inven[i].setPreferredSize(new Dimension(200,50));
			invenButtonPanel.add(inven[i]);
		}
		add(invenButtonPanel,BorderLayout.SOUTH);
		revalidate();
		setVisible(true);
	}
	// method if the user goes to the market
	private void marketChoice(int num)
	{
		// stores price of item
		int price;
		if(num==0)
		{
			price=3;
		}
		else if(num==1)
		{
			price=6;
		}
		else if(num==2)
		{
			price=2;
		}
		else
		{
			price=0;
		}
		System.out.println(user.getCoins()+"VS PRICE:"+price);
		if(price>user.getCoins())
		{
			story.setText("You do not have enough coins for that item.\n\nYou have "+user.getCoins()+" coins.");
		}
		else
		{
			storyLayout();
			revalidate();
			counter=28;
			user.changeCoins(-price);
			// displays image of purchased item
			if(num==0)
			{
				try
				{
					BufferedImage sword = ImageIO.read(new File("ObsidianKnife.png"));
					temp= new JLabel(new ImageIcon(sword));
					storyPanel.add(temp,BorderLayout.SOUTH);
				}
				catch(IOException ex)
				{
					System.out.println("Picture not found");
					state=STOPPED;
				}
				story.setText("The OBSIDIAN DAGGER was added to your inventory.");
				user.addItem(getItem("Obsidian Dagger"));
			}
			else if(num==1)
			{
				try
				{
					BufferedImage sword = ImageIO.read(new File("Potion.png"));
					temp= new JLabel(new ImageIcon(sword));
					storyPanel.add(temp,BorderLayout.SOUTH);
				}
				catch(IOException ex)
				{
					System.out.println("Picture not found");
					state=STOPPED;
				}
				story.setText("The MYSTERIOUS POTION was added to your inventory.");
				user.addItem(getItem("Mysterious Potion"));
			}
			else if(num==2)
			{
				try
				{
					BufferedImage sword = ImageIO.read(new File("Meat.png"));
					temp= new JLabel(new ImageIcon(sword));
					storyPanel.add(temp,BorderLayout.SOUTH);
				}
				
				catch(IOException ex)
				{
					System.out.println("Picture not found");
					state=STOPPED;
				}
				story.setText("The BAG OF DRIED MEAT was added to your inventory.");
				user.addItem(getItem("Dried Meat"));
			}
			else
			{
				story.setText("You decided to save your coins for a different day.");
				counter=31;
			}
		}
	}
	// Layout for battles
	private void battleLayout(Monster monster,boolean newBattle)
	{
		getContentPane().removeAll();
		battlePanel=new JPanel(new BorderLayout());
		battlePanel.setBackground(Color.BLACK);
		battlePanel.setPreferredSize(new Dimension(200,155));
		state=BATTLE;
		System.out.println("Battle");
		currentMonster = monster;
		contin.setVisible(false);
		System.out.println("USER HP: "+user.getHpCurrent());
		if(newBattle)
		{
			// allows user to have the first turn advantage
			if(user.hasItem("Obsidian Dagger"))
			{
				turn=0;
				battleText.setText("You sneak up on "+currentMonster.getName()+" with your Obsidian Dagger.");
			}
			// restores HP + first turn advantage
			else if(user.hasItem("Golden Crown"))
			{
				turn =0;
				battleText.setText("The crown gives you a newfound speed and vitality. Do you want to attack?");
				user.setHpCurrent(user.getHpMax());
			}
			else
			{
				// who starts is randomly determined
				turn = randomInt(0,1);
				if(turn==0)
				{
					battleText.setText("Do you wish to attack?");
				}
				else
				{
					battleText.setText(currentMonster.getName()+" quickly charges at you.");
					contin.setVisible(true);
					revalidate();
				}
			}
			monHealthBar.setMaximum(currentMonster.getHpMax());
			userHealthBar.setMaximum(user.getHpMax());
			healthBarPanel.setVisible(true);
			monLabel.setText(currentMonster.getName()+"'s HP");
			userLabel.setText(user.getName()+ "'s HP");
		}
		monHealthBar.setValue(currentMonster.getHpCurrent());
		userHealthBar.setValue(user.getHpCurrent());
		battleText.setLayout(new BorderLayout());
		battleText.add(contin, BorderLayout.EAST);
		monsterPanel.add(battleText, BorderLayout.SOUTH);
		add(monsterPanel,BorderLayout.CENTER);
		add(healthBarPanel,BorderLayout.NORTH);
		battlePanel.add(battleButtonPanel,BorderLayout.SOUTH);
		add(battlePanel,BorderLayout.SOUTH);
		setVisible(true);
	}
	// Layout to display user's special inventory
	private void itemMenuLayout()
	{
		extraDef=0;
		state=ITEM_MENU;
		battlePanel.remove(battleButtonPanel);
		invenButtonPanel=new JPanel();
		inven = new JButton[user.getSpecInventory().size()+1];
		inven[user.getSpecInventory().size()]=new JButton("Back");
		inven[user.getSpecInventory().size()].addActionListener(this);
		inven[user.getSpecInventory().size()].setFont(buttonFont);
		inven[user.getSpecInventory().size()].setBackground(pink);
		invenButtonPanel.setLayout(new GridLayout(0,1));
		// if user has no usable items
		if(user.getSpecInventory().size() ==0)
		{
			battleText.setText("You have no items you can use.");
			battleText.setVisible(true);
		}
		else
		{
			System.out.println(user.getSpecInventory());		
			for(int i=0;i<user.getSpecInventory().size();i++)
			{
				inven[i] = new JButton("Use "+user.getSpecInventory().get(i).getName());
				inven[i].addActionListener(this);
				inven[i].setFont(buttonFont);
				inven[i].setBackground(pink);	
				invenButtonPanel.add(inven[i]);
			}
		}
		invenButtonPanel.add(inven[user.getSpecInventory().size()]);
		battlePanel.add(invenButtonPanel);
		setVisible(true);
	}
	// Layout if user faints
	private void gameOverLayout()
	{
		getContentPane().removeAll();
		revalidate();
		try
		{
			BufferedImage gameOverScreen = ImageIO.read(new File("GameOver.png"));
			JLabel gameOver = new JLabel(new ImageIcon(gameOverScreen));
			gameOver.setPreferredSize(new Dimension(1000,700));
			add(gameOver);	
		}
		catch(IOException ex)
		{
			state=STOPPED;
			System.out.println("Couldn't find png");
		}
		getContentPane().setBackground(Color.BLACK);
		startButton.setText("Restart?");
		startButton.setBackground(new Color(145,11,11));
		startButton.setForeground(Color.BLACK);
		revalidate();
		add(bpanel,BorderLayout.SOUTH);
		revalidate();
		setVisible(true);
	}
	// Attack method for current monster
	private void monsterAttack()
	{
		contin.setVisible(false);
		// the current monster's attack - user's defense - extra defense if user defended
		int damage = currentMonster.getAttack()-user.getDefense()-extraDef;
		if(damage<0)
		{
			damage=0;
		}
		if(user.getDefense()>0)
		{
			battleText.setText(currentMonster.getName()+" attacked you, but you blocked with your shield. You took "+damage+" damage."); 
		}
		else
		{
			battleText.setText(currentMonster.getName()+" attacked you! You took "+damage+" damage.");
		}
		// update user's stats and health bar
		user.changeHpCurrent(-damage);
		userHealthBar.setValue(user.getHpCurrent());
		turn=0;
		// if user fainted
		if(user.getHpCurrent()==0)
		{
			// check if user has reviving potion
			if(user.getWholeInventory().contains(allInventory.get(4)))
			{
				turn=0;
				battleText.setText("You begin to feel faint, but you were revived by the MYSTERIOUS POTION!");
				user.getWholeInventory().remove(allInventory.get(4));
				user.setHpCurrent(user.getHpMax());
				userHealthBar.setValue(user.getHpCurrent());
				revalidate();
			}
			// else, game over
			else
			{
				turn=1;
				state=FAINTED;
				contin.setVisible(true);
				battleText.setText("You were defeated by "+currentMonster.getName()+"!");
			}
		}
	}
	// Layout for entering name
	private void startLayout()
	{
		getContentPane().removeAll();
		state=STORY;
		System.out.println("Story");
		userText.setText("");
		story();
		add(storyPanel,BorderLayout.CENTER);
		revalidate();
		add(miniPanel,BorderLayout.SOUTH);
		setVisible(true);
	}
	// Layout for main menu screen
	private void mainMenuLayout()
	{	
		state=START;
		getContentPane().removeAll();
		BufferedImage startMenu;
		JLabel picLabel = new JLabel();
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		bpanel.add(startButton);
		bpanel.setBackground(Color.BLACK);
		startButton.setText("Start Game");
		startButton.setBackground(pink);
		startButton.setForeground(new Color(51,51,51));
		try
		{
			startMenu = ImageIO.read(new File("StartGame.png"));
			picLabel = new JLabel(new ImageIcon(startMenu));
			panel.add(picLabel);
		}
		catch(IOException ex)
		{
			state=STOPPED;
		}
		panel.add(picLabel,BorderLayout.NORTH);
		panel.add(bpanel, BorderLayout.SOUTH);
		add(panel,BorderLayout.CENTER);
		setVisible(true);
	}
	// Layout for story
	private void storyLayout()
	{
		getContentPane().removeAll();
		setLayout(new BorderLayout());
		state=STORY;
		System.out.println("Story");
		story.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		story();
		storyPanel.add(story,BorderLayout.CENTER);
		add(storyPanel,BorderLayout.CENTER);
		revalidate();
		add(contin2,BorderLayout.SOUTH);
		setVisible(true);
	}
	// Layout for when user does not have choice to progress story
	private void continLayout()
	{
		remove(storyButtonPanel);
		revalidate();
		add(contin2,BorderLayout.SOUTH);
		revalidate();
	}
	// Layout for when user has a yes or no choice to make
	private void decisionLayout()
	{
		remove(contin2);
		revalidate();
		add(storyButtonPanel,BorderLayout.SOUTH);
		revalidate();
	}
	// method if user does not want to go to market
	private void sideBattle()
	{
		Slime regSlime = new Slime("Common Slime", 5,0,16,Slime.COMMON,5);
		battleLayout(regSlime,true);
		try
		{
			temp.setIcon(null);
			temp.revalidate();
			BufferedImage slimePic = ImageIO.read(new File("RegSlime.png"));
			temp = new JLabel(new ImageIcon(slimePic));
			monsterPanel.add(temp,BorderLayout.NORTH);
			revalidate();
		}
		catch(IOException ex)
		{
			state=STOPPED;
		}
		setVisible(true);
	}
	// if user wants to use item in battle
	private void useItem(Item currentItem)
	{
		if(currentItem.getName().equals("Watering Can") && currentMonster.getName().equals("Flower Slime"))
		{
			invenButtonPanel.setVisible(false);
			System.out.println("Used Watering Can");
			battleText.setText("You try to give the Flower Slime some water from your WATERING CAN!");
			state = STORY;
			contin.setVisible(true);
			user.removeItem("Watering Can");
			user.addMonster(currentMonster);
		}
		else if(currentItem.getName().equals("Dried Meat")&& currentMonster.getName().equals("Walker"))
		{
			invenButtonPanel.setVisible(false);
			System.out.println("Used Dried Meat");
			battleText.setText("You try to give the Walker some of your DRIED MEAT!");
			state = STORY;
			contin.setVisible(true);
			user.removeItem("Dried Meat");
			user.addMonster(currentMonster);
		}
		else
		{
			remove(invenButtonPanel);
			revalidate();
			battleText.setText(currentItem.getName()+" was not effective against "+currentMonster.getName()+".");
			state = BATTLE;
			battleLayout(currentMonster,false);
		}
	}
	// method to get item from the allInventory field
	private Item getItem(String name)
	{
		if(allInventory.contains(new Item(name,0,0,0,0)))
		{
			int i = allInventory.indexOf(new Item(name,0,0,0,0));
			return allInventory.get(i);
		}
		return new Item("",0,0,0,0);
	}
	// method to construct allInventory
	private void inventoryMaker()
	{
		allInventory.add(new Item("Watering Can",0,0,0,Item.SPECIAL));
		allInventory.add(new Item("Old Shield",0,2,0,Item.BATTLE));
		allInventory.add(new Item("Old Sword",2,0,0,Item.BATTLE));
		allInventory.add(new Item("Obsidian Dagger",0,0,0,Item.BATTLE));
		allInventory.add(new Item("Mysterious Potion",0,0,1,Item.BATTLE));
		allInventory.add(new Item("Dried Meat",0,0,0,Item.SPECIAL));
		allInventory.add(new Item("Golden Crown",0,0,0,Item.BATTLE));
	}
	// method for generating random ints	
	public static int randomInt(int min, int max)
	{
		int ranNum = (int)(Math.random()*(max-min+1)+min);
		return ranNum;
	}
	// method for capitalizing Strings
	private static String capitalizer(String name)
	{
		if(name.length()>0)
		{
			name = name.substring(0,1).toUpperCase()+name.substring(1);
		}
		return name;
	}
	// main method
	public static void main(String[] args) 
	{  
		MyGame m=new MyGame();
	}  
}
