package clientApplication.fileSupporter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import clientApplication.auction.Auction;
import clientApplication.client.Client;
import clientApplication.item.Item;

public class FileSupporter {
	
	private final String FILE_FORMAT = ".txt";
	private final String FILE_HEADER = "UMOWA SPRZEDAZY";
	private final String PAUSE = "*************************************";
	private final String NEW_LINE = System.getProperty("line.separator");
	private final String FIRST_PARAGRAPH = "Umowa sprzedazy zawarta miedzy:";
	private final String SECOND_PARAGRAPH = "zwanym dalej Sprzedajacym, a";
	private final String THIRD_PARAGRAPH = "zwanym dalej Kupujacym,\n dotyczaca sprzedazy przedmiotu:";
	private final String FORTH_PARAGRAPH = "Sprzedajacy zapewnia zgodnosc towaru z opisem.";
	private final String FIFTH_PARAGRAPH = "W przypadku braku takowej zgodosci Kupujacy ma prawo" + NEW_LINE + "do reklamacji w terminie 7 dni od daty zakupu.";
	private String sellerSignature;
	private String sellerData;
	private String buyerSignature;
	private String buyerData;
	private String itemDetails;
	private String name;
	private File file;
	private FileWriter fileWriter;
	private Auction auction;
	
	public FileSupporter(Auction auction){
		this.auction = auction;
		setSellerSingature();
		setSellerData();
		setBuyerSignature();
		setBuyerData();
		setFileName();
		setItemDetails();
		writeAgreementToFile();
	}
	
	private void writeAgreementToFile(){
		try {
			createAgreementFile();
			fileWriter.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeFileWriter();
		}
	}
	
	private void createAgreementFile() throws IOException{
		file = new File(name);
		fileWriter = new FileWriter(file);
		fileWriter.append(FILE_HEADER);
		fileWriter.append(NEW_LINE);
		fileWriter.append(PAUSE);
		fileWriter.append(NEW_LINE);
		fileWriter.append(FIRST_PARAGRAPH);
		fileWriter.append(NEW_LINE);
		fileWriter.append(sellerData);
		fileWriter.append(NEW_LINE);
		fileWriter.append(SECOND_PARAGRAPH);
		fileWriter.append(NEW_LINE);
		fileWriter.append(buyerData);
		fileWriter.append(NEW_LINE);
		fileWriter.append(THIRD_PARAGRAPH);
		fileWriter.append(NEW_LINE);
		fileWriter.append(PAUSE);
		fileWriter.append(NEW_LINE);
		fileWriter.append(NEW_LINE);
		fileWriter.append(itemDetails);
		fileWriter.append(NEW_LINE);
		fileWriter.append(NEW_LINE);
		fileWriter.append(PAUSE);
		fileWriter.append(NEW_LINE);
		fileWriter.append(FORTH_PARAGRAPH);
		fileWriter.append(NEW_LINE);
		fileWriter.append(FIFTH_PARAGRAPH);
		fileWriter.append(NEW_LINE);
		fileWriter.append(NEW_LINE);
		fileWriter.append("Sprzedajacy:                                                       Kupujacy:");
		fileWriter.append(NEW_LINE);
		fileWriter.append(sellerSignature + "                                                              " + buyerSignature);
	}
	
	private void closeFileWriter(){
		try {
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setFileName(){
		Item item = auction.getItem();
		name = item.getName() + FILE_FORMAT;
	}
	
	private void setSellerData(){
		Client client = auction.getOwner();
		String adress = client.getAdress();
		sellerData = sellerSignature + ", zamieszkalym przy " + adress;
	}
	
	private void setBuyerData(){
		Client client = auction.getWinner();
		String adress = client.getAdress();
		buyerData = buyerSignature + ", zamieszkalym przy " + adress; 
	}
	
	private void setBuyerSignature(){
		Client client = auction.getWinner();
		String firstName = client.getFirstName();
		String lastName = client.getLastName();
		buyerSignature = firstName + " " + lastName;
	}
	
	private void setSellerSingature(){
		Client client = auction.getOwner();
		String firstName = client.getFirstName();
		String lastName = client.getLastName();
		sellerSignature = firstName + " " + lastName;
	}
	
	private void setItemDetails(){
		Item item = auction.getItem();
		itemDetails = item.toString() + " " + auction.getPrize();
	}
}
