package kr.priv.woorisms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClearDocu {

	private static interface Patterns {
		public static final Pattern SCRIPTS = Pattern.compile(
				"<(no)?script[^>]*>.*?</(no)?script>", Pattern.DOTALL);
		public static final Pattern STYLE = Pattern.compile(
				"<style[^>]*>.*</style>", Pattern.DOTALL);
		public static final Pattern Blank = Pattern.compile("\n\n",
				Pattern.DOTALL);		
		public static final Pattern BR = Pattern.compile("<br/>",
				Pattern.DOTALL);
//		public static final Pattern TABLE = Pattern.compile("<table.*width='410'>",
//				Pattern.DOTALL);
		//public static final Pattern COMMANT = Pattern.compile("<!--.*-->",
			//	Pattern.DOTALL);
		
		
	}

	public static String clean(String str) {

		if (str == null) {
			return null;
		}
		
		Matcher mat;
		mat = Patterns.SCRIPTS.matcher(str);
		str = mat.replaceAll("");
		mat = Patterns.STYLE.matcher(str);
		str = mat.replaceAll("");
		mat = Patterns.Blank.matcher(str);
		str = mat.replaceAll("");
		mat = Patterns.BR.matcher(str);
		str = mat.replaceAll("#BR#");
	//	mat = Patterns.TABLE.matcher(str);
	//	str = mat.replaceAll("");
	//	mat = Patterns.COMMANT.matcher(str);
		//str = mat.replaceAll("");
		
		
		return str;

	}
}
