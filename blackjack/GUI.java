package blackjack;

import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.util.*;

public class GUI extends JFrame {
	
	//randomizer for cards
	Random rand = new Random();
	
	//used for card index
	int tempC;
	
	//boolean that indicates whether the dealer is thinking or not
	boolean dHitter = false;
	
	//list of cards
	ArrayList<Card> Cards = new ArrayList<Card>();
	
	//list of messages
	ArrayList<Message> Log = new ArrayList<Message>();
	
	//fonts used
	Font fontCard = new Font("Times New Roman", Font.PLAIN, 40);
	Font fontQuest = new Font("Calibri", Font.BOLD, 40);
	Font fontButton = new Font("Calibri", Font.PLAIN, 25);
	Font fontLog = new Font("Calibri", Font.PLAIN, 30);
	
	//Log message colors
	Color cDealer = Color.red;
	Color cPlayer = new Color(25,55,255);
	
	//strings used
	String questHitStay = new String("Hit or Stay?");

	
	//colors used
	Color colorBackground = new Color(40,50,70);
	Color colorButton = new Color(204,204,0);
	
	//buttons used
	JButton bHit = new JButton();
	JButton bStay = new JButton();
	JButton bAgain = new JButton();
	
	//window resolution
	int aW = 1120;
	int aH = 640;
	
	//card grid position and dimensions
	int gridX = 50;
	int gridY = 50;
	int gridW = 900;
	int gridH = 400;
	
	//card spacing and dimensions
	int spacing = 10;
	int rounding = 10;
	int tCardW = (int) gridW/6;
	int tCardH = (int) gridH/2;
	int cardW = tCardW - spacing*2;
	int cardH = tCardH - spacing*2;
	
	//booleans for managing phases
	boolean hit_stay_q = true;
	boolean dealer_turn = false;
	boolean play_more_q = false;
	
	//player and dealer card array
	ArrayList<Card> pCards = new ArrayList<Card>();
	ArrayList<Card> dCards = new ArrayList<Card>();
	
	//player and dealer totals
	int pMinTotal = 0;
	int pMaxTotal = 0;
	int dMinTotal = 0;
	int dMaxTotal = 0;
	
	//polygons for diamond shapes
	int[] polyX = new int[4];
	int[] polyY = new int[4];
	
	public GUI() {
		this.setTitle("Blackjack");
		this.setBounds(aW, aH, aW, aH);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		Board board = new Board();
		this.setContentPane(board);
		board.setLayout(null);
		

		
		//button stuff
		
		ActHit actHit = new ActHit();
		bHit.addActionListener(actHit);
		bHit.setBounds(800, 200, 100, 50);
		bHit.setBackground(colorButton);
		bHit.setFont(fontButton);
		bHit.setText("HIT");
		board.add(bHit);
		
		ActStay actStay = new ActStay();
		bStay.addActionListener(actStay);
		bStay.setBounds(1000, 200, 100, 50);
		bStay.setBackground(colorButton);
		bStay.setFont(fontButton);
		bStay.setText("STAY");
		board.add(bStay);
		
		ActAgain ActAgain = new ActAgain();
		bAgain.addActionListener(ActAgain);
		bAgain.setBounds(800, 500, 200, 50);
		bAgain.setBackground(colorButton);
		bAgain.setFont(fontButton);
		bAgain.setText("Play Again");
		board.add(bAgain);
		
		
		
		//creating all cards
		
		String temp_str = "";
		for (int i = 0; i < 52; i++) {
			if (i % 4 == 0) {
				temp_str = "Spades";
			} else if (i % 4 == 1) {
				temp_str = "Hearts";
			} else if (i % 4 == 2) {
				temp_str = "Diamonds";
			} else if (i % 4 == 3) {
				temp_str = "Clubs";
			}
			Cards.add(new Card((i/4) + 1, temp_str, i));
		}
		
		//randomly selecting initial cards for player and dealer
		
		tempC = rand.nextInt(52);
		pCards.add(Cards.get(tempC));
		Cards.get(tempC).setUsed();
	
		
		tempC = rand.nextInt(52);
		while (Cards.get(tempC).used == true) {
			tempC = rand.nextInt(52);
		}
		dCards.add(Cards.get(tempC));
		Cards.get(tempC).setUsed();
	
		
		tempC = rand.nextInt(52);
		while (Cards.get(tempC).used == true) {
			tempC = rand.nextInt(52);
		}
		pCards.add(Cards.get(tempC));
		Cards.get(tempC).setUsed();
	
		
		tempC = rand.nextInt(52);
		while (Cards.get(tempC).used == true) {
			tempC = rand.nextInt(52);
		}
		dCards.add(Cards.get(tempC));
		Cards.get(tempC).setUsed();
	
	}
	
