package SemanticStructrue;

import java.util.ArrayList;

public class Func extends Identifier{
	public ArrayList<Var> par; //parameters
	public SybTable table;
	public Var rvalue;
	public String name;
	public  Func()
	{
		this.itype="func";
		table=new SybTable();
		par=new ArrayList<Var>();
	}
	public String toString()
	{
		String r="{\r\n";
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
}
