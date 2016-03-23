package LexicalAnylazer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import Token.*;

public class LexicalAnalyzer {
	private Hashtable<ArrayList,Integer> table; //Transition Table
	private String r="";
	private String s;
	private int id;
	private int state;
	private int rowNum;
	private int colNum;
	private int startRow;
	private int startCol;
	private boolean com1;//Mark if in the "//" comment
	private boolean com2;//Mark if in the "/*" comment
	private Set<Character> chSet; //Set of valid Characters
	private Hashtable<Integer,String> fStateMap;
	private FileReader in;
	private FileWriter eout;//Out stream for error file
	private Set<String> keyWords;
	private int count;
	private void addTrans(int cState,char ch,int nState)
	{
		ArrayList l=new ArrayList();
		l.add(cState);
		l.add(ch);
		table.put(l, nState);
		
	}
	
	private char nextCh()
	{
		int r = 0;
		try {
			r=in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (char)r;
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
	
	public LexicalAnalyzer()
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
		chSet.add('\t');
		
		
		/*construct transition table*/
		table=new Hashtable<ArrayList,Integer>();
		this.addTrans(0, ' ', 0);
		this.addTrans(0, '\r', 0);
		this.addTrans(0, '\n', 0);
		this.addTrans(0, '\t', 0);
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
		this.addTrans(7, '.', 8);
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
		
		fStateMap.put(5, "integer");
		fStateMap.put(6, "integer");
		fStateMap.put(7, "integer");
		fStateMap.put(9, "double");
		fStateMap.put(10, "double");
		
		fStateMap.put(12, "punctuation");// token ";"
		fStateMap.put(13, "punctuation");// token ","
		fStateMap.put(14, "operator");// token "("
		fStateMap.put(15, "operator");// token ")"
		fStateMap.put(16, "operator");// token "{"
		fStateMap.put(17, "operator");// token "}"
		fStateMap.put(18, "operator");// token "["
		fStateMap.put(19, "operator");// token "]"
		fStateMap.put(20, "operator");// token "."
		fStateMap.put(21, "operator");// token "="
		fStateMap.put(22, "operator");// token "=="
		fStateMap.put(23, "operator");// token "<"
		fStateMap.put(24, "operator");// token "<>"
		fStateMap.put(25, "operator");// token "<="
		fStateMap.put(26, "operator");// token ">"
		fStateMap.put(27, "operator");// token ">="
		fStateMap.put(28, "operator");// token "+"
		fStateMap.put(29, "operator");// token "-"
		fStateMap.put(30, "operator");// token "*"
		fStateMap.put(31, "punctuation");// token "*/"
		fStateMap.put(32, "operator");// token "/"
		fStateMap.put(33, "punctuation");// token "/*"
		fStateMap.put(34, "punctuation");// token "//"
		
		/*add key words*/
		keyWords=new HashSet();
		keyWords.add("and");
		keyWords.add("not");
		keyWords.add("or");
		keyWords.add("if");
		keyWords.add("then");
		keyWords.add("else");
		keyWords.add("for");
		keyWords.add("class");
		keyWords.add("int");
		keyWords.add("float");
		keyWords.add("get");
		keyWords.add("put");
		keyWords.add("return");
		keyWords.add("program");
	
		state=0;
	}
	
	private boolean valid(char c)
	{
		return chSet.contains(c);
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
		count++;
		Token token=null;
		char c;
		while ((c=nextCh())!=(char)-1)
		{
			try {
				eout.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (c=='\t')
				colNum+=4;
			else
				colNum++;
			if (c=='\r'||c=='\n')
			{
				nextCh();//"/n/r"
				rowNum++;
				colNum=0;
			}
			if (!valid(c)) //If invalid Character or in comment, skip it
			{
				try {
					eout.write(rowNum+" "+colNum+": "+"Contain invalid Chracter "+c+'\n');
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			if (com1) //If in the "//" comment
			{
				if (c!='\n'&&c!='\r')
					continue;
				else
					com1=false;
			}
			
			if (com2) //If in the "/*" comment
			{
				char nc = ' ';
				if (c=='*'&&(nc=nextCh())!=-1)
				{
					colNum++;
					if (nc=='\r'||nc=='\n')
					{
						nextCh();//"/n/r"
						rowNum++;
						colNum=0;
					}
					if (nc=='/')
					{
						com2=false;
						return nextToken();
					}
				}
				
				continue;
			}
			
		
			int nstate=trans(state,getType(c));
			if (nstate==-1)
			{
				if (finalState(state))
				{
						
						token=addToken();
						
					
				}
				else
				{
					try {
						if (!s.equals(""))
						eout.write(startRow+" "+startCol+": "+"Incomplete token "+s+'\n');
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				state=0;
				s="";
				nstate=trans(state,getType(c));
				
			}
			if (state==0&&nstate!=0)
			{
				startRow=rowNum;
				startCol=colNum;
			}
			state=nstate;
			s=s+c;
			if (state==33)//If in "/*" comment
			{
				com2=true;
				state=0;
				token=new Token("/*","punctuation",startRow,startCol);
			}
			if (state==34)//If in "//" comment
			{	
				com1=true;
				state=0;
				token=new Token("//","punctuation",startRow,startCol);;
			}
			if (state==-1)
			{
				try {
					eout.write(rowNum+" "+colNum+": "+c+" can not start a token "+'\n');
					eout.flush();
				} catch (IOException e) {
						// TODO Auto-generated catch block
					e.printStackTrace();
				}
				state=0;
			}
			if (state==0)
				s="";
			if (token!=null)
			{
				if (token.value.equals("//")||token.value.equals("/*")||token.value.equals("*/"))
				{
					return nextToken();
				}
				else return token;
			}
		}
		if (finalState(state))
		{
			token=addToken();
			state=0;
		}
		if (com2)
			try {
				eout.write(rowNum+" "+colNum+": "+"No */ match /* "+'\n');
				eout.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if (state!=0)
		{
			try{	
				eout.write(startRow+" "+startCol+": "+"Incomplete token "+s+'\n');
				eout.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		if (token!=null)	
		if (token.value.equals("//")||token.value.equals("/*")||token.value.equals("*/"))
		{
			return nextToken();
		}
		else
		return token;
		else
			return null;
	
	}
	
	public void addInput(String fileName)
	{
		try {
			in=new FileReader(fileName);
			File f=new File(fileName);
			eout=new FileWriter("errors/"+f.getName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("no file");
			e.printStackTrace();
		}
		r=fileName;
		id=0;
		state=0;
		s="";
		com1=false;
		com2=false;
		rowNum=1;
		colNum=0;
		count=0;
	}
	
	private Token addToken()
	{
		String t=fStateMap.get(state);
		if (keyWords.contains(s))
		t="keyword";
		if (t.equals("double")&&Double.isInfinite(Double.parseDouble(s)))
		{
			
			try {
				eout.write(startRow+" "+startCol+": "+"Double overflow "+s+'\n');
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (t.equals("integer"))
		{
			try{
				Integer.parseInt(s);
				return new Token(s,t,startRow,startCol);
			}
			catch (Exception e){
				try {
					eout.write(startRow+" "+startCol+": "+"Int overflow "+s+'\n');
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}
		else
		return new Token(s,t,startRow,startCol);
		try {
			eout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void close()
	{
		//System.out.println(count);
		try {
			in.close();
			eout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
