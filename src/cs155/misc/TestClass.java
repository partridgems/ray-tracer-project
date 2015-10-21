package cs155.misc;

import java.util.function.Function;
import java.util.function.Supplier;

public class TestClass {

	public static void main(String[] args) {
		
		TestClass s = new TestClass(0);
		Supplier<Double> f = makeSquare(p -> (1+p), s );
		
		for (; s.getNum() < 5; s.inc()) {
			System.out.println(s.getNum());
			System.out.println(f.get());
		}
		
		
	}
	
	public static Supplier<Double> makeSquare(Function<Integer,Integer> fun, TestClass tc) {
		return (() -> (Math.pow(fun.apply(tc.getNum()), 2)));
	}
	

	private int num;
	public TestClass(int num) {
		this.num = num;
	}
	public int getNum() {
		return num;
	}
	public void inc() {
		this.num++;
	}
}
