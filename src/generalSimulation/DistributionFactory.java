package generalSimulation;

import java.util.Random;

public class DistributionFactory
{
	private Random stream;

	public DistributionFactory()
	{
		this.stream = new Random();
	}

	public DistributionFactory(long seed)
	{
		this.stream = new Random(seed);
	}

	public Distribution createNormal(double mean, double standardDeviation)
	{
		return new NormalDistribution(this.stream, mean, standardDeviation);
	}

	public Distribution createExponential(double mean)
	{
		return new ExponentialDistribution(this.stream, mean);
	}

	public Distribution createEmpiricalDistribution(double[] values, double[] probabilities)
	{
		return new EmpiricalDistribution(this.stream, values, probabilities);
	}

	public Distribution createUniformDistribution(int first, int last)
	{
		return new UniformDistribution(this.stream, first, last);
	}

	public DiscreteDistribution createEquilikelyDistribution(int first, int last)
	{
		return new EquilikelyDistribution(this.stream, first, last);
	}

	public DiscreteEmpiricalDistribution createDiscreteEmpiricalDistribution(int[] values, double[] probabilities)
	{
		return new DiscreteEmpiricalDistribution(this.stream, values, probabilities);
	}
}

class NormalDistribution implements Distribution
{
	private double mean;
	private double standardDeviation;
	private Random stream;

	protected NormalDistribution(Random stream, double mean, double standardDeviation)
	{
		this.stream = stream;
		this.mean = mean;
		this.standardDeviation = standardDeviation;
	}

	@Override
	public double getNext()
	{
		return this.stream.nextGaussian() * this.standardDeviation + this.mean;
	}
}

class ExponentialDistribution implements Distribution
{
	private double mean;
	private Random stream;

	protected ExponentialDistribution(Random stream, double mean)
	{
		this.stream = stream;
		this.mean = mean;
	}

	@Override
	public double getNext()
	{
		return -this.mean * Math.log(this.stream.nextDouble());
	}
}

class EmpiricalDistribution implements Distribution
{
	private Random stream;
	private double[] values;
	private double[] cumulativeProbabilities;

	public EmpiricalDistribution(Random stream, double[] values, double[] probabilities)
	{
		this.stream = stream;
		this.values = values;
		this.cumulativeProbabilities = new double[probabilities.length];
		this.cumulativeProbabilities[0] = probabilities[0];
		for (int i = 1; i < probabilities.length; i++)
			this.cumulativeProbabilities[i] = probabilities[i] + this.cumulativeProbabilities[i - 1];
	}

	@Override
	public double getNext()
	{
		double random = this.stream.nextDouble();
		for (int i = 0; i < this.cumulativeProbabilities.length; i++)
			if (random < this.cumulativeProbabilities[i])
				return this.values[i];
		return this.values[this.values.length - 1];

	}
}

class UniformDistribution implements Distribution
{
	private Random stream;
	private int last;
	private int first;
	public UniformDistribution(Random stream, int first, int last)
	{
		this.stream = stream;
		this.first = first;
		this.last = last;
	}

	@Override
	public double getNext()
	{
		double random = this.stream.nextDouble();
		random = (random * (this.last - this.first));
		random += this.first;
		return random;
	}
}

class EquilikelyDistribution implements DiscreteDistribution
{
	private UniformDistribution uniform;

	EquilikelyDistribution(Random stream, int first, int last)
	{
		this.uniform = new UniformDistribution(stream, first, last + 1);
	}

	@Override
	public int getNext()
	{
		return (int) this.uniform.getNext();
	}
}

class DiscreteEmpiricalDistribution implements DiscreteDistribution
{
	private EmpiricalDistribution empirical;

	DiscreteEmpiricalDistribution(Random stream, int[] values, double[] probabilities)
	{
		double[] vals = new double[values.length];
		for (int i = 0; i < vals.length; i++)
			vals[i] = values[i] + 0.1;
		this.empirical = new EmpiricalDistribution(stream, vals, probabilities);
	}

	@Override
	public int getNext()
	{
		return (int) this.empirical.getNext();
	}
}
///////////////////////
