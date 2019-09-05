package Question4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Inventory {

	//syncronized values
	ArrayList<String> inventory = new ArrayList<String>();
	ArrayList<String> purchaseOrder = new ArrayList<String>();
	int orderID = 1;
	//first time loading file
	public void loadInventory() throws IOException{
		File file = new File(System.getProperty("user.dir")+"\\src\\input\\inventory.txt"); 
			    
			    
			    BufferedReader br = new BufferedReader(new FileReader(file)); 
			    
			    String st; 
			    while ((st = br.readLine()) != null) 
			    	inventory.add(st);
	}
	
	public synchronized ArrayList<String> getInventory() {  
	    return inventory;
	}
	
	public synchronized int getOrderID() {
		return orderID;
	}
	
	public synchronized void incrementOrderID() {
		orderID+=1;
	}
	
	public synchronized byte [] List(ArrayList<String> inventory) {
		// TODO Auto-generated method stub
		String inventoryString = "";
		
		for(int x = 0; x<inventory.size();x++) {
			 inventoryString+=inventory.get(x);
			 inventoryString+="\n";
		}
		return inventoryString.getBytes();
	}

	public synchronized byte [] Search(String [] commandList) {
		// TODO Auto-generated method stub
		String [] each_purchase_record = {};
		String clientName = commandList[1];
		String searchResults = "";
		for (int x=0; x<purchaseOrder.size(); x++) {
			each_purchase_record = purchaseOrder.get(x).split(" ");
			if(clientName.contains(each_purchase_record[1])) {
				searchResults = searchResults +"Order ID: "+ each_purchase_record[0]+"\nProduct name: " +each_purchase_record[2]+"\nQuantity: "+each_purchase_record[3]+".\n" ;
			}
		}
		
		if(searchResults == ""){
			
			searchResults = "No order found for "+ clientName;
		}
		return searchResults.getBytes();
	}

	public synchronized byte [] Cancel(int orderID) {
		// TODO Auto-generated method stub
		String id = String.valueOf(orderID);
		String [] returnResponses = {"Order-id"+id+" not found, no such order", "OrderID"+id+" is canceled"};
		for (int x=0;x<purchaseOrder.size();x++) {
			
			if(id.equals(purchaseOrder.get(x).substring(0, purchaseOrder.get(x).indexOf(" ")).replaceAll("\\D+", ""))) {
				purchaseOrder.remove(x);
				//System.out.println("found order, order canceled\n");
				//System.out.println(returnResponses[1]);
				return returnResponses[1].getBytes();
			}
		}
		//System.out.println("order not found");
		return returnResponses[0].getBytes();
	}

	public synchronized byte [] Purchase(String [] commandList, ArrayList<String> inventory) {
		// TODO Auto-generated method stub
		
		//return responses
		String returnedStringPurchaseComplete = "Your order has been placed, OrderID"+String.valueOf(orderID)+" "+commandList[1]+" "+commandList[2]+" "+commandList[3];
		String [] returnResponses = {"Not Available - We do not sell this product","Not Available - Not enough items",returnedStringPurchaseComplete};
		
		
		//String [] commandList = data.split(" ");
		//System.out.println(commandList);
		//check if item is in inventory, not enough item, or enough item
		for (int x=0; x<inventory.size() ;x++) {
			if (commandList[2].contentEquals(inventory.get(x).substring(0, inventory.get(x).indexOf(" ")))) {
				//(Integer.valueOf(inventory.get(x).substring(inventory.get(x).indexOf(" ")))
				int purchaseNumber = Integer.valueOf(commandList[3].replaceAll("\\D+", ""));
				int inventoryNumber = Integer.valueOf(inventory.get(x).substring(inventory.get(x).indexOf(" ")+1));
				
				if(purchaseNumber > inventoryNumber) {
					return returnResponses[1].getBytes();
				}
				else {
					//change inventory, add purchase order to history, increase orderID, return string
					purchaseOrder.add(String.valueOf(orderID)+" "+commandList[1]+" "+commandList[2]+" "+commandList[3]);
					inventory.set(x, commandList[2]+" "+String.valueOf((inventoryNumber-purchaseNumber)));
					incrementOrderID();
					System.out.println(purchaseOrder);
					System.out.println(inventory);
					return returnResponses[2].getBytes();
				}
			}
		}
		
		return returnResponses[0].getBytes();
	} 
	
}