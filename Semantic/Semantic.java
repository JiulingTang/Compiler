package Semantic;
import SemanticStructrue.*;
import SyntacticAnalyzer.Stack;
import SyntacticAnalyzer.SyntacticalAnalyzer;
import Token.Token;

public class Semantic {
	public SybTable gTable;// global table
	public Stack<Integer> stack;
	public Stack<Record> stack2;
	public Stack<Token> tStack;

	public Semantic()
	{
		stack =new Stack<Integer>();
		stack2=new Stack<Record>();
		tStack=new Stack<Token>();
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
		System.out.println(table.toString());
	}
	public void writeError(String error)
	{
		System.out.println(error);
	}
	public void a1()
	{
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
		if (search(scope,idName)==null)
		{
			
			scope.map.put(idName,newClass);
			
		}
		else
		{
			writeError("\""+idName+"\"has been defined. "+"location: "+t.row+","+t.col);
		}
	}
	public void a3() //class id a2 { N } a3; 
					//funcbody a3
	{
		stack2.pop();
		printTable(this.gTable);
	}
	public void a4() //program a4 funcbody...
	{
		stack2.push(new Record(new SybTable(),0));
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
		if (search(scope,idName)==null)
		{
			//System.out.println(idName);
		
			scope.map.put(idName,newFunc);
			
		}
		else
		{
			writeError("\""+idName+"\"has been defined. "+"location: "+t.row+","+t.col);
		}
	}
	public void a6(Token t)// type of variable
	{
		Var v=new Var();
		v.dtype=t.value;
		stack2.push(new Record(v,3));
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
		if (Integer.parseInt(t.value)==0)
		writeError("array length should be larger than 0. "+"location: "+t.row+","+t.col);
		else
		var.dim.add(Integer.parseInt(t.value));
	}
	
	public void a9()//fparams -> type id E a9 K, fparamstail -> , type id E a9
	{
		Var var=(Var)stack2.top().o;
		stack2.pop();
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
	}
	
	public void a10()//add variable to scope
	{
		Var var=(Var)stack2.top().o;
		stack2.pop();
		SybTable scope=getScope(stack2.top());
		if (search(scope,var.name)==null)
		{
			scope.map.put(var.name, var);
		}
		else
		{
			writeError("\""+var.name+"\"has been defined. "+"location: "+var.t.row+","+var.t.col);
		}
	}
	
	public void a11(Token t)//add a string
							//O ->id a11 U
	{
		stack2.push(new Record(t,4));
	}
	
	public void a12(Token t)//another version to add variable
							//U ->id a12 E ; O
	{
		Token s=(Token)stack2.top().o;
		Var var=new Var();
		var.dtype=s.value;
		var.name=t.value;
		var.t=t;
		stack2.pop();
		stack2.push(new Record(var,3));
		
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
	
	
}









