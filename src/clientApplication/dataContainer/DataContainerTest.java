package clientApplication.dataContainer;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import clientApplication.client.Client;

public class DataContainerTest {

	private DataContainer dataContainer;
	private Method method;
	private static String METHOD_NAME = "addClientToList";
	private Class[] parameterTypes;
	private Object[] parameters;
	
	@Before
	public void setup() throws Exception {
		dataContainer = new DataContainer();
		parameterTypes = new Class[1];
		parameterTypes[0] = clientApplication.client.Client.class;
		method = dataContainer.getClass().getDeclaredMethod("addClientToList", parameterTypes);
		method.setAccessible(true);
		parameters = new Object[1];
	}
	
	@Test
	public void testAddClientToList() throws Exception {
		Client client = new Client("John", "Doe", "john", "doe", "Florena st. 19");
		parameters[0] = client;
		method.invoke(dataContainer, parameters[0]);
		ArrayList<Client> arrayListTest = dataContainer.getClients();
		String test = arrayListTest.toString();
		assertEquals("[john]", test);
	}
	
	@Test
	public void testIsRegisterTrue() {
		DataContainer dataContainer = new DataContainer();
		Client client = new Client("John", "Doe", "john", "doe", "Florena st. 19");
		boolean testBoolean = dataContainer.registerClient(client);
		assertTrue(testBoolean);
	}
	
	@Test
	public void testIsRegisterFalse(){
		DataContainer dataContainer = new DataContainer();
		Client clientOne = new Client("John", "Doe", "john", "doe", "Florena st. 19");
		Client clientTwo = new Client("John", "Doe", "john", "doe", "Florena st. 19");
		dataContainer.registerClient(clientOne);
		boolean testBoolean = dataContainer.registerClient(clientTwo);
		assertFalse(testBoolean);
	}
	
	@Test
	public void testLoginClient(){
		DataContainer dataContainer = new DataContainer();
		Client client = new Client("John", "Doe", "john", "doe", "Florena st. 19");
		dataContainer.registerClient(client);
		boolean testBoolean = dataContainer.loginClient("john", "doe");
		assertTrue(testBoolean);
	}
}
