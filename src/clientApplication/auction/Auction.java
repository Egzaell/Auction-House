package clientApplication.auction;

import java.io.Serializable;

import clientApplication.client.Client;
import clientApplication.item.Item;

public class Auction implements Comparable<Auction>, Serializable{

	private Double prize;
	private Item item;
	private Client winner;
	private Client owner;
	
	public Auction(double prize, Item item, Client owner){
		this.prize = prize;
		this.item = item;
		this.owner = owner;
		this.winner = owner;
	}
	
	public Auction(double prize, Item item, Client owner, Client winner){
		this.prize = prize;
		this.item = item;
		this.owner = owner;
		this.winner = winner;
	}
	
	public void placeBid(double bidValue, Client winner){
		double newPrize = prize + bidValue;
		setWinner(winner);
		setPrize(newPrize);
	}
	
	@Override
	public int compareTo(Auction auction) {
		int compare = prize.compareTo(auction.getPrize());
		return compare;
	}
	
	public String toString(){
		String value = String.format(item + " : %1$.2f | winner: " + winner, prize);
		return value;
	}
	
	public double getPrize(){
		return prize;
	}
	
	public Client getWinner(){
		return winner;
	}
	
	public Client getOwner(){
		return owner;
	}
	
	public Item getItem(){
		return item;
	}
	
	public boolean equals(Object anAuction){
		Auction auction = (Auction) anAuction;
		Client testOwner = auction.getOwner();
		Item testItem = auction.getItem();
		String testItemName = testItem.getName();
		String itemName = item.getName();
		double testPrize = auction.getPrize();
		return owner.equals(testOwner) && itemName.equals(testItemName);
	}
	
	private void setPrize(double prize){
		this.prize = prize;
	}
	
	private void setWinner(Client winner){
		this.winner = winner;
	}
}
