package threeMusketeers;

public class ReplayIterator implements IteratorInterface {
	
	private ReplayCollection collection;
	private int count;
	
	public ReplayIterator(ReplayCollection c) {
		this.collection = c;
		this.count = 0;
	}

	@Override
	public Move first() {
		return collection.getItem(0);
	}

	@Override
	public void next() {
		count++;
	}

	@Override
	public Move currentItem() {
		return collection.getItem(count);
	}

	@Override
	public Boolean isDone() {
		return count >= collection.getSize();
	}

}
