package SemanticStructrue;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public class SybTable {
	public Hashtable<String,Identifier> map; 
	public SybTable()
	{
		map= new Hashtable<String,Identifier>();
	}
	public String toString()
	{
		String r="";
		r=r+"{";
		Iterator<Map.Entry<String, Identifier>> entries = map.entrySet().iterator();  
		while (entries.hasNext()) {  
			Map.Entry<String, Identifier> entry = entries.next();
			r=r+"\r\n"+entry.getKey()+"\r\n";
			r=r+entry.getValue().toString();
		  
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
