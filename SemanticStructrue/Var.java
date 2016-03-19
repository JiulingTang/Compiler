package SemanticStructrue;

import java.util.ArrayList;

public class Var extends Identifier{
	public String value="default";
	public String dtype; //data type
	public String name;
	public ArrayList<Integer> dim;
	public Var()
	{
		dim=new ArrayList<Integer>();
	}
	public String toString()
	{
		String r="";
		r+="{\r\n";
		r+="value: "+value+"\r\n";
		if (dim.size()>0)
		r+="dimention: "+dim.size()+"\r\n";
		r+="type£º "+dtype+"\r\n";
		r+="}";
		return r;
	}
}
