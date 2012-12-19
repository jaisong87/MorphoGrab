package preprocessing;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LineBreakGlue {

	String startOfLine;
	
	public LineBreakGlue(String stLine){
	startOfLine = stLine;
	}
	
	public Vector<String> getGluedStream(Vector<String> inpStream) {
		Vector<String> outStream = new Vector<String>();
		String curInput = "";
		int totLines = 0;
		for(int i=0;i<inpStream.size();i++) {
			String curLine = inpStream.get(i);
			if(curLine.matches(startOfLine)) {
				System.out.println("+"+curLine);
				if(curInput!=""){
					outStream.add(curInput);
					totLines++;
				}
				curInput = curLine;
			}
			else {
				System.out.println("-"+curLine);
				curInput+=curLine;
			}
		}
		
		if(curInput!=""){
			outStream.add(curInput);
			totLines++;
		}

		System.out.println(" ========== There are "+totLines+" total lines ========== ");
		return outStream;
	}
}
