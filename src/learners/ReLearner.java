/**
 * 
 */
package learners;
import java.util.Collections;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import learners.regexTemplate;

/**
 * @author jaison <jaisong87@gmail.com>
 * This class would learn appropriate regex'es from one supervised example
 * Constructor would take a large text as input and after learning is over 
 * an appropriate flag should be marked indicating learning-complete.
 * If the flag is marked and extraction is requested , data would be extracted
 * and returned using the regular expressions learned. 
 * States should be maintained and regex's should be saved in the class
 */
public class ReLearner {   
	public Vector<String> regexMatchedStates = new  Vector<String>();

        public static enum AlgoType{ DELIMITER_SEPARATOR, REGEX_TEMPLATE, KNN_LEARNER };
    
	boolean learningOver; /* Is learning Over - state variable */
	String stateExpr; /* Used by REGEX_TEMPLATE : Expression for character and states */

	Vector<String> dataRows; /* These are rows in the input document. We operate on this for information-extraction */
	boolean enableDebugging; /* For more debug prints*/
	
	String charStateDelimiter; /* To split between character and all of the remaining states*/
    Vector<String> stateDelimiters; /* Used by DELIMITER_SEPERATOR - Set of possible delimiters */
    
      private Vector<Vector<regexTemplate>>   multipleTemplates;
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
		/* Initialise the data-rows. More could be added later */
		dataRows = new Vector<String>();
		for(int i=0;i<dr.size();i++)
		{
			String str = dr.elementAt(i);
			//str.replace(",", ";"); - No replace for now
			System.out.print("Going to add "+str);
			dataRows.add(str);
		}
		
