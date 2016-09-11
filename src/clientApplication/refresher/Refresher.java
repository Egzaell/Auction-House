package clientApplication.refresher;

import java.util.ArrayList;

import javax.swing.DefaultListModel;

import clientApplication.auction.Auction;
import clientApplication.client.Client;
import clientApplication.dataContainer.DataContainer;
import clientApplication.user.User;
import clientApplication.userInterface.GUI;

public class Refresher implements Runnable {

	private DataContainer dataContainer;
	private GUI gui;
	private DefaultListModel auctionListModel;
	private DefaultListModel myAuctionList;
	private int auctionsQuantity = 0;
	private double auctionPrizeSummary = 0.0;
	private ArrayList<Auction> auctionList = new ArrayList<>();

	public Refresher(GUI gui, DataContainer dataContainer) {
		this.gui = gui;
		this.dataContainer = dataContainer;
		this.auctionListModel = gui.getAuctionList();
		this.myAuctionList = gui.getMyAuctionList();
	}

	@Override
	public void run() {
		boolean isQuantityDiffrent = false;
		boolean isPrizeSumDiffrent = true;
		while (dataContainer.workingStatus) {
			dataContainer.lockTheLock();
			if (dataContainer.isUserLogged) {
				getAuctions();
				isQuantityDiffrent = isQuantityChanged();
				isPrizeSumDiffrent = isPrizeChanged();
				if (isQuantityDiffrent || isPrizeSumDiffrent) {
					clearListModels();
					updateListModels();
				}
			}
			dataContainer.unlockTheLock();
		}
	}

	private void getAuctions() {
		if (!auctionList.isEmpty()) {
			auctionList.clear();
		}
		auctionList = dataContainer.getAuctions();
	}

	private int getAuctionsQuantity() {
		int quantity = 0;
		for (Auction auction : auctionList) {
			quantity++;
		}
		return quantity;
	}

	private boolean isQuantityChanged() {
		int actualAuctionQuantity = getAuctionsQuantity();
		if (auctionsQuantity == actualAuctionQuantity) {
			return false;
		} else {
			auctionsQuantity = actualAuctionQuantity;
			return true;
		}
	}

	private boolean isPrizeChanged() {
		double actualAuctionPrizeSummary = getPrizeSum();
		if (auctionPrizeSummary == actualAuctionPrizeSummary) {
			return false;
		} else {
			auctionPrizeSummary = actualAuctionPrizeSummary;
			return true;
		}
	}

	private double getPrizeSum() {
		double prizeSum = 0.0;
		for (Auction auction : auctionList) {
			prizeSum += auction.getPrize();
		}
		return prizeSum;
	}

	private void clearListModels() {
		auctionListModel.clear();
		myAuctionList.clear();
	}

	private void updateListModels() {
		if (dataContainer.isUserLogged) {
			for (Auction auction : dataContainer.getAuctions()) {
				auctionListModel.addElement(auction);
				Client client = auction.getOwner();
				Client nullClient = new Client(null, null, null, null, null);
				User user = gui.getUser();
				if (!user.equals(nullClient)) {
					if (client.equals(user)) {
						myAuctionList.addElement(auction);
					}
				}
			}
		}
	}
}
