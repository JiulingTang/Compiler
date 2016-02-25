package SyntacticAnalyzer;

import java.util.ArrayList;

public class Stack <T>{
	private ArrayList<T> list;
	public Stack ()
	{
		list=new ArrayList<T>();
	}
	public T top()
	{
		if (list.size()>0)
			return list.get(list.size()-1);
		else
			return null;
	}
	
	public void pop()
	{
		if (list.size()>0)
			list.remove(list.size()-1);
	}
	
	public void push(T a)
	{
		list.add(a);
	}
	
	public boolean empty()
	{
		return list.size()==0;
	}
	
	public T get(int i)
	{
		return list.get(i);
	}
	
	public int size()
	{
		return list.size();
	}
}
