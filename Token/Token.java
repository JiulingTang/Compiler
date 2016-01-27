package Token;

public class Token {
	private String value;
	private String type;
	private int row;
	private int col;
	public Token(String pvalue,String ptype,int prow,int pcol )
	{
		value=pvalue;
		ptype=type;
		prow=row;
		pcol=col;
	}
	
	public String toString()
	{
		return "value:"+value+"type:"+type+"row:"+row+"col:"+col;
	}

}
