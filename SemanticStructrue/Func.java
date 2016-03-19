package SemanticStructrue;

import java.util.ArrayList;

public class Func extends Identifier{
	public ArrayList<Var> par; //parameters
	public SybTable table;
	public Var rvalue;
	public  Func()
	{
		this.itype="func";
		table=new SybTable();
		par=new ArrayList<Var>();
	}
	public String toString()
	{
		String r="{\r\n";
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
			r+="scope:\r\n";
			r+=table.toString()+"\r\n";
		}
		r+="}";
		return r;
	}
}
