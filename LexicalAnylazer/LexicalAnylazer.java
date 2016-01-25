package LexicalAnylazer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class LexicalAnylazer {
	private Hashtable<ArrayList,Integer> table; //Transition Table
	private String r="";
	private int id;
	private int state;
	private Set<Character> chSet; //Set of valid Characters
	private Hashtable<Integer,String> fStateMap;
	private void addTrans(int cState,char ch,int nState)
	{
		ArrayList l=new ArrayList();
		l.add(cState);
		l.add(ch);
		table.put(l, nState);
		
	}
	
	private boolean valid(char c)
	{
		return chSet.contains(c);
	}
	
	public LexicalAnylazer()
	{
		
		chSet=new HashSet<Character>();
		/*Add valid Characters*/
		for (char c='a';c<='z';c++)
			chSet.add(c);
		for (char c='A';c<='Z';c++)
			chSet.add(c);
		for (char c='0';c<='9';c++)
			chSet.add(c);
		chSet.add('=');
		chSet.add('<');
		chSet.add('>');
		chSet.add('+');
		chSet.add('-');
		chSet.add('*');
		chSet.add('/');
		chSet.add(';');
		chSet.add(',');
		chSet.add('.');
		chSet.add('*');
		chSet.add('(');
		chSet.add(')');
		chSet.add('[');
		chSet.add(']');
		chSet.add('{');
		chSet.add('}');
		chSet.add(' ');
		chSet.add('\r');
		chSet.add('\n');
		
		
		
		table=new Hashtable<ArrayList,Integer>();
		this.addTrans(0, ' ', 0);
		this.addTrans(0, '\r', 0);
		this.addTrans(0, '\n', 0);
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
		
		state=0;
	}
	
	private int trans(int cState,char ch)
	{
		ArrayList l=new ArrayList();
		l.add(cState);
		l.add(ch);
		if (!table.contains(l))
			return -1;
		else
			return table.get(l);
		
	}
	
	private boolean finalState(int st)
	{
		return fStateMap.contains(st);
	}
	
	public String nextToken()
	{
		
		String s="";
		while (id!=r.length())
		{
			char c=r.charAt(id++);
			if (!valid(c)) //If invalid Character, skip it
			continue;
			state=trans(state,c);
			if (state==-1)
			{
				state=0;
				id--;
			}
			if (finalState(state))
			{
				;
			}
		}
	
		return s;
	}
	
}
