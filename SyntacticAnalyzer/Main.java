package SyntacticAnalyzer;

import java.io.File;
import java.io.FileWriter;

import LexicalAnylazer.LexicalAnalyzer;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SyntacticalAnalyzer sA=new SyntacticalAnalyzer();
		//sA.
		File dic = new File("testCases");
		String[] names=dic.list();
		for (int i=0;i<names.length;i++)
		{
			sA.addInput(names[i]);
			sA.derive(1);
			//sA.derive(2);
		}
		
		
	}

}
