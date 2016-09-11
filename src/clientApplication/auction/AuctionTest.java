package clientApplication.auction;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import clientApplication.client.Client;
import clientApplication.item.Item;

public class AuctionTest {

	@Test
	public void testPlaceBid() {
		Item item = new Item("toster", "stary toster");
		Client client = new Client("john", "doe", "john", "doe", "Victory st. 14");
		Auction auction = new Auction(12.0, item, client);
		double auctionPrize;
		auction.placeBid(12.0, client);
		auctionPrize = auction.getPrize();
		assertEquals(24.0, auctionPrize, 0);
	}
	
	@Test
	public void testAuctionToString(){
		Item item = new Item("pralka", "piekna pralka");
		Client client = new Client("john", "doe", "john", "doe", "Victory st. 14");
		Auction auction = new Auction(40.0, item, client);
		String auctionToString = auction.toString();
		assertEquals("pralka : piekna pralka : 40.0", auctionToString);
	}
	
	@Test
	public void testGetPrize(){
		Item item = new Item("telefon", "telefon stacjonarny");
		Client client = new Client("john", "doe", "john", "doe", "Victory st. 14");
		Auction auction = new Auction(10.0, item, client);
		double testPrize = auction.getPrize();
		assertEquals(10.0, testPrize, 0);
	}

}
