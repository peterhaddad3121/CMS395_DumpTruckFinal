import java.util.PriorityQueue;
import java.util.Queue;

import generalSimulation.DiscreteDistribution;
import generalSimulation.Distribution;
import generalSimulation.DistributionFactory;

/**
 * Dump Truck Simulation Class
 * 
 * Simulates a specified number of dump trucks for a given amount of time
 * 
 * Simulates their loading, weighing and traveling events calculating time they spent in queue,
 * total number of loads, weighs, and travels and the max, total and average time of each.
 * 
 * @author Peter, Lenuel, Brandt, Roderick
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
	 * Constructor creates distributionFactory and creates the necessary distributions. 
	 * Creates the eventLists and queues. Creates stats objects.
	 * 
	 * Lastly, starts off the simulation with 2 trucks loading and 4 trucks in the loading queue.
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
		this.stats.incrementLoadEvents(loadTime);
		Event truckOne = new Event(Event.LOAD, loadTime);
		
		loadTime = this.loadingTime.getNext();
		this.stats.incrementLoadEvents(loadTime);
		Event truckTwo = new Event(Event.LOAD, loadTime);
		
		processLoadingEvent(truckOne);
		processLoadingEvent(truckTwo);
		
		for (int i = 0; i < 4; i++)
			this.loadingQueue.add(new Event(Event.TRAVEL, 0));
	}
	
	/**
	 * Schedule completion of current event type and increment number of loaders in use
	 * @param event
	 */
	public void processLoadingEvent(Event event) {
		this.numberOfLoadersInUse++; // Loading event uses a loader
		this.scheduleCompletion(event.getEventType());		
	}
	
	/**
	 * Schedule completion of current event type and increment number of weighers in use
	 * @param event
	 */
	public void processWeighingEvent(Event event) {
		this.numberOfWeighersInUse++; // Weighing event uses a weigher
		this.scheduleCompletion(event.getEventType());
	}
		
	/**
	 * Schedule completion of current event type
	 * @param event
	 */
	public void processTravelEvent(Event event) {
		this.scheduleCompletion(event.getEventType());
	}
	
	/**
	 * Schedule the next completion event, and the start of the next type of event afterwards
	 * Increment stats for each type of event
	 * 
	 * @param event
	 */
	public void scheduleCompletion(int eventType) {		
		double callTime;
		
		if (eventType == Event.LOAD) {
			callTime = this.clock + this.loadingTime.getNext();
			this.stats.incrementLoadEvents(callTime - this.clock);
			this.eventList.addEvent(new Event(Event.COMPLETE_LOAD, callTime)); // Add a completion event to increment available loaders
			this.eventList.addEvent(new Event(Event.WEIGH, callTime));		 // Add a weighing event because that comes after loading
		}else if (eventType == Event.WEIGH) {
			callTime = this.clock + this.weighingTime.getNext();
			this.stats.incrementWeighEvents(callTime - this.clock);
			this.eventList.addEvent(new Event(Event.COMPLETE_WEIGH, callTime)); // Add a completion event to increment available weighers
			this.eventList.addEvent(new Event(Event.TRAVEL, callTime));		  // Add a travel event because that comes after weighing
		}else{
			callTime = this.clock + this.travelTime.getNext();
			this.stats.incrementTravelEvents(callTime - this.clock);
			this.eventList.addEvent(new Event(Event.COMPLETE_TRAVEL, callTime)); // Add a completion event to increment total trips
			this.eventList.addEvent(new Event(Event.LOAD, callTime));			   // Add a load event because that comes after traveling
		}
	}
	
	/**
	 * When an event is completed this method gets called
	 * Decrements number of Weighers/Loaders in use if necessary or
	 * increments number of trips
	 * 
	 * If the weighing or loading queue isn't empty
	 * calls next event from the given queue
	 * 
	 * @param event
	 */
	public void processCompletion(Event event) {		
		if (event.getEventType() == Event.COMPLETE_WEIGH) 
			this.numberOfWeighersInUse--;
		else if (event.getEventType() == Event.COMPLETE_LOAD)
			this.numberOfLoadersInUse--;
		else if (event.getEventType() == Event.COMPLETE_TRAVEL)
			this.numberOfTrips ++;
		
		if (!this.loadingQueue.isEmpty()) { // Processes all loading events in queue before event list
			Event nextLoading = this.loadingQueue.poll();
			stats.incrementLoadEventsInQueue(this.clock - nextLoading.getEventTime());
			processLoadingEvent(nextLoading);
		}
		if (!this.weighingQueue.isEmpty()) { // Processes all weighing events in queue before event list
			Event nextWeighing = this.weighingQueue.poll();
			stats.incrementWeighEventsInQueue(this.clock - nextWeighing.getEventTime());
			processWeighingEvent(nextWeighing);
		}
	}
	
	/**
	 * Run through the simulation, stops adding loading events at time 1000
	 * After eventList and queues are empty print stats
	 */
	public void startSimulation() {
		// Run simulation until all queues and event list are empty
		while (!this.loadingQueue.isEmpty() || !this.weighingQueue.isEmpty() || !this.eventList.isEmpty()) {
			Event event = this.eventList.getImminentEvent();
			this.clock = event.getEventTime();
									
			if (event.getEventType() == Event.LOAD && this.numberOfLoadersInUse < this.NUM_OF_LOADERS && this.clock <= 1000) {
				processLoadingEvent(event);  // Add loading events to the event list as long as time isn't greater than 1000
			}else if (event.getEventType() == Event.WEIGH && this.numberOfWeighersInUse < this.NUM_OF_WEIGHERS) {
				processWeighingEvent(event); // Add weighing events to the event list as long as there are loading events that are finishing
			}else if (event.getEventType() == Event.TRAVEL) {
				processTravelEvent(event);   // Add traveling events to event list as long as there are weighing events
			}else if (event.getEventType() >= 3) {
				processCompletion(event);    // Add completion events to queue until all events are complete
			}
			else{
				if (event.getEventType() == Event.LOAD && this.clock <= 1000) {
					this.loadingQueue.add(event); // Add overflow loading events to queue unless time is greater than 1000
				}else if(event.getEventType() == Event.WEIGH){
					this.weighingQueue.add(event); // Add overflow weighing events to queue until there are no more
				}
			}
		}
		// Printing out statistics
		this.stats.setTotalTime(this.clock);
		this.stats.setTotalEvents(this.numberOfTrips);
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
