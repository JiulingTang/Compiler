package Semantic;
import SemanticStructrue.*;
import SyntacticAnalyzer.Stack;
import Token.Token;

public class Semantic {
	private SybTable gTable;// global table
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
		stack2.push(new Record(new SybTable(),1)); //1 means scope
		return (SybTable)stack2.top().o;
	}
	public void writeError(String error)
	{
		System.out.println(error);
	}
	public void a1()
	{
		gTable=createTable();
	}
	public void a2() //classdecl-> class id a2... 
	{
		SybTable scope=(SybTable)stack2.top().o;
		Token t=tStack.top();
		String idName=t.value;
		if (!scope.map.containsKey(idName))
		{
			Cla newClass=new Cla();
			newClass.table=new SybTable();
			scope.map.put(idName,newClass);
			stack2.push(new Record(newClass.table,1));
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
	}
	public void a4() //program a4 funcbody...
	{
		stack2.push(new Record(new SybTable(),1));
	}
	public void a5() // funchead-> type id (fparams)
	{
		SybTable scope=(SybTable)stack2.top().o;
		Token t=tStack.top();
		String idName=t.value;
		if (!scope.map.containsKey(idName))
		{
			Func newFunc=new Func();
			newFunc.table=new SybTable();
			scope.map.put(idName,newFunc);
			stack2.push(new Record(newFunc.table,1));
		}
		else
		{
			writeError("\""+idName+"\"has been defined. "+"location: "+t.row+","+t.col);
		}
	}
	
}









