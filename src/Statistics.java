/**
 * This class holds all the statistics methods necessary for calculating and printing out
 * the statistics for the dump truck simulation.
 */
public class Statistics
{
	private String name;
	
	private int totalEvents = 0;
	private int loadEvents = 0;
	private int weighEvents = 0;
	private int travelEvents = 0;
	private double totalTime = 0;
	private double totalLoadTime = 0;
	private double totalWeighTime = 0;
	private double totalTravelTime = 0;

	// Extra Variables
	private double totalTimeInLoadQueue = 0;
	private double maxTimeInLoadQueue = 0;
	private int totalEventsInLoadQueue = 0;
	private double totalTimeInWeighQueue = 0;
	private double maxTimeInWeighQueue = 0;
	private int totalEventsInWeighQueue = 0;

	private int totalEventsInQueue = 0;
	private double totalTimeInQueue = 0;

	public Statistics(String name)
	{
		this.name = name;
	}

	/**
	 * Increases total load time and increments number of load events
	 * 
	 * @param time
	 */
	public void incrementLoadEvents(double time)
	{
		this.loadEvents ++;
		this.totalLoadTime += time;
	}

	/**
	 * Increases total weighing time and increments number of weighing events
	 * 
	 * @param time
	 */
	public void incrementWeighEvents(double time)
	{
		this.weighEvents ++;
		this.totalWeighTime += time;
	}

	/**
	 * Increases total travel time and increments number of travel events
	 * 
	 * @param time
	 */
	public void incrementTravelEvents(double time)
	{
		this.travelEvents ++;
		this.totalTravelTime += time;
	}

	/**
	 * Sets the total time the simulation lasted
	 * 
	 * @param time
	 */
	public void setTotalTime(double time)
	{
		this.totalTime = time;
	}

	/**
	 * Sets the total number of events that happened in the simulation
	 * 
	 * @param Events
	 */
	public void setTotalEvents(int Events)
	{
		this.totalEvents = Events;
	}
	
	/**
	 * Sets the maximum time in all queues and load queue as well as adds to total time in queue and increments number of events
	 * 
	 * @param time
	 */
	public void incrementLoadEventsInQueue(double time){
		this.totalEventsInLoadQueue ++;
		this.totalEventsInQueue ++;
		
		if (time > this.maxTimeInLoadQueue)
			this.maxTimeInLoadQueue = time;
		
		this.totalTimeInLoadQueue += time;
		this.totalTimeInQueue += time;
	}

	/**
	 * Sets the maximum time in all queues and weighing queue as well as adds to total time in queue and increments number of events
	 * 
	 * @param time
	 */
	public void incrementWeighEventsInQueue(double time){
		this.totalEventsInWeighQueue ++;
		this.totalEventsInQueue ++;
		
		if (time > this.maxTimeInWeighQueue)
			this.maxTimeInWeighQueue = time;
		
		this.totalTimeInWeighQueue += time;
		this.totalTimeInQueue += time;
	}
	
	/**
	 * Returns the maximum time spent in any queue
	 * 
	 * @param time
	 */
	public double maxTimeInQueue() {
		if(this.maxTimeInLoadQueue > this.maxTimeInWeighQueue)
			return this.maxTimeInLoadQueue;
		else
			return this.maxTimeInWeighQueue;
	}

	/**
	 * Prints out statistics in a readable format
	 */
	public void reportGeneration()
	{
		double totalCallTime = this.totalLoadTime + this.totalWeighTime + this.totalTravelTime;
		
		System.out.println(this.name);
		System.out.print(String.format("There were %d loads, for a total of %.2f minutes, ",
				this.loadEvents, this.totalLoadTime));
		System.out.println(String.format("and average of %.2f minutes per load call.",
				this.totalLoadTime / this.loadEvents));
		System.out.println(String.format("The proportion of total time spent loading is %.2f\n",
				this.totalLoadTime / totalCallTime));
		
		System.out.println(String.format("The average time spent in queue for loading was: %.2f minutes",
				this.totalTimeInLoadQueue / this.totalEventsInLoadQueue));
		System.out.println(String.format("The maximum time spent in queue for loading was: %.2f minutes\n",
				this.maxTimeInLoadQueue));

		System.out.print(String.format("There were %d weighing events, for a total of %.2f minutes, ",
				this.weighEvents, this.totalWeighTime));
		System.out.println(String.format("and average of %.2f minutes per weighing event.",
				this.totalWeighTime / this.weighEvents));
		System.out.println(String.format("The proportion of total time spent on weighing is %.2f\n",
				this.totalWeighTime / totalCallTime));
		
		System.out.println(String.format("The average time spent in queue for weighing was: %.2f minutes",
				this.totalTimeInWeighQueue / this.totalEventsInWeighQueue));
		System.out.println(String.format("The maximum time spent in queue for weighing was: %.2f minutes\n",
				this.maxTimeInWeighQueue));

		System.out.print(String.format("There were %d travel events, for a total of %.2f minutes, ",
				this.travelEvents, this.totalTravelTime));
		System.out.println(String.format("and average of %.2f minutes per travel event.",
				this.totalTravelTime / this.travelEvents));
		System.out.println(String.format("The proportion of total time spent traveling is %.2f\n",
				this.totalTravelTime / totalCallTime));

		System.out.println(String.format("There was a total of %d trips in %.2f minutes.", this.totalEvents, this.totalTime));
		
		System.out.println(String.format("The average time spent is queue for all events was: %.2f minutes",
				this.totalTimeInQueue / this.totalEventsInQueue));
		System.out.println(String.format("The maximum time spent is queue for all events was: %.2f minutes\n",
				this.maxTimeInQueue()));
		System.out.println(String.format("The total number of events in both queues: " + 
				this.totalEventsInQueue));
	}
}
