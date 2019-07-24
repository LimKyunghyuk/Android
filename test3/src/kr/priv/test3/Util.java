package kr.priv.test3;

import java.util.ArrayList;

public class Util {

	public static ArrayList<String> AdivListEven(ArrayList<String> oriList) {

		ArrayList<String> evenList = new ArrayList<String>();

		int i = 0;

		while (i < oriList.size()) {

			evenList.add(oriList.get(i));
			i = i + 2;

		}
		return evenList;
	}

	public static ArrayList<String> AdivListOdd(ArrayList<String> oriList) {

		ArrayList<String> evenList = new ArrayList<String>();
		
		int i = 1;

		while (i < oriList.size()) {

			evenList.add(oriList.get(i));
			i = i + 2;

		}
		return evenList;
	}

}
