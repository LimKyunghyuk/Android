package kr.priv.thread;
public class JavaThread {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Top t = new Top();
		Thread thd1 = new Thread(t);
	//	Thread thd2 = new Thread(t);
		thd1.start();
		for(int i =1 ;i<10000;i++){
			if(i%100==0)System.out.print("? ");
			if(i%1000==0)System.out.println();
		}
		
		//thd2.start();
		
		
		System.out.println("프로그램 종료");
	}


}
