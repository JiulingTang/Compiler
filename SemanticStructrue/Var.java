package SemanticStructrue;

import java.util.ArrayList;

import Token.Token;
import Semantic.*;

public class Var extends Identifier{
	public String value="0";
	public String dtype; //data type
	public String name;
	public ArrayList<Integer> dim;
	public Token t;// The token for the name of variable
	public boolean isArray;
	public Var pp;
	public int isCons;
	public Location location;
	public int memberOffset;
	public int size;
	public static enum Error{
		Match,TypeNotMatch,DimensionNotMatch
	}
	public Var()
	{
		this.itype="var";
		dim=new ArrayList<Integer>();
		def=false;
	}
	public String toString()
	{
		String r="";
		r+="{\r\n";
		//r+="value: "+value+"\r\n";
		if (dim.size()>0)
		r+="dimention: "+dim.size()+"\r\n";
		r+="type£º "+dtype+"\r\n";
		if(this.location!=null)
		r+="location in generated code: "+location.startLable+"\r\n"; 
		r+=this.size+"\n";
		r+=this.memberOffset;
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
		//System.out.println(a.value);
		//System.out.println("hh"+a.dim.size());
		//System.out.println("hh"+b.dim.size());
		if (!a.dtype.equals(b.dtype))
			return Error.TypeNotMatch;
		if (a.dim==null&&b.dim==null)
			return Error.Match;
		if (a.dim==null&&b.dim!=null)
			return Error.DimensionNotMatch;
		if (b.dim==null&&a.dim!=null)
			return Error.DimensionNotMatch;
		if (a.dim.size()!=b.dim.size())
			return Error.DimensionNotMatch;
		return Error.Match;
	}
	
	public int countSize()
	{
		int singleSize;
		if (this.dtype.equals("int"))
			singleSize=1;
		else if (this.dtype.equals("float"))
			singleSize=2;// float size?
		else 
		{
			Cla c;
			Identifier id=Semantic.gTable.map.get(this.dtype);
			if (id==null || !id.isCla())
			{
				return 0;
			}
			else
			{
				singleSize=((Cla)id).countSize();
			}
		}
		int amount=1;
		for (int i=0;i<dim.size();i++)
		{
			amount=dim.get(i)*amount;
		}
		size=amount*singleSize;
		return size;
	}
}
