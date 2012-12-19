package preprocessing;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Vector;

/**
 *
 * @author prasad
 */
public class PdfParser {

      /** The resulting PDF. */
    public static final String PDF = "/home/jaison/CSE523/inputGen/Gauthier.pdf";//C:/Study/523/Cont/Gauthier.pdf";
    
    public static final String PDF1 = "C:/Study/523/Cont/stylometric.pdf";
    /** A possible resulting after parsing the PDF. */
    public static final String TEXT1 = "C:/Study/523/Cont/result1.txt";
    /** A possible resulting after parsing the PDF. */
    public static final String TEXT2 = "/home/jaison/CSE523/inputGen/Gauthier.txt";
    /** A possible resulting after parsing the PDF. */
    public static final String TEXT3 = "C:/Study/523/Cont/result3.txt";
 
    PdfReader reader ;
    public PdfParser(String pdf) throws IOException
    {
       reader = new PdfReader(pdf);
    }
 
    /**
     * Parses the PDF using PRTokeniser
     * @param src  the path to the original PDF file
     * @param dest the path to the resulting text file
     * @throws IOException
     */
    public void parsePdf(String src, String dest) throws IOException {
        PdfReader reader = new PdfReader(src);
        // we can inspect the syntax of the imported page
        PrintWriter out = new PrintWriter(new FileOutputStream(dest));
        
 //       for(int i= 1 ; i<=reader.getNumberOfPages() ;i++)
        {
        byte[] streamBytes = reader.getPageContent(77);
        PRTokeniser tokenizer = new PRTokeniser(streamBytes);
        while (tokenizer.nextToken()) {
            if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING)
            {
                
                System.out.println(tokenizer.getStringValue());
            }
        }
        }
        out.flush();
        out.close();
    }
 
     public void parsePdf3(String pdf, String txt) throws IOException {
        PdfReader reader = new PdfReader(pdf);
        
        byte[] data = reader.getPageContent(75);
        String str = new String(data, Charset.defaultCharset());
        
        //System.out.println(str);
        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        RenderFilter filter = new RegionTextRenderFilter(reader.getPageSize(75));;
        TextExtractionStrategy strategy;
        //for (int i = 1; i <= reader.getNumberOfPages(); i++)
        {
            strategy = new FilteredTextRenderListener(new SimpleTextExtractionStrategy(), filter);
            //out.println(PdfTextExtractor.getTextFromPage(reader, 76, strategy));
            out.println(PdfTextExtractor.getTextFromPage(reader, 76));
        }
        out.flush();
        out.close();
    }
     
    public int getNoOfPages() throws IOException
    {
        return reader.getNumberOfPages();
    }
    
    public Vector<String> parsePdf2(String pdf, String txt ,int startpage, int endPage) throws IOException {
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        PrintWriter out = new PrintWriter(new FileOutputStream(txt));
        TextExtractionStrategy strategy;
        
        Vector<String> pdfParsedOutput = new Vector<String>(); 
        
        for (int i = startpage; i <= reader.getNumberOfPages()&& i<=endPage; i++)
        {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
//            System.out.println("======= Page "+i+" ==============");
  //          System.out.println(strategy.getResultantText());  
//            pdfParsedOutput.add(strategy.getResultantText());
            String[] contentLines = strategy.getResultantText().split("\n");
            for(int j=0;j<contentLines.length;j++)
            	{
            		String line ="";
            		for(int k=0;k<contentLines[j].length()&&contentLines[j].charAt(k) !='\n';k++)
            			line+=contentLines[j].charAt(k);
            		
            		pdfParsedOutput.add(line);
            	}
            }
        out.flush();
        out.close();
        return pdfParsedOutput;
    }
    
    private String parsedText="" ;
    
    public String getParsedText()
    {
        return parsedText;
    }
    
    public Vector<String> parsePdf(int startpage, int endPage) throws IOException {
        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
        TextExtractionStrategy strategy;
        
        Vector<String> pdfParsedOutput = new Vector<String>(); 
        
        for (int i = startpage; i <= reader.getNumberOfPages()&& i<=endPage; i++)
        {
            strategy = parser.processContent(i, new SimpleTextExtractionStrategy());
//            System.out.println("======= Page "+i+" ==============");
  //          System.out.println(strategy.getResultantText());  
//            pdfParsedOutput.add(strategy.getResultantText());
            String[] contentLines = strategy.getResultantText().split("\n");
            parsedText+=strategy.getResultantText();
            for(int j=0;j<contentLines.length;j++)
            	{
            		String line ="";
            		for(int k=0;k<contentLines[j].length()&&contentLines[j].charAt(k) !='\n';k++)
            			line+=contentLines[j].charAt(k);
            		
            		pdfParsedOutput.add(line);
                        
            	}
            }
        return pdfParsedOutput;
    }
    
 static void vectorDump(Vector<String> vs) {
	 for(int i=0;i<vs.size();i++)
		 System.out.println(vs.get(i));
 }
    
    /**
     * Main method.
     * @param    args    no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
  /*
    public static void main(String[] args) throws DocumentException, IOException {
        PdfParser example = new PdfParser();
        Vector<String> parsedPdf = example.parsePdf2(PDF, TEXT2,77,87);
 
        Vector<String> knownNoise = new Vector<String>();
        NoiseHandler noiseFilter = new NoiseHandler(3, knownNoise);
        
        Vector<String> noiseFreePdf = noiseFilter.deNoisify(parsedPdf);
        vectorDump(noiseFreePdf);
        
        LineBreakGlue lbGlue = new LineBreakGlue("^[0-9]+.*");
        System.out.println("noiseFreePdf has size "+noiseFreePdf.size());
        Vector<String> inputStream = lbGlue.getGluedStream(noiseFreePdf);
        System.out.println("InoutStream has size "+inputStream.size());
        System.out.println("===========Final noise free glued stream ========== ");
        vectorDump(inputStream);
        //example.parsePdf2(PDF1, TEXT3,2);
        //example.extractText(PDF, TEXT3);
    }
    * 
    */
}