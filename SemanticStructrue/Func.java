package SemanticStructrue;

import java.util.ArrayList;

import SemanticStructrue.Var.Error;
import Token.Token;

public class Func extends Identifier{
	public ArrayList<Var> par; //parameters
	public SybTable table;
	public Var rvalue;
	public String name;
	public Token t;
	public String lable;
	public static enum Error{
		Match,ParameterNotMatch,ParameterNumberNotMacth
	}
	public  Func()
	{
		this.itype="func";
		table=new SybTable();
		par=new ArrayList<Var>();
	}
	public String toString()
	{
		String r="{\r\n";
		if (this.lable!=null)
		r=r+"lable in generated code";
		r=r+"type:function\r\n";
		r=r+"parameter:\r\n";
		if (par!=null)
		{
			for (int i=0;i<par.size();i++)
				r+=par.get(i).toString()+"\r\n";
		}
		if (rvalue!=null)
		{
			r+="return:\r\n";
			r+=rvalue.toString()+"\r\n";
		}
		if (table!=null)
		{
			r+="symbolTable:\r\n";
			r+=table.toString()+"\r\n";
		}
		String rr="";
		for (int i=0;i<r.length();i++)
		{
			rr+=r.charAt(i);
			if (r.charAt(i)=='\n')
				rr+='\t';
		}
		rr+="\r\n}";
		return rr;
	
	}
	
	public static Error match(Func a,Func b) //Check if two function macth
	{
		if (a.par==null&&b.par==null)
			return Error.Match;
		if (a.par==null&&b.par!=null)
			return Error.ParameterNumberNotMacth;
		if (b.par==null&&a.par!=null)
			return Error.ParameterNumberNotMacth;
		if (a.par.size()!=b.par.size())
			return Error.ParameterNumberNotMacth;
		for (int i=0;i<a.par.size();i++)
		{
			if (b.par.get(i).isCons==1)
			{
				if (a.par.get(i).dim.size()!=0)
					return Error.ParameterNotMatch;
			}
			else
			{
				int size=b.par.get(i).dim.size();
				if (a.par.get(i).dim.size()!=size)
					return Error.ParameterNotMatch;
			}
		}
		return Error.Match;
			
	}
}
