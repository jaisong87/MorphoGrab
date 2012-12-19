package learners;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class NexusBuilder {

	private HashMap<String, ArrayList<String>> characterStates;

	public void clear() {
		characterStates = null;
	}

	public void setMap(HashMap<String, ArrayList<String>> chMap )
	{
		characterStates = chMap;
	}

	public ArrayList<String> getNexusStream() {
		ArrayList<String> nxStr = new ArrayList();
		nxStr.add("#NEXUS \n");
		nxStr.add("BEGIN CHARACTERS;\n");
		
		int chCount = characterStates.size();
		nxStr.add("DIMENSIONS NCHAR="+chCount+"\n");
		nxStr.add("	FORMAT DATATYPE=STANDARD MISSING=? GAP=- SYMBOLS=\"0123456789\";\n");
		nxStr.add("\n	CHARLABELS\n");
		
		Iterator itr = characterStates.entrySet().iterator();
		int chPos = 1;
		for(String key : characterStates.keySet()) {
			nxStr.add("		 ["+chPos+"] '"+key+"'\n");
			chPos++;
		}
		nxStr.add("	;\n\n");
		
		nxStr.add("	STATELABELS\n\n");
		chPos = 1;
		for(ArrayList<String> stateList : characterStates.values()){
			nxStr.add(chPos+"\n");
			for(String state : stateList) {
				nxStr.add("'" + state + "'\n");
			}
			nxStr.add(",\n");
			chPos++;
		}
		
		nxStr.add("ENDBLOCK;\n");
		return nxStr;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		NexusBuilder nx = new NexusBuilder();
		HashMap<String, ArrayList<String>>  chMap = new HashMap<String, ArrayList<String>>();
		
		ArrayList<String> animalStates = new ArrayList<String>();
		animalStates.add("cat");
		animalStates.add("dog");
		chMap.put("animal" , animalStates);
		
		ArrayList<String> fruitStates = new ArrayList<String>();
		fruitStates.add("apple");
		fruitStates.add("orange");
		chMap.put("fruits" , fruitStates);
		
		nx.setMap(chMap);
		ArrayList<String> nxStream = nx.getNexusStream();
		
		for(String line : nxStream)
			System.out.print(line);
	}

}
