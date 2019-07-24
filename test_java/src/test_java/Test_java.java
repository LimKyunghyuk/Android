package test_java;


public class Test_java {

	public static void main(String[] args) {
		System.out.println("프로그램 시작");
		
//		Top t = new Top();
//		Thread thd1 = new Thread(t);
//		Thread thd2 = new Thread(t);
//		thd1.start();
//		for(int i =1 ;i<10000;i++){
//			if(i%100==0)System.out.print("? ");
//			if(i%1000==0)System.out.println();
//		}
//		
//		//thd2.start();
//		
//		
//		System.out.println("프로그램 종료");
//		
		
		String strTemp = "0.4245";
		float r = 0;
		
		
		r = Float.valueOf(strTemp);
		r = r + 0.0001; 
		System.out.print(r);
		
		
		// String a = "hello world";
		// String b = "owo";
		//		
		//		
		// if(a.contains(b)){
		// System.out.print("1");
		//			
		// }else{
		//			
		// System.out.print("2");
		// }

		
//		
//		ArrayList<String> d = new ArrayList<String>();
//		ArrayList<String> b = new ArrayList<String>();
//		
//		
//String c = "이세상 사람들아 모두 모여라 여행을 떠나자 이세상";
//
//		d.add("이세상");
//		d.add("사람들아");
//		d.add("여행");
//		d.add("이세상");
//
//		b.add("이 세상");
//		b.add("사람들아!");
//		b.add("휴가");
//		b.add("출바알!");

		
//		String c = "반갑수딘 안녕하셩 안녕하세영";
//		d.add("반갑수딘");
//		d.add("안녕하셩");
//		d.add("안녕하세영");
//	
//
//		b.add("반갑수딘");
//		b.add("안녕하세요");
//		b.add("안녕하세영");
//		

		
	//	c = ChangeSMS.ChangeStr(c,d,b);
//
		//System.out.println(c);
//
//		System.out.println("///////////////////////////////////////////");
//
//		System.out.println(c);
//
//		System.out.println("///////////////////////////////////////////");
//		int[] ary = { 0, 2, 3, 1 };
//		System.out.println("ary length : " + ary.length);
//
//		ArrayList<String> s = new ArrayList<String>();
//		s.add("A#br#B#br#G#br#");
//		s.add("B#br#F#br#");
//		s.add("C#br#D#br#Z#br#");
//		s.add("E#br#");
//
//	
//		ArrayList<String> o = new ArrayList<String>();
//		o.add("a");
//		o.add("b");
//		o.add("c");
//		o.add("d");
//
//		
//		ArrayList<String> rst;
//
//		TokenManager toker = new TokenManager(s);
//		rst = toker.selectedList(ary,o);
//
//		for(int i = 0;i<toker.size();i++)
//			System.out.println(">"+rst.get(i));
//		
////		ArrayList<String> h = new ArrayList<String>();
//
//		h = toker.getSltedAryLst(3);
//
//		if (h != null) {
//			for (int i = 0; i < h.size(); i++) {
//
//				System.out.println(">" + h.get(i));
//			}
//
//		} else {
//			System.out.println("1개 밖에 없습니다");
//
//		}

		//		
		// System.out.println("toker.size()"+toker.size());
		//		
		// for(int i =0;i<toker.size();i++){
		// System.out.println(">"+toker.getMyCnt(i));
		//			
		// }
		//		
		//		
		//		
		// int []cry = toker.getCntAry();
		//		
		// for(int i =0;i<toker.size();i++){
		// System.out.println("getCntAry:" + cry[i]);
		//			
		// }
		//		
		//		
		// System.out.println(">"+toker.getPrevCntSum(0));
		// System.out.println(">"+toker.getMyCnt(0));
		//		
		//	
		//		
		// for(int i = 0 ;i<4; i++){
		// System.out.println(rst.get(i));
		// }
	}
}
