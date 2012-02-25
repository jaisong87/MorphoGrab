/**
 * 
 */
package learners;
import java.util.Vector;
import java.util.HashMap;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


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
	boolean learningOver; /* Is learning Over - state variable */
	String characterExpr, stateExpr; /* Expression for character and states */
	Vector<String> dataRows; /* These are rows in the input document. We operate on this for information-extraction */
	boolean enableDebugging;
	
	String charStateDelimiter;
	
	public ReLearner(Vector<String> dataRows){
		/* Constructor - should take in a large chunk of Text */
		learningOver = false;
		characterExpr = "";
		stateExpr = "";
		enableDebugging = true;
		charStateDelimiter = new String();
	}
	
	public void resetStates(){
		learningOver = false;
		return;
	}
	
	public boolean addExample(String dataRow, int startCharPos, int endCharPos, Vector<Integer> startStatePos, Vector<Integer> endStatePos) /* Add an example for Character*/
	{
		System.out.println("ReLearner::addExample() Called");
		
	if(enableDebugging)
	{
		System.out.println("Example : "+dataRow);
		System.out.println("Character :"+dataRow.substring(startCharPos, endCharPos- startCharPos));
			
		charStateDelimiter = dataRow.substring( endCharPos, startStatePos.elementAt(0) );

		System.out.println("Char/State - Delimiter : "+charStateDelimiter);
		
		System.out.println("States : "+dataRow.substring(startStatePos.elementAt(0), endStatePos.elementAt(0) ) );
		for(int i=1;i<startStatePos.size();i++)
		{
			System.out.println(" , "+dataRow.substring(startStatePos.elementAt(i), endStatePos.elementAt(i) )  );			
		}
	}
	
	return false; /* Not Implemented*/	
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
		
		
	}
	
	/*
	 * This part is cool , Just extract Information
	 */
	public HashMap<String, Vector<String> > extractCharacterAndStates()
	{
		HashMap<String, Vector<String>> characterAndStates = new HashMap<String, Vector<String>>();
		
		return characterAndStates;
	}

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
		
		
		String line1 = "1. Pigmentation: †Carapace, tergites, pedipalps and metasoma (e.g., pedipalpal and metasomal carinae): pigmented/infuscated (0); not pigmented/infuscated (1).";	
		String line2 = "2. Chelicerae: Fixed finger, median and basal teeth: fused into a bicusp (conjoined on a ‘trunk’) (0); separate, not fused into a bicusp (not conjoined on a ‘trunk’) (1). [S&S01/9; S&F03.5/44]";
		String line3 = "3. Chelicerae: Fixed finger, number of teeth: four (subdistal present) (0); three (subdistal absent) (1).";
		String line4 = "4. Chelicerae: Movable finger, distal tooth alignment (internal distal and external distal teeth): opposable, internal distal tooth completely overlaps external distal tooth in dorsal view, U-shape in anterior aspect (0); not opposable, internal distal tooth does not overlap or at most partially overlaps external distal tooth in dorsal view, V-shape in anterior aspect (1). [L80/21; P00/11; S&S01/1, 6; S&F03.5/39]";
		String line5 = "5. Chelicerae: Movable finger, dorsal edge, number of subdistal teeth: two (0); one (1); none (2). [L80/10; S89/31, 32; P00/10; S&S01/3; S&F03.5/41]";

		dataRows.add(line1);
		dataRows.add(line2);
		dataRows.add(line3);
		dataRows.add(line4);
		dataRows.add(line5);
		
		
		ReLearner myLearner = new ReLearner(dataRows);
		//106, 130
		//132, 159
		Vector<Integer> startPos = new Vector<Integer>();
		Vector<Integer>   endPos = new Vector<Integer>();
		
		startPos.add(103); endPos.add(128);
		startPos.add(129); endPos.add(157);
		
		myLearner.addExample(line1, 0, 101, startPos, endPos);
	
		myLearner.learnExpressions();
		return;
	}
}




