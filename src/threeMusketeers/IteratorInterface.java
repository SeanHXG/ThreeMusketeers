package threeMusketeers;

public interface IteratorInterface {
	
	Move first();
	
	Move currentItem();
	
	Boolean isDone();
	
	void next();

}