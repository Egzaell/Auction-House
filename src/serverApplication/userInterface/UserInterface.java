package serverApplication.userInterface;

import java.util.Scanner;

import serverApplication.dataBaseConnector.DataBaseConnector;
import serverApplication.dataContainer.DataContainer;

public class UserInterface implements Runnable {

	private final String WELCOME = "Witaj w programie Auction House! Jest to serwer aplikacji";
	private final String MENU = "Menu:";
	private final String OPTION_ONE = "1. Zapisz dane do bazy";
	private final String OPTION_TWO = "2. Zamknij serwer";
	private final String OPTION_THREE = "3. Przywroc menu";
	private DataBaseConnector dataBaseConnector;
	private DataContainer dataContainer;
	private int userChoice = 0;

	public UserInterface(DataBaseConnector dataBaseConnector, DataContainer dataContainer) {
		this.dataBaseConnector = dataBaseConnector;
		this.dataContainer = dataContainer;
		setUpMenu();
	}

	@Override
	public void run() {
		while (dataContainer.workingStatus){
			getUserChoice();
			makeUserRequest();
		}
	}

	private void setUpMenu() {
		System.out.println(WELCOME);
		System.out.println(MENU);
		System.out.println(OPTION_ONE);
		System.out.println(OPTION_TWO);
		System.out.println(OPTION_THREE);
	}

	private void getUserChoice() {
		Scanner scanner = new Scanner(System.in);
		userChoice = scanner.nextInt();
	}

	private void makeUserRequest() {
		switch (userChoice) {
		case 1: {
			dataContainer.lockTheLock();
			dataContainer.checkPoint = true;
			dataContainer.unlockTheLock();
			break;
		}
		case 2: {
			dataContainer.lockTheLock();
			dataContainer.workingStatus = false;
			dataContainer.unlockTheLock();
			break;
		}
		case 3: {
			setUpMenu();
			break;
		}
		}
	}
}
