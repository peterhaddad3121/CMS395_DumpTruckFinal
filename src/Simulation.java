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
		this.loadingTime = (Distribution) distributionFactory.createDiscreteEmpiricalDistribution(WEIGHING_TIME, LOADING_PROBABILITY);
	}
	
	/**
	 * Start of the simulation.
	 */
	public void startSimulation() {
		
		do {
			
		}while(DO STUFF);
	}
	
	/**
	 * Main Method
	 */
	public static void main(String args[]) {
		
		Simulation simulation = new Simulation();
		simulation.startSimulation();
	}
}
