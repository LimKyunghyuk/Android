package kr.priv.thread;

import android.os.Handler;
import android.os.Message;


public class Background implements Runnable{
	
	int mBackVal = 10;
	Handler mHandler;
	
	Background(Handler mHandler){
		
		this.mHandler = mHandler;
	}
	
	public void run(){
		
		while(0!=mBackVal){
			Message msg = new Message();
			mBackVal--;
			msg.what = 0;
			msg.arg1 = mBackVal;
			mHandler.sendMessage(msg);
			
			try{
				Thread.sleep(1000);	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		Message msg2 = new Message();
		msg2.what = 1;
		msg2.arg1 = 1;
		mHandler.sendMessage(msg2);
	}
}