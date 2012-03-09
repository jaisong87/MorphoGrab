/**
 * 
 */
package learners;
import java.util.Collections;
import java.util.Vector;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import learners.regexTemplate;



/**
 * @author jaison
 * This class would learn appropriate regex'es from one supervised example
 * Constructor would take a large text as input and after learning is over 
 * an appropriate flag should be marked indicating learning-complete.
 * If the flag is marked and extraction is requested , data would be extracted
 * and returned using the regular expressions learned. 
 * States should be maintained and regex's should be saved in the class
 */
public class ReLearner {
    
        public static enum AlgoType{ DELIMITER_SEPARATOR, REGEX_TEMPLATE, KNN_LEARNER };
    
	boolean learningOver; /* Is learning Over - state variable */
	String stateExpr; /* Used by REGEX_TEMPLATE : Expression for character and states */

	Vector<String> dataRows; /* These are rows in the input document. We operate on this for information-extraction */
	boolean enableDebugging; /* For more debug prints*/
	
	String charStateDelimiter; /* To split between character and all of the remaining states*/
    Vector<String> stateDelimiters; /* Used by DELIMITER_SEPERATOR - Set of possible delimiters */
    
    AlgoType learningAlgorithm; /* Specify the type of learning algorithm */
   
    Vector<regexTemplate> stateTemplates; /* All the regexTemplates for states are in this container */
    
	/* Constructor - should take in a large chunk of Text 
	 * All the datarows are taken in the constructor 
	 * AlgoType is also specified in the constructor
	 */
    public ReLearner(Vector<String> dr, AlgoType alg){
		learningAlgorithm = alg;
		learningOver = false;
		enableDebugging = true;                   /* Enable debugging by default */
		charStateDelimiter = new String();        /* Char-State delimiter */ 
		dataRows = new Vector<String>(dr);		  /* Initialise the data-rows. More could be added later */
		stateDelimiters = new Vector<String>();   /* Used in DELIMITER_SEPARATOR */
		stateTemplates = new Vector<regexTemplate>(); /* Used in REGEX_TEMPLATE*/ 
    }
	
    /* Reset the states and start all over again
     * All the learning would be cleared. Except for the data-rows, the regexLearner
     * won't know of anything else.
     */
	public void resetStates(){
		learningOver = false;
		return;
	}
	
	/*
	 * Add an example. As a consequence some learning should happen.
	 */
        public boolean Train(String Character , String States , String line)
        {
            return true;
        
        }
        
	public boolean addExample(String dataRow, int startCharPos, int endCharPos, Vector<Integer> startStatePos, Vector<Integer> endStatePos) /* Add an example for Character*/
	{
		System.out.println("ReLearner::addExample() Called");
		
	if(enableDebugging)
	{
		System.out.println("Example : "+dataRow);
		System.out.println("Character :"+dataRow.substring(startCharPos, endCharPos- startCharPos));
			
		charStateDelimiter = dataRow.substring( endCharPos, startStatePos.elementAt(0) );

		System.out.println("Char/State - Delimiter is \""+charStateDelimiter+"\"");
		
	if(AlgoType.DELIMITER_SEPARATOR == learningAlgorithm )
	{
		System.out.print("States : "+dataRow.substring(startStatePos.elementAt(0), endStatePos.elementAt(0) ) );
		for(int i=1;i<startStatePos.size();i++)
		{
			stateDelimiters.addElement(dataRow.substring(endStatePos.elementAt(i-1), startStatePos.elementAt(i)));
			System.out.print(" , "+dataRow.substring(startStatePos.elementAt(i), endStatePos.elementAt(i) )  );			
		}
		
	}
	else if (AlgoType.REGEX_TEMPLATE == learningAlgorithm)
	{
		for(int i=0;i<startStatePos.size();i++)
		{
		regexTemplate rtemplate = new regexTemplate(dataRow.substring(startStatePos.elementAt(0), endStatePos.elementAt(0)));
		stateTemplates.addElement(rtemplate);
		System.out.println("Latest learned expression is "+rtemplate.getRegex());
		}
		
	}
		for(int i=1;i<startStatePos.size();i++)
		{
			//stateDelimiters.addElement(dataRow.substring(endStatePos.elementAt(i-1), startStatePos.elementAt(i)));
			System.out.println(" DLT :  "+dataRow.substring(endStatePos.elementAt(i-1), startStatePos.elementAt(i))  );			
		}
		
	}
	
	return false; /* Not Implemented*/	
	}

	public void printSummary()
	{
		System.out.println("================== SUMMARY ========================");
	if(AlgoType.DELIMITER_SEPARATOR == learningAlgorithm)
	{
		System.out.println("Learning Algorithm : DELIMITER_SEPERATORS Heuristic");
	}
	else if(AlgoType.REGEX_TEMPLATE == learningAlgorithm)
	{
		System.out.println("Learning Algorithm : REGEX_TEMPLATE");
		for(int i=0;i<stateTemplates.size();i++)
		{
		regexTemplate stateTemplate = stateTemplates.elementAt(0);
		System.out.println("Template#"+i+" : "+stateTemplate.getRegex());
		}
	}
	else {
		System.out.println("Learning Algorithm Specified is unknown");
	}
		System.out.println("===================================================");
	}
	
	public boolean learnExpressions()
	{
		
		/*
		 *  From a set of temporary expressions for states, Generalise a single expression
		 */
		learningOver = true;
		return true;
	}

