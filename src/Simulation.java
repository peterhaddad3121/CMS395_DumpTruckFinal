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
	
	private Statistics stats;
	
	private Distribution travelTime;
	private DiscreteDistribution weighingTime;
	private DiscreteDistribution loadingTime;
	
	private Queue<Event> weighingQueue;
	private Queue<Event> loadingQueue;
	private EventList eventList;
	
	private double clock = 0;
	private int numberOfTrips = 0;
	
	private final int NUM_OF_LOADERS = 2;
	private final int NUM_OF_WEIGHERS = 1;
	private int numberOfLoadersInUse = 0;
	private int numberOfWeighersInUse = 0;
	
	//Probabilities and values.
	private final double LOADING_PROBABILITY[] = {0.3, 0.5, 0.2};
	private final int LOADING_TIME[] = {5, 10, 15};
	
	private final double WEIGHING_PROBABILITY[] = {0.7, 0.3};
	private final int WEIGHING_TIME[] = {12, 16};
	
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
		
		this.stats = new Statistics("Dump Truck Simulation with Six Dump Trucks!");
		
		// Start simulation off with 2 trucks loading and rest in loading queue.
		double loadTime = this.loadingTime.getNext();
		this.stats.incrementLoadCalls(loadTime);
		Event truckOne = new Event(Event.LOAD, loadTime);
		
		loadTime = this.loadingTime.getNext();
		this.stats.incrementLoadCalls(loadTime);
		Event truckTwo = new Event(Event.LOAD, loadTime);
		
		processLoadingEvent(truckOne);
		processLoadingEvent(truckTwo);
		
		for (int i = 0; i < 4; i++)
			this.loadingQueue.add(new Event(Event.TRAVEL, 0));
	}
	
	/**
	 * Processes the event.
	 * @param event
	 */
	public void processLoadingEvent(Event event) {
		this.numberOfLoadersInUse++;
		this.scheduleCompletion(event.getEventType());		
	}
	
	public void processWeighingEvent(Event event) {
		this.numberOfWeighersInUse++;
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
		double callTime;
		
		if (eventType == Event.LOAD) {
			callTime = this.clock + this.loadingTime.getNext();
			this.stats.incrementLoadCalls(callTime - this.clock);
			this.eventList.addEvent(new Event(Event.COMPLETE_LOAD, callTime));
			this.eventList.addEvent(new Event(Event.WEIGH, callTime));
		}else if (eventType == Event.WEIGH) {
			callTime = this.clock + this.weighingTime.getNext();
			this.stats.incrementWeighCalls(callTime - this.clock);
			this.eventList.addEvent(new Event(Event.COMPLETE_WEIGH, callTime));
			this.eventList.addEvent(new Event(Event.TRAVEL, callTime));
		}else{
			callTime = this.clock + this.travelTime.getNext();
			this.stats.incrementTravelCalls(callTime - this.clock);
			this.eventList.addEvent(new Event(Event.COMPLETE_TRAVEL, callTime));
			this.eventList.addEvent(new Event(Event.LOAD, callTime));
		}
	}
	
	public void processCompletion(Event event) {		
		if (event.getEventType() == Event.COMPLETE_WEIGH) 
			this.numberOfWeighersInUse--;
		else if (event.getEventType() == Event.COMPLETE_LOAD)
			this.numberOfLoadersInUse--;
		else if (event.getEventType() == Event.COMPLETE_TRAVEL)
			this.numberOfTrips ++;
		
		if (!this.loadingQueue.isEmpty()) {
			Event nextLoading = this.loadingQueue.poll();
			stats.incrementLoadCallsInQueue(this.clock - nextLoading.getEventTime());
			processLoadingEvent(nextLoading);
		}else if (!this.weighingQueue.isEmpty()) {
			Event nextWeighing = this.weighingQueue.poll();
			stats.incrementWeighCallsInQueue(this.clock - nextWeighing.getEventTime());
			processWeighingEvent(nextWeighing);
		}
	}
	
	
	/**
	 * Start of the simulation.
	 */
	public void startSimulation() {
		
		while (this.clock <= 1000 || this.loadingQueue.isEmpty() & this.weighingQueue.isEmpty()) {
			Event event = this.eventList.getImminentEvent();
			this.clock = event.getEventTime();
						
			if (event.getEventType() == Event.LOAD && this.numberOfLoadersInUse < this.NUM_OF_LOADERS) {
				processLoadingEvent(event);
			}else if (event.getEventType() == Event.WEIGH && this.numberOfWeighersInUse < this.NUM_OF_WEIGHERS) {
				processWeighingEvent(event);
			}else if (event.getEventType() == Event.TRAVEL) {
				processTravelEvent(event);
			}else if (event.getEventType() >= 3) {
				processCompletion(event);
			}
			else{
				if (event.getEventType() == Event.LOAD) {
					this.loadingQueue.add(event);
				}else if(event.getEventType() == Event.WEIGH){
					this.weighingQueue.add(event);
				}
			}
		}
		
		this.stats.setTotalTime(this.clock);
		this.stats.setTotalCalls(this.numberOfTrips);
		this.stats.reportGeneration();
	}
	
	/**
	 * Main Method
	 */
	public static void main(String args[]) {
		
		Simulation simulation = new Simulation();
		simulation.startSimulation();
	}
}