	public void totalsChecker() {
		
		int acesCount;
		
		//calculation of player's totals
		pMinTotal = 0;
		pMaxTotal = 0;
		acesCount = 0;
		
		for (Card c : pCards) {
			pMinTotal += c.value;
			pMaxTotal += c.value;
			if (c.name == "Ace")
				acesCount++;
			
		}
		
		if (acesCount > 0)
			pMaxTotal += 10;
		
		dMinTotal = 0;
		dMaxTotal = 0;
		acesCount = 0;
		
		for (Card c : dCards) {
			dMinTotal += c.value;
			dMaxTotal += c.value;
			if (c.name == "Ace")
				acesCount++;
			
		}
		
		if (acesCount > 0)
			dMaxTotal += 10;
	}
	
	public void setWinner() {
		int pPoints = 0;
		int dPoints = 0;
		
		if (pMaxTotal > 21) {
			pPoints = pMinTotal;
		} else {
			pPoints = pMaxTotal;
		}
		
		if (dMaxTotal > 21) {
			dPoints = dMinTotal;
		} else {
			dPoints = dMaxTotal;
		}
		
		if (pPoints > 21 && dPoints > 21) {
			Log.add(new Message("Nobody wins!", "Dealer"));
		} else if (dPoints > 21) {
			Log.add(new Message("You win!", "Player"));
			
		} else if (pPoints > 21) {
			Log.add(new Message("Dealer wins!", "Dealer"));
			
		} else if (pPoints > dPoints) {
			Log.add(new Message("You win!", "Player"));
			
		} else {
			Log.add(new Message("Dealer wins!", "Dealer"));
		}
		
	}
	
	public void dealerHitStay() {
		dHitter = true;
		
		int dAvailable = 0;
		if (dMaxTotal > 21) {
			dAvailable = dMinTotal;
		} else {
			dAvailable = dMaxTotal;
		}
		
		int pAvailable = 0;
		if (pMaxTotal > 21) {
			pAvailable = pMinTotal;
		} else {
			pAvailable = pMaxTotal;
		}
		
		repaint();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if ((dAvailable < pAvailable && pAvailable <= 21) || dAvailable < 16) {
			int tempMax = 0;
			if (dMaxTotal <= 21) {
				tempMax = dMaxTotal;
			} else {
				tempMax = dMinTotal;
			}
			String mess = ("Dealer decided to hit! (total: " + Integer.toString(tempMax) + ")");
			Log.add(new Message(mess, "Dealer"));
			Log.clear();
			tempC = rand.nextInt(52);
			while (Cards.get(tempC).used == true) {
				tempC = rand.nextInt(52);
			}
			dCards.add(Cards.get(tempC));
			Cards.get(tempC).setUsed();

		} else {
			int tempMax = 0;
			if (dMaxTotal <= 21) {
				tempMax = dMaxTotal;
			} else {
				tempMax = dMinTotal;
			}
			String mess = ("Dealer decided to stay! ");
			Log.add(new Message(mess, "Dealer"));
			Log.clear();
			setWinner();
			dealer_turn = false;
			play_more_q = true;
		}
		dHitter = false;
	}
	
