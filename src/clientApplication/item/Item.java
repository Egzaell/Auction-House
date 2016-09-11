package clientApplication.item;

import java.io.Serializable;

public class Item implements Serializable {

	private String name;
	private String description;
	
	public Item(String name, String description){
		this.name = name;
		this.description = description;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String toString(){
		return name + " : " + description;
	}
	
	public boolean equals(Object anItem){
		Item testItem = (Item) anItem;
		return name.equals(testItem.getName()) && description.equals(testItem.getDescription());
	}
	
}
