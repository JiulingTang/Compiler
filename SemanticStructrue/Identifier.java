package SemanticStructrue;

public abstract class Identifier {
	public String itype; //identifier type
	public boolean def=false;
	public boolean isVar()
	{
		return itype.equals("var");
	}
	public boolean isFunc()
	{
		return itype.equals("func");
	}
	public boolean isCla()
	{
		return itype.equals("cla");
	}
}
