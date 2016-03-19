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
		if (table!=null)
		{
		r+="scope\r\n";
		r+=table.toString()+"\r\n";
		}
		r+="}";
		return r;
	}
}
