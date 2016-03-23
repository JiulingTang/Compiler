package SemanticStructrue;

import java.util.ArrayList;

import Token.Token;

public class Var extends Identifier{
	public String value="default";
	public String dtype; //data type
	public String name;
	public ArrayList<Integer> dim;
	public Token t;// The token for the name of variable
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
}
