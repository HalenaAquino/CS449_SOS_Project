package sprint_0;

import static org.junit.Assert.*;
import org.junit.Test;

// THIS IS A TEST

public class Practice_Unit_Test extends Sample_Code {
	
	Sample_Code calc = new Sample_Code();
	
	@Test
	public void addTest() {
		assertEquals(8, calc.addAB(2,  6));
	}
	
	@Test
	public void subTest() {
		assertEquals(5, calc.subAB(8,  3));
	}
	
	@Test
	public void subNegTest() {
		assertEquals(-1, calc.subAB(-2,  -1));
	}
	
	@Test
	public void multTest() {
		assertEquals(12, calc.multAB(4,  3));
	}
	
	@Test
	public void multZeroTest() {
		assertEquals(0, calc.multAB(20,  0));
	}
	
	@Test
	public void divTest() {
		assertEquals(8.00, calc.divAB(16,  2), 0.0001);
	}
	
	@Test
	public void divZeroTest() {
		assertEquals(0.00, calc.divAB(2,  0), 0.0001);
	}
	

} 
