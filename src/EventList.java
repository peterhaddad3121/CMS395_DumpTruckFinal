import java.util.PriorityQueue;

/**
 * Event List objects are priority queues that hold the events in order based on their event time
 */
public class EventList {
	private PriorityQueue<Event> events;

	public EventList()
	{
		this.events = new PriorityQueue<Event>();
	}

	public void addEvent(Event event)
	{
		this.events.add(event);
	}

	public void removeEvent()
	{
		this.events.poll();
	}

	public Event getImminentEvent()
	{
		Event event = this.events.peek();
		this.removeEvent();
		return event;
	}
	
	public boolean isEmpty() {
		return this.events.isEmpty();
	}

	@Override
	public String toString()
	{
		String s = "Event list: ";
		for (Event event : this.events)
			s += event.toString() + " ";
		return s;
	}
}
