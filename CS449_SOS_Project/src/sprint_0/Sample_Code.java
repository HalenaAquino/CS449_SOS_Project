// Java Program to Implement sort()
package sprint_0;


public class Sample_Code {
	
	public int addAB(int a, int b) {
		return a + b;
	}
	
	public int subAB(int a, int b) {
		return a - b;
	}
	
	public int multAB(int a, int b) {
		return a * b;
	}
	
	public double divAB(int a, int b) {
		if (b == 0) {
			return 0.00;           // just return 0 if A or B are an invalid number
		}
		else {
			return (double) a / b;
		}
	}
}