		stateDelimiters = new Vector<String>();   /* Used in DELIMITER_SEPARATOR */
		stateTemplates = new Vector<regexTemplate>(); /* Used in REGEX_TEMPLATE*/ 
                //statesDelimiter = new String();
                multipleTemplates = new Vector<Vector<regexTemplate>>();
    }

	/* Clear all the learning and start from zero */
	public void clearStates()
	{
		learningOver = false;
		enableDebugging = true;
		charStateDelimiter = "";
		stateDelimiters.clear();
		stateTemplates.clear();
		multipleTemplates.clear(); 
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
        
        private boolean checkiftemplateExist(Vector<regexTemplate> stateTemplateslocal,regexTemplate rtemplate)
        {
            for(int i = 0 ;i< stateTemplateslocal.size();i++)
            {
                  if(stateTemplateslocal.get(i).getRegex().equals(rtemplate.getRegex()))
                      return true;
            }
            return false ;
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
		
		/*
		 * Old Code by Jaison - Just keeping it here . Need to figure out which works better
		 */
		for(int i=0;i<startStatePos.size();i++)
		{
		regexTemplate rtemplate = new regexTemplate(dataRow.substring(startStatePos.elementAt(i), endStatePos.elementAt(i)));
		String rtemplateExpr = rtemplate.getRegex();
		
		
		boolean isExistingTemplate = false;
		/*
		for(int j=0;j<stateTemplates.size();j++)
		{
				String curExpr = stateTemplates.elementAt(j).getRegex();
				if(curExpr.equals(rtemplateExpr))
						isExistingTemplate = true;
		}
		*/
		if(false == isExistingTemplate)
					{
					stateTemplates.addElement(rtemplate);
					System.out.println("Latest added expression is "+rtemplate.getRegex());
					}
		System.out.println("Latest learned expression is "+rtemplate.getRegex());
		}
		
		/*
		 * Recent Changes  by prasad - One of these will eventually go in
		 */
        Vector<regexTemplate> stateTemplateslocal = new Vector<regexTemplate>();
		for(int i=0;i<startStatePos.size();i++)
		{
		 regexTemplate rtemplate = new regexTemplate(dataRow.substring(startStatePos.elementAt(i), endStatePos.elementAt(i)));
                 
                 // For better performance need to add only unique regex
                 // Better to convert this hashamp( Yet to be done)
                 if(!checkiftemplateExist(stateTemplateslocal,rtemplate))
                      stateTemplateslocal.addElement(rtemplate);
                  
                    if( i !=0 )
                    stateDelimiters.addElement(dataRow.substring(endStatePos.elementAt(i-1), startStatePos.elementAt(i)));
		System.out.println("Latest learned expression is "+rtemplate.getRegex());
		}
                	
                multipleTemplates.add(stateTemplateslocal);
	
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
		System.out.println("There are "+stateTemplates.size()+" regex templates");
		System.out.println("There are "+multipleTemplates.size()+" [P]templates");
		for(int i=0;i<multipleTemplates.size();i++)
		{
			//System.out.println(multipleTemplates.elementAt(i));
			Vector<regexTemplate> vrt = multipleTemplates.elementAt(i);
			for(int j=0;j<vrt.size();j++)
					System.out.print(vrt.elementAt(j).getRegex()+" & ");
				System.out.println();
		}
		//charStateDelimiter; /* To split between character and all of the remaining states*/
	    //Vector<String> stateDelimiters;
		
		System.out.println("Charater State seperator is "+charStateDelimiter);
		System.out.println(" There are "+stateDelimiters+" state delimters");
		
		for(int i=0;i<stateTemplates.size();i++)
		{
		regexTemplate stateTemplate = stateTemplates.elementAt(i);
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

	/*
	 * This part is cool , Just extract Information
	 */
	public  Vector<Pairs>/*HashMap<String, Vector<String> >*/ extractCharacterAndStates()
	{
		//HashMap<String, Vector<String>> characterAndStates = new HashMap<String, Vector<String>>();
		Vector<Pairs> characterAndStates = new Vector<Pairs>();

		if(AlgoType.DELIMITER_SEPARATOR == learningAlgorithm)
			characterAndStates = getCharacterAndStatesUsingDelimiters();
	else if(AlgoType.REGEX_TEMPLATE == learningAlgorithm){
		characterAndStates = getCharacterAndStatesUsingRegexTemplates();
			}
		
		return characterAndStates;
	}

        public Pattern getRegex()
        {
            regexTemplate stateTemplate = stateTemplates.elementAt(0);
            Pattern pattern = Pattern.compile(stateTemplate.getRegex());
            return pattern;
        }
	
        public Vector<Pairs> getCharacterAndStatesUsingRegexTemplatesPair()
	{
               	Vector<Pairs> pp = new  Stack<Pairs>();
	        
                for(int template = 0 ; template< multipleTemplates.size() ;template++)
                {
                  String finalString ="";
                  String currentRegex = "";  
                  String finalState = "";
                  Vector<regexTemplate> localstateTemplate =  multipleTemplates.elementAt(template);
                  for(int ii = 0 ; ii< localstateTemplate.size();ii++)
                  {
                   if(ii!=0)
                   {
                       finalString+="|";
                       finalState +="|";
                   }
                   regexTemplate stateTemplate = localstateTemplate.elementAt(ii);
                   currentRegex = stateTemplate.getRegex();
                   
                  // if( ii == localstateTemplate.size()-1)
                   {
                     finalState += currentRegex+ "\\.";  // adding end delimeter hard coded
                     currentRegex += "|" + currentRegex + "\\." ;
                     
                   }
                    finalString+= currentRegex;
                  }
                
                
                  Pattern pattern = Pattern.compile(finalString);
                  Pattern finalStatePatern = Pattern.compile(finalState);
                  
                  HashMap<String, Vector<String>> characterAndStates = new HashMap<String, Vector<String>>(); /* Hashmap containing character->set(states) */
		
                    for(int i=0;i<dataRows.size();i++)
                    {
			System.out.println("-----------------------------------------------------------------");
			System.out.println("Row is : "+dataRows.elementAt(i));
			String[] tokens = dataRows.elementAt(i).split(charStateDelimiter);
			String curCharacter = "";
			String combinedStates = "";
			for(int j=0;j<tokens.length-1;j++)
			{
			if(j>0) curCharacter+= charStateDelimiter;
				curCharacter += tokens[j];
			}
			combinedStates = tokens[tokens.length-1];
			
			if(true == enableDebugging)
				{
				System.out.print(curCharacter+ " => ");
				}
									
		    Vector<String> curStates = new Vector<String>();
                    String firstdel =stateDelimiters.elementAt(0).toString();
                    //char c = firstdel.charAt(0);
                    
                    // 
                    
                    Matcher finalStateMatcher = finalStatePatern.matcher(combinedStates);
                    if( finalStateMatcher.find())
                    {
                        String finalStateString = finalStateMatcher.group();
                        int endDelimeterIndex= combinedStates.indexOf(finalStateString) + finalStateString.length();
                        combinedStates= combinedStates.substring(0,endDelimeterIndex -1 );
                    }
                    
                    String[] statestokens = combinedStates.split(firstdel);
		
		    Matcher matcher = pattern.matcher(combinedStates);
                    
                    int noOfStates = 0 ;
		    while (matcher.find()) {
//		                System.out.println("I found the text - " + matcher.group() + " starting at " +"index " + matcher.start() +" and ending at index " + matcher.end());
                            String curre = matcher.group().trim();
                            if( statestokens[noOfStates].equals(curre))
                            {
                                curStates.addElement(curre);
                                noOfStates++;
                            }
                            else
                                break;
                            
                      }

			
			for(int j=0;j<curStates.size();j++)
			{
				if(true == enableDebugging)
					{
					System.out.print(" { "+curStates.elementAt(j) + " } ");
					}
			}
			
			System.out.println("");
                        if(curStates.size() == statestokens.length)
                        {
                            characterAndStates.put(curCharacter, curStates);
                                            Pairs p = new Pairs();
                        p.character = curCharacter;
                        p.states = curStates;
                        
                        pp.add(p);
        
                        }
                    }
                }
                printSummary();
                return pp;
	}
       	
	public Vector<String> getRegExpr()
	{
		Vector<String> regExprs = new Vector<String>();
		  for(int i=0;i<stateTemplates.size();i++)
                   regExprs.add(stateTemplates.elementAt(i).getRegex());
		return regExprs;
	}
		
	 
        private /*HashMap<String, Vector<String>>*/ Vector<Pairs>  getCharacterAndStatesUsingRegexTemplates()
	{
        	System.out.println("*********** ExtractCharacterAndStates() using RegexTemplates @jaison");
               	Vector<Pairs> pp = new  Stack<Pairs>();
	        String finalString ="";
                regexTemplate stateTemplate = stateTemplates.elementAt(0);
                finalString+= stateTemplate.getRegex();
               
               for(int i=1;i<stateTemplates.size();i++) 
            	   finalString+="|"+stateTemplates.elementAt(i).getRegex();
               
                Pattern pattern = Pattern.compile(finalString);
               
		for(int i=0;i<dataRows.size();i++)
		{
			System.out.println("-----------------------------------------------------------------");
			System.out.println("Row is : "+dataRows.elementAt(i));

			String curLine = dataRows.elementAt(i);
		if(curLine.contains(charStateDelimiter))
		{
			boolean char_over = false;
			String[] tokens = dataRows.elementAt(i).split(charStateDelimiter);
			String curCharacter = "";
			String combinedStates = "";
			int startOfState = -1; 
			
			for(int j=0;j<tokens.length-0;j++)
			{
			    Matcher matcher_test = pattern.matcher(tokens[j]);
			    if(matcher_test.find())
			    	char_over = true;
			    	
				//if(j>0) curCharacter+= charStateDelimiter;
				//curCharacter += tokens[j];
				if(char_over) combinedStates += ":" + tokens[j];
				else {	
						if(j>0) curCharacter+=":";
						curCharacter += tokens[j];
						}
				}
			//combinedStates = tokens[tokens.length-1];
			
			if(true == enableDebugging)
				{
				System.out.print(curCharacter+ " => ");
				}
									
		    Vector<String> curStates = new Vector<String>();
                    String firstdel =stateDelimiters.elementAt(0).toString();
                    //char c = firstdel.charAt(0);
                    String[] statestokens = combinedStates.split(firstdel);
		
		    Matcher matcher = pattern.matcher(combinedStates);
                    
                    int noOfStates = 0 ;
		    while (matcher.find()) {
//		                System.out.println("I found the text - " + matcher.group() + " starting at " +"index " + matcher.start() +" and ending at index " + matcher.end());
                        String curre = matcher.group().trim();  
			  curStates.addElement(curre);
			  regexMatchedStates.add(curre);
                            noOfStates++;
                    }

			
			for(int j=0;j<curStates.size();j++)
			{
				if(true == enableDebugging)
					{
					System.out.print(" { "+curStates.elementAt(j) + " } ");
					}
			}
			
			System.out.println("");
                        if(curStates.size() == statestokens.length) /* Line is not very clear */
                        {
                     //       characterAndStates.put(curCharacter, curStates);
                        
			Pairs p = new Pairs();
                        p.character = curCharacter;
                        p.states = curStates;
                        
                        if(!pp.contains(p))
                            pp.add(p);
        
                        }
			}
		}
	printSummary();
	System.out.println("There are "+regexMatchedStates.size()+" matches ");
	return pp;//characterAndStates;
	}
	
        
        
        
        
	/* Show characters and states using delimters */
	private  Vector<Pairs>/*HashMap<String, Vector<String>>*/ getCharacterAndStatesUsingDelimiters()
	{
                //HashMap<String, Vector<String>> characterAndStates = new HashMap<String, Vector<String>>(); /* Hashmap containing character->set(states) */
		 Vector<Pairs> characterAndStates = new Vector<Pairs>();

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
			if(true == enableDebugging)
				{
				System.out.print(curCharacter+ " => ");
				}
			
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
				if(true == enableDebugging)
				{
				System.out.print(" { "+curStates.elementAt(j) + " } ");
				}
			}
			if(true == enableDebugging)
			{	
				System.out.println("");
			}
			   Pairs p = new Pairs();
                        p.character = curCharacter;
                        p.states = curStates;
			characterAndStates.add(p);
                        
		}
			for(int j=0;j<stateDelimiters.size();j++)
			{
				if(true == enableDebugging)
				{
			System.out.println(stateDelimiters.elementAt(j));
				}
		}
		return characterAndStates;
	}
	
	/*
	public static void main(String args[])
	{
		String regexp = "[\w\s,]+.[(][\d][)]";
		String line = "Row is : ﻿Prendini, L. and Linder, H.P. 1998. Phylogeny of the South African species of restioid leafhoppers (Cicadellidae, Cephalelini). Entomologica scandinavica  29: 11–18.";
		


"
		return;
	}
      
	public static void main(String args[])
	{
		String data = "1. Colouration: Carapace, interocular surface, infuscation: entirely infuscated (0); partially infuscated (1); triangle lacking infuscation between lateral and median ocelli (2). Previous characters: FSB01/9–11 (part).";
		String[] tokens = data.split(":");
		System.out.println("tokens are "+tokens.length+" in number");
		
		for(int i=0;i<tokens.length;i++)
				System.out.println("Tokens is "+tokens[i]);
		
		if(data.matches(":"))	
				System.out.println("String contains delimter ");
		else 
			System.out.println("String does not contain delimter ");
	}
	
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
