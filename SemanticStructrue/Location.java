package SemanticStructrue;

public class Location {
	public String startLable;
	public int offset;
	public Location(String s,int o)
	{
		startLable=s;
		offset=o;
	}
	public String toString()
	{
		return this.startLable+"("+this.offset+")";
	}

}
