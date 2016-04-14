package SyntacticAnalyzer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import LexicalAnylazer.LexicalAnalyzer;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SyntacticalAnalyzer sA=new SyntacticalAnalyzer();
		//sA.
		/*File dic = new File("testCases");
		String[] names=dic.list();
		for (int i=0;i<names.length;i++)
		{
			sA.addInput(names[i]);
			sA.derive(1);
			sA.derive(2);
		}*/
		BufferedReader bf=new BufferedReader(new InputStreamReader(System.in));
		String fileName = null;
		try {
			fileName=bf.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sA.addInput(fileName);
		sA.derive(1);
		sA.derive(2);
		
	}

}
