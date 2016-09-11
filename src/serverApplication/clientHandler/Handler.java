package serverApplication.clientHandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import serverApplication.networking.Networking;

public class Handler implements Runnable {

	private Networking networking;
	
	public Handler(Networking networking){
		this.networking = networking;
	}

	@Override
	public void run() {
		networking.connect();
	}
}
