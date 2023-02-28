package threeMusketeers;


public interface CollectionInterface {	
	
	void addItem(Move m);
	
	int getSize();
	
	Move getItem(int n);
	
	ReplayIterator createIterator();

}
