package threeMusketeers;

import java.util.ArrayList;

public class ReplayCollection implements CollectionInterface {
	
	private int numItems;
	private ArrayList<Move> moveList;
	
	/**
	 * Constructor for a new ReplayCollection.
	 */
	public ReplayCollection() {
		numItems = 0;
		moveList = new ArrayList<Move>();
	}

	@Override
	public void addItem(Move m) {
		moveList.add(m);
		this.numItems++;
	}

	@Override
	public int getSize() {
		return numItems;
	}
	
	@Override
	public Move getItem(int n) {
		return moveList.get(n);
	}

	@Override
	public ReplayIterator createIterator() {
		return new ReplayIterator(this);
	}

	
}
