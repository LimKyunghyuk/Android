package kr.priv.woorisms;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


public class Background implements Runnable{
	
	private static int THREADCNT = 5;
	Handler mHandler;
	ArrayList<String> item;
	String URL;
	String TEXT;

	
	Background(Handler mHandler, String URL, String TEXT){
		
		this.mHandler = mHandler;
		this.URL = URL;
		this.TEXT = TEXT;
		
	}
	
	public void run(){
		
		while(0!=THREADCNT){
			
			item = readyItem();
			
			if(null != item){
				break;
			}
			THREADCNT--;
		}
		
		// 메시지 전달
		Message msg = new Message();
		msg.what = 0;
		msg.obj = item;
		mHandler.sendMessage(msg);
		
		
	}
	

	public ArrayList<String> readyItem(){
		
		
		ArrayList<String> uncookedList;
		
		String HtmlPage = HttpConnect.getString(HttpConnect
				.postData(URL, TEXT));

		// ****** HttpConnect 작동 불능 시 처리
		if (null == HtmlPage) {
	//		Toast.makeText(getApplicationContext(), "인터넷이 연결이 되지 않습니다.",
		//			Toast.LENGTH_SHORT).show();

			return null;
		}
		// 정규식을 사용하여 쓸데없는 부분은 삭제
		String cleanPage = ClearDocu.clean(HtmlPage);

		// 깨끗하게 된 페이지에서 재료들을 추출
		uncookedList = Orthography.check(cleanPage);

		if (uncookedList.size() == 0) {
//			Toast.makeText(getApplicationContext(), "문법 및 철자 오류가 발견되지 않았습니다.",
	//				Toast.LENGTH_SHORT).show();

			return null;
		}
		
		
		return uncookedList;
	}
	
}