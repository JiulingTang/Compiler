package SemanticStructrue;

public class Cla extends Identifier{
	public SybTable table;
	public String name;
	public int size;
	public Cla()
	{
		this.itype="cla";
		table=new SybTable();
	}
	public String toString()
	{
		String r;
		r="{\r\n";
		r=r+"type:class\r\n";
		if (table!=null)
		{
		r+="symbolTable\r\n";
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
	public int countSize()
	{
		int sum=0;
		for (Identifier id: table.map.values())
		{
			if (id.isVar())
			{
				((Var)id).memberOffset=sum;
				sum+=((Var)id).countSize();
			}
		}
		size=sum;
		return sum;
	}
	
	
}
