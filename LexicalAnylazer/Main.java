package LexicalAnylazer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import Token.Token;


public class Main {
	public static void main(String[] args)
	{
		FileReader in=null;
		FileWriter out=null;
		File dic = new File("testCases");
		String[] names=dic.list();
		for (int i=0;i<names.length;i++)
		{
		try {
			out=new FileWriter("outputs/"+names[i]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LexicalAnalyzer azer=new LexicalAnalyzer();
		azer.addInput("testCases/"+names[i]);
		while (true)
		{
			Token token=azer.nextToken();
			if (token==null)
				break;
			else
				try {
					out.write(token.toString()+"\r\n");
					out.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		}
	}

}
