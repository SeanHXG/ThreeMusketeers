package threeMusketeers;

// COMMAND PATTERN: OBSERVER
public interface Observable {
	
	public void addObserver(Observer observer);
	
	public void removeObserver(Observer observer);
	
	public void updateObservers();
}
