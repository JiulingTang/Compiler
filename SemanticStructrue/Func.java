package SemanticStructrue;

import java.util.ArrayList;

public class Func extends Identifier{
	public ArrayList<Var> par; //parameters
	public SybTable table;
	public Var rvalue;
	public Func()
	{
		this.itype="func";
	}
}
