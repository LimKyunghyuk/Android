package kr.priv.woorisms;

import android.os.Handler;
import android.os.Message;


public class Background implements Runnable{
	
	private String m_URL;
	private String m_text;
	Handler mHandler;
	
	Background(Handler mHandler){
		
		this.mHandler = mHandler;
	}
	
	public void setText(String m_text){
		this.m_text = m_text;	
	}
	
	public void setURL(String m_URL){
		this.m_URL = m_URL;	
	}
	
	public void run(){
		
			Message msg = new Message();
			
			String HtmlPage = HttpConnect.getString(HttpConnect.postData(m_URL, m_text));
			msg.what = 0;
			msg.obj = HtmlPage;
			mHandler.sendMessage(msg);
		
//		Message msg2 = new Message();
//		msg2.what = 1;
//		msg2.arg1 = 1;
//		mHandler.sendMessage(msg2);
	}
}