package serverApplication.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import clientApplication.auction.Auction;
import clientApplication.client.Client;
import serverApplication.dataContainer.DataContainer;

public class Networking {

	private boolean isConnected;
	private Socket socket;
	private DataContainer dataContainer;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private ArrayList<Auction> auctionList = new ArrayList<>();
	private ArrayList<Client> clientList = new ArrayList<>();

	public Networking(DataContainer dataContainer, Socket socket) {
		this.dataContainer = dataContainer;
		this.socket = socket;
		this.isConnected = true;
	}

	public void connect() {
		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
			synchronizeData();
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		} finally {
			disconnect();
		}
	}

	private void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void synchronizeData() throws InterruptedException, IOException {
		while (dataContainer.workingStatus) {
			if (!isConnected) {
				break;
			}
			dataContainer.lockTheLock();
			receiveData();
			sendData();
			dataContainer.unlockTheLock();
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
				} else if (receivedDataLabel.equals("CONNECTION_END")) {
					isConnected = false;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	private void sendData() {
		try {
			sendClients();
			sendEndedAuctions();
			sendAuctions();
			sendEndLabel();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void receiveClients() throws ClassNotFoundException, IOException {
		Client client = (Client) inputStream.readObject();
		dataContainer.registerClient(client);
	}

	private void receiveAuctions() throws ClassNotFoundException, IOException {
		Auction auction = (Auction) inputStream.readObject();
		dataContainer.registerAuction(auction);
	}

	private void receivedEndedAuction() throws ClassNotFoundException, IOException {
		Auction auction = (Auction) inputStream.readObject();
		dataContainer.removeAuction(auction);
	}

	private void sendClients() throws IOException {
		clientList = dataContainer.getClients();
		Object[] clientArray = clientList.toArray();
		for (Object client : clientArray) {
			outputStream.writeObject("CLIENTS");
			outputStream.flush();
			outputStream.writeObject(client);
			outputStream.flush();
		}
	}

	private void sendAuctions() throws IOException {
		auctionList = dataContainer.getAuctions();
		Object[] auctionArray = auctionList.toArray();
		for (Object auction : auctionArray) {
			outputStream.writeObject("AUCTIONS");
			outputStream.flush();
			outputStream.writeUnshared(auction);
			outputStream.flush();
		}
	}

	private void sendEndedAuctions() throws IOException {
		ArrayList<Auction> endedAuction = dataContainer.getEndedAuctions();
		Object[] endedAuctionArray = endedAuction.toArray();
		for (Object auction : endedAuctionArray) {
			outputStream.writeObject("ENDED_AUCTION");
			outputStream.flush();
			outputStream.writeObject(auction);
			outputStream.flush();
		}
	}

	private void sendEndLabel() throws IOException {
		outputStream.writeObject("END");
		outputStream.flush();
	}
}
