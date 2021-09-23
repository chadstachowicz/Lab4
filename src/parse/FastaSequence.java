package parse;

import java.io.File;
import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FastaSequence {
	
	private String header;
	private String sequence;
	private int aCount;
	private int cCount;
	private int gCount;
	private int tCount;
	private String sequenceId;
	private Float gc_ratio;
	public static List<FastaSequence> readFastaFile(String filepath) throws Exception
	{
		List<FastaSequence> list = new ArrayList<FastaSequence>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filepath));
			String line = reader.readLine();
			String atcg = "";
			FastaSequence seq = new FastaSequence();
			boolean firstSeq = true;
			while (line != null) {
				if(line.indexOf('>') != -1)
				{
					if (firstSeq == false)
					{
						seq.sequence = atcg;
						seq.calcGC();
						atcg = "";
						list.add(seq);
					}
					firstSeq = false;
					seq = new FastaSequence();
					String newHeader = line.replace("> ","");
					String[] parts = newHeader.split(" ");
					seq.sequenceId =  parts[0];
					seq.header = newHeader;
				} else {
					atcg += line;
				}
				line = reader.readLine();
			}
			seq.sequence = atcg;
			seq.calcGC();
			atcg = "";
			list.add(seq);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	 
	}
	public static void writeTableSummary( List<FastaSequence> list, File outputFile) 
			throws Exception 
	{
		String str = "sequenceID\tnumA\tnumC\tnumG\tnumT\tsequence\n";
	    BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
	    writer.write(str);
		for( FastaSequence fs : list)
	     {
			String row = fs.sequenceId + '\t' + fs.aCount + '\t' + fs.cCount + '\t' + fs.gCount + '\t' + fs.tCount + '\t' + fs.sequence + '\n';
		    writer.write(row);
	     }
	    
	    writer.close();
	}
	public void calcGC()
	{
		int len = this.sequence.length();
		this.gCount = countChars('G');
		this.cCount = countChars('C');
		this.aCount = countChars('A');
		this.tCount = countChars('T');
		int gc = this.gCount + this.cCount;
		float rat = (float) gc/len;
		this.gc_ratio = rat;
	}
	public int countChars(char letter) {
		int totalChars = 0;
		for (int i = 0; i < this.sequence.length(); i++) {

        char temp = this.sequence.charAt(i);
        if (temp == letter)
            totalChars++;
		}
		return totalChars;
	}

	public String getHeader()
	{
		return this.header;
	}
	public String getSequence()
	{
		return this.sequence;
	}
	public String getGCRatio()
	{
		String ratio = this.gc_ratio.toString();
		return ratio;
	}
	public static void main(String[] args) throws Exception
	{
	     List<FastaSequence> fastaList = 
		FastaSequence.readFastaFile(
			"/Users/chadstachowicz/Desktop/sequences.txt");

	     for( FastaSequence fs : fastaList)
	     {
	         System.out.println(fs.getHeader());
	         System.out.println(fs.getSequence());
	         System.out.println(fs.getGCRatio());
	      }

	     File myFile = new File("/Users/chadstachowicz/Desktop/sequence-table.txt");

	     writeTableSummary( fastaList,  myFile);
	}

}
