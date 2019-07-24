package test_java;

public class TempTest {

	static int a = 0;
	
	TempTest(){
		a = 0;
		
	}
	

	public static int t1(int a1) {

		a = a + 3;
		return a;
	}

	public static int t2() {

		return a;
	}

}
