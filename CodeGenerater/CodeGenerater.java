package CodeGenerater;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import Semantic.Semantic;
import SemanticStructrue.Cla;
import SemanticStructrue.Func;
import SemanticStructrue.Identifier;
import SemanticStructrue.Location;
import SemanticStructrue.Var;
import SyntacticAnalyzer.Stack;
import Token.Token;

public class CodeGenerater {
	public String defCode;
	public String funcCode;
	public String mainCode;
	public HashSet<String> symbolSet;
	public HashSet<String> tmpSymbolSet;
	public HashSet<String> lableSet;
	public String fileName="code.txt";
	public FileWriter eout;
	public Stack<String> elseStack;
	public Stack<String> endIfStack;
	public Stack<String> forStack;
	public Stack<String> endforStack;
	public CodeGenerater()
	{
		defCode="";
		funcCode="";
		mainCode="";
		symbolSet=new HashSet();
		tmpSymbolSet=new HashSet();
		symbolSet.add("j");
		symbolSet.add("dw");
		this.elseStack=new Stack<String>();
		this.endIfStack=new Stack<String>();
		this.forStack=new Stack<String>();
		this.endforStack=new Stack<String>();
		try {
			eout=new FileWriter(new File(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.writeStack();
		this.writeEntry();
	}
	public void writeDef(String s)
	{
		defCode=defCode+s+"\r\n";
	}
	public void writeFunc(String s)
	{
		funcCode=funcCode;
	}
	
	public void writeMain(String s)
	{
		mainCode=mainCode+s;
	}
	
	public void putSymbol(String s)
	{
		symbolSet.add(s);
	}
	
	public boolean checkSymbol(String s)
	{
		return symbolSet.contains(s);
	}
	
	public void defineInCode(String s,int size)
	{
		String ndf=null;
		if (size==1)
		{
			ndf=s+" dw 0";
		}
		else
		{
			ndf=s+" res "+4*size;
		}
		this.writeDef(ndf);
	}
	
	public void defineVariable(Var var)
	{
		String name=var.name;
		var.countSize();
		for (int i=0;;i++)
		{
			String name2;
			if (i==0)
				name2=name;
			else
				name2=name+i;
			if (!checkSymbol(name2))
			{
				
				this.putSymbol(name2);
				var.location=new Location(name2,0);
				this.defineInCode(name2, var.size);
				break;
			}
		}
	}
	
	public void defineClass(Cla cla)
	{
		cla.countSize();
	}
	
	public void defineFunc(Func func)
	{
		String name=func.name;
		for (int i=0;;i++)
		{
			String name2;
			if (i==0)
				name2=name;
			else
				name2=name+i;
			if (!checkSymbol(name2))
			{
				this.putSymbol(name2);
				func.lable=name2;
				break;
			}
		}
	}
	public void output()
	{
		try {
			eout.write(this.defCode);
			eout.write(this.mainCode);
			eout.write(this.funcCode);
			eout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("def:");
		System.out.println(this.defCode);
		System.out.println("main:");
		System.out.println(this.mainCode);
		System.out.println("func:");
		System.out.println(this.funcCode);
	}
	
	public String lw(int k1,String o,int k2)
	{
		return "lw "+"R"+k1+","+o+"(R"+k2+")\r\n";
	}
	
	public String sw(int k1,String o,int k2)
	{
		return "sw "+o+"(R"+k1+"),"+"R"+k2+"\r\n";
	}
	
	public String addi(int k1,int k2,int i)
	{
		return "addi"+" R"+k1+",R"+k2+","+i+"\r\n";
	}
	
	public String add(int k1,int k2,int k3)
	{
		return "add"+" R"+k1+",R"+k2+",R"+k3+"\r\n";
	}
	
	public String subi(int k1,int k2,int i)
	{
		return "subi"+" R"+k1+",R"+k2+","+i+"\r\n";
	}
	
	public String sub(int k1,int k2,int k3)
	{
		return "sub"+" R"+k1+",R"+k2+",R"+k3+"\r\n";
	}
	
	public String muli(int k1,int k2,int i)
	{
		return "muli"+" R"+k1+",R"+k2+","+i+"\r\n";
	}
	
	public String mul(int k1,int k2,int k3)
	{
		return "mul"+" R"+k1+",R"+k2+",R"+k3+"\r\n";
	}
	
	public String divi(int k1,int k2,int i)
	{
		return "divi"+" R"+k1+",R"+k2+","+i+"\r\n";
	}
	
	public String div(int k1,int k2,int k3)
	{
		return "div"+" R"+k1+",R"+k2+",R"+k3+"\r\n";
	}
	
	
	public String ceq(int k1,int k2,int k3)
	{
		return "ceq"+" R"+k1+",R"+k2+",R"+k3+"\r\n";
	}
	
	public String ceqi(int k1,int k2,int i)
	{
		return "ceq"+" R"+k1+",R"+k2+","+i+"\r\n";
	}
	
	public String getc(int k)
	{
		return "getc R"+k+"\r\n";
	}
	
	public String putc(int k)
	{
		return "putc R"+k+"\r\n";
	}
	
	public String not(int k1,int k2)
	{
		return "not R"+k1+",R"+k2+"\r\n";
	}
	
	public String and(int k1,int k2,int k3)
	{
		return "and R"+k1+",R"+k2+",R"+k3+"\r\n";
	}
	
	public String or(int k1,int k2,int k3)
	{
		return "or R"+k1+",R"+k2+",R"+k3+"\r\n";
	}
	
	public String noti(int k1,int i)
	{
		return "noti "+"R"+k1+","+i;
	}
	
	public String andi(int k1,int k2,int i)
	{
		return "andi"+" R"+k1+",R"+k2+","+i+"\r\n"; 
	}
	
	public String ori(int k1,int k2,int i)
	{
		return "ori"+" R"+k1+",R"+k2+","+i+"\r\n";
	}
	
	public String cgt(int k1,int k2,int k3)
	{
		return "cgt R"+k1+",R"+k2+",R"+k3+"\r\n";
	}
	
	public String cgti(int k1,int k2,int i)
	{
		return "cgti R"+k1+",R"+k2+","+i+"\r\n"; 
	}
	
	public String cge(int k1,int k2,int k3)
	{
		return "cge R"+k1+",R"+k2+",R"+k3+"\r\n";
	}
	
	public String cgei(int k1,int k2,int i)
	{
		return "cgei R"+k1+",R"+k2+","+i+"\r\n"; 
	}
	
	public String load(int k1,Location l)
	{
		if (l.offset==0)
			return lw(k1,l.startLable,0);
		else
		{
			String r="";
			r=r+
			  addi(10,0,4*l.offset)+
			  lw(k1,l.startLable,10);
			return r;
		}
			
	}
	
	public String carry(Location l,int k1)
	{
		
		if (l.offset==0)
			return sw(0,l.startLable,k1);
		else
		{
			String r="";
			r=r+
			  addi(10,0,4*l.offset)+
			  sw(10,l.startLable,k1);
			return r;
		}
			
	}
	
	public Location equalll(Location l1,Location l2)
	{
		String r=
		load(1,l1)+
		load(2,l2)+
		ceq(3,1,2);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,3);
		this.writeCode(r);
		return tmp;
	}
	
	public Location equalli(Location l1,int i)
	{
		String r=
		load(1,l1)+
		ceq(2,1,i);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,2);
		this.writeCode(r);
		return tmp;
		
	}
	
	public Location addll(Location l1,Location l2)
	{
		String r=
		load(1,l1)+
		load(2,l2)+
		add(3,1,2);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,3);
		this.writeCode(r);
		return tmp;
	}
	
	public Location addli(Location l1,int i)
	{
		String r=
		load(1,l1)+
		addi(2,1,i);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,2);
		this.writeCode(r);
		return tmp;
	}
	
