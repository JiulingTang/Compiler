package junit;

import static org.junit.Assert.*;
import Token.*;
import SyntacticAnalyzer.Stack;

import org.junit.Test;

public class StackTest {
	public static Stack<Token> stack=new Stack<Token>();
	
	@Test
	public void testEmpty(){
		assertEquals(true,stack.empty());
	}
	
	@Test
	public void testPush() {
		stack.push(new Token("a","b",1,1));
		assertEquals("a",stack.top().value);
	}
	
	@Test
	public void testPop() {
		stack.pop();
		assertEquals(true,stack.empty());
	}

}
