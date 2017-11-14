public class Event implements Comparable
{
	

	private int eventType;
	private double eventTime;

	public Event(int eventType, int callType, double eventTime)
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
		return null;
		
	}
}