package threeMusketeers;

import java.util.List;

// COMMAND PATTERN: OBSERVER
public abstract class Observer {
	
	public abstract void update(List<Player> ranks);
}