	public void showCharacterAndStates()
	{
		if(AlgoType.DELIMITER_SEPARATOR == learningAlgorithm)
				showCharacterAndStatesUsingDelimiters();
		else if(AlgoType.REGEX_TEMPLATE == learningAlgorithm){
				showCharacterAndStatesUsingRegexTemplates();
		}
	}
	
	private void showCharacterAndStatesUsingRegexTemplates()
	{
		regexTemplate stateTemplate = stateTemplates.elementAt(0);
	    Pattern pattern = Pattern.compile(stateTemplate.getRegex());

		
		for(int i=0;i<dataRows.size();i++)
		{
			System.out.println("-----------------------------------------------------------------");
			System.out.println("Row is : "+dataRows.elementAt(i));
			String[] tokens = dataRows.elementAt(i).split(charStateDelimiter);
			String curCharacter = "";
			String combinedStates = "";
			for(int j=0;j<tokens.length-1;j++)
			{
				if(j>0) curCharacter+= ":";
				curCharacter += tokens[j];
			}
			combinedStates = tokens[tokens.length-1];
			System.out.print(curCharacter+ " => ");
									
			Vector<String> curStates = new Vector<String>();
			
		    Matcher matcher = pattern.matcher(combinedStates);

		    while (matcher.find()) {
//		                System.out.println("I found the text - " + matcher.group() + " starting at " +"index " + matcher.start() +" and ending at index " + matcher.end());
		    	curStates.addElement(matcher.group());
		    }

			
			for(int j=0;j<curStates.size();j++)
			{
				System.out.print(" { "+curStates.elementAt(j) + " } ");
			}
			System.out.println("");
		}
	}
	
	
	/* Show characters and states using delimters */
	private void showCharacterAndStatesUsingDelimiters()
	{
		
			for(int i=0;i<dataRows.size();i++)
		{
			System.out.println("-----------------------------------------------------------------");
			System.out.println("Row is : "+dataRows.elementAt(i));
			String[] tokens = dataRows.elementAt(i).split(charStateDelimiter);
			String curCharacter = "";
			String curState = "";
			for(int j=0;j<tokens.length-1;j++)
					curCharacter += tokens[j];
			curState = tokens[tokens.length-1];
			System.out.print(curCharacter+ " => ");
			
	//		System.out.println("DEBUG : "+curState);
			
			Vector<String> curStates = new Vector<String>();
			Vector<String> prevStates= new Vector<String>();
			curStates.addElement(curState);
			prevStates.addElement(curState);
			
			for(int j=0;j<stateDelimiters.size();j++)
			{
	//			prevStates = curStates;
				curStates.clear();
	//			System.out.println("Using delimiter "+stateDelimiters.elementAt(j)+" on prevStates of "+prevStates.size()+" elements..curStates have "+curStates.size()+" elements");
				for(int k=0;k<prevStates.size();k++)
				{
					String[] tmpTokens = prevStates.elementAt(k).split(stateDelimiters.elementAt(j));
					//System.out.println("Got "+tmpTokens.length+" pieces from "+prevStates.elementAt(k));
					for(int l = 0;l<tmpTokens.length;l++)
							curStates.addElement(tmpTokens[l]);
				}
				prevStates = curStates;
			}
			
			for(int j=0;j<curStates.size();j++)
			{
				System.out.print(" { "+curStates.elementAt(j) + " } ");
			}
			System.out.println("");
		}
			for(int j=0;j<stateDelimiters.size();j++)
			{
			System.out.println(stateDelimiters.elementAt(j));
			}

	}
	
	/*
	 * This part is cool , Just extract Information
	 */
	public HashMap<String, Vector<String> > extractCharacterAndStates()
	{
		HashMap<String, Vector<String>> characterAndStates = new HashMap<String, Vector<String>>();
		
		return characterAndStates;
	}

        /*
	public static void main(String args[])
	{
		System.out.println("Hello World!!!");
		Vector<String> dataRows = new Vector<String>();
		
		FileInputStream fin;
		try {
			fin = new FileInputStream("/home/jaison/workspace/MorphoGrab/src/learners/input.txt");
			DataInputStream din = new DataInputStream(fin);
			BufferedReader br = new BufferedReader(new InputStreamReader(din));
			String nextLine;
			try {
				while((nextLine = br.readLine()) != null)
					dataRows.addElement(nextLine);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.reverse(dataRows);
	
		String line1 = "1. Pigmentation: †Carapace, tergites, pedipalps and metasoma (e.g., pedipalpal and metasomal carinae): pigmented/infuscated (0); not pigmented/infuscated (1).";	
		
		//ReLearner myLearner = new ReLearner(dataRows,AlgoType.DELIMITER_SEPARATOR);
		ReLearner myLearner = new ReLearner(dataRows,AlgoType.REGEX_TEMPLATE);
		//106, 130
		//132, 159
		Vector<Integer> startPos = new Vector<Integer>();
		Vector<Integer>   endPos = new Vector<Integer>();
		
		startPos.add(103); endPos.add(127);
		startPos.add(129); endPos.add(157);
		
		myLearner.addExample(line1, 0, 101, startPos, endPos);
		
		myLearner.learnExpressions();
		
		myLearner.showCharacterAndStates();
		
		myLearner.printSummary();
		
		return;
	}
        * 
        */
}