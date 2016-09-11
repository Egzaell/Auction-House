package clientApplication.client;

import java.io.Serializable;

public class Client implements Serializable {
	
	private String firstName;
	private String lastName;
	private String login;
	private String password;
	private String adress;
	
	public Client(String firstName, String lastName, String login, String password, String adress){
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
		this.adress = adress;
	}
	
	public boolean equals(Object anClient){
		Client client = (Client) anClient;
		return login.equals(client.getLogin());
	}
	
	public String toString(){
		return login;
	}
	
	public String getFirstName(){
		return firstName;
	}
	
	public String getLastName(){
		return lastName;
	}
	
	public String getLogin(){
		return login;
	}
	
	public String getPassword(){
		return password;
	}
	
	public String getAdress(){
		return adress;
	}
}