	public void refresher() {
		
		if (hit_stay_q == true) {
			bHit.setVisible(true);
			bStay.setVisible(true);
		} else {
			bHit.setVisible(false);
			bStay.setVisible(false);
		}
		
		if (dealer_turn == true) {
			if (dHitter == false)
				dealerHitStay();
		}
		
		if (play_more_q == true) {
			bAgain.setVisible(true);
			
		} else {
			bAgain.setVisible(false);
			
		}
		
		totalsChecker();
		
		
		if ((pMaxTotal == 21) && hit_stay_q == true) 
		{
			String mess = ("Blackjack!!");
			Log.add(new Message(mess, "Player"));
			hit_stay_q = false;
			dealer_turn = true;
		}	
		
		if ((pMaxTotal == 21 || pMinTotal >= 21) && hit_stay_q == true) {
			int tempMax = 0;
			if (pMaxTotal <= 21) {
				tempMax = pMaxTotal;
			} else {
				tempMax = pMinTotal;
			}
			String mess = ("Bust!");
			Log.add(new Message(mess, "Player"));
			hit_stay_q = false;
			dealer_turn = true;
		}
		
		if ((dMaxTotal == 21) && dealer_turn == true) 
		{
			String mess = ("Blackjack!!");
			Log.add(new Message(mess, "Dealer"));
			dealer_turn = false;
			play_more_q = true;
		}	
		
		if ((dMaxTotal == 21 || dMinTotal >= 21) && dealer_turn == true) {
			int tempMax = 0;
			if (dMaxTotal <= 21) {
				tempMax = dMaxTotal;
			} else {
				tempMax = dMinTotal;
			}
			String mess = ("Dealer went bust!");
			Log.add(new Message(mess, "Dealer"));
			setWinner();
			dealer_turn = false;
			play_more_q = true;
		}
		
		repaint();
	}
	
	public class Board extends JPanel {
		
