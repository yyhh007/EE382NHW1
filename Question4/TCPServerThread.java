package Question4;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class TCPServerThread extends Thread {

	Inventory inven;
	Socket theClient;
	
	public TCPServerThread(Inventory i, Socket s) {
		this.inven=i;
		theClient = s;
	}
	
	public void run() {
		try {
			Scanner s = new Scanner(theClient.getInputStream());
			PrintWriter pout = new PrintWriter(theClient.getOutputStream());
			
			String [] commandList = s.nextLine().split(" ");
			ArrayList<String> inventory = inven.getInventory();
			
			switch(commandList[0]) {
			case "exit":
				theClient.close();
				System.exit(-1);
				break;
			
			}
			byte [] returnByte = null;
			switch (commandList[0]) {
			case "purchase":
				returnByte = inven.Purchase(commandList, inventory);
				break;
			case "search":
				returnByte = inven.Search(commandList);
				break;
			case "cancel":
				returnByte = inven.Cancel(Integer.valueOf(commandList[1]));
				break;
			case "list":
				returnByte = inven.List(inventory);
				break;
			
			}
			//byte [] returnByte = inven.Purchase(commandList, inventory);
			
			//System.out.println("Recived command: "+s.nextLine());
			pout.println(new String(returnByte));
			pout.flush();
			theClient.close();
			//
		}catch(IOException e){
			System.out.println(e);
		}
		
	}
	
}