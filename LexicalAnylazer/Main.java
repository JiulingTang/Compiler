package LexicalAnylazer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Token.*;


public class Main {
	public static void main(String[] args)
	{
		InputStreamReader in=null;
		try {
			in=new FileReader("a.txt");
			//in=new InputStreamReader(new InputStream(b));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("no file");
			e.printStackTrace();
		}
		int c;
		LexicalAnylazer azer=new LexicalAnylazer();
		int k=0;
		String s="";
		try {
			while ((c=in.read())!=-1)
			{
				s=s+(char)c;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(s);
		azer.addInput(s);
		while (true)
		{
			Token token=azer.nextToken();
			if (token==null)
				break;
			else
				System.out.println(token);
		}
		System.out.println("end");
	}

}