		public void paintComponent(Graphics g) {
			//background
			g.setColor(colorBackground);
			g.fillRect(0, 0, aW, aH);
			
			//questions
			if (hit_stay_q == true) {
				g.setColor(Color.yellow);
				g.setFont(fontQuest);
				g.drawString(questHitStay, gridX+gridW-110, gridY+90);
				g.drawString("Total:", gridX+gridW-110, gridY+290);
				if (pMinTotal == pMaxTotal) {
					g.drawString(Integer.toString(pMaxTotal), gridX+gridW-110, gridY+350);
				} else if (pMaxTotal <= 21) {
					g.drawString(Integer.toString(pMinTotal) + " or " + Integer.toString(pMaxTotal), gridX+gridW-110, gridY+350);
				} else {
					g.drawString(Integer.toString(pMinTotal), gridX+gridW-110, gridY+350);
				}
			} else if (play_more_q == true) {
				g.setColor(Color.black);
				g.setFont(fontQuest);
			}
	
			//Create message box
			g.setColor(Color.white);
			g.fillRect(gridX, gridY+gridH+25, gridW-400, 150);
			
			//Maintain log messages
			g.setFont(fontLog);
			int logIndex = 0;
			for (Message L : Log) {
				if (L.getWho().equalsIgnoreCase("Dealer")) {
					g.setColor(cDealer);
				} else {
					g.setColor(cPlayer);
				}
				g.drawString(L.getMessage(), gridX+20, gridY+460+logIndex*30);
				logIndex++;
			}
			
			
			//player cards
			int index = 0;
			for (Card c : pCards) {
				g.setColor(Color.white);
				g.fillRect(gridX+spacing+tCardW*index+rounding, gridY+spacing, cardW-rounding*2, cardH);
				g.fillRect(gridX+spacing+tCardW*index, gridY+spacing+rounding, cardW, cardH-rounding*2);
				g.fillOval(gridX+spacing+tCardW*index, gridY+spacing, rounding*2, rounding*2);
				g.fillOval(gridX+spacing+tCardW*index, gridY+spacing+cardH-rounding*2, rounding*2, rounding*2);
				g.fillOval(gridX+spacing+tCardW*index+cardW-rounding*2, gridY+spacing, rounding*2, rounding*2);
				g.fillOval(gridX+spacing+tCardW*index+cardW-rounding*2, gridY+spacing+cardH-rounding*2, rounding*2, rounding*2);
				
				g.setFont(fontCard);
				if (c.shape.equalsIgnoreCase("Hearts") || c.shape.equalsIgnoreCase("Diamonds")) {
					g.setColor(Color.red);
				} else {
					g.setColor(Color.black);
				}
				
				g.drawString(c.symbol, gridX+spacing+tCardW*index+rounding, gridY+spacing+cardH-rounding);
				
				if (c.shape.equalsIgnoreCase("Hearts")) {
					g.fillOval(gridX+tCardW*index+42, gridY+70, 35, 35);
					g.fillOval(gridX+tCardW*index+73, gridY+70, 35, 35);
					g.fillArc(gridX+tCardW*index+30, gridY+90, 90, 90, 51, 78);
				} else if (c.shape.equalsIgnoreCase("Diamonds")) {
					polyX[0] = gridX+tCardW*index+75;
					polyX[1] = gridX+tCardW*index+50;
					polyX[2] = gridX+tCardW*index+75;
					polyX[3] = gridX+tCardW*index+100;
					polyY[0] = gridY+60;
					polyY[1] = gridY+100;
					polyY[2] = gridY+140;
					polyY[3] = gridY+100;
					g.fillPolygon(polyX, polyY, 4);
				} else if (c.shape.equalsIgnoreCase("Spades")) {
					g.fillOval(gridX+tCardW*index+42, gridY+90, 35, 35);
					g.fillOval(gridX+tCardW*index+73, gridY+90, 35, 35);
					g.fillArc(gridX+tCardW*index+30, gridY+15, 90, 90, 51+180, 78);
					g.fillRect(gridX+tCardW*index+70, gridY+100, 10, 40);
				} else {
					g.fillOval(gridX+tCardW*index+40, gridY+90, 35, 35);
					g.fillOval(gridX+tCardW*index+75, gridY+90, 35, 35);
					g.fillOval(gridX+tCardW*index+58, gridY+62, 35, 35);
					g.fillRect(gridX+tCardW*index+70, gridY+75, 10, 70);
				}
				
	
				index++;
			}
			
			if (dealer_turn == true || play_more_q == true) {
				//dealer cards
				index = 0;
				for (Card c : dCards) {
					g.setColor(Color.white);
					g.fillRect(gridX+spacing+tCardW*index+rounding, gridY+spacing+200, cardW-rounding*2, cardH);
					g.fillRect(gridX+spacing+tCardW*index, gridY+spacing+rounding+200, cardW, cardH-rounding*2);
					g.fillOval(gridX+spacing+tCardW*index, gridY+spacing+200, rounding*2, rounding*2);
					g.fillOval(gridX+spacing+tCardW*index, gridY+spacing+cardH-rounding*2+200, rounding*2, rounding*2);
					g.fillOval(gridX+spacing+tCardW*index+cardW-rounding*2, gridY+spacing+200, rounding*2, rounding*2);
					g.fillOval(gridX+spacing+tCardW*index+cardW-rounding*2, gridY+spacing+cardH-rounding*2+200, rounding*2, rounding*2);
					
					g.setFont(fontCard);
					if (c.shape.equalsIgnoreCase("Hearts") || c.shape.equalsIgnoreCase("Diamonds")) {
						g.setColor(Color.red);
					} else {
						g.setColor(Color.black);
					}
					
					g.drawString(c.symbol, gridX+spacing+tCardW*index+rounding, gridY+spacing+cardH-rounding+200);
					
					if (c.shape.equalsIgnoreCase("Hearts")) {
						g.fillOval(gridX+tCardW*index+42, gridY+70+200, 35, 35);
						g.fillOval(gridX+tCardW*index+73, gridY+70+200, 35, 35);
						g.fillArc(gridX+tCardW*index+30, gridY+90+200, 90, 90, 51, 78);
					} else if (c.shape.equalsIgnoreCase("Diamonds")) {
						polyX[0] = gridX+tCardW*index+75;
						polyX[1] = gridX+tCardW*index+50;
						polyX[2] = gridX+tCardW*index+75;
						polyX[3] = gridX+tCardW*index+100;
						polyY[0] = gridY+60+200;
						polyY[1] = gridY+100+200;
						polyY[2] = gridY+140+200;
						polyY[3] = gridY+100+200;
						g.fillPolygon(polyX, polyY, 4);
					} else if (c.shape.equalsIgnoreCase("Spades")) {
						g.fillOval(gridX+tCardW*index+42, gridY+90+200, 35, 35);
						g.fillOval(gridX+tCardW*index+73, gridY+90+200, 35, 35);
						g.fillArc(gridX+tCardW*index+30, gridY+15+200, 90, 90, 51+180, 78);
						g.fillRect(gridX+tCardW*index+70, gridY+100+200, 10, 40);
					} else {
						g.fillOval(gridX+tCardW*index+40, gridY+90+200, 35, 35);
						g.fillOval(gridX+tCardW*index+75, gridY+90+200, 35, 35);
						g.fillOval(gridX+tCardW*index+58, gridY+62+200, 35, 35);
						g.fillRect(gridX+tCardW*index+70, gridY+75+200, 10, 70);
					}
					
					//-------------------------
					index++;
				}
				
				g.setColor(Color.yellow);
				g.setFont(fontQuest);
				g.drawString("Player's total: ", gridX+gridW-110, gridY+40);
				if (pMaxTotal <= 21) {
					g.drawString(Integer.toString(pMaxTotal), gridX+gridW-110, gridY+120);
				} else {
					g.drawString(Integer.toString(pMinTotal), gridX+gridW-110, gridY+120);
				}
				g.drawString("Dealer's total: ", gridX+gridW-110, gridY+240);
				if (dMaxTotal <= 21) {
					g.drawString(Integer.toString(dMaxTotal), gridX+gridW-110, gridY+320);
				} else {
					g.drawString(Integer.toString(dMinTotal), gridX+gridW-110, gridY+320);
				}
			}
			
		}
		
	}
	
	
	
