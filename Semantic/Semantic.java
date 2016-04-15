package Semantic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import CodeGenerater.CodeGenerater;
import SemanticStructrue.*;
import SyntacticAnalyzer.Stack;
import SyntacticAnalyzer.SyntacticalAnalyzer;
import Token.Token;

public class Semantic {
	public static SybTable gTable;// global table
	public Stack<Integer> stack;
	public static Stack<Record> stack2;
	public Stack<Token> tStack;
	public int round;
	private PrintStream eout;
	private PrintStream errorOut;
	public static String outPutFolderName="SymbolTableOutPuts";
	public static String errorFolderName="SymbolTableError";
	public CodeGenerater codeGenerater;
	public String fileName;
	public static boolean useGenerate=true;
	public Semantic()
	{
		stack =new Stack<Integer>();
		stack2=new Stack<Record>();
		tStack=new Stack<Token>();
		this.codeGenerater=new CodeGenerater();
	}
	public void addOutFile(String inputFileName)
	{
		fileName=inputFileName;
		try {
			this.codeGenerater.addInput(fileName);
			eout=new PrintStream(new File (outPutFolderName+"/"+inputFileName));
			errorOut=new PrintStream(new File (this.errorFolderName+"/"+inputFileName));
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
		System.out.println(this.fileName+":"+error);
		this.errorOut.println(error);
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
		newClass.name=idName;
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
		newFunc.name=idName;
		if (round==1)
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
			{
				this.popN(1);
				this.addError(12);
				writeError("\""+idName+"\"has been defined. "+"location: "+t.row+","+t.col);
			}
			else
			{
				if (!f.def)
				{
					this.codeGenerater.writeFunc(((Func)f).lable+" ");
					f.def=true;
					stack2.pop();
					stack2.push(new Record(f,2));
				}
				else
				{
					this.popN(1);
					this.addError(12);
					writeError("\""+idName+"\"has been defined. "+"location: "+t.row+","+t.col);
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
			writeError("No type "+v.dtype+". location: "+t.row+","+t.col);
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
		System.out.println(stack2.top().type);
		if (this.checkError(2))
		{
			this.popN(2);
			this.addError(12);
			return;
		}
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
		if (round==1f)
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
			
				this.codeGenerater.defineVariable(var);
		}
		else 
		{
			if(round==1)
			writeError("\""+var.name+"\"has been defined. "+"location: "+var.t.row+","+var.t.col);
			else if (!v.def)
			{
				v.def=true;
				if (this.codeGenerater!=null&&this.useGenerate==true)
				this.codeGenerater.defineVariable((Var)v);
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
			writeError("No type "+var.dtype+". location: "+t.row+","+t.col);
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
	
	public void a15()//Checking of called function
	{
		
		if (round==2)
		{
			Record r2=stack2.top();
			Record r=this.getLastK(2);
			if (r.type==15)
			{
				this.popN(2);
				this.addError(13);
				return;
			}
			if (r.type==5)
				{
					this.popN(2);
					if (isError(r2))
					{
						this.addError(13);
						return;
					}
					Var cv=(Var)r.o;
					Func f=(Func)r2.o;
					Cla cla=(Cla)search(gTable,cv.dtype);
					Identifier id;
					if (cla!=null)
					id=search(cla.table,f.name);
					else
					id=null;
					if (id!=null&&id.itype.equals("func"))
					{	
						Func.Error e=Func.match((Func)id, f);
						if (e==Func.Error.ParameterNotMatch)
						{
							writeError("parameter type not match. location: "+f.t.row+","+f.t.col);
							this.addError(13);
						}
						else if (e==Func.Error.ParameterNumberNotMacth)
						{	
							writeError("parameter number not match. location: "+f.t.row+","+f.t.col);
							this.addError(13);
						}
						else
						{
							
							f.rvalue=new Var();
							f.rvalue.isCons=1;
							f.rvalue.dtype=((Func)id).rvalue.dtype;
							stack2.push(new Record(f.rvalue,3));
						}
					}
					else
					{
					writeError("Member "+f.name+" is not defined. location: "+f.t.row+","+f.t.col);
					addError(13);
					}
				}
				else
				{
					this.popN(1);
					if (isError(r2))
					{
						this.addError(13);
						return;
					}
					Func f=(Func)r2.o;
					Func id=this.checkFuncDefiend(f.name);
					if (id==null)
					{
						writeError("Function "+f.name+" is not defined. location: "+f.t.row+","+f.t.col);
						addError(13);
					}
					else
					{
						Func.Error e=Func.match((Func)id, f);
						if (e==Func.Error.ParameterNotMatch)
						{
							writeError("parameter type not match. location: "+f.t.row+","+f.t.col);
							this.addError(12);
						}
						else if (e==Func.Error.ParameterNumberNotMacth)
						{	
							writeError("parameter number not match. location: "+f.t.row+","+f.t.col);
							this.addError(12);
						}
						else
						{
							f.rvalue=new Var();//need modify
							f.rvalue.isCons=1;
							f.rvalue.dtype=((Func)id).rvalue.dtype;
							
							
							if (this.useGenerate==true)
							{
								f.rvalue.isCons=0;
								this.funAssign((Func)id,f);
								this.codeGenerater.writeCode("jl R15,"+((Func)id).name+"\r\n");
							}
							//System.out.println(((Func)id).rvalue.location);
							stack2.push(new Record(((Func)id).rvalue,3));
						}
						
					}
					
				}
			
		}
	}
	public void a16()//The checking of called value
	{
		
		if (round==2)
		{

			Record r2=stack2.top();
			Record r=this.getLastK(2);
				if (r.type==15)
				{
					this.popN(2);
					this.addError(13);
					return;
				}
				else if (r.type==5)
				{
					
					this.popN(2);
					if (isError(r2))
					{
						this.addError(13);
						return ;
					}
					Var v=(Var)r2.o;
					Var cv=(Var)r.o;
					Cla cla=(Cla)search(gTable,cv.dtype);
					
					Identifier id;
					if (cla!=null)
					id=search(cla.table,v.name);
					else
					id=null;
					if (id!=null&&id.itype.equals("var"))
					{
						Var pv=(Var)id;
						if (pv.dim.size()<v.dim.size())
						{
							writeError("Dimension of variable do not match type. location: "+v.t.row+","+v.t.col);
							this.addError(13);
						}
						else if (pv.dim.size()>v.dim.size())
							v.isArray=true;
						
						v.pp=pv;
						v.dtype=((Var)id).dtype;
					
						this.codeGenerater.getInfoFromParent(cv,pv,v);
						stack2.push(new Record(v,3));
						//System.out.println("hh");
						
					}
					else
					{
					writeError("Member "+v.name+" is not defined. location: "+v.t.row+","+v.t.col);
					addError(13);
					}
				}
				else
				{
					this.popN(1);
					if (isError(r2))
					{
						this.addError(13);
						return ;
					}
					Var v=(Var)r2.o;
					Var gv=this.checkVariableDefined(v.name);
					if (gv==null)
					{
						writeError("Variable "+v.name+" is not defined. location: "+v.t.row+","+v.t.col);
						addError(13);
					}
					else
					{

						
						if (gv.dim.size()<v.dim.size())
						{
							writeError("Dimension of variable do not match type. location: "+v.t.row+","+v.t.col);
							this.addError(13);
							return;
						}
						else
							{
							if (gv.dim.size()>v.dim.size())
							
							v.isArray=true;
						v.dtype=gv.dtype;
						v.pp=gv;
						//System.out.println(v.name+" "+v.t.row+","+v.t.col);
						//System.out.println(this.useGenerate);
						System.out.println(gv.name);
						System.out.println(gv.location);
						this.codeGenerater.getInfoFromPro(gv, v);
						stack2.push(new Record(v,3));
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
		if (round==2)
		{
			Record r=stack2.top();
			if (isError(r))
			{
				r.type=15;
			}
			else
			{
				Var v=(Var)r.o;
				if (isArray(v))
				{
					stack2.pop();
					this.addError(15);
				}
				r.type=5;
			}
		}
		
	}
	
	
	public void a21()//new called function
	{
		Func f=new Func();
		if (checkError(1))
		{
			
			this.popN(1);
			this.addError(12);
		}
		else
		{
			
			Token t=(Token)stack2.top().o;
			this.popN(1);
			f.t=t;
			f.name=t.value;
			stack2.push(new Record(f,2));
		}
	}
	
	public void a22()//new called variable
	{
		Var v=new Var();
		if (checkError(1))
		{
			
			this.popN(1);
			this.addError(13);
		}
		else
		{
			
			Token t=(Token)stack2.top().o;
			this.popN(1);
			v.t=t;
			v.name=t.value;
			stack2.push(new Record(v,3));
		}
	}
	
	
	/*The below action is for assign4*/
	public void a23(Token t)//Add a constant to stack
	{
		if (round==2)
		{
			Var var = new Var();
			var.value=t.value;
			if (t.type.equals("integer"))
				var.dtype="int";
			if (t.type.equals("double"))
				var.dtype="float";
			var.t=t;
			var.value=t.value;
			var.isCons=1;
			stack2.push(new Record(var,3));
		}
	}
	
	/*Check not type*/
	public void a24()
	{
		if (!checkError(2))
		{	
		Var var=(Var)stack2.top().o;
		if (!isInt(var)||isArray(var))
		{
			this.popN(2);
			this.addError(14);
			this.addError(13);
			writeError("Type do not match for operator not, need int. location£º"+var.t.row+", "+var.t.col);
		}
		}
		unary();
	}
	

	
	/*Check relop type*/
	public void a25()
	{
		if (!checkError(3))
		{
		Var num2=(Var)stack2.top().o;
		Token t=(Token)getLastK(2).o;
		Var num1=(Var)getLastK(3).o;
		if (isObject(num1)||isObject(num2)||isArray(num1)||isArray(num2))
		{
			writeError("Type do not match for operator "+t.value+", need int. location£º"+t.row+", "+t.col);
			popN(3);
			this.addError(13);
			this.addError(14);
			this.addError(13);
		}
		}
		binary();
	}
	/*Check addop type*/
	public void a26()
	{
		if (!checkError(3))
		{
		Var num2=(Var)stack2.top().o;
		Token t=(Token)getLastK(2).o;
		Var num1=(Var)getLastK(3).o;
		if (t.value.equals("+")||t.value.equals("-"))
		{
			if (isObject(num1)||isObject(num2)||isArray(num1)||isArray(num2))
			{
				writeError("Type do not match for operator "+t.value+", need int or float. location£º"+t.row+", "+t.col);
				popN(3);
				this.addError(13);
				this.addError(14);
				this.addError(13);
			}
		}
		else
		{
			if (!isInt(num1)||!isInt(num2)||isArray(num1)||isArray(num2))
			{
				writeError("Type do not match for operator "+t.value+", need int. location£º"+t.row+", "+t.col);
				popN(3);
				this.addError(13);
				this.addError(14);
				this.addError(13);
			}
		}
		}
		binary();
	}
	/*Check mulop type*/
	public void a27()
	{
		if (!checkError(3))
		{	
		Var num2=(Var)stack2.top().o;
		Token t=(Token)getLastK(2).o;
		Var num1=(Var)getLastK(3).o;
		if (t.value.equals("*")||t.value.equals("/"))
		{
			if (isObject(num1)||isObject(num2)||isArray(num1)||isArray(num2))
			{
				writeError("Type do not match for operator "+t.value+", need int or float. location£º"+t.row+", "+t.col);
				popN(3);
				this.addError(13);
				this.addError(14);
				this.addError(13);
			}
		}
		else
		{
			if (!isInt(num1)||!isInt(num2)||isArray(num1)||isArray(num2))
			{
				writeError("Type do not match for operator "+t.value+", need int. location£º"+t.row+", "+t.col);
				popN(3);
				this.addError(13);
				this.addError(14);
				this.addError(13);
			}
		}
		}
		binary();
	}
	
	/*check assignop */
	public void a28()
	{
		if (!checkError(3))
		{	
		Var num2=(Var)stack2.top().o;
		Token t=(Token)getLastK(2).o;
		Var num1=(Var)getLastK(3).o;
		if (isObject(num1)||isObject(num2)||isArray(num1)||isArray(num2))
		{
			
			writeError("Type do not match for operator "+t.value+", need int or float. location£º"+t.row+", "+t.col);
			popN(3);
			this.addError(13);
		}
		else
		{
			popN(3);
			if (this.codeGenerater!=null&&this.useGenerate==true)
				this.codeGenerater.assign(num1, num2);
			stack2.push(new Record(num1,3));
		}
		}
		else
		{
		this.popN(3);
		this.addError(13);
		}
		
		
	}
	
	/*Check sign type*/
	public void a29()
	{
		if (!checkError(2))
		{
		Var var=(Var)stack2.top().o;
		Token t=(Token)getLastK(2).o;
		if (isObject(var)||isArray(var))
		{
			this.popN(2);
			this.addError(14);
			this.addError(13);
			writeError("Type do not match for operator "+t.value+" . location£º"+var.t.row+", "+var.t.col);
		}
		}
		unary();
	}
	
	/*add dimension to variable call*/
	public void a30()
	{
		if (this.checkError(2))
		{
			this.popN(2);
			this.addError(13);
			return;
		}
		
		Var v=(Var)stack2.top().o;
		Var v2=(Var)this.getLastK(2).o;
		this.popN(2);
		if (!isInt(v))
		{
				writeError("Array index should be int at location: "+v.t.row+","+v.t.col);
				this.addError(13);
				
			}
			else
			{
				v2.dim.add(Integer.parseInt(v.value));
				stack2.push(new Record(v2,3));
					
			}
		
		
	}
	/*add parameter to called function*/
	public void a31()
	{
		if (this.checkError(2))
		{
			this.popN(2);
			this.addError(12);
			return;
		}
		Var v=(Var)stack2.top().o;
		Func f=(Func)this.getLastK(2).o;
		this.popN(2);
		f.par.add(v);
		//System.out.println("hhh"+v.location);
		stack2.push(new Record(f,2));
	}
	
	/*check return type*/
	public void a32()
	{
		if (this.checkError(2))
		{
			return;
		}
		Var v=(Var)stack2.top().o;
		Func f=(Func)this.getLastK(2).o;
		if (Var.match(f.rvalue, v)!=Var.Error.Match)
		{
			writeError("return type don't match, location: "+v.t.row+","+v.t.col);
		}
		else
		{
			if (this.useGenerate==true)
			{
			this.codeGenerater.assign(f.rvalue, v);
			this.codeGenerater.writeFunc("jr R15\r\n");
			this.codeGenerater.inFunc=false;
			}
		}
	}
	
	
	// below action is for code generation
	/*after a class definition*/
	public void a33()
	{
		if (!this.checkError(1))
		{
			Cla cla=(Cla)stack2.top().o;
			if (this.useGenerate==true)
			this.codeGenerater.defineClass(cla);
		}
	}
	
	/*aftet function definition*/
	public void a34()
	{
		if (!this.checkError(1))
		{
			Func f=(Func)stack2.top().o;
		}
	}
	
	/*after variable definition*/
	public void a35()
	{
		if (!this.checkError(1))
		{
			Var v=(Var)stack2.top().o;
			if (this.useGenerate==true&&(inMain()||inFunc()))
			{
				this.codeGenerater.defineVariable(v);
			}
				
		}
	}
	
	//after gets
	public void a36()
	{
		
		if (!this.checkError(1))
		{
			Var v=(Var)stack2.top().o;
			if (v.isCons==1)
			{
				this.popN(1);
				this.addError(13);
				this.writeError("constant is forbidden. lcation: "+v.t.row+","+v.t.col);
				return;
			}
			if (this.codeGenerater!=null&&this.useGenerate==true)
			{
				this.codeGenerater.getv(v);
			}
		}
	}
	
	//after put
	public void a37()
	{
		if (!this.checkError(1))
		{
			Var v=(Var)stack2.top().o;
			if (this.codeGenerater!=null&&this.useGenerate==true)
			{
			
				
				this.codeGenerater.putv(v);
			}
		}
	}
	
	//after if
	public void a38()
	{
		if (round==2&&this.useGenerate==true)
		this.codeGenerater.ifState();
	}
	
	//meet then
	public void a39()
	{
		if (!checkError(1))
		{
			Var v=(Var)stack2.top().o;
			if (!isInt(v))
			{
				this.writeError("Need int. location: "+v.t.row+","+v.t.col);
				this.popN(1);
				this.addError(13);
			}
			if (round==2&&this.useGenerate==true)
			this.codeGenerater.then(v);
		}
	}
	//meet else
	public void a40()
	{
		if (this.useGenerate==true)
		this.codeGenerater.elseAct();
	}
	
	public void a41()
	{
		if (this.useGenerate==true)
		this.codeGenerater.endIf();
	}
	
	//for begin
	public void a42()
	{
		
		if (this.useGenerate==true)
		this.codeGenerater.forStart();
	}
	
	//for loop check condition
	public void a43()
	{
		if (!checkError(1))
		{
			Var v=(Var)stack2.top().o;
			if (!isInt(v))
			{
				this.writeError("Need int. location: "+v.t.row+","+v.t.col);
				this.popN(1);
				this.addError(13);
			}
			if (this.useGenerate==true)
			this.codeGenerater.checkFor(v);
		}

	}
	
	//end for
	public void a44()
	{
		if (this.useGenerate==true)
		this.codeGenerater.endFor();
	}
	
	//define a function in generated code
	public void a45()
	{
		if (this.round==2)
			this.codeGenerater.inFunc=true;
		else 
		if(!this.checkError(1))
		{
			Func f=(Func)stack2.top().o;
			//if (this.useGenerate==true
			
			this.codeGenerater.defineFunc(f);
			//System.out.println(f.lable);
			
			for (int i=0;i<f.par.size();i++)
			{
				this.codeGenerater.defineVariable(f.par.get(i));
				((Var)f.table.map.get(f.par.get(i).name)).location=f.par.get(i).location;
				f.rvalue.name="ret";
				
			}
			System.out.println(this.fileName+"define"+f.lable);
			this.codeGenerater.defineVariable(f.rvalue);
			
		}
	}
	
	public void a46()
	{
		this.codeGenerater.inFunc=true;
	}
	
	
	public void unary()
	{
		if (!this.checkError(2))
		{
			Token t=(Token)this.getLastK(2).o;
			Var num=(Var)stack2.top().o;
			Var v=new Var();
			if (t.equals("not"))
			{
				v.dtype="int";
			}
			else
			{
				v.dtype=num.dtype;
			}
			this.popN(2);
			if (num.isCons==1)
			{
				v.isCons=1;
				if (t.value.equals("+"))
					v.value=num.value;
				else if (t.value.equals("-"))
				{
					if (isFloat(num))
					v.value=Double.toString((Double.parseDouble(num.value)*(-1)));
					else if (isInt(num))
					v.value=Integer.toString((Integer.parseInt(num.value)*(-1)));
				}
				else if (t.value.equals("not"))
					v.value=Integer.toString(not(Integer.parseInt(num.value)));
			}
			else
			{
				if(this.useGenerate==true)
				this.codeGenerater.unary(v, num,t);
			}
			stack2.push(new Record(v,3));
			v.t=t;
		}
		else
		{
			this.popN(2);
			this.addError(13);
		}
	}
	
	public void binary()
	{
		if (!this.checkError(3))
		{
			Token t=(Token)this.getLastK(2).o;
			Var num2=(Var)stack2.top().o;
			Var num1=(Var)this.getLastK(3).o;
			Var v=new Var();
			if (t.equals("<>")||t.equals("and")||t.equals("or")||t.equals(">")||t.equals("<")||t.equals(">=")||t.equals("<="))
			{
				v.dtype="int";
			}
			else
			{
				if (num1.dtype.equals("float")||num2.dtype.equals("float"))
					v.dtype="float";
				else
					v.dtype="int";
			}
			this.popN(3);
			v.t=num1.t;
			if (num1.isCons==1&&num2.isCons==1)
			{
				v.isCons=1;
				double value1=Double.parseDouble(num1.value);
				double value2=Double.parseDouble(num2.value);
				double value3 = 0;
				if (t.value.equals("+"))
					value3=value1+value2;
				if (t.value.equals("-"))
					value3=value1-value2;
				if (t.value.equals("*"))
					value3=value1*value2;
				if (t.value.equals("/"))
					value3=value1/value2;
				if (isInt(v))
				{
					v.value=Integer.toString((int)value3);
				}
				else
				{
					v.value=Double.toString(value3);
				}
				int value4 = 0;
				if (t.value.equals(">"))
					value4=value1>value2?1:0;
				if (t.value.equals("<"))
					value4=value1<value2?1:0;
				if (t.value.equals(">="))
					value4=value1>=value2?1:0;
				if (t.value.equals("<="))
					value4=value1<=value2?1:0;
				if (t.value.equals("=="))
					value4=value1==value2?1:0;
				if (t.value.equals("and"))
					value4=and((int)value1,(int)value2);
				if (t.value.equals("or"))
					value4=or((int)value1,(int)value2);
				v.value=Integer.toString(value4);
				
				
			}
			else
			{
				if (this.useGenerate==true)
				this.codeGenerater.binary(v, num1, num2, t);
				v.isCons=0;
			}
			stack2.push(new Record(v,3));
		}
		else
		{
			this.popN(3);
			this.addError(13);
		}
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
	
	public Var checkVariableDefined(String vname)
	{
		for (int i=stack2.size()-1;i>=0;i--)
		{
			Record r=stack2.get(i);
			if (r.type<3)
			{
				SybTable scope=getScope(r);
				Identifier id=search(scope,vname);
				
				if (id!=null&&id.itype.equals("var"))
					return (Var)id;
				else if (id!=null)
					return null;
			}
		}
		return null;
	}
	
	public Func checkFuncDefiend(String fname)
	{
		for (int i=stack2.size()-1;i>=0;i--)
		{
			Record r=stack2.get(i);
			if (r.type<3)
			{
				SybTable scope=getScope(r);
				Identifier id=search(scope,fname);
				if (id!=null&&id.itype.equals("func"))
					return (Func)id;
				else if (id!=null)
					return null;
			}
		}
		return null;
	}
	
	public void writeResult()
	{
		eout.println("Global:");
		printTable(gTable);
		eout.close();
		//errorOut.close();
	}
	
	public boolean isError(Record r)
	{
		return r.type>=10;//10 means an error record on the stack
	}
	
	public void addError(int i)
	{
		stack2.push(new Record(new Object(),i));
	}
	
	public boolean isInt(Var var)
	{
		return var.dtype.equals("int");
	}
	
	public boolean isFloat(Var var)
	{
		return var.dtype.equals("float");
	}
	public boolean isObject(Var var)
	{
		return !(isInt(var)||isFloat(var));
	}
	
	public boolean checkError(int n)//check the error in stack
	{
		for (int i=stack2.size()-1;i>=stack2.size()-n;i--)
			if (isError(stack2.get(i)))
				return true;
		return false;
	}
	
	public Record getLastK(int k)//get the last k record in stack
	{
		return stack2.get(stack2.size()-k);
	}
	
	public void popN(int n)//pop n record from stack
	{
		for (int i=0;i<n;i++)
			stack2.pop();
	}
	
	public boolean isArray(Var v)
	{
		return v.isArray;
	}
	
	public boolean inMain()
	{
		return stack2.get(1).type==0;
	}
	
	public int not(int k)
	{
		if (k>0)
			return 0;
		else return 1;
	}
	
	public int or(int k1,int k2 )
	{
		if (k1>0||k2>0)
			return 1;
		else
			return 0;
	}	
	
	public int and(int k1,int k2)
	{
		if (k1>0&&k2>0)
			return 1;
		else
			return 0;
	}
	
	public static boolean inFunc()
	{
		for (int i=stack2.size()-1;i>=0;i--)
		{
			if(stack2.get(i).type==2)
				return true;
		}
		return false;
	}
	
	public void funAssign(Func f,Func fp)
	{
		for(int i=0;i<fp.par.size();i++)
		{
			parameterAssin(f.par.get(i),fp.par.get(i));
		}
	}
	
	public void parameterAssin(Var v1,Var v2)
	{
		System.out.println(v2.location);
		v2.countSize();
		for (int i=0;i<v2.size;i++)
		{
			Var tmp2=new Var();
			Var tmp1=new Var();
			tmp1.location=new Location(v1.location.startLable,v1.location.offset+i);
			tmp1.dtype=v1.dtype;
			if (v2.isCons==1)
			{
				tmp2.value=v2.value;
				tmp2.isCons=1;
			}
			else
			{
				tmp2.location=new Location(v2.location.startLable,v2.location.offset+i);
				tmp2.dtype=v2.dtype;
			}
			this.codeGenerater.assign(tmp1, tmp2);
			v1.size=v2.size;
		}
	}
	

}







