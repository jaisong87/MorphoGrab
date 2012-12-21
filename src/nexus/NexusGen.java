package nexus;

import java.util.Vector;

import learners.Pairs;
/*
 * Feed a Vector<String> of lines in order from a data matrix pdf
 * and get the corresponding nexus block  
 */
public class NexusGen {

	private Vector<String> inpData;
	private Vector<Pairs> chAndStates;
	public String title;
	
	public NexusGen(Vector<Pairs> vp, Vector<String> inp, String t) {
		chAndStates = vp;
		inpData = inp;
		title = t;
	}
	
	/*
	 * Get the nexus string out of the given rows of strings
	 * Find char labels and corresponding rows and also form 
	 * other nexus specific meta information
	 */
	public String getNexusStr() {

		if(inpData==null && chAndStates ==null)
			System.out.println("FATAL ERROR : No matrix and no character and states given to build nexus(both null)");

		Vector<String> chars = null;
		Vector<String> charRow = null;
		
		if(inpData != null && inpData.size()!=0)
			System.out.println("NexusGen : Matrix input with " + inpData.size() +" lines");
		if(chAndStates != null)
			System.out.println(" Character and states map with " + chAndStates.size() + " character entries");
		
		if(inpData != null) {
		charRow = new Vector<String>();
		chars = new Vector<String>();
		
		String curChar = "", curCharRow = "";
		for(int i=0;i<inpData.size();i++) {
			String curLine = inpData.get(i).trim();			

			if(isCharacterLine(curLine))
				{ 
				curChar = curLine;
				//System.out.println("Found a char");
				}
			else {
				
				curCharRow = curLine;
				chars.add(curChar);
				charRow.add(curCharRow);
				//System.out.println("Found a charRow");
				}
			}
		}

		String nexStr = "#NEXUS\n";
		nexStr += "BEGIN TAXA;\n";

		
		nexStr += "\tDIMENSIONS NTAX=" + chars.size() +";\n";
		nexStr += "\tTAXLABELS\n";
		
		for(int i=0;i<chars.size();i++)
			nexStr += "\t\t'" + chars.get(i) + "'\n";

		nexStr += ";\n";
		nexStr += "ENDBLOCK;\n\n\n\n";

		
		
		nexStr += "BEGIN CHARACTERS;\n";  
//		nexStr += "TITLE '"+ title + "'\n";
		
		if(chAndStates != null )		
			nexStr += "\tDIMENSIONS NCHAR="+ chAndStates.size()+";\n";
		else 
			nexStr += "\tDIMENSIONS NCHAR="+ chars.size()+";\n";
			
		/* HardCoding this line - talk to maureen and infer this appropriately ( should be simple to infer)*/
		nexStr += "\tFORMAT DATATYPE=STANDARD RESPECTCASE MISSING=? GAP=- SYMBOLS=\"01234\";\n\n";

		/* If characters and states are present inser char labels and state labels */
		if(chAndStates != null ){	
			nexStr +="\tCHARLABELS\n";
			
			int chCount = 0;
			for(int i=0;i<chAndStates.size();i++) {
				chCount++;
				nexStr +="\t\t["+chCount+"] '" + chAndStates.get(i).character+"'\n";
			}
			nexStr +="\t;\n\n";

			chCount = 0;
			nexStr += "\tSTATELABELS\n";

			for(int i=0;i<chAndStates.size();i++) {
				chCount++;
				nexStr += "\t\t" + chCount+"\n";
			
				Vector<String> curStates = chAndStates.get(i).states;
				for(int j=0;j<curStates.size();j++) {
					nexStr += "\t\t'" + curStates.get(j) + "'\n";
					if(chCount != chAndStates.size())
						nexStr += "\t\t,\n";
				}
			}
			nexStr +="\t;\n";
	}

		nexStr += "MATRIX\n";

		for(int i=0;i<chars.size();i++) 
			nexStr += "\t'" + chars.get(i)+ "'      " + charRow.get(i) + "\n";
		
		nexStr += "\t;\n";
		nexStr += "ENDBLOCK;\n\n";
		return nexStr;
	}

	/*
	 * Checks whether the given line belongs to a character label or 
	 * false means its a matrix row and not a char label line
	 */
	private boolean isCharacterLine(String curLine) {
		float alphaCount = 0;
		for(int i=0;i<curLine.length();i++)
			if( (curLine.charAt(i) >='a' && curLine.charAt(i) <= 'z') || 
					(curLine.charAt(i) >='A' && curLine.charAt(i) <= 'Z') )
				alphaCount++;

		//System.out.println(curLine + " => " + alphaCount + "/" + curLine.length());
		
		if(alphaCount >= (curLine.length()/2))
			return true;
		return false;
	}
	
	public static void main(String args[]) {
		
		Vector<String> v = new Vector<String>();
		
		v.add("Sinonyx");
		v.add("000001?0000000101110000012030000?00??????????0?01?0011?01000?010000101010000010300????0(123)?(123)(01)002?(12)?000 000???00");
		v.add("Mesonyx");
		v.add("0000000000010??01110000012040000?00???00?0010100?00011?????010(01)?00010?01?00003?(13)000???030110020(12)000000000?00");
		v.add("Pachyaena");
		v.add("00?000000000001?111??0001(12)04??????0???????????????????????????(01)?000100010000010(34)000000010200020(12)?00000000000");
		v.add("Susscrofa");
		v.add("1000010300001000100011300204111101100000-000110110001(12)?010-012-100--1110000000001000?00(34)0(01)(01)00(34)0(01)001000000000");
		v.add("Hippopotamus");
		v.add("000(12)112200001002102011300204?000001???10-??0?10110001(12)?0(01)-03?21100000220010(01)(01)1(01)3100???0(34)00??040(01)000000000(01)00");
		v.add("Ambulocetus");
		v.add("???????????11??01000????0?0(34)?1???1????????????????1?????(12)?????????0?????010111(01)??10????402110301?01???000000");
		v.add("Remingtonocetus");
		v.add("112(12)12?011311010010011(02)00121??20?1????1111?01111011002?130?1?1211?020120-02(23)(23)2(23)41?00100???0?0301???????????0");
		
		NexusGen b = new NexusGen(null, v, "SAMPLE NEXUS");	
		System.out.println(b.getNexusStr());
	}
}
