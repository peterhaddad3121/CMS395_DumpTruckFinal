public class Statistics
{
	private String name;
	
	private int totalCalls = 0;
	private int loadCalls = 0;
	private int weighCalls = 0;
	private int travelCalls = 0;
	private double totalTime = 0;
	private double totalLoadTime = 0;
	private double totalWeighTime = 0;
	private double totalTravelTime = 0;

	// Extra Variables
	private double totalTimeInLoadQueue = 0;
	private double maxTimeInLoadQueue = 0;
	private int totalCallsInLoadQueue = 0;
	private double totalTimeInWeighQueue = 0;
	private double maxTimeInWeighQueue = 0;
	private int totalCallsInWeighQueue = 0;

	private int totalCallsInQueue = 0;
	private double totalTimeInQueue = 0;
	private double maxTimeInQueue = 0;

	public Statistics(String name)
	{
		this.name = name;
	}

	public void incrementLoadCalls(double time)
	{
		this.loadCalls ++;
		this.totalLoadTime += time;
	}

	public void incrementWeighCalls(double time)
	{
		this.weighCalls ++;
		this.totalWeighTime += time;
	}

	public void incrementTravelCalls(double time)
	{
		this.travelCalls ++;
		this.totalTravelTime += time;
	}

	public void setTotalTime(double time)
	{
		this.totalTime = time;
	}

	public void setTotalCalls(int calls)
	{
		this.totalCalls = calls;
	}
	
	/**
	 * Sets the maximum time in false alarms queue as well as adds to total time in queue
	 * @param time
	 */
	public void incrementLoadCallsInQueue(double time){
		this.totalCallsInLoadQueue ++;
		this.totalCallsInQueue ++;
		
		if (time > this.maxTimeInLoadQueue)
			this.maxTimeInLoadQueue = time;
		
		this.totalTimeInLoadQueue += time;
		this.totalTimeInQueue += time;
	}


	public void incrementWeighCallsInQueue(double time){
		this.totalCallsInWeighQueue ++;
		this.totalCallsInQueue ++;
		
		if (time > this.maxTimeInWeighQueue)
			this.maxTimeInWeighQueue = time;
		
		this.totalTimeInWeighQueue += time;
		this.totalTimeInQueue += time;
	}

	public void reportGeneration()
	{
		double totalCallTime = this.totalLoadTime + this.totalWeighTime + this.totalTravelTime;
		
		System.out.println(this.name);
		System.out.print(String.format("There were %d loads, for a total of %.2f minutes, ",
				this.loadCalls, this.totalLoadTime));
		System.out.println(String.format("and average of %.2f minutes per load call.",
				this.totalLoadTime / this.loadCalls));
		System.out.println(String.format("The proportion of total time spent loading is %.2f\n",
				this.totalLoadTime / totalCallTime));
		
		System.out.println(String.format("The average time spent in queue for loading was: %.2f minutes",
				this.totalTimeInLoadQueue / this.totalCallsInLoadQueue));
		System.out.println(String.format("The maximum time spent in queue for loading was: %.2f minutes\n",
				this.maxTimeInLoadQueue));

		System.out.print(String.format("There were %d weighing events, for a total of %.2f minutes, ",
				this.weighCalls, this.totalWeighTime));
		System.out.println(String.format("and average of %.2f minutes per weighing event.",
				this.totalWeighTime / this.weighCalls));
		System.out.println(String.format("The proportion of total time spent on weighing is %.2f\n",
				this.totalWeighTime / totalCallTime));
		
		System.out.println(String.format("The average time spent in queue for weighing was: %.2f minutes",
				this.totalTimeInWeighQueue / this.totalCallsInWeighQueue));
		System.out.println(String.format("The maximum time spent in queue for weighing was: %.2f minutes\n",
				this.maxTimeInWeighQueue));

		System.out.print(String.format("There were %d travel events, for a total of %.2f minutes, ",
				this.travelCalls, this.totalTravelTime));
		System.out.println(String.format("and average of %.2f minutes per travel event.",
				this.totalTravelTime / this.travelCalls));
		System.out.println(String.format("The proportion of total time spent traveling is %.2f\n",
				this.totalTravelTime / totalCallTime));

		System.out.println(String.format("There was a total of %d events in %.2f minutes.", this.totalCalls, this.totalTime));
		
		System.out.println(String.format("The average time spent is queue for all events was: %.2f minutes",
				this.totalTimeInQueue / this.totalCallsInQueue));
		System.out.println(String.format("The maximum time spent is queue for all events was: %.2f minutes\n",
				this.maxTimeInQueue));
		System.out.println(String.format("The total number of events in queue: " + 
				this.totalCallsInQueue));
	}
}
