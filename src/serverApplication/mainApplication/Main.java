package serverApplication.mainApplication;

import java.net.ServerSocket;

import serverApplication.clientHandler.Handler;
import serverApplication.dataBaseConnector.DataBaseConnector;
import serverApplication.dataContainer.DataContainer;
import serverApplication.networking.Networking;
import serverApplication.userInterface.UserInterface;

public class Main {
	
	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = new ServerSocket(9000);
		DataContainer dataContainer = new DataContainer();
		DataBaseConnector dataBaseConnector = new DataBaseConnector(dataContainer);
		UserInterface userInterface = new UserInterface(dataBaseConnector, dataContainer);
		Thread userInterfaceThread = new Thread(userInterface);
		Thread dataBaseConnectorThread = new Thread(dataBaseConnector);
		userInterfaceThread.start();
		dataBaseConnectorThread.start();
		while(dataContainer.workingStatus){
			new Thread(new Handler(new Networking(dataContainer, serverSocket.accept()))).start();
		}
	}
}
