package clientApplication.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import clientApplication.auction.Auction;
import clientApplication.client.Client;
import clientApplication.dataContainer.DataContainer;
import clientApplication.observerInterfaces.Observer;

public class Networking implements Runnable, Observer {

	private Socket socket;
	private DataContainer dataContainer;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private ArrayList<Auction> auctionList = new ArrayList<>();
	private ArrayList<Client> clientList = new ArrayList<>();

	public Networking(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
	}

	@Override
	public void run() {
		connect();
	}

	@Override
	public void update() {

	}

	private void connect() {
		try {
			socket = new Socket("127.0.0.1", 9000);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			synchronizeData();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	private void disconnect() {
		try {
			sendConnectionEndLabel();
			sendEndLabel();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void synchronizeData() throws IOException {
		while (dataContainer.workingStatus) {
			dataContainer.lockTheLock();
			getAuctions();
			sendData();
			receiveData();
			dataContainer.unlockTheLock();
		}
	}

	private void sendData() {
		try {
			sendClients();
			sendEndedAuctions();
			sendAuctions();
			sendEndLabel();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveData() {
		String receivedDataLabel = "";
		try {
			while (!receivedDataLabel.equals("END")) {
				receivedDataLabel = (String) inputStream.readObject();
				if (receivedDataLabel.equals("CLIENTS")) {
					receiveClients();
				} else if (receivedDataLabel.equals("AUCTIONS")) {
					receiveAuctions();
				} else if (receivedDataLabel.equals("ENDED_AUCTION")) {
					receivedEndedAuction();
				}	
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	private void sendClients() throws IOException {
		clientList = dataContainer.getClients();
		for (Client client : clientList) {
			outputStream.writeObject("CLIENTS");
			outputStream.flush();
			outputStream.writeObject(client);
			outputStream.flush();
		}
	}

	private void receiveClients() throws ClassNotFoundException, IOException {
		Client client = (Client) inputStream.readObject();
		dataContainer.registerClient(client);
	}

	private void sendAuctions() throws IOException {
		for (Auction auction : auctionList) {
			outputStream.writeObject("AUCTIONS");
			outputStream.flush();
			outputStream.writeUnshared(auction);
			outputStream.flush();
		}
	}
	
	private void sendEndedAuctions() throws IOException {
		ArrayList<Auction> endedAuction = dataContainer.getEndedAuctions();
		Object[] endedAuctionArray = endedAuction.toArray();
		for(Object auction : endedAuctionArray){
			outputStream.writeObject("ENDED_AUCTION");
			outputStream.flush();
			outputStream.writeObject(auction);
			outputStream.flush();
		}
	}

	private void getAuctions() {
		if (!auctionList.isEmpty()) {
			auctionList.clear();
		}
		auctionList = dataContainer.getAuctions();
	}

	private void receiveAuctions() throws ClassNotFoundException, IOException {
		Auction auction = (Auction) inputStream.readObject();
		dataContainer.registerAuction(auction);
	}
	
	private void receivedEndedAuction() throws ClassNotFoundException, IOException {
		Auction auction = (Auction) inputStream.readObject();
		dataContainer.removeAuction(auction);
	}

	private void sendEndLabel() throws IOException {
		outputStream.writeObject("END");
		outputStream.flush();
	}

	private void sendConnectionEndLabel() throws IOException {
		outputStream.writeObject("CONNECTION_END");
		outputStream.flush();
	}
}
