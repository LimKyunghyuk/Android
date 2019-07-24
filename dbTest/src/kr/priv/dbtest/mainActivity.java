package kr.priv.dbtest;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.Phones;
import android.provider.ContactsContract.Data;
import android.widget.Toast;

public class mainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
      Cursor c = managedQuery(Phones.CONTENT_URI, null,null,null," name desc limit 1");
      
      
      int idxName = c.getColumnIndex(Contacts.Phones.NAME);
      
      c.moveToFirst();
      String name = c.getString(idxName);
      
      Toast.makeText(this, name, Toast.LENGTH_LONG).show();

    }
}