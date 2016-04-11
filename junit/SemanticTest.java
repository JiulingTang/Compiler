package junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Semantic.Semantic;
import SemanticStructrue.Cla;
import SemanticStructrue.Func;
import SemanticStructrue.Identifier;
import SemanticStructrue.Record;
import SemanticStructrue.Var;

public class SemanticTest {
	
	private static Semantic sem=new Semantic();
	@Before
	public void set(){
		sem.round=1;
	}
	
	@Test
	public void testA1() {
		sem.a1();
		assertNotNull(sem.stack2.top());
	}
	
	@Test
	public void testInsert() {
		sem.insert(sem.gTable, "c", new Cla());
		assertNotNull(sem.search(sem.gTable, "c"));
	}

	@Test
	public void testSearch() {
		assertNotNull(sem.search(sem.gTable, "c"));
	}



	@Test
	public void testGetScope() {
		Cla c=new Cla();
		Record r=new Record(c,1);
		assertSame(c.table,sem.getScope(r));
	}


	@Test
	public void testCheckType() {
		sem.insert(sem.gTable, "c", new Cla());
		assertTrue(sem.checkType("c"));
		assertFalse(sem.checkType("b"));
	}

	@Test
	public void testCheckVariableDefined() {
		sem.insert(sem.gTable, "v", new Var());
		assertTrue(sem.checkVariableDefined("v")!=null);
	}

	@Test
	public void testCheckFuncDefiend() {
		sem.insert(sem.gTable, "f", new Func());
		assertTrue(sem.checkFuncDefiend("f")!=null);
		assertFalse(sem.checkVariableDefined("f")!=null);
	}

}
