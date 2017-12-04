import java.util.PriorityQueue;
import java.util.Queue;

import generalSimulation.DiscreteDistribution;
import generalSimulation.Distribution;
import generalSimulation.DistributionFactory;

/**
 * 
 * @author peter, lenuel, brandt, rod
 *
 */

public class Simulation {
	
	private Distribution travelTime;
	private DiscreteDistribution weighingTime;
	private DiscreteDistribution loadingTime;
	
	private Queue<Event> weighingQueue;
	private Queue<Event> loadingQueue;
	private EventList eventList;
	
	private double clock = 0;
	private final int TOTAL_CALLS = 1000;
	private int numberOfCalls = 0;
	
	private final int NUM_OF_LOADERS = 2;
	private final int NUM_OF_WEIGHERS = 1;
	private int numberOfLoadersInUse = 0;
	private int numberOfWeighersInUse = 0;
	
	
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
		
		this.travelTime = distributionFactory.createNormal(58, 10);
		this.weighingTime = distributionFactory.createDiscreteEmpiricalDistribution(WEIGHING_TIME, WEIGHING_PROBABILITY);
		this.loadingTime = distributionFactory.createDiscreteEmpiricalDistribution(LOADING_TIME, LOADING_PROBABILITY);
		
		this.loadingQueue = new PriorityQueue<Event>();
		this.weighingQueue = new PriorityQueue<Event>();
		this.eventList = new EventList();
		
		Event truckOne = new Event(Event.LOADING, this.loadingTime.getNext());
		Event truckTwo = new Event(Event.LOADING, this.loadingTime.getNext());
		
		processLoadingEvent(truckOne);
		processLoadingEvent(truckTwo);
		
		for (int i = 0; i < 4; i++)
			this.loadingQueue.add(new Event(Event.TRAVEL, 0));
		
		
	}
	
	public void processWeighingEvent(Event event) {
		this.numberOfWeighersInUse++;
		
		this.scheduleCompletion(event.getEventType());
	}
	
	/**
	 * Processes the event.
	 * @param event
	 */
	public void processLoadingEvent(Event event) {
		this.numberOfLoadersInUse++;
		
		this.scheduleCompletion(event.getEventType());
		
	}
	
	public void processTravelEvent(Event event) {
		this.scheduleCompletion(event.getEventType());
	}
	
	/**
	 * Schedule the next completion event.
	 * @param event
	 */
	public void scheduleCompletion(int eventType) {
		if (eventType == Event.LOADING) {
			this.eventList.addEvent(new Event(Event.LOADING, this.clock + this.loadingTime.getNext()));
		}else if (eventType == Event.WEIGHING) {
			this.eventList.addEvent(new Event(Event.WEIGHING, this.clock + this.weighingTime.getNext()));
		}else{
			this.eventList.addEvent(new Event(Event.TRAVEL, this.clock + this.travelTime.getNext()));
		}
	}
	
	public void processCompletion(Event event) {
		
		if (event.getEventType() == Event.WEIGHING) {
			this.numberOfWeighersInUse--;
		}else if (event.getEventType() == Event.LOADING) {
			this.numberOfLoadersInUse--;
		}
		
		if (!this.loadingQueue.isEmpty()) {
			Event nextLoading = this.loadingQueue.poll();
			processLoadingEvent(nextLoading);
		}else if (!this.weighingQueue.isEmpty()) {
			Event nextWeighing = this.weighingQueue.poll();
			processWeighingEvent(nextWeighing);
		}
		
		//Update other stats.
	}
	
	
	/**
	 * Start of the simulation.
	 */
	public void startSimulation() {
		
		while (this.TOTAL_CALLS > this.numberOfCalls) {
			Event event = this.eventList.getImminentEvent();
			
			System.out.println(event.getEventType());
			
			if (event.getEventType() == Event.LOADING && this.numberOfLoadersInUse < this.NUM_OF_LOADERS) {
				processLoadingEvent(event);
			}else if (event.getEventType() == Event.WEIGHING && this.numberOfWeighersInUse < this.NUM_OF_WEIGHERS) {
				processWeighingEvent(event);
			}else if (event.getEventType() == Event.TRAVEL) {
				processCompletion(event);
			}else{
				if (event.getEventTime() == Event.LOADING) {
					this.loadingQueue.add(event);
				}else{
					this.weighingQueue.add(event);
				}
			}
		}
	}
	
	/**
	 * Main Method
	 */
	public static void main(String args[]) {
		
		Simulation simulation = new Simulation();
		simulation.startSimulation();
	}
}
