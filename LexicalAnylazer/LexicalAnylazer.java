package LexicalAnylazer;

import java.util.ArrayList;
import java.util.Hashtable;

public class LexicalAnylazer {
	private Hashtable<ArrayList,Integer> table; //Transition Table
	private void addTrans(int a,char b,int c)
	{
		ArrayList l=new ArrayList();
		/*The transition for letter*/
		this.addTrans(0, 'a', 1);
		this.addTrans(1, 'a', 2);
		this.addTrans(1, '0', 3);
		this.addTrans(1, '1', 3);
		this.addTrans(2, 'a', 2);
		this.addTrans(2, '0', 3);
		this.addTrans(2, '1', 3);
		this.addTrans(3, 'a', 2);
		this.addTrans(3, '0', 2);
		this.addTrans(3, '1', 2);
		
	}
	public LexicalAnylazer()
	{
		table=new Hashtable<ArrayList,Integer>();
		ArrayList l=new ArrayList();
	}
	
	public String nextToken()
	{
		
		state=trans(state,)
	}
	
}
