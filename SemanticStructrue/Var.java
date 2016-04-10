package SemanticStructrue;

import java.util.ArrayList;

import Token.Token;

public class Var extends Identifier{
	public String value="default";
	public String dtype; //data type
	public String name;
	public ArrayList<Integer> dim;
	public Token t;// The token for the name of variable
	public static enum Error{
		Match,TypeNotMatch,DimensionNotMatch
	}
	public Var()
	{
		this.itype="var";
		dim=new ArrayList<Integer>();
	}
	public String toString()
	{
		String r="";
		r+="{\r\n";
		//r+="value: "+value+"\r\n";
		if (dim.size()>0)
		r+="dimention: "+dim.size()+"\r\n";
		r+="type£º "+dtype+"\r\n";
		r+="name in generated code: "+name+"\r\n"; 
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
	
	
	public static Error match(Var a,Var b) //Check if two vairbale macth
	{
		if (a.dim==null&&b.dim==null)
			return Error.Match;
		if (!a.dtype.equals(b.dtype))
			return Error.TypeNotMatch;
		if (a.dim==null&&b.dim!=null)
			return Error.DimensionNotMatch;
		if (b.dim==null&&a.dim!=null)
			return Error.DimensionNotMatch;
		if (a.dim.size()==b.dim.size())
			return Error.DimensionNotMatch;
		return Error.Match;
	}
}
