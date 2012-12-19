package preprocessing;

import java.util.HashMap;
import java.util.Vector;


public class NoiseHandler {
	
	int frequencyThreshold;
	Vector<String> knownNoise; 
	
	public NoiseHandler(int frqTh, Vector<String> noise)
	{
		frequencyThreshold = frqTh;	
		knownNoise = noise;
	}

        
	public Vector<String> deNoisify(Vector<String> inpStream) {
		Vector<String> noiseFreeStream = new Vector<String>();
		/* Create Frequency map first*/
		HashMap<String, Integer > frqMap = new HashMap<String, Integer >();
		
		for(int i=0;i<inpStream.size();i++)
		{
			int count = 0;
			if (frqMap.containsKey(inpStream.get(i)))
				count = frqMap.get(inpStream.get(i));
			frqMap.put(inpStream.get(i), count + 1);
		}
		
		int noiseLines = 0;
		
		for(int i=0;i<inpStream.size();i++)
		{
			String curLine = inpStream.get(i);
			int count = frqMap.containsKey(inpStream.get(i)) ? frqMap.get(inpStream.get(i)) : 0;
			
			if(count < frequencyThreshold && knownNoise.contains(curLine)==false && curLine.length()>=3)
				noiseFreeStream.add(curLine);
			else {
				System.out.println("INFO : Removing NOISE - "+curLine);
				noiseLines++;
				}
		}
		
		System.out.println("---------------------- Removed "+noiseLines+" noise lines ------------------");	
		return noiseFreeStream;
	}	

}
