package SyntacticAnalyzer;

import CodeGenerater.CodeGenerater;
import LexicalAnylazer.*;
import Semantic.Semantic;
import SemanticStructrue.Record;
import SemanticStructrue.Var;
import Token.Token;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class SyntacticalAnalyzer {
	private Hashtable<String,Hashtable<String,String>> table;
	private Hashtable<String,Integer> map;
	private HashSet<Integer> ts;
	private int[][][] tTable;
	private LexicalAnalyzer lA;
	private HashSet<Integer>[] follow;
	private ArrayList<String> nt;
	private PrintStream eout;
	private PrintStream error;
	public int n=89;
	public int count;
	public Semantic sem;
	
	ArrayList <Token> ms;
	public SyntacticalAnalyzer()
	{
		lA=new LexicalAnalyzer();
		ms=new ArrayList<Token>();
		sem=new Semantic();
		table=new Hashtable<String,Hashtable<String,String>>();
		nt=new ArrayList<String>(); 
		nt.add("prog");//1
		nt.add("classdecl");//2
		nt.add("progbody");//3
		nt.add("funchead");//4
		nt.add("funcdef");//5
		nt.add("funcbody");//6
		nt.add("vardecl");//7
		nt.add("statement");//8
		nt.add("assignstat");//9
		nt.add("statblock");//10
		nt.add("expr");//11
		nt.add("relexpr");//12
		nt.add("arithexpr");//13
		nt.add("sign");//14
		nt.add("term");//15
		nt.add("factor");//16
		nt.add("variable");//17
		nt.add("idnest");//18
		nt.add("indice");//19
		nt.add("arraysize");//20
		nt.add("type");//21
		nt.add("fparams");//22
		nt.add("aparams");//23
		nt.add("fparastail");//24
		nt.add("aparastail");//25
		nt.add("assignop");//26
		nt.add("relop");//27
		nt.add("addop");//28
		nt.add("multop");//29
		nt.add("and");//30
		nt.add("not");//31
		nt.add("or");//32
		nt.add("if");//33
		nt.add("then");//34
		nt.add("else");//35
		nt.add("for");//36
		nt.add("class");//37
		nt.add("int");//38
		nt.add("float");//39
		nt.add("get");//40
		nt.add("put");//41
		nt.add("return");//42
		nt.add("program");//43
		nt.add("-");//44
		nt.add("(");//45
		nt.add(")");//46
		nt.add("*");//47
		nt.add(",");//48
		nt.add(".");//49
		nt.add("/");//50
		nt.add(";");//51
		nt.add("[");//52
		nt.add("]");//53
		nt.add("{");//54
		nt.add("}");//55
		nt.add("+");//56
		nt.add("<");//57
		nt.add("<=");//58
		nt.add("<>");//59
		nt.add("=");//60
		nt.add("==");//61
		nt.add(">");//62
		nt.add(">=");//63
		nt.add("id");//64
		nt.add("integer");//65
		nt.add("double");//66
		nt.add("$");//67
		nt.add("EP");//68
		for (char c='A';c<='U';c++)
			nt.add(Character.toString(c));
		
		map=new Hashtable<String,Integer>();
		for (int i=0;i<nt.size();i++)
		{
			map.put(nt.get(i), i+1);
		}
		
		tTable=new int[n+1][][];
		for (int i=1;i<=n;i++)
		{
			tTable[i]=new int[n+1][];
		}
		
		tTable[1][43]=tTable[1][37]=new int[]{69,3};
		tTable[2][37]=new int[]{37,64,252,54,82,55,283,253,51};
		tTable[3][43]=new int[]{43,254,6,51,71};
		tTable[4][38]=tTable[4][39]=tTable[4][64]=new int[]{21,256,64,255,45,22,46};
		tTable[5][38]=tTable[5][39]=tTable[5][64]=new int[]{4,6,51};
		tTable[6][54]=new int[]{54,83,55,253};
		tTable[7][38]=tTable[7][39]=tTable[7][64]=new int[]{21,256,64,257,73,259,51};
		
		tTable[8][64]=new int[]{9,51};
		tTable[8][42]=new int[]{42,45,11,282,46,267,51};
		tTable[8][41]=new int[]{41,45,11,287,46,267,51};
		tTable[8][40]=new int[]{40,45,17,286,46,267,51};
		tTable[8][33]=new int[]{33,45,11,267,46,34,10,35,10,51};
		tTable[8][36]=new int[]{36,269,45,21,256,64,257,260,268,272,266,26,11,278,267,51,12,267,51,9,46,10,253,51};
		
		tTable[9][64]=new int[]{17,26,11,278,267};
		tTable[10][68]=new int[]{};
		tTable[10][36]=tTable[10][33]=tTable[10][40]=tTable[10][41]=tTable[10][42]=tTable[10][64]=new int[]{8};
		tTable[10][54]=new int[]{54,72,55};
		tTable[11][45]=tTable[11][65]=tTable[11][66]=tTable[11][31]=tTable[11][56]=tTable[11][44]=tTable[11][64]=new int[]{13,75};//expr->arithexpr G
		tTable[12][45]=tTable[12][65]=tTable[12][66]=tTable[12][31]=tTable[12][56]=tTable[12][44]=tTable[12][64]=new int[]{13,27,13,275};		
		tTable[13][45]=tTable[13][65]=tTable[13][66]=tTable[13][31]=tTable[13][56]=tTable[13][44]=tTable[13][64]=new int[]{15,74};
		tTable[14][44]=new int[]{44};
		tTable[14][56]=new int[]{56};
		
		tTable[16][44]=tTable[16][56]=new int[]{14,268,16,279};//factor -> sign factor
		tTable[16][64]=new int[]{85};//factor->Q
		tTable[16][31]=new int[]{31,268,16,274};//factor->not factor
		tTable[16][65]=new int[]{65,273};//factor->integer
		tTable[16][66]=new int[]{66,273};//factor->double
		tTable[16][45]=new int[]{45,13,46};//factor->( arithexpr )
		
		tTable[17][64]=new int[]{64,268,272,78,266,86};
		tTable[18][64]=new int[]{64,78};
		
		tTable[20][52]=new int[]{52,65,258,53};
		tTable[21][64]=new int[]{64};
		tTable[21][38]=new int[]{38};
		tTable[21][39]=new int[]{39};
		tTable[22][68]=new int[]{};
		tTable[22][38]=tTable[22][39]=tTable[22][64]=new int[]{21,256,64,257,73,259,79};
		
		tTable[24][48]=new int[]{48,21,256,64,257,73,259};
		tTable[25][48]=new int[]{48,11,281};
		tTable[26][60]=new int[]{60,268};
		
		for (int i=57;i<=63;i++)
			if (i!=60)
				tTable[27][i]=new int[]{i};
		
		for (int i=69;i<=84;i++)
			tTable[i][68]=new int[]{};
		tTable[86][68]=new int[]{};
		tTable[69][37]=new int[]{2,69};
		tTable[70][38]=tTable[70][39]=tTable[70][64]=new int[]{7,70};
		tTable[71][38]=tTable[71][39]=tTable[71][64]=new int[]{5,71};
		tTable[72][40]=tTable[72][41]=tTable[72][64]=tTable[72][33]=tTable[72][36]=tTable[72][42]=new int[]{8,72};
		tTable[73][52]=new int[]{20,73};
		
		tTable[77][64]=new int[]{19,77};
		tTable[79][48]=new int[]{24,79};
		tTable[82][38]=tTable[82][39]=tTable[82][64]=new int[]{21,256,64,87};
		
		tTable[83][64]=new int[]{64,261,89};
		tTable[83][38]=new int[]{38,256,64,257,73,260,51,83};
		tTable[83][39]=new int[]{39,256,64,257,73,260,51,83};
		tTable[83][40]=tTable[83][41]=tTable[83][33]=tTable[83][36]=tTable[83][42]=new int[]{88,72};
		
		
		tTable[85][64]=new int[]{64,268,81}; //Q->id M
		tTable[86][49]=new int[]{49,270,17};
		tTable[87][45]=new int[]{255,45,22,46,6,51,71};		
		tTable[87][51]=tTable[87][52]=new int[]{257,73,260,51,82};
		
		tTable[88][42]=new int[]{42,45,11,282,46,267,51};
		tTable[88][41]=new int[]{41,45,11,287,46,267,51};
		tTable[88][40]=new int[]{40,45,17,286,46,267,51};
		tTable[88][33]=new int[]{33,45,11,267,46,34,10,35,10,51};
		tTable[88][36]=new int[]{36,264,45,21,256,64,257,260,268,272,266,26,11,278,267,51,12,267,51,9,46,10,253,51};
		
		tTable[89][64]=new int[]{64,262,73,260,51,83};
		tTable[89][49]=tTable[89][52]=tTable[89][60]=new int[]{263,272,78,266,86,26,11,278,267,51,72};
		
		tTable[81][52]=tTable[81][68]=tTable[81][49]=new int[]{272,78,266,84};//M-> J P
		tTable[81][45]=new int[]{271,45,23,46,265};//M-> ( aparams )
		
		tTable[78][52]=new int[]{19,78};//J -> indice J
		tTable[78][68]=new int[]{};//J -> EPSILON
		
		tTable[19][52]=new int[]{52,13,280,53};//indice -> [ arithexpr ]
		
		tTable[13][45]=tTable[13][65]=tTable[13][66]=tTable[13][31]=tTable[13][64]=tTable[13][56]=tTable[13][44]=new int[]{15,74};//arithexpr->term F
		
		tTable[15][45]=tTable[15][65]=tTable[15][66]=tTable[15][31]=tTable[15][64]=tTable[15][56]=tTable[15][44]=new int[]{16,76};//term-> factor H
		
		tTable[76][68]=new int[]{};//H->EPSILON
		tTable[76][47]=tTable[76][50]=tTable[76][30]=new int[]{29,16,277,76};//H->maltop factor H
		
		tTable[29][47]=new int[]{47,268};//maltop->*
		tTable[29][50]=new int[]{50,268};//maltop->/
		tTable[29][30]=new int[]{30,268};//maltop->and
		
		tTable[74][68]=new int[]{};
		tTable[74][56]=tTable[74][44]=tTable[74][32]=new int[]{28,15,276,74};//F->addop term F
		
		tTable[28][56]=new int[]{56,268};
		tTable[28][44]=new int[]{44,268};
		tTable[28][32]=new int[]{32,268};
		
		tTable[84][68]=new int[]{};//P->EPSILON
		tTable[84][49]=new int[]{49,270,85};//P-> . Q
		
		tTable[23][68]=new int[]{};//aparams->EPSILON
		tTable[23][45]=tTable[23][65]=tTable[23][66]=tTable[23][31]=tTable[23][56]=tTable[23][44]=tTable[23][64]=new int[]{11,281,80};//aparams -> expr L 
		
		tTable[80][68]=new int[]{};//L-> EPSILON
		tTable[80][48]=new int[]{25,80};//L -> aparamtail L
		
		

		tTable[75][68]=new int[]{};
		tTable[75][57]=tTable[75][58]=tTable[75][59]=tTable[75][61]=tTable[75][62]=tTable[75][63]=new int[]{27,13,275};//G->relop arithexp
		
		tTable[27][57]=new int[]{57,268};//relop
		tTable[27][58]=new int[]{58,268};
		tTable[27][59]=new int[]{59,268};
		tTable[27][61]=new int[]{61,268};
		tTable[27][62]=new int[]{62,268};
		tTable[27][63]=new int[]{63,268};
		
		
		
		
		follow=new HashSet[n+1];
		ts=new HashSet<Integer>();
		for (int i=30;i<=67;i++)
			ts.add(i);
		
		adf(10,new int[]{35,51});
		adf(22,new int[]{46});
		adf(23,new int[]{46});
		adf(69,new int[]{43});
		adf(70,new int[]{});
		adf(71,new int[]{67,55});
		adf(72,new int[]{55});
		adf(73,new int[]{46,48,51});
		adf(74,new int[]{46,48,51,57,58,59,61,62,63,53});
		adf(75,new int[]{46,48,51});
		adf(76,new int[]{46,48,51,57,58,59,61,62,63,53,44,56,32});
		adf(77,new int[]{});
		adf(78,new int[]{46,48,51,57,58,59,61,62,63,53,44,56,32,49,60,47,50,30});
		adf(79,new int[]{46});
		adf(80,new int[]{46});
		adf(81,new int[]{46,48,51,57,58,59,61,62,63,53,44,56,32,47,50,30});//M
		adf(82,new int[]{55});
		adf(83,new int[]{55});
		adf(84,new int[]{46,48,51,57,58,59,61,62,63,53,44,56,32,47,50,30});//P
		adf(86,new int[]{46,60});
		
		//add follow set for error recovery
		adf(1,new int[]{67});
		adf(2,new int[]{37,43});
		adf(3,new int[]{67});
		adf(4,new int[]{54});
		adf(5,new int[]{67,55,38,39,64});
		adf(6,new int[]{51});
		adf(7,new int[]{38,39,64});
		adf(8,new int[]{40,41,42,64,33,36});
		adf(9,new int[]{46,51});
		adf(11,new int[]{46,51,48});
		adf(12,new int[]{51});
		adf(13,new int[]{46,51,48,57,58,59,61,62,63,53});
		adf(14,new int[]{38,39,64,45,31,44,56});
		adf(15,new int[]{46,51,48,57,58,59,61,62,63,53,32,44,56});
		adf(16,new int[]{46,51,48,57,58,59,61,62,63,53,32,44,56,30,47,50});
		adf(17,new int[]{47,60});
		adf(18,new int[]{64});
		adf(19,new int[]{46,51,48,57,58,59,61,62,63,53,32,44,56,30,47,50,49,52,30});
		adf(20,new int[]{38,39,64,46,48,51,52});
		adf(21,new int[]{64});
		adf(24,new int[]{46,48});
		adf(25,new int[]{46,48});
		adf(26,new int[]{44,45,56,64,65,66,31});
		adf(27,new int[]{44,45,56,64,65,66,31});
		adf(28,new int[]{44,45,56,64,65,66,31});
		adf(29,new int[]{44,45,56,64,65,66,31});
		
		adf(30,new int[]{44,45,56,64,65,66,31});
		adf(31,new int[]{44,45,56,64,65,66});
		adf(32,new int[]{44,45,56,64,65,66,31});
		adf(33,new int[]{45});
		adf(34,new int[]{33,35,36,40,41,42,54,64});
		adf(35,new int[]{33,51,36,40,41,42,54,64});
		adf(36,new int[]{45});
		adf(37,new int[]{64});
		adf(38,new int[]{64});
		adf(39,new int[]{64});
		adf(40,new int[]{45});
		adf(41,new int[]{45});
		adf(42,new int[]{45});
		adf(43,new int[]{54});
		adf(44,new int[]{45,56,64,65,66,31});
		adf(45,new int[]{44,46,56,64,65,66,31,38,39});
		adf(46,new int[]{30,32,33,34,36,40,41,42,44,47,48,50,51,53,54,56,57,58,59,61,62,63,64});
		adf(47,new int[]{31,44,45,56,64,65,66});
		adf(48,new int[]{31,38,39,44,45,56,64,65,66});
		adf(49,new int[]{64});
		adf(50,new int[]{31,44,45,56,64,65,66});
		adf(51,new int[]{31,33,35,36,37,38,39,40,41,42,43,44,45,55,56,64,65,66,67});
		adf(52,new int[]{31,44,45,56,64,65,66});
		adf(53,new int[]{30,32,38,39,44,46,47,48,49,50,51,52,56,57,58,59,60,61,62,63,64});
		adf(54,new int[]{33,36,38,39,40,41,42,55,64});
		adf(55,new int[]{35,51});
		adf(56,new int[]{45,44,64,65,66,31});
		adf(57,new int[]{45,44,56,64,65,66,31});
		adf(58,new int[]{45,44,56,64,65,66,31});
		adf(59,new int[]{45,44,56,64,65,66,31});
		adf(60,new int[]{45,44,56,64,65,66,31});
		adf(61,new int[]{45,44,56,64,65,66,31});
		adf(62,new int[]{45,44,56,64,65,66,31});
		adf(63,new int[]{45,44,56,64,65,66,31});
		adf(64,new int[]{30,32,44,45,46,47,48,49,50,51,52,53,54,56,57,58,59,60,61,62,63});
		adf(65,new int[]{30,32,44,46,47,48,50,51,53,56,57,58,59,61,62,63});
		adf(66,new int[]{30,32,44,46,47,48,50,51,53,56,57,58,59,61,62,63});
		adf(85,new int[]{46,48,51,57,58,59,61,62,63,53,44,56,32,47,50,30});
		adf(87,new int[]{55});
		adf(88,new int[]{33,36,55,40,41,42,64});
		adf(89,new int[]{55});
	}
	
	public void addInput(String fileName)
	{
		lA.addInput("testCases/"+fileName);
		sem.addOutFile(fileName);
		try {
			eout=new PrintStream(new File ("semanticOutputs/"+fileName));
			error=new PrintStream(new File("semanticErrors/"+fileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*while (true)
		{
			Token t=lA.nextToken();
			if (t==null)
				break;
			else
				System.out.println(t+" "+find(t));
		}*/
	}
	
	public void derive(int round)
	{
		if (round==2)
			sem.codeGenerater=new CodeGenerater();
		if (round==1)
			ms=new ArrayList();
		Stack<Integer> stack=new Stack<Integer>();
		stack.push(253);
		stack.push(67);
		stack.push(1);
		sem.round=round;
		sem.a1();
		int sb;
		Token t;
		count=0;
		while (true)
		{
			if (round==1)
			t=lA.nextToken();
			else if (count<ms.size())
			t=ms.get(count);
			else
			t=null;
			
			if (t==null)
				break;
			String v=t.value;
			if(!v.equals("//")&&!t.equals("/*")&&!t.equals("*/"))
				break;
		}
		sb= find(t);
	//	eout.println(nt.get(stack.top()-1));
	//	ArrayList<Integer> ms=new ArrayList();
		while (!stack.empty())
		{
			int top=stack.top();
			if (top>250)
			{
				action(top,round);
				stack.pop();
				continue;
			}
				//eout.println(nt.get(top-1)+" "+nt.get(sb-1));
			if (ts.contains(top)&&top==sb)
			{
				
					
					stack.pop();
					if (round==1)
					ms.add(t);
					count++;
					if (round==1)
					t= lA.nextToken();
					else if (count<ms.size())
					t=ms.get(count);
					else
					t=null;
					//System.out.println(t.col+" "+t.row);
					sb=find(t);
				
				
			}
			else if (tTable[top][sb]!=null)
			{
				int[] list=tTable[top][sb];
				stack.pop();
				for (int i=list.length-1;i>=0;i--)
				{
					stack.push(list[i]);
				}
			}
			else if (tTable[top][68]!=null&&follow[top].contains(sb))
			{
				int [] list=tTable[top][68];
				stack.pop();
				for (int i=list.length-1;i>=0;i--)
				{
					stack.push(list[i]);
				}
				
			}
			else 
			{
				if (t!=null)
				error.print("Error at "+"row:"+t.row+" col:"+t.col+", ");
				else
				error.print("Program is not complete, ");
				if (ts.contains(top))
					error.print(nt.get(top-1)+" is expected");
				error.println();
				//System.out.println("hh");
				while (tTable[top][sb]==null&&sb!=top)
				{
					//System.out.println(top);
					if (sb==67||follow[top].contains(sb))
					{
						//System.out.println("hh");
						stack.pop();
						if (round==1)
						ms.add(t);
						count++;
						break;
					}
					if (round==1)
					t=lA.nextToken();
					else if (count<ms.size())
					t=ms.get(count++);
					else
					t=null;
					sb=find(t);
						
				}
				continue;
			}
			if (round==1&&!ts.contains(top))
			{
			for (int i=0;i<count;i++)
				eout.print(show(ms.get(i))+" ");
			for (int i=stack.size()-1;i>=0;i--)
			{
				if (stack.get(i)<89)
				eout.print(nt.get(stack.get(i)-1)+" ");
				
			
			}
			eout.println();
			}
		}
		while (t!=null)
		{
			
			if (round==1)
			t=lA.nextToken();
			else if (count<ms.size())
			t=ms.get(count++);
			else
			t=null;
			if (round==1)
			error.println("Error at "+"row:"+t.row+" col:"+t.col);
		}
		lA.close();
		eout.close();
		error.close();
		if (round==2)
		{
			sem.writeResult();
			sem.codeGenerater.output();
		}
	}
	
	public int find(Token t)
	{
		if (t==null)
		{
			return 67;
		}
		//System.out.println(t.value);
		return map.get(show(t)).intValue();
		
		
	}
	
	public String show(Token t)
	{
		if (t.type.equals("punctuation")||t.type.equals("operator")||t.type.equals("keyword"))
		{
			return t.value;
		}
		else
			return t.type;
	}
	
	public void adf(int a,int []b)
	{
		follow[a]=new HashSet<Integer>();
		for (int i=0;i<b.length;i++)
			follow[a].add(b[i]);
	}
	
	public void writeSet()
	{
		for (int i=0;i<89;i++)
		if (i!=67&&i!=68)
		{
			System.out.print("first("+nt.get(i)+") = {");
			for (int j=1;j<=89;j++)
			if (tTable[i+1][j]!=null)
			{
				System.out.print(nt.get(j-1));
				System.out.print(",");
			}
			System.out.print("}    ");
			if (follow[i]!=null)
			{
				System.out.print("follow("+nt.get(i)+") = {");
				for (int j=1;j<=89;j++)
					if (follow[i].contains(j))
					{
						System.out.print(nt.get(j-1));
						System.out.print("  ");
					}
			
			System.out.print("}");
			}
			System.out.println();
		}
	}
	public void action(int n,int round)
	{
		/*System.out.println(n);
		if (count<ms.size())
		System.out.println(ms.get(count-1));
		
		for(int i=1;i<=3;i++)
		{
		if (sem.stack2.size()<i)
			continue;
		Record r=sem.getLastK(i);
		System.out.println(r.type);
		if (r.type==3)
		System.out.println(((Var)r.o).name);
		if (r.type==4)
			System.out.println(((Token)r.o).value);
		}*/
		n-=250;
		if (n==1)
		sem.a1();
		else if (n==2)
		sem.a2(ms.get(count-1));
		else if (n==3)
		sem.a3();
		else if (n==4)
		sem.a4();
		else if (n==5)
		sem.a5(ms.get(count-1));
		else if (n==6)
		sem.a6(ms.get(count-1));
		else if (n==7)
		sem.a7(ms.get(count-1));
		else if (n==8)
		sem.a8(ms.get(count-1));
		else if (n==9)
		sem.a9();
		else if (n==10)
		sem.a10();
		else if (n==11)
		sem.a11(ms.get(count-1));
		else if (n==12)
		sem.a12(ms.get(count-1));
		else if (n==13)
		sem.a13();
		else if (n==14)
		sem.a14();
		else if (n==15)
		sem.a15();
		else if (n==16)
		sem.a16();
		else if (n==17)
		sem.a17();
		else if (n==18)
		sem.a18(ms.get(count-1));
		else if (n==19)
		sem.a19();
		else if (n==20)
		sem.a20();
		
		if (n>20&&round==1)
			return;
		else if (n==21)
		sem.a21();
		else if (n==22)
		sem.a22();
		else if (n==23)
		sem.a23(ms.get(count-1));
		else if (n==24)
		sem.a24();
		else if (n==25)
		sem.a25();
		else if (n==26)
		sem.a26();
		else if (n==27)
		sem.a27();
		else if (n==28)
		sem.a28();
		else if (n==29)
		sem.a29();
		else if (n==30)
		sem.a30();
		else if (n==31)
		sem.a31();
		else if (n==32)
		sem.a32();
		else if (n==36)
		sem.a36();
		else if (n==37)
		sem.a37();
		
	}

}


