package clientApplication.item;

import static org.junit.Assert.*;

import org.junit.Test;

public class ItemTest {

	@Test
	public void test() {
		Item item = new Item("thing", "description");
		String itemToString = item.toString();
		assertEquals("thing : description", itemToString);
	}

}
