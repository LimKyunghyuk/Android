package kr.priv.java_casting;

public class Java_casting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		String f = "6.2";
		String g = "3";
		
		float f2, g2;
		f2 = Float.valueOf(f).floatValue();
		g2 = Float.valueOf(g).floatValue();
		
		
		
		System.out.println(f2+g2);
		
		// 반올림
		int  num2;
		float num,rate;
		
		rate = 5;
		System.out.println(rate);
		num = (float)(Math.round((rate*10))/10.0);
		System.out.println(num);
		num2 = (int)(num*2);
		
		System.out.println(num2);
	}
	
	

}
