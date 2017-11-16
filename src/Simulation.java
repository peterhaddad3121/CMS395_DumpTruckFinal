import java.util.PriorityQueue;
import java.util.Queue;

import generalSimulation.Distribution;
import generalSimulation.DistributionFactory;

/**
 * 
 * @author peter, lenuel, brandt, rod
 *
 */

public class Simulation {
	
	private Distribution travelTimes;
	private Distribution weighingTime;
	private Distribution loadingTime;
	
	private Queue<Event> weighingQueue;
	private Queue<Event> loadingQueue;
	
	private double clock = 0;
	private int numberOfCalls = 1000;
	
	//Probabilities and values.
	private final double LOADING_PROBABILITY[] = {0.3, 0.5, 0.2};
	private final int LOADING_TIME[] = {5, 10, 15};
	
	private final double WEIGHING_PROBABILITY[] = {0.7, 0.3};
	private final int WEIGHING_TIME[] = {12, 16};
	

	//Simulate for 1,000
	
	//For distribution travel time:
		//Standard Deviation is 10
		//Mean is 58
	
	/**
	 * Constructor
	 */
	public Simulation() {
		DistributionFactory distributionFactory = new DistributionFactory(12345);
		
		this.travelTimes = distributionFactory.createNormal(58, 10);
		this.weighingTime = (Distribution) distributionFactory.createDiscreteEmpiricalDistribution(WEIGHING_TIME, WEIGHING_PROBABILITY);
		this.loadingTime = (Distribution) distributionFactory.createDiscreteEmpiricalDistribution(LOADING_TIME, LOADING_PROBABILITY);
		
		this.loadingQueue = new PriorityQueue<Event>();
		this.weighingQueue = new PriorityQueue<Event>();
		
		Event truckOne = new Event(Event.LOADING, this.loadingTime.getNext());
		Event truckTwo = new Event(Event.LOADING, this.loadingTime.getNext());
		
		this.loadingQueue.add(truckOne);
		this.loadingQueue.add(truckTwo);
		
		
	}
	
	/**
	 * Processes the event.
	 * @param event
	 */
	public void processEvent(Event event) {
		//Arrival Loading
		//Arrival Waiting
	}
	
	/**
	 * Schedule the next event.
	 * @param event
	 */
	public void scheduleEvent(Event event) {
		
	}
	
	
	/**
	 * Start of the simulation.
	 */
	public void startSimulation() {
		
		//While loop
	}
	
	/**
	 * Main Method
	 */
	public static void main(String args[]) {
		
		Simulation simulation = new Simulation();
		simulation.startSimulation();
	}
}
