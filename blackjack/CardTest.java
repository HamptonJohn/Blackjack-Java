package blackjack;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardTest {

	

	@Test
	public void testsetUsed() 
	{
	  Card c = new Card(2,"Spades",2);

	  c.setUsed();

	
			
	  assertTrue("Check card is used", c.used); //Place object here 
			
			
	}
	
	@Test
	public void testsetNotUsed()
	{
		Card c = new Card(2,"Spades",2);

		  c.setNotUsed();

		
				
		  assertFalse("Check card is not used", c.used); //Place object here 
	}

}
