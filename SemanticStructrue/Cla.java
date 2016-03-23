package SemanticStructrue;

public class Cla extends Identifier{
	public SybTable table;
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
}
