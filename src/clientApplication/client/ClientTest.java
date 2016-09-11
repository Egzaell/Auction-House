package clientApplication.client;

import static org.junit.Assert.*;

import org.junit.Test;

public class ClientTest {

	@Test
	public void testClientNotEquals() {
		Client clientOne = new Client("John", "Doe", "john", "doe", "Flower st. 16");
		Client clientTwo = new Client("Angela", "Coroner", "angela", "coroner", "Carcass st. 12");
		assertNotEquals(clientOne, clientTwo);
	}
	
	@Test
	public void testClientEquals(){
		Client clientOne = new Client("John", "Doe", "john", "doe", "Flower st. 16");
		Client clientTwo = new Client("Angela", "Coroner", "john", "coroner", "Carcass st. 12");
		assertEquals(clientOne, clientTwo);
	}

}
