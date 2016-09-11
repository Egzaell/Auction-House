package serverApplication.dataContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import clientApplication.auction.Auction;
import clientApplication.client.Client;
import clientApplication.observerInterfaces.Observer;
import clientApplication.observerInterfaces.Subject;
import clientApplication.lock.Lock;

public class DataContainer implements Subject {

	public Lock lock = new Lock();
	public boolean workingStatus = true;
	public boolean checkPoint = false;
	private List<Client> clientList = new ArrayList<>();
	private List<Auction> auctionList = new ArrayList<>();
	private List<Observer> observersList = new ArrayList<>();
	private List<Auction> endedAuction = new ArrayList<>();

	public boolean loginClient(String login, String password) {
		boolean returnValue = false;
		boolean isClientOnList = isClientOnList(login);
		if (isClientOnList) {
			returnValue = isLoginDataCorrect(login, password);
		}
		return returnValue;
	}

	public boolean registerClient(Client client) {
		String testLogin = client.getLogin();
		boolean isClientOnList = isClientOnList(testLogin);
		if (isClientOnList) {
			return false;
		} else {
			addClientToList(client);
			return true;
		}
	}

	public void registerAuction(Auction auction) {
		boolean isAuctionOnList = isAuctionOnList(auction);
		if (isAuctionOnList) {
			int index = auctionList.indexOf(auction);
			Auction previousAuction = auctionList.get(index);
			updateAuction(previousAuction, auction);
		} else if (!isAuctionOnList) {
			if (!endedAuction.contains(auction))
				auctionList.add(auction);
		}
	}

	public Auction[] getAuctionsArray() {
		Auction[] auctionsArray = (Auction[]) auctionList.toArray();
		return auctionsArray;
	}

	public ArrayList<Client> getClients() {
		ArrayList<Client> clients = (ArrayList<Client>) clientList;
		return clients;
	}

	public ArrayList<Auction> getAuctions() {
		ArrayList<Auction> auctions = (ArrayList<Auction>) auctionList;
		return auctions;
	}

	public ArrayList<Auction> getEndedAuctions() {
		ArrayList<Auction> auctions = (ArrayList<Auction>) endedAuction;
		return auctions;
	}

	public void removeAuction(Auction auction) {
		if (auctionList.contains(auction))
			auctionList.remove(auction);
		if (!endedAuction.contains(auction))
			endedAuction.add(auction);
	}

	public void updateAuction(Auction previousAuction, Auction actualAuction) {
		if (previousAuction.getPrize() < actualAuction.getPrize()) {
			int index = auctionList.indexOf(previousAuction);
			auctionList.remove(previousAuction);
			auctionList.add(index, actualAuction);
		}
	}

	private boolean isAuctionOnList(Auction auction) {
		boolean testIsAuctionOnList = false;
		if (auctionList.contains(auction)) {
			testIsAuctionOnList = true;
		}
		return testIsAuctionOnList;
	}

	private void addClientToList(Client client) {
		clientList.add(client);
	}

	private void addAuctionToSet(Auction auction) {
		auctionList.add(auction);
	}

	private boolean isClientOnList(String login) {
		boolean returnValue = false;
		for (Client client : clientList) {
			String clientLogin = client.getLogin();
			if (clientLogin.equals(login)) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	private boolean isLoginDataCorrect(String login, String password) {
		boolean returnValue = false;
		for (Client client : clientList) {
			String testLogin = client.getLogin();
			String testPassword = client.getPassword();
			if (testLogin.equals(login) && testPassword.equals(password)) {
				returnValue = true;
			}
		}
		return returnValue;
	}

	@Override
	public void addObserver(Observer observer) {
		observersList.add(observer);
	}

	@Override
	public void removeObserver(Observer observer) {
		int observerIndex = observersList.indexOf(observer);
		observersList.remove(observerIndex);
	}

	@Override
	public void updateObservers() {
		for (Observer observer : observersList) {
			observer.update();
		}
	}

	public void lockTheLock() {
		try {
			lock.lock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void unlockTheLock() {
		lock.unlock();
	}

}
