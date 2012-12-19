package learners;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Class that would take a string as an argument in the constructor and 
 * tries to generalize a regular expression.
 */
public class regexTemplate {
		static String specialChars = "()?;-+"; /* Some special characters */
                		
                static String charStateDelimiters = ":";
		Vector<Integer> charType;
		Vector<Boolean> repeatAllowed;
	
	public regexTemplate(String str)
	{
		charType = new Vector<Integer>();
		repeatAllowed = new Vector<Boolean>();
		
		int curChType = getCharType(str.charAt(0));
		int prevChType = -1;
		int lastKnownIndex = -1;
		boolean repeatSeen = false;
		
		//System.out.println("Making Template for : "+str);
		//printScheme();
		
		for(int i=1;i<str.length();i++)
		{
			prevChType = curChType;
			curChType = getCharType(str.charAt(i));
			
			if((curChType == prevChType)&&(curChType != specialChars.length()+2))
			{
				repeatSeen = true;
			}
			else {
				lastKnownIndex = i;
				//System.out.println("Adding "+prevChType+" ending at position "+i);
				charType.addElement(prevChType);
				repeatAllowed.addElement(repeatSeen);
				repeatSeen = false;
			}
		}
		
		//System.out.println(lastKnownIndex+" : "+str.length()+" : "+curChType+" : "+prevChType);
		
			charType.addElement(curChType);
			repeatAllowed.addElement(repeatSeen);
		
			/*
		System.out.println("");
	
		for(int i=0;i<charType.size();i++)
			System.out.print(charType.elementAt(i)+" , "); 
		System.out.println("");
		*/
	}
	
	private void printScheme()
	{
		int i=0;
		for(i=0;i<specialChars.length();i++)
			System.out.print("<"+i+","+specialChars.charAt(i)+"> ");
		System.out.print("<d,"+i+"> "); i++;
		System.out.print("<w,"+i+"> "); i++;
		System.out.print("<.,"+i+"> \n"); i++;		
	}
	
	private int getCharType(char ch)
		{
		
		int ctype = -1;
		
		int i=0;
	
		for(i=0;i<specialChars.length();i++)
			if(specialChars.charAt(i) == ch)
					ctype = i;
		
		if(ch>='0' && ch <='9')
			ctype = i;
		
		else if( (ch>='a'&&ch<='z')||(ch>='A'&&ch<='Z'))
			ctype = i+1;

		else if ( ch==' ' || ch==',')
			ctype = i+1;
		
		else if ( ch == ':')
			ctype = i+3;
		else if(ctype == -1){ 
			ctype = i+2; /* Other characters */
		}
		
		//System.out.print(ctype);
		
		return ctype;
		}

	public String getRegex()
	{
		//String tmp = "[\\w]*.[\\w]*.[(][\\d]*[)]";
		String pattern ="";
		int specialCharCount = specialChars.length();
		
		for(int i=0;i<charType.size();i++)
		{
			int ctype = charType.elementAt(i);
			if(ctype < specialCharCount)
			{
				pattern+="["+specialChars.charAt(ctype)+"]";
				//pattern+=specialChars.charAt(ctype);
				if(repeatAllowed.elementAt(i)==true) pattern+="+";
			}
			else if(ctype == specialCharCount )
			{
				pattern+="[\\d]";
				if(repeatAllowed.elementAt(i)==true)  {
												pattern+="+";
														}
			}
			else if(ctype== specialCharCount+1)
			{
				if(i==0)
					pattern+="[\\w\\s]";
				pattern+="[\\w\\s,]";
				if(repeatAllowed.elementAt(i)==true) pattern+="+";
			}
			else if(ctype == specialCharCount+2)
			{
				if(i>0)
					pattern+="."; /* This should be changed to a custom character */
			}
			else if(ctype == specialCharCount+3)
			{	
				if(i>0)
					pattern+=":";
			}
		}
		return pattern;
	}
};