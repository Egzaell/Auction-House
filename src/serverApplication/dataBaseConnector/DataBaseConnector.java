package serverApplication.dataBaseConnector;

import java.io.Console;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import clientApplication.auction.Auction;
import clientApplication.client.Client;
import clientApplication.item.Item;
import serverApplication.dataContainer.DataContainer;

public class DataBaseConnector implements Runnable {

	private ArrayList<Client> clientList = new ArrayList<>();
	private ArrayList<Item> itemList = new ArrayList<>();
	private ArrayList<Auction> auctionList = new ArrayList<>();
	private DataContainer dataContainer;
	private static final String DRIVER = "org.sqlite.JDBC";
	private static final String DB_URL = "jdbc:sqlite:baza.db";
	private Connection connection;
	private Statement statement;
	private Console console;

	public DataBaseConnector(DataContainer dataContainer) {
		getJDBCDriverClass();
		connection = connectToDataBase();
		statement = createStatement();
		this.dataContainer = dataContainer;
		createTables();
		selectDataFromDataBase();
		updateDataContainer();
		clearLists();
	}

	@Override
	public void run() {
		while (dataContainer.workingStatus) {
			dataContainer.lockTheLock();
			if (dataContainer.checkPoint) {
				dropTables();
				createTables();
				setClientList();
				setAuctionList();
				setItemList();
				instertDataIntoDataBase();
				dataContainer.checkPoint = false;
			}
			dataContainer.unlockTheLock();
		}
	}

	private void instertDataIntoDataBase() {
		System.out.println("Saving Clients");
		for (Client client : clientList) {
			instertClient(client);
			loading(clientList.size(), clientList.indexOf(client));
		}
		System.out.println("");
		System.out.println("Saving Items");
		for (Item item : itemList) {
			insertItem(item);
			loading(itemList.size(), itemList.indexOf(item));
		}
		System.out.println("");
		System.out.println("Saving Autions");
		for (Auction auction : auctionList) {
			insertAuction(auction);
			loading(auctionList.size(), auctionList.indexOf(auction));
		}
		System.out.println("");
	}

	private void updateDataContainer() {
		for (Client client : clientList) {
			dataContainer.registerClient(client);
		}
		for (Auction auction : auctionList) {
			dataContainer.registerAuction(auction);
		}
	}

	private void selectDataFromDataBase() {
		selectClients();
		selectItems();
		selectAuctions();
	}

	private void clearLists() {
		clientList.clear();
		itemList.clear();
		auctionList.clear();
	}

	private void setClientList() {
		clientList = dataContainer.getClients();
	}

	private void setAuctionList() {
		auctionList = dataContainer.getAuctions();
	}

	private void setItemList() {
		for (Auction auction : auctionList) {
			Item item = auction.getItem();
			itemList.add(item);
		}
	}

	private void createTables() {
		String createClients = "CREATE TABLE IF NOT EXISTS CLIENTS (id INTEGER PRIMARY KEY AUTOINCREMENT, firstName VARCHAR(255), lastName VARCHAR(255), login VARCHAR(255), password VARCHAR(255), adress VARCHAR(255))";
		String createItems = "CREATE TABLE IF NOT EXISTS ITEMS (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(255), description VARCHAR(255))";
		String createAuctions = "CREATE TABLE IF NOT EXISTS AUCTIONS(id INTEGER PRIMARY KEY AUTOINCREMENT, prize DECIMAL(8,2), item_id INT references ITEMS(id), winner_id INTEGER references CLIENTS(id), owner_id INTEGER references CLIENTS(id))";
		try {
			statement.execute(createClients);
			statement.execute(createItems);
			statement.execute(createAuctions);
		} catch (SQLException e) {
			System.err.println("Blad przy tworzeniu tabel");
			e.printStackTrace();
		}
	}

	private void dropTables() {
		String dropClients = "DROP TABLE CLIENTS";
		String dropItems = "DROP TABLE ITEMS";
		String dropAuctions = "DROP TABLE AUCTIONS";
		try {
			statement.execute(dropClients);
			statement.execute(dropItems);
			statement.execute(dropAuctions);
		} catch (SQLException e) {
			System.err.println("Blad przy usowaniu tabel");
			e.printStackTrace();
		}
	}

