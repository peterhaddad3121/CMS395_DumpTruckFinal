public class Event implements Comparable
{
	// private static customerNumber = 1;
	public final static int CALL = 1;
	public final static int COMPLETE = 2;
	public final static int END = 3;

	private int eventType;
	private int callType;
	private double eventTime;

	public Event(int eventType, int callType, double eventTime)
	{
		this.eventType = eventType;
		this.callType = callType;
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

	public int getCallType()
	{
		return this.callType;
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
		String s = "(CALL, ";
		if (this.eventType == COMPLETE)
			s = "(DONE, ";
		s = String.format(s + "%d, %.3f)", this.getCallType(), this.getEventTime());
		return s;
	}
}