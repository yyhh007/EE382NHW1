package Question3;

import java.util.HashMap;
import java.util.Map;

public class Seats {
	
	//current code
	Map<Integer, String> seats = new HashMap<Integer, String>();
	
	public void initSeats(int seatNumbers) {
		for (int i = 1; i<=seatNumbers; i++) this.seats.put(i, "");
	}
	
	public synchronized void loadCurrentSeatStatus(Map currentSeats) {
		this.seats=currentSeats;
	}
	
	public synchronized Map<Integer, String> getCurrentSeatAssignment() {
		return this.seats;
	}
	
	//reserve a seat of there is any, assigned by first available
	public synchronized String reserveSeat(String name) {
		String [] returnString = {"Sold out - No seat available", "Seat already booked against the name provided", "Seat assigned to you is "};
		
		if(seats.containsValue(name)) return returnString[1];
		
		for (Integer key : seats.keySet()) {
		    if(seats.get(key)=="") {
		    	seats.put(key, name);
		    	return returnString[2]+Integer.toString(key);
		    }
		}
		return returnString[0];
	}
	
	//reserve a specific seat number, if available
	public synchronized String bookSeat(String [] seatNum) {
		String returnString = seatNum[1]+" is not available";
		int seatNumber = Integer.parseInt(seatNum[1]);
		if(seats.containsValue(seatNum[0])|| seats.get(seatNumber)!="") return returnString;
		else {
			seats.put(seatNumber, seatNum[0]);
			return "Reserved";
		}
	}
	
	
	//find if a person had reserved any seats previously
	public synchronized String searchName(String name) {
		String returnString = "No reservation found for "+name;
		if(seats.containsValue(name)) {
			for (Integer key : seats.keySet()) {
			    if(seats.get(key)==name) {
			    	return name+" reserved seat "+Integer.toString(key);
			    }
			}
		}
		return returnString;
	}
	
	//delete seat a person has reserved
	public synchronized String delete(String name) {
		String returnString  = "No reservation found for "+name;
		
		if(seats.containsValue(name)) {
			for (Integer key : seats.keySet()) {
			    if(seats.get(key)==name) {
			    	seats.put(key, "");
			    	return name+" released seat "+Integer.toString(key);
			    }
			}
		}
		return returnString;		
	}
}