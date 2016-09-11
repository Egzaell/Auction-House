package clientApplication.userInterface;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import clientApplication.auction.Auction;
import clientApplication.client.Client;
import clientApplication.dataContainer.DataContainer;
import clientApplication.fileSupporter.FileSupporter;
import clientApplication.item.Item;
import clientApplication.user.User;

public class GUI {

	private final String NEW_LINE = System.getProperty("line.separator");
	private final String ABOUT_PROGRAM = ".:PROGRAM AUCTION HOUSE:." + NEW_LINE
			+ "Program sluzy do przeprowadzania interaktywnych aukcji." + NEW_LINE + NEW_LINE
			+ "Aby wystawic przedmiot na aukcje cene nalezy podawac w formacie 100.00, uzywajac kropki zamiast przecinka."
			+ NEW_LINE + NEW_LINE + "Wykonali:";
	private User user;
	private DataContainer dataContainer;
	private JFrame mainFrame;
	private JPanel loginPanel;
	private JLabel loginPanelTitlelabel;
	private JLabel loginPanelLoginLabel;
	private JTextField loginPanelLoginTextField;
	private JLabel loginPanelPasswordLabel;
	private JTextField loginPanelPasswordTextLabel;
	private JButton loginPanelLoginButton;

	private JPanel[] panelArray;
	private JPanel registerPanel;
	private JLabel registerPanelTitleLabel;
	private JLabel registerPanelFirstNameLabel;
	private JTextField registerPanelFirstNameTextField;
	private JLabel registerPanelLastNameLabel;
	private JTextField registerPanelLastNameTextField;
	private JLabel registerPanelLoginLabel;
	private JTextField registerPanelLoginTextField;
	private JLabel registerPanelPasswordLabel;
	private JTextField registerPanelPasswordTextField;
	private JLabel registerPanelAdressLabel;
	private JTextField registerPanelAdressTextField;
	private JButton registerPanelRegisterButton;
	private JPanel auctionPanel;
	private JList auctionPanelAuctionList;

	private DefaultListModel<Auction> auctionList = new DefaultListModel<>();
	private DefaultListModel<Auction> myAuctionList = new DefaultListModel<>();
	private JScrollPane auctionPanelAuctionListScroll;
	private JPanel auctionPanelButtonPanel;
	private JButton auctionPanelBidButton;
	private JTabbedPane tabbedPane;
	private JPanel itemPanel;
	private JLabel itemPanelItemNameLabel;
	private JTextField itemPanelItemNameTextField;
	private JLabel itemPanelItemDescriptionLabel;
	private JTextField itemPanelItemDescriptionTextField;
	private JTextArea itemPanelItemDescriptionTextArea;
	private JScrollPane itemPanelItemDescriptionScrollPane;
	private JLabel itemPanelAuctionPrizeLabel;
	private JTextField itemPanelAuctionPrizeTextField;
	private JPanel itemPanelButtonPanel;
	private JButton itemPanelCreateAuctionButton;
	private JPanel myAuctionPanel;
	private JList myAuctionPanelAuctionList;
	private JScrollPane myAuctionPanelAuctionListScrollPane;
	private JPanel myAuctionPanelButtonPanel;
	private JButton myAuctionPanelEndAuctionButton;
	private JPanel aboutPanel;
	private JTextArea aboutPanelAboutTextArea;
	private JButton auctionPanelExitButton;
	private JButton loginPanerExitButton;
	private JTextField auctionPanelBidTextField;

	public GUI(DataContainer dataContainer) {
		this.dataContainer = dataContainer;
		initGUI();
	}

	public DefaultListModel getAuctionList() {
		return auctionList;
	}

	public DefaultListModel getMyAuctionList() {
		return myAuctionList;
	}

	public User getUser() {
		return user;
	}

