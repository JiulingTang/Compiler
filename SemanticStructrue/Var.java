package SemanticStructrue;

import java.util.ArrayList;

public class Var extends Identifier{
	String value;
	String dtype; //data type 
	ArrayList<Integer> dim;
	public Var(String dtype,ArrayList<Integer> dim,String value)
	{
		this.itype="var";
		this.dtype="dtype";
		for (int i=0;i<dim.size();i++)
			this.dim.add(new Integer(dim.get(i)));
		this.value=value;
	}
}
