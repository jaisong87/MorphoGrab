package learners;

public class StrUtils {
	public String charStateDelimiter = "";
	
	public static int getIndex(String str, String substr, int start)
	{
		int L= str.length();
		int l = substr.length();
		
		for(int i=start;i<L-l;i++)
		{
			boolean match = true;
			for(int j=0;j<l&&match==true;j++)
			{
				if(substr.charAt(j) !=str.charAt(i+j))
						match = false;
			}
			if(match==true)
				return i;
		}
		
		return -1;
	}

	public boolean addCharacterExample(String line, int startCharPos, int endCharPos)
    {
    	charStateDelimiter = "";
    	int nwCount =0;
    	for(int pos= endCharPos; (pos<line.length()-1 )&& nwCount==0;pos++ )
    		{
    		String curCh = ""+line.charAt(pos);
			if(curCh.matches(".*\\w.*"))
    			{
    			nwCount++;	
    			}
			else{
				charStateDelimiter +=curCh;
		    	}
			}
    	System.out.println("CharStateDelimiter learned is "+charStateDelimiter+" from "+line.substring(startCharPos,endCharPos));
    	return true;
    }


	public static void main(String args[])
	{
		StrUtils u1 = new StrUtils();
		//u1.addCharacterExample("19. Egg sac development{} brood pouch absent (0);",3,23);
		 String line = "  'Aristotle        Aristotle ( , Aristot\\xc3\\xa9l\\xc4\\x93s) (384 BC\\xc2\\xa0\\xe2\\x80\\x93 322 BC)  was a Greek philosopher and polymath,'";//value.toString();
         line = line.replaceAll("[^a-zA-Z0-9]+"," ");
         line = line.toLowerCase();
         System.out.println(line);

	}
}
