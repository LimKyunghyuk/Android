package kr.priv.woorisms;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

public class Dialog {

	static int [] resultStrmAry;
	static TokenManager toker;
	static ArrayList<String> cpOriList = new ArrayList<String>();
	static String text;
	
	private static void cpArrayList(ArrayList<String> cp,ArrayList<String> ori){
		
		for(int i = 0 ;i<ori.size();i++){
			cp.add(i,ori.get(i));
		}
	}
	
	public static ArrayList<String> makeDialog(final Context context, String uncookedText, ArrayList<String> oriList,
			ArrayList<String> uncookedchangedList) {
		
		// Display List
		ArrayList<String> listMenu = new ArrayList<String>();
		
		if(0 == uncookedchangedList.size())
			return null;
		
		// 리스너가 사용하기 위해 원문장을 전역으로 저장
		cpArrayList(cpOriList, oriList);
		text = uncookedText;
		
		// 재료들을 토커에게 전달
		toker = new TokenManager(uncookedchangedList);
		
		// 주문서 작성
		resultStrmAry = new int[toker.size()]; 
		for(int i = 0 ;i<toker.size();i++)
			resultStrmAry[i] = 0;

		for(int i=0;i<toker.size();i++){
			
			// 여러 대치어가 존재한다면
			if(0 != toker.getMyCnt(i)){
				listMenu.add(oriList.get(i) + "\n> " + "여러 대치어 존재");
			}else{
				listMenu.add(oriList.get(i) + "\n> " + toker.getFirstItem(i));
			}
		}
		
		
		// ArrayList로 받은 아이템들을 스트링 배열로 변환
		String[] listMenu2 = listMenu.toArray(new String[listMenu.size()]);

		// 기본값은 모두 체크 해제
		boolean[] boolArr = new boolean[oriList.size()];

		for (int i = 0; i < oriList.size(); i++) {
			boolArr[i] = false;
		}
		Log.e("TAG", "0 cpOriList size is :" +oriList.size());
		for(int i = 0 ;i<oriList.size();i++) Log.d("fF","0 >cpOriList["+i+"] :" + oriList.get(i));
	
		
		//c
		listMenu.clear();
		oriList.clear();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("순화목록");

		builder.setMultiChoiceItems(listMenu2, boolArr,
				new DialogInterface.OnMultiChoiceClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton, boolean isChecked) {

						
						if(isChecked){
							// 기본으로 첫번째 대치어를 일단 선택
							resultStrmAry[whichButton] = 1;
							// 하지만 여러 대치어가 존재한다면 재선택
							if(0!=toker.getMyCnt(whichButton)){
								makeRadioDialog(context,whichButton,cpOriList.get(whichButton),toker.getSltedAryLst(whichButton));
							}
							
						}else{
							resultStrmAry[whichButton] = 0;
						}
						
						
						Log.e("TAG", "whichButton:" + whichButton);
						Log.e("TAG", "isChecked:" + isChecked);
						Log.e("TAG", "1 cpOriList size is :" +cpOriList.size());
						for(int i = 0 ;i<cpOriList.size();i++) Log.d("fF","1 >cpOriList["+i+"] :" + cpOriList.get(i));
						for(int i = 0 ;i<toker.size();i++) Log.d("fF","1 recipe["+i+"] :"+ +resultStrmAry[i]);
						
					}

				});

		builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

				mainActivity.cookText(text, cpOriList,toker.selectedList(resultStrmAry, cpOriList));
				cpOriList.clear();
			}

		});

		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Log.e("TAG", "cencel");
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

		return oriList;
	}

	
	static int selectedBtn = 0;
	private static int makeRadioDialog(Context context, final int i, String oriTitle, ArrayList<String> uncookedchangedList) {

		
		
		// ArrayList로 받은 아이템들을 스트링 배열로 변환
		String[] listMenu= uncookedchangedList.toArray(new String[uncookedchangedList.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		//####### 원문장으로 교체
		builder.setTitle(oriTitle);

		//###### 다시 들어왔을 시 마지막 선택했던 버튼이 불이 들어오게
		builder.setSingleChoiceItems(listMenu, -1,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog,
							int whichButton) {
						for(int i = 0 ;i<toker.size();i++) Log.d("fF","2 recipe["+i+"] :"+ +resultStrmAry[i]);
						Log.e("TAG", "whichButton2:" + whichButton);
						selectedBtn = whichButton + 1;
					}

				});

		
		builder.setPositiveButton("선택", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				
				for(int i = 0 ;i<toker.size();i++) Log.d("fF","3 recipe["+i+"] :"+ +resultStrmAry[i]);
				Log.e("TAG", "Change");
				Log.e("TAG", "whichButton3:" + whichButton);
				resultStrmAry[i] = selectedBtn;
			}

		});

		builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Log.e("TAG", "Cencel");
				Log.e("TAG", "whichButton4:" + whichButton);
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
		
		return 0;
	}
	
	
}
