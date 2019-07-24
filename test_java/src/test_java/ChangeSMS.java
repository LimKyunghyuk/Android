package test_java;

import java.util.ArrayList;


public class ChangeSMS {

	public static String ChangeStr(String oriSMS, ArrayList<String> oriWord, ArrayList<String> changedWord){
		
		String cooking = null;
		for(int i = 0 ;i<oriWord.size();i++){
			
			cooking = oriSMS.replaceFirst(oriWord.get(i), changedWord.get(i));
			oriSMS = cooking;
			System.out.println(">"+cooking);
		}
		return cooking;
	}
}
