package test_java;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class TokenManager {

	// 선택된 리스트(Queue)
	private static ArrayList<String> selectedAryLst = new ArrayList<String>();

	// 리턴할 최종 선택된 리스트
	private static ArrayList<String> resultAryLst = new ArrayList<String>();

	// 대치문장리스트의 각 첫 아이탬
	private static ArrayList<String> strAryLst = new ArrayList<String>();

	// 한 원문장에 대치할 문장이 복수개 존재할 경우
	// sltedAryLst[i]= (앞에서 넣은 문장의 수 - 1 ) + (해당 번째의 아이탬이 넣을 아이탬 인덱스) - 1 을
	// 만족한다.
	// 이 배열의 목적는 '앞에서 넣은 문장의 수'를 알기 위해 사용한다.
	private int[] cntAry; // 각 i번째에서 넣었던 아이탬의 수를 기록

	private int arrSize; // 배열 크기

	TokenManager(ArrayList<String> str) {

		String temp;
		StringTokenizer tokenStr;
		arrSize = str.size();

		cntAry = new int[str.size()];

		for (int i = 0; i < str.size(); i++) {

			cntAry[i] = 0;

			// i번째 아이템 토큰
			tokenStr = new StringTokenizer(str.get(i), "#br#");

			int tokenCnt = 0;

			while (tokenStr.hasMoreTokens()) {

				temp = tokenStr.nextToken();

				// 두 번째 순회라면 ( 원문장에 대치할 문장이 복수개 존재한다면 )
				if (0 != tokenCnt) {
					selectedAryLst.add(temp);
					cntAry[i]++; // 각 i번째에서 넣었던 아이탬의 수를 기록

				} else {
					strAryLst.add(i, temp);
				}

				tokenCnt++;
			}
		}
	}

	public ArrayList<String> getSltedAryLst() {

		return selectedAryLst;
	}

	// i번 째 항목의 대치어들을 보여줌
	public ArrayList<String> getSltedAryLst(int i) {

		// i번 째 항목에 대치어가 복수개 존재하지 않다면 ret n
		if (0 == cntAry[i])
			return null;

		ArrayList<String> ret = new ArrayList<String>();

		// 일단 첫 번째 아이탬을 넣고
		ret.add(strAryLst.get(i));

		// selectedAryLst에 들어있는 i번 째 대치어의 첫번째 idx
		int idx = getPrevCntSum(i);

		// selectedAryLst에 들어있는 i번 째 대치어의 수 만큼 반복
		for (int j = 0; j < cntAry[i]; j++) {

			ret.add(selectedAryLst.get(idx));

			idx++;
		}

		return ret;
	}

	public int getMyCnt(int i) {

		return cntAry[i];
	}

	public int[] getCntAry() {

		return cntAry;
	}

	public int size() {

		return arrSize;
	}

	public int getPrevCntSum(int i) {

		int sum = 0;

		for (int j = 0; j < i; j++) {
			sum = sum + cntAry[j];
		}

		return sum;
	}

	public String getFirstItem(int i) {

		return strAryLst.get(i);
	}

	// strAryLst
	// 선택한 문장을 담은 최종 리스트를 리턴
	public ArrayList<String> selectedList(int[] resultStrmAry,
			ArrayList<String> oriList) {

		int idx;

		if (null == resultStrmAry)
			return null;

		for (int i = 0; i < resultStrmAry.length; i++) {

			// 대치어 적용하지 않음, 원문장 그대로 복사
			if (0 == resultStrmAry[i]) {
				resultAryLst.add(i,oriList.get(i));
				
			// 대치어 적용
			}else{
				// 선택한 대치어 적용
				if (1 != resultStrmAry[i]) {
					idx = (getPrevCntSum(i) - 1) + (resultStrmAry[i] - 1);
					resultAryLst.add(i, selectedAryLst.get(idx));

				// 첫번째 대치어 적용
				} else {
					resultAryLst.add(i, strAryLst.get(i));
				}
			}
		}

		return resultAryLst;
	}
}
