package kr.priv.woorisms;

import java.util.ArrayList;

public class Util {

	// 짝수( 원문장 )만 골라 리턴
	public static ArrayList<String> AdivListEven(ArrayList<String> oriList) {

		ArrayList<String> evenList = new ArrayList<String>();

		int i = 0;

		while (i < oriList.size()) {

			evenList.add(oriList.get(i));
			i = i + 2;

		}
		return evenList;
	}

	// 홀수 ( 대치어 )만 골라 리턴
	public static ArrayList<String> AdivListOdd(ArrayList<String> oriList) {

		ArrayList<String> evenList = new ArrayList<String>();

		int i = 1;

		while (i < oriList.size()) {

			evenList.add(oriList.get(i));
			i = i + 2;

		}
		return evenList;
	}

	// SMS를 지정한 단어로 교체
	public static String ChangeStr(String oriSMS, ArrayList<String> oriWord,
			ArrayList<String> changedWord) {

		String cooking = null;
		for (int i = 0; i < oriWord.size(); i++) {

			cooking = oriSMS.replaceFirst(oriWord.get(i), changedWord.get(i));
			oriSMS = cooking;
			System.out.println(">" + cooking);
		}
		return cooking;
	}
}
