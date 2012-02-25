package learners;
import java.util.Iterator;
import java.util.Vector;
import java.util.ArrayList;

public class BoolKNN {
	int N; /* Dimensioanlity */
	Vector<ArrayList<Boolean>> learnedInputs;
	Vector<Boolean> learnedAnswers;
    int K;	
	
	public BoolKNN(int dim /*, int k*/) {
		// TODO Auto-generated constructor stub
			N = dim;
			K = 3; /* K for KNN */
			
			learnedInputs = new Vector<ArrayList<Boolean>>();
			learnedAnswers = new Vector<Boolean>();
			}

	public boolean trainSample(ArrayList<Boolean> featureVector, boolean ans)
	{
		if(featureVector.size() != N)
		{
			System.out.println("BoolKNN : DimensionsOfSample do not match with the problem");
			return false;
		}
		learnedInputs.addElement(featureVector);
		learnedAnswers.addElement(ans);
		return true;
	}
	
	private int getDist(ArrayList<Boolean> x1, ArrayList<Boolean> x2)
	{
	int dis =0;
	   
	if(x1.size() != x2.size())
		{
		System.out.println("BoolKNN : Fatal error - Incompatible Input for testing data");
		dis = N+1; /* Equivalend to Infinity*/
		}
	else {
		Iterator<Boolean> itr1 = x1.iterator();
		Iterator<Boolean> itr2 = x2.iterator();

		while(itr1.hasNext())
		{
			if(itr1.next() != itr2.next() )
				dis++;
		}
	}
	return dis;
	}
	
	public boolean predictOutput(ArrayList<Boolean> featureVector)
	{
		boolean result = false;
		int trainingSz = learnedInputs.size();
		
		/*Assume K=3 for now and At-least 3 samples are guarenteed in learnedInputs*/
		int closestDis1 = 1000000, closestDis2 = 1000000, closestDis3 = 1000000;
		boolean res1 = false, res2 = false, res3 = false;
		
		int curDis, tmpDis;
		boolean curAns, tmpAns;
		
for(int i=0;i<trainingSz; i++)
{
	    curDis = getDist(featureVector,learnedInputs.get(i));		
		curAns = learnedAnswers.get(i);
	
		if(curDis < closestDis3)
		{
			tmpDis = closestDis3; closestDis3 = curDis; curDis = tmpDis;		
			tmpAns = res3; res3 = curAns; curAns = tmpAns;		
		}
		
		if(curDis < closestDis2)
		{
			tmpDis = closestDis2; closestDis2 = curDis; curDis = tmpDis;		
			tmpAns = res2; res2 = curAns; curAns = tmpAns;		
		}
		
		if(curDis < closestDis1)
		{
			tmpDis = closestDis1; closestDis1 = curDis; curDis = tmpDis;		
			tmpAns = res1; res1 = curAns; curAns = tmpAns;		
		}		
}	
		int trueCount = 0;
		if(res1) trueCount++;
		if(res2) trueCount++;
		if(res3) trueCount++;
		
		if(trueCount>=2) result = true;
		return result;
	}

	public static void main(String args[])
	{
		/* Simple example where we get true if at-least 4 are true out of 7 - Just to test the classifier */
		BoolKNN majorityVoter = new BoolKNN(7);
		
		ArrayList<Boolean> x1 = new ArrayList<Boolean>(); 
		x1.add(true);x1.add(true);x1.add(true);x1.add(true);
		x1.add(true);x1.add(true);x1.add(true);
		
		majorityVoter.trainSample(x1, true);
		
		ArrayList<Boolean> x2 = new ArrayList<Boolean>(); 
		x2.add(true);x2.add(true);x2.add(true);x2.add(true);
		x2.add(true);x2.add(true);x2.add(false);
		majorityVoter.trainSample(x2, true);
		
		ArrayList<Boolean> x3 = new ArrayList<Boolean>(); 
		x3.add(true);x3.add(true);x3.add(true);x3.add(true);
		x3.add(false);x3.add(false);x3.add(false);
		majorityVoter.trainSample(x2, true);

		ArrayList<Boolean> x4 = new ArrayList<Boolean>(); 
		x4.add(false);x4.add(true);x4.add(false);x4.add(true);
		x4.add(true);x4.add(false);x4.add(false);
		majorityVoter.trainSample(x4, false);

		ArrayList<Boolean> x5 = new ArrayList<Boolean>(); 
		x5.add(false);x5.add(false);x5.add(true);x5.add(true);
		x5.add(false);x5.add(false);x5.add(false);
		majorityVoter.trainSample(x5, false);

		ArrayList<Boolean> x6 = new ArrayList<Boolean>(); 
		x6.add(true);x6.add(true);x6.add(true);x6.add(true);
		x6.add(true);x6.add(true);x6.add(false);
		majorityVoter.trainSample(x6, true);

		
		ArrayList<Boolean> x7 = new ArrayList<Boolean>(); 
		x7.add(false);x7.add(true);x7.add(false);x7.add(true);
		x7.add(false);x7.add(false);x7.add(false);
		majorityVoter.trainSample(x7, false);

		ArrayList<Boolean> x8 = new ArrayList<Boolean>(); 
		x8.add(false);x8.add(false);x8.add(false);x8.add(false);
		x8.add(true);x8.add(true);x8.add(false);
		majorityVoter.trainSample(x2, false);

/*--------------------------------------------------------------------------- */
	ArrayList<Boolean> t1 = new ArrayList<Boolean>();
	t1.add(true); t1.add(true); t1.add(true); t1.add(true);
	t1.add(true); t1.add(true); t1.add(false);
	
	boolean ans = majorityVoter.predictOutput(t1);
	System.out.println("For Input 1111110 output is "+ans);
	
	ArrayList<Boolean> t2 = new ArrayList<Boolean>();
	t2.add(false); t2.add(true); t2.add(false); t2.add(true);
	t2.add(false); t2.add(true); t2.add(false);	
	boolean ans2 = majorityVoter.predictOutput(t2);
	System.out.println("For Input 0101010 output is "+ans2);

	}
}
