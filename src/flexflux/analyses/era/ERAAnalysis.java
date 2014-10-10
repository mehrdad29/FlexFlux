package flexflux.analyses.era;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import parsebionet.biodata.BioEntity;
import flexflux.analyses.Analysis;
import flexflux.analyses.result.ERAResult;
import flexflux.general.Bind;
import flexflux.general.Vars;
import flexflux.thread.ResolveThread;
import flexflux.thread.ThreadEra;

public class ERAAnalysis extends Analysis {

	/**
	 * Number of simulations to do
	 */
	private int numberSimulations = 0;
	
	/**
	 * Mean of the gaussian distribution used for initializing the number of activated inputs
	 */
	private double gaussianMean;
	
	/**
	 * Standard deviation of the gaussian distribution used for initializing the number of activated inputs
	 */
	private double gaussianStd;
	
	/**
	 * Min number of activated inputs in the randomized media 
	 */
	private int minInputs;
	
	/**
	 * Max number of activated inputs in the randomized media 
	 */
	private int maxInputs;
	
	/**
	 * List of input random parameters containing the activation, inhibition and weight values
	 */
	ArrayList<InputRandomParameters> inputRandomParameters;
	
	/**
	 * List of inputs in inputRandomParameters
	 */
	Set<String> inputs;

	/**
	 * Map of objectives, key : name, value : expression
	 */
	public HashMap<String, String> objectives;
	
	/**
	 * List containing the threads that will solve each step of simulation
	 */
	protected List<ResolveThread> threads = new ArrayList<ResolveThread>();

	/**
	 * Constructor
	 * 
	 * @param b
	 * @param numberSimulations
	 */
	public ERAAnalysis(Bind b, int numberSimulations,
			HashMap<String, String> objectives,
			ArrayList<InputRandomParameters> inputRandomParameters,
			double gaussianMean, double gaussianStd, int minInputs,
			int maxInputs) {
		super(b);

		this.numberSimulations = numberSimulations;
		this.objectives = objectives;

		this.inputRandomParameters = inputRandomParameters;

		this.inputs = new HashSet<String>();
		for(InputRandomParameters input : inputRandomParameters) {
			inputs.add(input.getId());
		}

		this.gaussianMean = gaussianMean;
		this.gaussianStd = gaussianStd;
		this.minInputs = minInputs;
		this.maxInputs = maxInputs;

	}

	@Override
	public ERAResult runAnalysis() {
		
		double startTime = System.currentTimeMillis();

		ERAResult result = new ERAResult(this.inputs, objectives.keySet());

		Queue<Integer> tasks = new LinkedBlockingQueue<Integer>();

		for (int i = 0; i < numberSimulations; i++) {
			tasks.add(i);
		}

		for (int i = 0; i < Vars.maxThread; i++) {

			Bind newBind = null;

			try {
				newBind = b.copy();
			} catch (ClassNotFoundException | NoSuchMethodException
					| SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
			ThreadEra threadEra = new ThreadEra(newBind, tasks, objectives,
					gaussianMean, gaussianStd, minInputs, maxInputs,
					inputRandomParameters, result);
			
			threads.add(threadEra);

		}
		
		if (Vars.verbose) {
			System.err.println("Progress : ");

			System.err.print("[");
			for (int i = 0; i < 50; i++) {
				System.err.print(" ");
			}
			System.err.print("]\n");
			System.err.print("[");
		}
		
		for (ResolveThread thread : threads) {
			thread.start();
		}

		for (ResolveThread thread : threads) {
			// permits to wait for the threads to end
			try {
				thread.join();
			} catch (InterruptedException e) {
				// e.printStackTrace();
			}
		}

		if (Vars.verbose) {
			System.err.print("]\n");
		}

		// we remove the threads to permit another analysis
		while (threads.size() > 0) {
			threads.remove(0);
		}
		
		if (Vars.verbose) {
			System.err.println("ERA analysis over "
					+ ((System.currentTimeMillis() - startTime) / 1000) + "s "
					+ Vars.maxThread + " threads");
		}
		return result;

	}
}
