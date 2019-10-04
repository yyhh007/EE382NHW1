package Question3;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;



public class CSMessageObserver {

	public List<Observer> observerz = new ArrayList<Observer>();
	public int state;
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state=state;
		notifyAllObservers();
		
	}
	
	public void attach(Observer observer) {
		observerz.add(observer);
	}
	
	public void notifyAllObservers() {
		
		for (Observer observers: observerz) {
			
			observers.update(null, observers);
		}
	}
}
