package Token;

public class Token {
	public String value;
	public String type;
	public int row;
	public int col;
	public Token(String pvalue,String ptype,int prow,int pcol )
	{
		value=pvalue;
		type=ptype;
		row=prow;
		col=pcol;
	}
	
	public String toString()
	{
		return "value: "+value+" type: "+type+" row: "+row+" col: "+col;
	}
	
	

}
