package clientApplication.dataContainer;

import java.util.ArrayList;
import java.util.List;

import clientApplication.auction.Auction;
import clientApplication.client.Client;
import clientApplication.lock.Lock;
import clientApplication.observerInterfaces.Observer;
import clientApplication.observerInterfaces.Subject;

public class DataContainer implements Subject {

	public Lock lock = new Lock();
	public boolean workingStatus = true;
	public boolean isUserLogged = false;
	private List<Auction> endedAuction = new ArrayList<>();
	private List<Client> clientList = new ArrayList<>();
	private List<Auction> auctionList = new ArrayList<>();
	private List<Observer> observersList = new ArrayList<>();

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

	public Client getClientByLogin(String login) {
		Client client = null;
		for (Client clientIterator : clientList) {
			String testLogin = clientIterator.getLogin();
			if (testLogin.equals(login)) {
				client = clientIterator;
			}
		}
		return client;
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

	public Auction getSelectedAuction(int selectedIndex) {
		Auction auction = auctionList.get(selectedIndex);
		return auction;
	}

	public Auction getSpecificAuction(Auction auction) {
		int auctionIndex = auctionList.indexOf(auction);
		Auction specificAuction = auctionList.get(auctionIndex);
		return specificAuction;
	}

	public Auction[] getAuctionsArray() {
		Auction[] auctionsArray = (Auction[]) auctionList.toArray();
		return auctionsArray;
	}

	public ArrayList<Client> getClients() {
		return (ArrayList<Client>) clientList;
	}

	public ArrayList<Auction> getAuctions() {
		ArrayList<Auction> auctionListCopy = new ArrayList<>();
		auctionListCopy.addAll(auctionList);
		return auctionListCopy;
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

	public void changeWorkingStatus() {
		workingStatus = false;
	}

	private boolean isAuctionOnList(Auction auction) {
		if (auctionList.contains(auction)) {
			return true;
		} else {
			return false;
		}
	}

	private void addClientToList(Client client) {
		clientList.add(client);
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
