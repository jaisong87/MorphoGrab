
package learners;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author prasad
 */
public class Pairs {
		
            public String character;
            public Vector<String> states;
            
         public String getChar ()
         {
             return character;
         }
         
         public Vector<String> getStates()
         {
             return states;
         }

public static void main(String args[])
	{
	String chr = "115. Behavior:Mesosomal percussion";

String str2 = 
"1. Carapace: Lateral ocelli, number of pairs: more than three (0); three (1); two (2).\n" +
"2. Carapace: Median ocular tubercle: raised (0); shallow (1).\n" + 
"3. Carapace: Median notch: absent (0); shallow (1); strongly excavated (2).\n" + 
"4. Carapace: Median longitudinal furrow: broad, shallow, without suture (0); narrow, suturiform (1).\n"+
"113. Telson: Venom glands: complex (0); simple (1).\n" +
"114. Telson: Venom pigment: opalescent (0); reddish (1).\n" +
"115. Behavior: Mesosomal percussion: absent (0); present (1).\n";

int startpos = str2.indexOf(chr, 5);
System.out.print("Startpos is "+startpos);
	}
}