	private void instertClient(Client client) {
		try {
			PreparedStatement preparedStatement = connection
					.prepareStatement("INSERT INTO CLIENTS VALUES(NULL, ?, ?, ?, ?, ?);");
			preparedStatement.setString(1, client.getFirstName());
			preparedStatement.setString(2, client.getLastName());
			preparedStatement.setString(3, client.getLogin());
			preparedStatement.setString(4, client.getPassword());
			preparedStatement.setString(5, client.getAdress());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Blad przy wstawianiu klienta");
			e.printStackTrace();
		}
	}

	private void insertItem(Item item) {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ITEMS VALUES(NULL, ?, ?);");
			preparedStatement.setString(1, item.getName());
			preparedStatement.setString(2, item.getDescription());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Blad przy wstawianiu przedmiotu");
			e.printStackTrace();
		}
	}

	private void insertAuction(Auction auction) {
		Item item = auction.getItem();
		Client owner = auction.getOwner();
		Client winner = auction.getWinner();
		int itemIndex = itemList.indexOf(item) + 1;
		int ownerIndex = getIndexOfClient(owner) + 1;
		int winnerIndex = getIndexOfClient(winner) + 1;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO AUCTIONS VALUES(NULL, ?, ?, ?, ?);");
			preparedStatement.setDouble(1, auction.getPrize());
			preparedStatement.setInt(2, itemIndex);
			preparedStatement.setInt(3, ownerIndex);
			preparedStatement.setInt(4, winnerIndex);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Blad przy wstawianiu aukcji");
			e.printStackTrace();
		}
	}

	private void selectClients() {
		int id;
		String firstName;
		String lastName;
		String login;
		String password;
		String adress;
		Client client;
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM CLIENTS");
			while (result.next()) {
				id = result.getInt("id") - 1;
				firstName = result.getString("firstName");
				lastName = result.getString("lastName");
				login = result.getString("login");
				password = result.getString("password");
				adress = result.getString("adress");
				client = new Client(firstName, lastName, login, password, adress);
				clientList.add(id, client);
			}
		} catch (SQLException e) {
			System.err.println("Blad przy pobieraniu klientow");
			e.printStackTrace();
		}
	}

	private void selectItems() {
		int id;
		String name;
		String description;
		Item item;
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM ITEMS");
			while (result.next()) {
				id = result.getInt("id") - 1;
				name = result.getString("name");
				description = result.getString("description");
				item = new Item(name, description);
				itemList.add(id, item);
			}
		} catch (SQLException e) {
			System.err.println("Blad przy pobieraniu przedmiotu");
			e.printStackTrace();
		}
	}

	private void selectAuctions() {
		int id;
		double prize;
		int itemId;
		int ownerId;
		int winnerId;
		Item item;
		Client owner;
		Client winner;
		Auction auction;
		try {
			ResultSet result = statement.executeQuery("SELECT * FROM AUCTIONS");
			while (result.next()) {
				id = result.getInt("id") - 1;
				prize = result.getDouble("prize");
				itemId = result.getInt("item_id") - 1;
				ownerId = result.getInt("owner_id") - 1;
				winnerId = result.getInt("winner_id") - 1;
				item = itemList.get(itemId);
				owner = clientList.get(ownerId);
				winner = clientList.get(winnerId);
				auction = new Auction(prize, item, owner, winner);
				auctionList.add(auction);
			}
		} catch (SQLException e) {
			System.err.println("Blad przy pobieraniu aukcji");
			e.printStackTrace();
		}
	}

	private Class getJDBCDriverClass() {
		Class jdbcClass = null;
		try {
			jdbcClass = Class.forName(DataBaseConnector.DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("Brak sterownika JDBC!");
			e.printStackTrace();
		}
		return jdbcClass;
	}

	private Connection connectToDataBase() {
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(DB_URL);
		} catch (SQLException e) {
			System.err.println("Problem z otwarciem polaczenia");
			e.printStackTrace();
		}
		return connection;
	}

	private Statement createStatement() {
		Statement statement = null;
		try {
			statement = connection.createStatement();
		} catch (SQLException e) {
			System.err.println("Problem ze stworzeniem statement");
			e.printStackTrace();
		}
		return statement;
	}

	private int getIndexOfClient(Client client) {
		int index = 0;
		index = clientList.indexOf(client);
		return index;
	}

	private void loading(int endValue, int currentValue) {
		int percentage = (currentValue / endValue) * 100;
		if ((percentage % 5) == 0) {
			System.out.print("=");
		} else if (endValue == currentValue) {
			System.out.println(" Done!");
		}
	}
}
