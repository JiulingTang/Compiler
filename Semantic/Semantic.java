package Semantic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import SemanticStructrue.*;
import SyntacticAnalyzer.Stack;
import SyntacticAnalyzer.SyntacticalAnalyzer;
import Token.Token;

public class Semantic {
	public SybTable gTable;// global table
	public Stack<Integer> stack;
	public Stack<Record> stack2;
	public Stack<Token> tStack;
	public int round;
	private PrintStream eout;
	private PrintStream errorOut;
	public static String outPutFolderName="SymbolTableOutPuts";
	public static String errorFolderName="SymbolTableError";
	public Semantic()
	{
		stack =new Stack<Integer>();
		stack2=new Stack<Record>();
		tStack=new Stack<Token>();
	}
	public void addOutFile(String inputFileName)
	{
		try {
			eout=new PrintStream(new File (outPutFolderName+"/"+inputFileName));
			//errorOut=new PrintStream(new File(errorFolderName+"/"+inputFileName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public SybTable createTable()
	{
		return new SybTable(); 
	}
	public  void insert(SybTable tb,String id, Identifier i)
	{
		tb.map.put(id, i);
	}
	
	public Identifier search(SybTable tb,String id)
	{
		if (tb.map.containsKey(id))
			return tb.map.get(id);
		else
			return null;
	}
	public void printTable(SybTable table)
	{
		eout.println(table.toString());
	}
	public void writeError(String error)
	{
		System.out.println(error);
	}
	public void a1()
	{
		while (!stack2.empty())
			stack2.pop();
		if (round==1)
		gTable=createTable();
		stack2.push(new Record(gTable,0));
	}
	public void a2(Token t) //classdecl-> class id a2... 
	{
		
		SybTable scope=(SybTable)stack2.top().o;
		String idName=t.value;
		Cla newClass=new Cla();
		newClass.table=createTable();
		stack2.push(new Record(newClass,1));
		Identifier c=search(scope,idName);
		if (c==null)
		{
			
			insert(scope, idName, newClass);
			
		}
		else
		{
			if (round==1)
			writeError("\""+idName+"\"has been defined. "+"location: "+t.row+","+t.col);
			else if (!c.def)
			{
				c.def=true;
				stack2.pop();
				stack2.push(new Record(c,1));
			}
		}
	}
	public void a3() //class id a2 { N } a3; 
					//funcbody a3
	{
		stack2.pop();
		//printTable(this.gTable);
	}
	public void a4() //program a4 funcbody...
	{
		Func f=new Func();
		SybTable tb=getScope(stack2.top());
		insert(tb,"Main",f);
		f.table=new SybTable();
		stack2.push(new Record(f.table,0));
	}
	public void a5(Token t) // funchead-> type id a5 (fparams)
	{
		
		Var rt=(Var)stack2.top().o;
		stack2.pop();
		SybTable scope=getScope(stack2.top());
		String idName=t.value;
		Func newFunc=new Func();
		newFunc.rvalue=rt;
		newFunc.table=new SybTable();
		stack2.push(new Record(newFunc,2));
		Identifier f=search(scope,idName);
		if (f==null)
		{
			//System.out.println(idName);
		
			scope.map.put(idName,newFunc);
			
		}
		else 
		{
			if (round==1)
			writeError("\""+idName+"\"has been defined. "+"location: "+t.row+","+t.col);
			else
			{
				if (!f.def)
				{
					f.def=true;
					stack2.pop();
					stack2.push(new Record(f,2));
				}
			}
		}
	}
	public void a6(Token t)// type of variable
	{
		Var v=new Var();
		v.dtype=t.value;
		stack2.push(new Record(v,3));
		if (round==2&&!checkType(t.value))
		{
			writeError("No such type. location: "+t.row+","+t.col);
		}
			
	}
	public void a7(Token t)// name of variable
	{
		String idName=t.value;
		Var newVar=(Var)stack2.top().o;
		newVar.t=t;
		newVar.name=t.value;
		
	}
	
	public void a8 (Token t)// arraysize -> [Integer a8]
	{
		Var var=(Var)stack2.top().o;
		//System.out.println(t.value);
		//System.out.println(t.row+" "+t.col);
		if (Integer.parseInt(t.value)==0&&round==1)
		writeError("array length should be larger than 0. "+"location: "+t.row+","+t.col);
		else
		var.dim.add(Integer.parseInt(t.value));
	}
	
	public void a9()//fparams -> type id E a9 K, fparamstail -> , type id E a9
	{
		Var var=(Var)stack2.top().o;
		stack2.pop();
		if (round==2)
			return ;
		Func f=(Func)stack2.top().o;
		for (Var p : f.par)
		{
			if (p.name.equals(var.name))
			{
				writeError("parameter name has been defined. "+"location: "+var.t.row+","+var.t.col );
				return;
			}
		}
		f.par.add(var);
		insert(f.table,var.name,var);
	}
	
	public void a10()//add variable to scope
	{
		Var var=(Var)stack2.top().o;
		stack2.pop();
		SybTable scope=getScope(stack2.top());
		Identifier v=search(scope,var.name);
		if (v==null)
		{
			scope.map.put(var.name, var);
		}
		else 
		{
			if(round==1)
			writeError("\""+var.name+"\"has been defined. "+"location: "+var.t.row+","+var.t.col);
			else if (!v.def)
			{
				v.def=true;
			}
		}
	}
	
	public void a11(Token t)//add a string
							//O ->id a11 U
	{
		stack2.push(new Record(t,4));
	}
	
	public void a12(Token t)//another version to add variable name
							//U ->id a12 E ; O
	{
		Token s=(Token)stack2.top().o;
		Var var=new Var();
		var.dtype=s.value;
		var.name=t.value;
		var.t=t;
		stack2.pop();
		stack2.push(new Record(var,3));
		if (round==2&&!checkType(var.dtype))
		{
			writeError("No such type. location: "+t.row+","+t.col);
		}
		
		
	}
	
	public void a13()// U->J R...
	{
			if (round==1)
			stack2.pop();
		
	}
	
	public void a14()//For a14 type id...
	{
		stack2.push(new Record(new SybTable(),0));
	}
	
	public void a15()//The name of call function
	{
		
		if (round==2)
		{
			Token t=(Token)stack2.top().o;
			stack2.pop();
				Record r=stack2.top();
				if (r.type==11)
					return;
				if (r.type==5)
				{
					Var cv=(Var)r.o;
					Cla cla=(Cla)search(gTable,cv.dtype);
					Identifier id=search(cla.table,t.value);
					stack2.pop();
					if (id!=null&&id.itype.equals("func"))
					{	
						stack2.push(new Record(new Func(),2));
					}
					else
					{
					writeError("Function is not defined. location: "+t.row+","+t.col);
					addError(11);
					}
				}
				else
				{

					if (!this.checkFuncDefiend(t.value))
					{
						writeError("Function is not defined. location: "+t.row+","+t.col);
						addError(11);
					}
					else
						
						stack2.push(new Record(new Func(),2));//need modify
					
				}
			
		}
	}
	public void a16()//The name of call value
	{
		
		if (round==2)
		{
			Record r2=stack2.top();
			Token t=(Token)r2.o;
			stack2.pop();
				Record r=stack2.top();
				if (r.type==11)
				{
					return;
				}
				if (r.type==5)
				{
					Var cv=(Var)r.o;
					
					Cla cla=(Cla)search(gTable,cv.dtype);
					//System.out.println(t.value+" "+cv.dtype);
					Identifier id=search(cla.table,t.value);
					stack2.pop();
					if (id!=null&&id.itype.equals("var"))
					{
						Var ncv=new Var();
						ncv.dtype=((Var)id).dtype;
						stack2.push(new Record(ncv,3));
						//System.out.println("hh");
					}
					else
					{
					writeError("Variable is not defined. location: "+t.row+","+t.col);
					addError(11);
					}
				}
				else
				{

					if (!this.checkVariableDefined(t.value))
					{
						writeError("Variable is not defined. location: "+t.row+","+t.col);
						addError(11);
					}
					else
					{
						Var ncv=new Var();
						for (int i=stack2.size()-1;i>=0;i--)
						{
							Record r3=stack2.get(i);
							if (r3.type<3)
							{
								SybTable scope=getScope(r3);
								Identifier id=search(scope,t.value);
								//System.out.println(t.value);
								if (id!=null&&id.itype.equals("var"))
								{
									ncv.dtype=((Var)id).dtype;
								
								stack2.push(new Record(ncv,3));
								break;
								}
							}
						}
						
					}
					
				}
			
		}
	}
	
	public void a17() //pop stack in round 2
	{
		if (round==2)
		{
			stack2.pop();
		}
	}
	
	public void a18(Token t)
	{
		if (round==2)
			a11(t);
	}
	
	public void a19()// open scope for for-loop
	{
		stack2.push(new Record(new SybTable(),0));
	}
	
	public void a20()//when meet '.'
	{
		Record r=stack2.top();
		if (isError(r))
		{
			r.type=11;
		}
		r.type=5;
		
	}
	
	public SybTable getScope(Record r)
	{
		if (r.type==0)
		return (SybTable) r.o;
		else if (r.type==1)
		return ((Cla)r.o).table;
		else
		return ((Func)r.o).table;
	}
	

	public int findIdScope(String name)//
	{
		for (int i=stack2.size()-1;i>=0;i--)
		{
			Record r=stack2.get(i);
			if (r.type<4)
			{
				Identifier re=search(getScope(r),name);
				if (re!=null)
					return r.type;
			}
		}
		return -1;
	}
	
	public boolean checkType(String name)
	{
		if (name.equals("int")||name.equals("float"))
			return true;
		else
		{
			Identifier i=search(gTable,name);
			if (i!=null&&i.itype.equals("cla"))
				return true;
			else
				return false;
		}
		
	}
	
	public boolean checkVariableDefined(String vname)
	{
		for (int i=stack2.size()-1;i>=0;i--)
		{
			Record r=stack2.get(i);
			if (r.type<3)
			{
				SybTable scope=getScope(r);
				Identifier id=search(scope,vname);
				
				if (id!=null&&id.itype.equals("var"))
					return true;
			}
		}
		return false;
	}
	
	public boolean checkFuncDefiend(String fname)
	{
		for (int i=stack2.size()-1;i>=0;i--)
		{
			Record r=stack2.get(i);
			if (r.type<3)
			{
				SybTable scope=getScope(r);
				Identifier id=search(scope,fname);
				if (id!=null&&id.itype.equals("func"))
					return true;
			}
		}
		return false;
	}
	
	public void writeResult()
	{
		printTable(gTable);
		eout.close();
		//errorOut.close();
	}
	
	public boolean isError(Record r)
	{
		return r.type==10;//10 means an error record on the stack
	}
	
	public void addError(int i)
	{
		stack2.push(new Record(new Object(),i));
	}
}