	private void initGUI() {
		BorderLayout borderLayout = new BorderLayout();
		mainFrame = new JFrame("Auction House");
		mainFrame.setSize(640, 480);
		mainFrame.setLocation(640, 480);
		mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		mainFrame.setLayout(borderLayout);
		createEntranceArray();
		tabbedPane = createTabbedPane(panelArray);

		mainFrame.getContentPane().add(BorderLayout.CENTER, tabbedPane);
		mainFrame.setVisible(true);
	}

	private JTabbedPane createTabbedPane(JPanel[] panelArray) {
		JTabbedPane tabbedPane = new JTabbedPane();
		for (JPanel panel : panelArray) {
			String title = panel.getName();
			tabbedPane.add(title, panel);
		}
		return tabbedPane;
	}

	private JPanel[] createEntranceArray() {
		panelArray = new JPanel[2];
		loginPanel = createLoginPanel();
		registerPanel = createRegisterPanel();
		panelArray[0] = loginPanel;
		panelArray[1] = registerPanel;
		return panelArray;
	}

	private JPanel[] createProgramArray() {
		panelArray = new JPanel[4];
		auctionPanel = createAuctionPanel();
		itemPanel = createItemPanel();
		myAuctionPanel = createMyAuctionPanel();
		aboutPanel = createAboutPanel();
		panelArray[0] = auctionPanel;
		panelArray[1] = itemPanel;
		panelArray[2] = myAuctionPanel;
		panelArray[3] = aboutPanel;
		return panelArray;
	}

	private JPanel createLoginPanel() {
		loginPanel = new JPanel();
		loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
		loginPanelTitlelabel = new JLabel("LOGOWANIE");
		loginPanelLoginLabel = new JLabel("login");
		loginPanelLoginTextField = new JTextField("");
		loginPanelPasswordLabel = new JLabel("haslo");
		loginPanelPasswordTextLabel = new JTextField("");
		loginPanelLoginButton = new JButton("Zaloguj");
		loginPanelLoginButton.addActionListener(new LoginListener());
		loginPanerExitButton = new JButton("Wyjdz");
		loginPanerExitButton.addActionListener(new ExitListener());
		loginPanel.add(loginPanelTitlelabel);
		loginPanel.add(loginPanelLoginLabel);
		loginPanel.add(loginPanelLoginTextField);
		loginPanel.add(loginPanelPasswordLabel);
		loginPanel.add(loginPanelPasswordTextLabel);
		loginPanel.add(loginPanelLoginButton);
		loginPanel.add(loginPanerExitButton);
		loginPanel.setName("PANEL LOGOWANIA");
		return loginPanel;
	}

	private JPanel createRegisterPanel() {
		registerPanel = new JPanel();
		registerPanel.setLayout(new BoxLayout(registerPanel, BoxLayout.Y_AXIS));
		registerPanelTitleLabel = new JLabel("REJESTRACJA");
		registerPanelFirstNameLabel = new JLabel("Imie:");
		registerPanelFirstNameTextField = new JTextField("");
		registerPanelLastNameLabel = new JLabel("Nazwisko:");
		registerPanelLastNameTextField = new JTextField("");
		registerPanelLoginLabel = new JLabel("Login:");
		registerPanelLoginTextField = new JTextField("");
		registerPanelPasswordLabel = new JLabel("Haslo:");
		registerPanelPasswordTextField = new JTextField("");
		registerPanelAdressLabel = new JLabel("Adres:");
		registerPanelAdressTextField = new JTextField("");
		registerPanelRegisterButton = new JButton("Zarejestruj");
		registerPanelRegisterButton.addActionListener(new RegisterListener());
		registerPanel.add(registerPanelTitleLabel);
		registerPanel.add(registerPanelFirstNameLabel);
		registerPanel.add(registerPanelFirstNameTextField);
		registerPanel.add(registerPanelLastNameLabel);
		registerPanel.add(registerPanelLastNameTextField);
		registerPanel.add(registerPanelLoginLabel);
		registerPanel.add(registerPanelLoginTextField);
		registerPanel.add(registerPanelPasswordLabel);
		registerPanel.add(registerPanelPasswordTextField);
		registerPanel.add(registerPanelAdressLabel);
		registerPanel.add(registerPanelAdressTextField);
		registerPanel.add(registerPanelRegisterButton);
		registerPanel.setName("PANEL REJESTRACJI");
		return registerPanel;
	}