	public class ActHit implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (hit_stay_q == true) {
				String mess = ("You decided to hit! ");
				Log.add(new Message(mess, "Player"));
				
				tempC = rand.nextInt(52);
				while (Cards.get(tempC).used == true) {
					tempC = rand.nextInt(52);
				}
				pCards.add(Cards.get(tempC));
				Cards.get(tempC).setUsed();
			
			}
		}
		
	}
	
	public class ActStay implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (hit_stay_q == true) {
				String mess = ("You decided to stay!");
				Log.add(new Message(mess, "Player"));
				
				hit_stay_q = false;
				dealer_turn = true;
			}
		}
		
	}
	
	public class ActAgain implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			for (Card c : Cards) {
				c.setNotUsed();
			}
			
			pCards.clear();
			dCards.clear();
			Log.clear();
			
			play_more_q = false;
			hit_stay_q = true;
			
			tempC = rand.nextInt(52);
			pCards.add(Cards.get(tempC));
			Cards.get(tempC).setUsed();
	
			
			tempC = rand.nextInt(52);
			while (Cards.get(tempC).used == true) {
				tempC = rand.nextInt(52);
			}
			dCards.add(Cards.get(tempC));
			Cards.get(tempC).setUsed();
	
			tempC = rand.nextInt(52);
			while (Cards.get(tempC).used == true) {
				tempC = rand.nextInt(52);
			}
			pCards.add(Cards.get(tempC));
			Cards.get(tempC).setUsed();
	
			
			tempC = rand.nextInt(52);
			while (Cards.get(tempC).used == true) {
				tempC = rand.nextInt(52);
			}
			dCards.add(Cards.get(tempC));
			Cards.get(tempC).setUsed();
		
			
		}
		
	}
	
	
		
	
}