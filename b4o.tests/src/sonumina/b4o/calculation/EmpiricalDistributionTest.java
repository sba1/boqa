package sonumina.b4o.calculation;

import static org.junit.Assert.*;

import org.junit.Test;

public class EmpiricalDistributionTest
{

	@Test
	public void test()
	{
		double [] obs = new double[]{ 1,1,1.5,2,2.5,3,3.5,4,0.5 };
		
		EmpiricalDistribution dis = new EmpiricalDistribution(obs);
		 
		assertEquals(dis.cdf(0.5,false),1/(double)obs.length,0.0001);
		assertEquals(dis.cdf(1,false),3/(double)obs.length,0.0001);
	}

}
