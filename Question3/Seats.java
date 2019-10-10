package Question3;

import java.util.HashMap;
import java.util.Map;

public class Seats {
	//current code
	Map<Integer, String> seats = new HashMap<Integer, String>();
	String changedValueHolder = null;
	public Seats(int seatNumbers) {
		for (int i = 1; i<=seatNumbers; i++) this.seats.put(i, "");
	}
	
	public String seatValueToString() {
		String returnString = "";
		for (int i = 1; i<=seats.size(); i++) {
			returnString=returnString+seats.get(i)+"-";
		}
		return returnString.substring(0, returnString.length());
	}
	
	
	//changed value will be used to sync between servers
	public String getChangedValue() {
		return changedValueHolder;
	}
	
	
	public synchronized void loadCurrentSeatStatus(Map currentSeats) {
		this.seats=currentSeats;
	}
	
	
	public synchronized Seats getCurrentSeatAssignment() {
		return this;
	}
	
	
	public synchronized void syncSeats(int key, String value) {
		seats.put(key, value);
	}
	
	
	//reserve a seat of there is any, assigned by first available
	public synchronized String reserveSeat(String name) {
		changedValueHolder = "";
		String [] returnString = {"Sold out - No seat available", "Seat already booked against the name provided", "Seat assigned to you is "};
		
		if(seats.containsValue(name)) return returnString[1];
		
		for (Integer key : seats.keySet()) {
		    if(seats.get(key).equals("")) {
		    	changedValueHolder = name+" "+Integer.toString(key);
		    	seats.put(key, name);
		    	return returnString[2]+Integer.toString(key);
		    }
		}
		return returnString[0];
	}
	
	
	//reserve a specific seat number, if available
	public synchronized String bookSeat(String [] seatNum) {
		changedValueHolder = "";
		String returnString = seatNum[2]+" is not available";
		int seatNumber = Integer.parseInt(seatNum[2]);
		if(seats.containsValue(seatNum[1])|| (!seats.get(seatNumber).equals(""))) return returnString;
		else {
			changedValueHolder = seatNum[1]+" "+seatNum[2];
			seats.put(seatNumber, seatNum[1]);
			return "Reserved";
		}
	}
	
	
	//find if a person had reserved any seats previously
	public synchronized String searchName(String name) {
		changedValueHolder = "";
		String returnString = "No reservation found for "+name;
		if(seats.containsValue(name)) {
			for (Integer key : seats.keySet()) {
			    if(seats.get(key).equals(name)) {
			    	changedValueHolder = name+" "+Integer.toString(key);
			    	return name+" reserved seat "+Integer.toString(key);
			    }
			}
		}
		return returnString;
	}
	
	
	//delete seat a person has reserved
		public synchronized String delete(String name) {
			String returnString  = "No reservation found for "+name;
			changedValueHolder = "";
			if(seats.containsValue(name)) {
				for (Integer key : seats.keySet()) {
				    if(seats.get(key).equals(name)) {
				    	seats.put(key, "");
				    	changedValueHolder = "delete "+Integer.toString(key);
				    	return name+" released seat "+Integer.toString(key);
				    }
				}
			}
			return returnString;		
		}
}