package kr.priv.android_listView;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class mainActivity extends Activity {
    /** Called when the activity is first created. */
    
	ListView lst;
	ArrayAdapter<String> adp;
	String []items;
	ArrayList<String> arr = new ArrayList<String>();
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        arr.add("하핳");
        arr.add("하핳2");
   
        items = arr.toArray(new String[arr.size()]);   
        lst = (ListView)findViewById(R.id.ListView01);
        adp = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);   
        lst.setAdapter(adp);
	
	}
}