package LexicalAnylazer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;
import Token.*;

public class LexicalAnylazer {
	private Hashtable<ArrayList,Integer> table; //Transition Table
	private String r="";
	private String s;
	private int id;
	private int state;
	private boolean com1;//Mark if in the "/*" comment
	private boolean com2;//Mark if in the "//" comment
	private Set<Character> chSet; //Set of valid Characters
	private Hashtable<Integer,String> fStateMap;
	private void addTrans(int cState,char ch,int nState)
	{
		ArrayList l=new ArrayList();
		l.add(cState);
		l.add(ch);
		table.put(l, nState);
		
	}
	
	private char getType(char c)
	{
		if('a'<=c&&c<='z')
		return 'a';
		if ('A'<=c&&c<='Z')
		return 'a';
		if ('1'<=c&&c<='9')
		return '1';
		return c;
		
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
		chSet.add('_');
		
		
		/*construct transition table*/
		table=new Hashtable<ArrayList,Integer>();
		this.addTrans(0, ' ', 0);
		this.addTrans(0, '\r', 0);
		this.addTrans(0, '\n', 0);
		/*The transition for letter*/
		this.addTrans(0, 'a', 1);
		this.addTrans(1, 'a', 2);
		this.addTrans(1, '0', 3);
		this.addTrans(1, '1', 3);
		this.addTrans(1, '_', 4);
		this.addTrans(2, 'a', 2);
		this.addTrans(2, '0', 3);
		this.addTrans(2, '1', 3);
		this.addTrans(2, '_', 4);
		this.addTrans(3, 'a', 2);
		this.addTrans(3, '0', 2);
		this.addTrans(3, '1', 2);
		this.addTrans(3, '_', 4);
		this.addTrans(4, 'a', 2);
		this.addTrans(4, '0', 3);
		this.addTrans(4, '1', 3);
		this.addTrans(4, '_', 4);
		
		/*The transition for number*/
		this.addTrans(0, '0', 5);
		this.addTrans(0, '1', 6);
		this.addTrans(5, '.', 8);
		this.addTrans(6, '.', 8);
		this.addTrans(6, '0', 7);
		this.addTrans(6, '1', 7);
		this.addTrans(7, '0', 7);
		this.addTrans(7, '1', 7);
		this.addTrans(8, '0', 9);
		this.addTrans(8, '1', 9);
		this.addTrans(9, '0', 11);
		this.addTrans(9, '1', 10);
		this.addTrans(10, '0', 11);
		this.addTrans(10, '1', 10);
		this.addTrans(11, '0', 11);
		this.addTrans(11, '1', 10);
		
		this.addTrans(0, ';', 12);
		this.addTrans(0, ',', 13);
		this.addTrans(0, '(', 14);
		this.addTrans(0, ')', 15);
		this.addTrans(0, '{', 16);
		this.addTrans(0, '}', 17);
		this.addTrans(0, '[', 18);
		this.addTrans(0, ']', 19);
		this.addTrans(0, '.', 20);
				
		this.addTrans(0, '=', 21);
		this.addTrans(21, '=', 22);
		this.addTrans(0, '<', 23);
		this.addTrans(23,'>', 24);
		this.addTrans(23, '=', 25);
		this.addTrans(0, '>', 26);
		this.addTrans(26, '=', 27);
		this.addTrans(0, '+', 28);
		this.addTrans(0, '-', 29);
		this.addTrans(0, '*', 30);
		//this.addTrans(30, '/', 31);
		this.addTrans(0, '/', 32);
		this.addTrans(32, '*', 33);
		this.addTrans(32, '/', 34);
		
		/*add final state*/
		fStateMap=new Hashtable<Integer,String>();
		fStateMap.put(1, "id"); 
		fStateMap.put(2, "id");
		fStateMap.put(3, "id");
		fStateMap.put(4, "id");
		
		fStateMap.put(5, "num");
		fStateMap.put(6, "num");
		fStateMap.put(7, "num");
		fStateMap.put(9, "num");
		fStateMap.put(10, "num");
		
		fStateMap.put(12, "punctuation");// token ";"
		fStateMap.put(13, "punctuation");// token ","
		fStateMap.put(14, "operator");// token "("
		fStateMap.put(15, "operatpr");// token ")"
		fStateMap.put(16, "operator");// token "{"
		fStateMap.put(17, "operatpr");// token "}"
		fStateMap.put(18, "operator");// token "["
		fStateMap.put(19, "operator");// token "]"
		fStateMap.put(20, "operator");// token "."
		fStateMap.put(21, "operator");// token "="
		fStateMap.put(22, "operator");// token "=="
		fStateMap.put(23, "operator");// token "<"
		fStateMap.put(24, "operatpr");// token ¡±<>"
		fStateMap.put(25, "operator");// token "<="
		fStateMap.put(26, "operator");// token ">"
		fStateMap.put(27, "operator");// token ">="
		fStateMap.put(28, "operator");// token "+"
		fStateMap.put(29, "operator");// token "-"
		fStateMap.put(30, "operator");// token "*"
		fStateMap.put(31, "punctuation");// token "*/"
		fStateMap.put(32, "operator");// token "/"
		fStateMap.put(33, "punctuation");// token "/*¡±
		fStateMap.put(34, "punctuation");// token "//"
		
		/*add state  which need backtracking*/
	
		state=0;
	}
	
	private int trans(int cState,char ch)
	{
		ArrayList l=new ArrayList();
		l.add(cState);
		l.add(ch);
		//System.out.println(table.get(l));
		if (!table.containsKey(l))
			return -1;
		else
			return table.get(l);
		
	}
	
	private boolean finalState(int st)
	{
		return fStateMap.containsKey(st);
	}
	
	
	private boolean tryCh(int st,char t)
	{
		ArrayList l=new ArrayList();
		l.add(st);
		l.add(t);
		return table.containsKey(l);
	}
	
	public Token nextToken()
	{
		
		Token token=null;
		while (id!=r.length())
		{
			char c=r.charAt(id++);
			if (!valid(c)) //If invalid Character or in comment, skip it
			continue;
			if (com1) //If in the "//" comment
			{
				if (c!='\n'&&c!='\r')
					continue;
				else
					com1=false;
			}
			
			if (com2) //If in the "/*" comment
			{
				if (c=='*'&&id!=r.length()&&r.charAt(id++)=='/')
				{
					com2=false;
					return new Token("*/","punctuation",1,1);
				}
				else
				continue;
			}
			//System.out.println(c);
			//System.out.println(state);
			//System.out.println(s);
		
			int nstate=trans(state,getType(c));
			if (nstate==-1)
			{
				if (finalState(state))
				{
					
						token=new Token(s,fStateMap.get(state),0,0);
						
					
				}
				else
				{
					
				}
				state=0;
				s="";
				nstate=trans(state,getType(c));
				
			}
			state=nstate;
			s=s+c;
			if (state==33)//If in "/*" comment
			{
				com2=true;
				state=0;
				token=new Token("/*","punctuation",1,1);
			}
			if (state==34)//If in "//" comment
			{	
				com1=true;
				state=0;
				token=new Token("//","punctuation",1,1);;
			}
			if (state==0)
				s="";
			if (token!=null)
				return token;
		}
		if (finalState(state))
		{
			state=0;
			return new Token(s,fStateMap.get(state),0,0);
		}
		else 
		{
			return null;
		}
	
	}
	
	public void addInput(String input)
	{
		r=input;
		id=0;
		state=0;
		s="";
		com1=false;
		com2=false;
	}
}