	public Location subll(Location l1,Location l2)
	{
		String r=
		load(1,l1)+
		load(2,l2)+
		sub(3,1,2);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,3);
		this.writeCode(r);
		return tmp;
	}
	
	public Location subli(Location l1,int i)
	{
		String r=
		load(1,l1)+
		subi(2,1,i);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,2);
		this.writeCode(r);
		return tmp;
	}
	
	public Location subli2(int i,Location l1)
	{
		String r=
				addi(3,1,i)+
				load(1,l1)+
				sub(2,3,1);
				Location tmp=this.nextTmp();
				r=r+carry(tmp,2);
				this.writeCode(r);
				return tmp;
	}
	
	public Location mulll(Location l1,Location l2)
	{
		String r=
		load(1,l1)+
		load(2,l2)+
		mul(3,1,2);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,3);
		this.writeCode(r);
		return tmp;
	}
	
	public Location mulli(Location l1,int i)
	{
		String r=
		load(1,l1)+
		muli(2,1,i);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,2);
		this.writeCode(r);
		return tmp;
	}
	
	public Location divll(Location l1,Location l2)
	{
		String r=
		load(1,l1)+
		load(2,l2)+
		div(3,1,2);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,3);
		this.writeCode(r);
		return tmp;
	}
	
	public Location divli(Location l1,int i)
	{
		String r=
		load(1,l1)+
		divi(2,1,i);
		Location tmp=this.nextTmp();
		r=r+carry(tmp,2);
		this.writeCode(r);
		return tmp;
	}
	
	public Location divli2(int i,Location l1)
	{
		String r=
				addi(3,1,i)+
				load(1,l1)+
				div(2,3,1);
				Location tmp=this.nextTmp();
				r=r+carry(tmp,2);
				this.writeCode(r);
				return tmp;
	}
	
	public void assignll(Location l1,Location l2)
	{
		String r=
				load(1,l2)+
				carry(l1,1);
		this.writeCode(r);
	}
	
	public void assignli(Location l1,int i)
	{
		String r=
				addi(1,0,i)+
				carry(l1,1);
		this.writeCode(r);
	}	
	
	
	
	public void get(Location l)
	{
		String r=""+
		this.sub(1, 1, 1)+
		getc(1)+
		carry(l,1);
		this.writeCode(r);
	}
	
	public void put(Location l)
	{
		String r=""+
		this.sub(1, 1, 1)+
		load(1,l)+
		putc(1);
		this.writeCode(r);
	}
	
	public void puti(int k)
	{
		String r=""+
		this.addi(1, 0, k)+
		putc(1);
		this.writeCode(r);
	}
	

	public Location notl(Location l)
	{
		Location tmp=this.nextTmp();
		String lable=this.nextLabel();
		String endLable=this.nextNotEndLable();
		String r=""+
				load (1,l)+
				"bz R1,"+lable+"\r\n"+
				carry(tmp,0)+
				"j "+endLable+"\r\n"+
				lable+" "+addi(1,0,1)+
				carry(tmp,1)+
				endLable+"\r\n";
		this.writeCode(r);
		return tmp;
	}
	
	public Location andll(Location l1,Location l2)
	{
		return notl(orll(notl(l1),notl(l2)));		
	}
	
	public Location orll(Location l1,Location l2)
	{
		Location tmp=this.nextTmp();
		String lable=this.nextLabel();
		String endLable=this.nextOrEndLable();
		String r=""+
		load(1,l1)+
		load(2,l2)+
		or(3,1,2)+
		"bz R3,"+lable+"\r\n"+
		lable+" "+addi(1,0,1)+
		carry(tmp,1)+
		"j "+endLable+"\r\n"+
		carry(tmp,0)+
		endLable+"\r\n";
		this.writeCode(r);
		return tmp;
		
	}
	
	public Location noti(int i)
	{
		Location tmp=this.nextTmp();
		String lable=this.nextLabel();
		String endLable=this.nextNotEndLable();
		String r=""+
				addi(1,0,i)+
				"bz R1,"+lable+"\r\n"+
				carry(tmp,0)+
				"j "+endLable+"\r\n"+
				lable+" "+addi(1,0,1)+
				carry(tmp,1)+
				endLable+"\r\n";
		this.writeCode(r);
		return tmp;
	}
	
	public Location andli(Location l1,int i)
	{
		return notl(orll(notl(l1),noti(i)));		
	}
	
	public Location orli(Location l1,int i)
	{
		Location tmp=this.nextTmp();
		String lable=this.nextLabel();
		String endLable=this.nextOrEndLable();
		String r=""+
		load(1,l1)+
		addi(2,0,i)+
		or(3,1,2)+
		"bz R3,"+lable+"\r\n"+
		lable+" "+addi(1,0,1)+
		carry(tmp,1)+
		"j "+endLable+"\r\n"+
		carry(tmp,0)+
		endLable+"\r\n";
		this.writeCode(r);
		return tmp;
	}
	
	public Location greaterll(Location l1,Location l2)
	{
		Location tmp=this.nextTmp();
		String r=""+
		load(1,l1)+
		load(2,l2)+
		cgt(3,1,2)+
		carry(tmp,3);
		this.writeCode(r);
		return tmp;
	}
	
	public Location greaterli(Location l1,int i)
	{
		Location tmp=this.nextTmp();
		String r=""+
		load(1,l1)+
		cgti(2,1,i)+
		carry(tmp,2);
		this.writeCode(r);
		return tmp;
	}
	
	public Location greaterEqll(Location l1,Location l2)
	{
		Location tmp=this.nextTmp();
		String r=""+
		load(1,l1)+
		load(2,l2)+
		cge(3,1,2)+
		carry(tmp,3);
		this.writeCode(r);
		return tmp;
	}
	
	public Location greaterEqli(Location l1,int i)
	{
		Location tmp=this.nextTmp();
		String r=""+
		load(1,l1)+
		cgei(2,1,i)+
		carry(tmp,2);
		this.writeCode(r);
		return tmp;
	}
	
	public Location greaterli2(int i,Location l1)
	{
		Location tmp=this.nextTmp();
		String r=""+
		addi(1,0,i)+
		load(2,l1)+
		cgt(3,1,2)+
		carry(tmp,3);
		this.writeCode(r);
		return tmp;
		
	}
	
	public Location greaterEqli2(int i,Location l1)
	{
		Location tmp=this.nextTmp();
		String r=""+
		addi(1,0,i)+
		load(2,l1)+
		cge(3,1,2)+
		carry(tmp,3);
		this.writeCode(r);
		return tmp;
	}
	
	public void assign(Var num1,Var num2)
	{
		if (num2.isCons==1)
			this.assignli(num1.location, Integer.parseInt(num2.value));
		else
			this.assignll(num1.location, num2.location);
	}
	
	public void putv(Var v)
	{
		if (v.isCons==1)
			puti(Integer.parseInt(v.value));//put constant
		else
			put(v.location);//put variable
	}
	
	public void getv(Var v )
	{
		get(v.location);
	}
	
	public Location nextTmp()
	{
		int i;
		String tname=null;
		for ( i=0;;i++)
		{
			tname="tmp"+i;
			if (!this.symbolSet.contains(tname))
			{
				this.symbolSet.add(tname);
				break;
			}
		}
		Location l=new Location(tname,0);
		this.defineInCode(tname, 1);
		return l;
	}
	
	public void unary(Var v,Var num,Token t)
	{
		Location l = null;
		if (num.isCons==1)
		{
			if (t.value.equals("not"))
				l=this.noti(Integer.parseInt(t.value));
		}
		else
		{
			if (t.value.equals("not"))
				l=this.notl(num.location);
		}
			
		v.location=l;
	}
	
	public void binary(Var v,Var num1,Var num2,Token t)
	{
		/*System.out.print(num1.name+","+num1.location);
		System.out.print(" "+t.value);
		System.out.println(" "+num2.name+","+num2.location);*/
		Location location=null;
		if (num2.isCons==1)
		{
			if (t.value.equals("+"))
				location=this.addli(num1.location, Integer.parseInt(num2.value));
			else if (t.value.equals("-"))
				location=this.subli(num1.location, Integer.parseInt(num2.value));
			else if (t.value.equals("*"))
				location=this.mulli(num1.location, Integer.parseInt(num2.value));
			else if (t.value.equals("/"))
				location=this.divli(num1.location, Integer.parseInt(num2.value));
			else if (t.value.equals(">"))
				location=this.greaterli(num1.location, Integer.parseInt(num2.value));
			else if (t.value.equals(">="))
				location=this.greaterEqli(num1.location, Integer.parseInt(num2.value));
			else if (t.value.equals("<"))
				location=this.greaterli2(Integer.parseInt(num2.value), num1.location);
			else if (t.value.equals("<="))
				location=this.greaterEqli2(Integer.parseInt(num2.value), num1.location);
			else if (t.value.equals("or"))
				location=this.orli(num1.location, Integer.parseInt(num2.value));
			else if (t.value.equals("and"))
				location=this.andli(num1.location, Integer.parseInt(num2.value));
		}
		else 
		if (num1.isCons==1)
		{
			if (t.value.equals("+"))
				location=this.addli(num2.location, Integer.parseInt(num1.value));
			else if (t.value.equals("-"))
				location=this.subli2( Integer.parseInt(num1.value),num2.location);
			else if (t.value.equals("*"))
				location=this.mulli(num2.location, Integer.parseInt(num1.value));
			else if (t.value.equals("/"))
				location=this.divli2(Integer.parseInt(num1.value),num2.location);
			else if (t.value.equals("<"))
				location=this.greaterli(num2.location, Integer.parseInt(num1.value));
			else if (t.value.equals("<="))
				location=this.greaterEqli(num2.location, Integer.parseInt(num1.value));
			else if (t.value.equals(">"))
				location=this.greaterli2(Integer.parseInt(num1.value), num2.location);
			else if (t.value.equals(">="))
				location=this.greaterEqli2(Integer.parseInt(num1.value), num2.location);
			else if (t.value.equals("or"))
				location=this.orli(num2.location, Integer.parseInt(num1.value));
			else if (t.value.equals("and"))
				location=this.andli(num2.location, Integer.parseInt(num1.value));
		}
		else 
		{
			if (t.value.equals("+"))
				location=this.addll(num1.location, num2.location);
			else if (t.value.equals("-"))
				location=this.subll(num1.location, num2.location);
			else if (t.value.equals("*"))
				location=this.mulll(num1.location, num2.location);
			else if (t.value.equals("/"))
				location=this.divll(num1.location, num2.location);
			else if (t.value.equals(">"))
				location=this.greaterll(num1.location, num2.location);
			else if (t.value.equals(">="))
				location=this.greaterEqll(num1.location, num2.location);
			else if (t.value.equals("<"))
				location=this.greaterll(num2.location, num1.location);
			else if (t.value.equals("<="))
				location=this.greaterEqll(num2.location, num1.location);
			else if (t.value.equals("or"))
				location=this.orll(num1.location, num2.location);
			else if (t.value.equals("and"))
				location=this.andll(num1.location, num2.location);
			
		}
		v.location=location;
		
	}
	
	public void getInfoFromPro(Var pr,Var v)
	{
		v.dtype=pr.dtype;
		int py=0;
		int singleSize;
		int amount=1;
		if (v.dtype.equals("int"))
			singleSize=1;
		else if (v.dtype.equals("float"))
			singleSize=2;// float size?
		else 
		{
			Cla c;
			Identifier id=Semantic.gTable.map.get(v.dtype);
			if (id==null && !id.isCla())
			{
				return ;
			}
			else
			{
				singleSize=((Cla)id).countSize();
			}
		}
		for (int i=pr.dim.size()-1;i>=0;i--)
		{
			
			if (i==v.dim.size()-1)
			{
				v.size=amount*singleSize;
			}
			if (i<v.dim.size())
			py+=v.dim.get(i)*amount*singleSize;
			amount=amount*pr.dim.get(i);
		}
		v.location=new Location(pr.location.startLable,pr.location.offset+py);
		int k=v.dim.size();
		v.dim=new ArrayList<Integer>();
		for (int i=pr.dim.size()-1;i>=k;i--)
		{
			v.dim.add(pr.dim.get(i));
		}
		//System.out.println(pr.location+pr.name+" "+v.location+v.name);
	}
	
	public void getInfoFromParent(Var pa,Var pv,Var v)
	{
		v.dtype=pv.dtype;
		int py=0;
		int singleSize;
		int amount=1;
		if (v.dtype.equals("int"))
			singleSize=1;
		else if (v.dtype.equals("float"))
			singleSize=2;// float size?
		else 
		{
			Cla c;
			Identifier id=Semantic.gTable.map.get(v.dtype);
			if (id==null && !id.isCla())
			{
				return ;
			}
			else
			{
				singleSize=((Cla)id).countSize();
			}
		}
		
		for (int i=pv.dim.size()-1;i>=0;i--)
		{
			
			if (i<v.dim.size())
			py+=v.dim.get(i)*amount*singleSize;
			
			if (i==v.dim.size()-1)
			{
				v.size=amount*singleSize;
			}
			amount=amount*pv.dim.get(i);

		}
		System.out.println(py);
		v.location=new Location(pa.location.startLable,pa.location.offset+pv.memberOffset+py);
		int k=v.dim.size();
		v.dim=new ArrayList<Integer>();
		for (int i=pv.dim.size()-1;i>=k;i--)
		{
			v.dim.add(pv.dim.get(i));
		}
		System.out.println(pa.location+pa.name+" "+v.location+v.name);
	}
	
	public void writeCode(String r)
	{
		//System.out.println(r);
		this.writeMain(r);
	}
	
	public void writeStack()
	{
		//this.writeDef("tmp res 1000");
	}
	
	public void writeEntry()
	{
		this.writeCode("entry\r\n");
	}
	
	public String nextLabel()
	{
		int i;
		String tname=null;
		for ( i=0;;i++)
		{
			tname="lable"+i;
			if (!this.symbolSet.contains(tname))
			{
				this.symbolSet.add(tname);
				break;
			}
		}
		return tname;
	}
	
	public String nextNotEndLable()
	{
		int i;
		String tname=null;
		for ( i=0;;i++)
		{
			tname="endNot"+i;
			if (!this.symbolSet.contains(tname))
			{
				this.symbolSet.add(tname);
				break;
			}
		}
		return tname;
	}
	
	public String nextAndEndLable()
	{
		int i;
		String tname=null;
		for ( i=0;;i++)
		{
			tname="endAnd"+i;
			if (!this.symbolSet.contains(tname))
			{
				this.symbolSet.add(tname);
				break;
			}
		}
		return tname;
	}
	public String nextOrEndLable()
	{
		int i;
		String tname=null;
		for ( i=0;;i++)
		{
			tname="endOr"+i;
			if (!this.symbolSet.contains(tname))
			{
				this.symbolSet.add(tname);
				break;
			}
		}
		return tname;
	}
	
	public String nextElseLable()
	{
		int i;
		String tname=null;
		for ( i=0;;i++)
		{
			tname="else"+i;
			if (!this.symbolSet.contains(tname))
			{
				this.symbolSet.add(tname);
				break;
			}
		}
		return tname;
	}
	public String nextEndIfLable()
	{
		int i;
		String tname=null;
		for ( i=0;;i++)
		{
			tname="endif"+i;
			if (!this.symbolSet.contains(tname))
			{
				this.symbolSet.add(tname);
				break;
			}
		}
		return tname;
	}
	
	public void ifState()
	{
		String r="";
		String endLable=this.nextEndIfLable();
		String elseLable=this.nextElseLable();
		elseStack.push(elseLable);
		endIfStack.push(endLable);
	}
	
	public void elseAct()
	{
		String endLable=this.endIfStack.top();
		String elseLable=this.elseStack.top();
		String r=""+
		"j "+endLable+"\r\n"+
		elseLable+" ";
		this.writeCode(r);
		this.elseStack.pop();
		
		
	}
	
	public void endIf()
	{
		String endLable=this.endIfStack.top();
		String r=""+
		endLable+" ";
		this.writeCode(r);
		this.endIfStack.pop();
	}	
	
	public void then(Var v)
	{
		String elseLable=this.elseStack.top();
		String r="";
		if (v.isCons==1)
		r=r+
		addi(1,0,Integer.parseInt(v.value))+"\r\n";
		else
		r=r+load(1,v.location);
		r=r+
		"bz R1,"+elseLable+"\r\n";
		this.writeCode(r);
	}
	
	public String nextForLable()
	{
		int i;
		String tname=null;
		for ( i=0;;i++)
		{
			tname="for"+i;
			if (!this.symbolSet.contains(tname))
			{
				this.symbolSet.add(tname);
				break;
			}
		}
		return tname;
	}
	
	public String nextEndForLable()
	{
		int i;
		String tname=null;
		for ( i=0;;i++)
		{
			tname="endfor"+i;
			if (!this.symbolSet.contains(tname))
			{
				this.symbolSet.add(tname);
				break;
			}
		}
		return tname;
	}
	
	public void forStart()
	{
		String forlable=this.nextForLable();
		String endLable=this.nextAndEndLable();
		this.forStack.push(forlable);
		this.endforStack.push(endLable);
		String r=forlable+" ";
		this.writeCode(r);
	}
	
	public void checkFor(Var v)
	{
		String r = null;
		String endLable=this.endforStack.top();
		if (v.isCons==1)
			r=""+
			addi(1,0,Integer.parseInt(v.value))+"\r\n"+
			"bz R1,"+endLable+"\r\n";
		else
			r=""+
			load(1,v.location)+
			"bz R1,"+endLable+"\r\n";
		this.writeCode(r);
	}
	
	public void endFor()
	{
		String forLable=this.forStack.top();
		String endLable=this.endforStack.top();
		String r=null;
		r=""+
		"j "+forLable+"\r\n"+
		endLable+" ";
		this.writeCode(r);
		this.forStack.pop();
		this.endforStack.pop();
	}
	
	
}