	private JPanel createAuctionPanel() {
		auctionPanel = new JPanel();
		auctionPanel.setLayout(new BorderLayout());
		auctionPanel.setName("PANEL AUKCYJNY");
		auctionPanelAuctionList = new JList(auctionList);
		auctionPanelAuctionListScroll = new JScrollPane(auctionPanelAuctionList);
		auctionPanelButtonPanel = new JPanel();
		auctionPanelButtonPanel.setLayout(new BoxLayout(auctionPanelButtonPanel, BoxLayout.Y_AXIS));
		auctionPanelBidTextField = new JTextField("");
		auctionPanelBidButton = new JButton("Podbij");
		auctionPanelBidButton.addActionListener(new BidListener());
		auctionPanelButtonPanel.add(auctionPanelBidTextField);
		auctionPanelButtonPanel.add(auctionPanelBidButton);
		auctionPanelExitButton = new JButton("Wyjdz z programu");
		auctionPanelExitButton.addActionListener(new ExitListener());
		auctionPanel.add(BorderLayout.CENTER, auctionPanelAuctionListScroll);
		auctionPanel.add(BorderLayout.WEST, auctionPanelButtonPanel);
		auctionPanel.add(BorderLayout.SOUTH, auctionPanelExitButton);
		return auctionPanel;
	}

	private JPanel createItemPanel() {
		itemPanel = new JPanel();
		itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
		itemPanel.setName("PANEL PRZEDMIOTOW");
		itemPanelItemNameLabel = new JLabel("Nazwa:");
		itemPanelItemNameTextField = new JTextField("");
		itemPanelItemDescriptionLabel = new JLabel("Opis");
		itemPanelItemDescriptionTextField = new JTextField("");
		itemPanelAuctionPrizeLabel = new JLabel("Cena:");
		itemPanelAuctionPrizeTextField = new JTextField("");
		itemPanelButtonPanel = new JPanel();
		itemPanelButtonPanel.setLayout(new BoxLayout(itemPanelButtonPanel, BoxLayout.X_AXIS));
		itemPanelCreateAuctionButton = new JButton("Wystaw na aukcje");
		itemPanelCreateAuctionButton.addActionListener(new CreateAuctionListener());
		itemPanelButtonPanel.add(itemPanelCreateAuctionButton);
		itemPanel.add(itemPanelItemNameLabel);
		itemPanel.add(itemPanelItemNameTextField);
		itemPanel.add(itemPanelItemDescriptionLabel);
		itemPanel.add(itemPanelItemDescriptionTextField);
		itemPanel.add(itemPanelAuctionPrizeLabel);
		itemPanel.add(itemPanelAuctionPrizeTextField);
		itemPanel.add(itemPanelButtonPanel);
		return itemPanel;
	}

	private JPanel createMyAuctionPanel() {
		myAuctionPanel = new JPanel();
		myAuctionPanel.setLayout(new BorderLayout());
		myAuctionPanel.setName("PANEL MOJE AUKCJE");
		myAuctionPanelAuctionList = new JList(myAuctionList);
		myAuctionPanelAuctionListScrollPane = new JScrollPane(myAuctionPanelAuctionList);
		myAuctionPanelButtonPanel = new JPanel();
		myAuctionPanelButtonPanel.setLayout(new BoxLayout(myAuctionPanelButtonPanel, BoxLayout.Y_AXIS));
		myAuctionPanelEndAuctionButton = new JButton("Zakoncz aukcje");
		myAuctionPanelEndAuctionButton.addActionListener(new EndAuctionListener());
		myAuctionPanelButtonPanel.add(myAuctionPanelEndAuctionButton);
		myAuctionPanel.add(BorderLayout.CENTER, myAuctionPanelAuctionListScrollPane);
		myAuctionPanel.add(BorderLayout.WEST, myAuctionPanelButtonPanel);
		return myAuctionPanel;
	}

