package clientApplication.user;

import clientApplication.client.Client;

public class User extends Client {

	public static final User INSTANCE = null;
	
	private User(String firstName, String lastName, String login, String password, String adress) {
		super(firstName, lastName, login, password, adress);
	}
	
	public static User getInstance(Client client){
		if(INSTANCE == null){
			String firstName = client.getFirstName();
			String lastName = client.getLastName();
			String login = client.getLogin();
			String password = client.getPassword();
			String adress = client.getAdress();
			return new User(firstName, lastName, login, password, adress);
		} else {
			return INSTANCE;
		}
	}
	
}
