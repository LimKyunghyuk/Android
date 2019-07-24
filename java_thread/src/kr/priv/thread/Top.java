package kr.priv.thread;

public class Top implements Runnable{

	public void run(){
		
		for(int i=0;i<10000;i++){
			if(i%100==0)System.out.print("!");
		}
	}
}