	private JPanel createAboutPanel() {
		aboutPanel = new JPanel();
		aboutPanel.setLayout(new BorderLayout());
		aboutPanel.setName("O PROGRAMIE");
		aboutPanelAboutTextArea = new JTextArea();
		aboutPanelAboutTextArea.setText(ABOUT_PROGRAM);
		aboutPanelAboutTextArea.setEditable(false);
		aboutPanel.add(BorderLayout.CENTER, aboutPanelAboutTextArea);
		return aboutPanel;
	}

	class LoginListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lockTheLock();
			String login = loginPanelLoginTextField.getText();
			String password = loginPanelPasswordTextLabel.getText();
			boolean loginCheck = dataContainer.loginClient(login, password);
			if (loginCheck) {
				Client client = dataContainer.getClientByLogin(login);
				dataContainer.isUserLogged = true;
				user = User.getInstance(client);
				changeView();
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "Zly login lub haslo!");
			}
			dataContainer.unlockTheLock();
		}

		private void changeView() {
			mainFrame.remove(tabbedPane);
			panelArray = null;
			panelArray = createProgramArray();
			tabbedPane = createTabbedPane(panelArray);
			mainFrame.getContentPane().add(BorderLayout.CENTER, tabbedPane);
			mainFrame.setVisible(true);
		}
	}

	class RegisterListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lockTheLock();
			String message;
			String firstName = registerPanelFirstNameTextField.getText();
			String lastName = registerPanelLastNameTextField.getText();
			String login = registerPanelLoginTextField.getText();
			String password = registerPanelPasswordTextField.getText();
			String adress = registerPanelAdressTextField.getText();
			Client clientNew = new Client(firstName, lastName, login, password, adress);
			boolean registerCheck = dataContainer.registerClient(clientNew);
			if (registerCheck) {
				message = "SUCCES!";
			} else {
				message = "FAILURE!";
			}
			JOptionPane.showMessageDialog(new JFrame(), message);
			dataContainer.unlockTheLock();
		}
	}

	class BidListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lockTheLock();
			Auction auction = getAuction();
			Auction actualAuction = auction;
			double bid = Double.parseDouble(auctionPanelBidTextField.getText());
			actualAuction.placeBid(bid, user);
			dataContainer.unlockTheLock();
		}

		private Auction getAuction() {
			Auction auction = null;
			int selectedIndex = auctionPanelAuctionList.getSelectedIndex();
			try {
				auction = dataContainer.getSelectedAuction(selectedIndex);
			} catch (ArrayIndexOutOfBoundsException e) {
				String message = "Aby podbic nalezy zaznaczyc aukcje!";
				JOptionPane.showMessageDialog(new JFrame(), message);
			}
			return auction;
		}
	}

	class CreateAuctionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lockTheLock();
			String name = itemPanelItemNameTextField.getText();
			String description = itemPanelItemDescriptionTextField.getText();
			String stringPrize = itemPanelAuctionPrizeTextField.getText();
			double prize = Double.parseDouble(stringPrize);
			Item item = new Item(name, description);
			Auction auction = new Auction(prize, item, user);
			dataContainer.registerAuction(auction);
			dataContainer.unlockTheLock();
		}
	}

	class EndAuctionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			dataContainer.lockTheLock();
			int index = myAuctionPanelAuctionList.getSelectedIndex();
			Auction auction = myAuctionList.getElementAt(index);
			Auction specificAuction = dataContainer.getSpecificAuction(auction);
			FileSupporter fileSupporter = new FileSupporter(specificAuction);
			dataContainer.removeAuction(specificAuction);
			dataContainer.unlockTheLock();
		}
	}
	
	class ExitListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			dataContainer.lockTheLock();
			dataContainer.changeWorkingStatus();
			dataContainer.unlockTheLock();
			mainFrame.dispose();
			System.exit(1);
		}
	}
}
