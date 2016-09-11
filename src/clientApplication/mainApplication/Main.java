package clientApplication.mainApplication;

import clientApplication.dataContainer.DataContainer;
import clientApplication.networking.Networking;
import clientApplication.refresher.Refresher;
import clientApplication.userInterface.GUI;

public class Main {

	public static void main(String[] args) {
		DataContainer dataContainer = new DataContainer();
		GUI gui = new GUI(dataContainer);
		Refresher refresher = new Refresher(gui, dataContainer);
		Networking networking = new Networking(dataContainer);
		Thread networkingThread = new Thread(networking);
		Thread refresherThread = new Thread(refresher);
		networkingThread.start();
		refresherThread.start();
		while (dataContainer.workingStatus) {
		}
	}
}
