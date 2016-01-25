package LexicalAnylazer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;


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
		try {
			while ((c=in.read())!=-1)
			{
				System.out.print(k++);
				System.out.print((char)c);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
