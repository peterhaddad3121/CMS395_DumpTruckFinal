/**
 * Event objects are put into the queues to mark the start/completion of the next event.
 */
public class Event implements Comparable{
	
	public static final int LOAD = 0;
	public static final int WEIGH = 1;
	public static final int TRAVEL = 2;
	
	public static final int COMPLETE_LOAD = 3;
	public static final int COMPLETE_WEIGH = 4;
	public static final int COMPLETE_TRAVEL = 5;

	private int eventType;
	private double eventTime;

	public Event(int eventType, double eventTime)
	{
		this.eventType = eventType;
		this.eventTime = eventTime;
	}

	public int getEventType()
	{
		return this.eventType;
	}

	public double getEventTime()
	{
		return this.eventTime;
	}

	@Override
	public int compareTo(Object otherEvent)
	{
		double otherTime = ((Event) otherEvent).getEventTime();
		if (this.getEventTime() < otherTime)
			return -1;
		if (this.getEventTime() == otherTime)
			return 0;
		return 1;
	}

	@Override
	public String toString()
	{
		return "(" + eventType + " " + eventTime + ")";
	}
}