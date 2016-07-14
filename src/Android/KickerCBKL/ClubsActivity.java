package Android.KickerCBKL;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

import java.util.ArrayList;

public class ClubsActivity extends Activity {	
			
	public void onCreate(Bundle savedInstanceState)
	{				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clubs);
		
		final Spinner spnAfdelingen = (Spinner) findViewById(R.id.spnAfdelingen);
		
		Intent i = getIntent();
		
		ArrayList<String> arrayAfdelingen = i.getStringArrayListExtra("afdelingen");
		ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayAfdelingen);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAfdelingen.setAdapter(spnAdapter);
        
        //fillTable(spnAfdelingen.getSelectedItem().toString());		
        
        spnAfdelingen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				fillTable(spnAfdelingen.getSelectedItem().toString());				
			}
			
			public void onNothingSelected(AdapterView<?> arg0)
			{
				return;				
			}       	
        });
	}
	
	public void fillTable(String afdeling)
	{
		final DBAdapter db = new DBAdapter(this);
	    db.open();
	    
		Cursor cursorClub = db.getAllClubsFromAfdeling(afdeling);
		db.close();
		
		
		TableLayout table = (TableLayout) findViewById(R.id.clubsTableLayout);
		table.removeAllViews();
		for (int i = 0; cursorClub.isAfterLast() == false ; i++)
		{
			TableRow tr = new TableRow(this);
			tr.setId(100+i);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			
			TextView txtClubnaam = new TextView(this);
			txtClubnaam.setId(200+i);
			txtClubnaam.setText(cursorClub.getString(0));			
		    final String clubnaam = cursorClub.getString(0);
			txtClubnaam.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			txtClubnaam.setOnClickListener(new OnClickListener() 
			{				
				@Override
				public void onClick(View v) 
				{
					//popup code	
					db.open();
					Cursor cursorInfo = db.getInfoFromClub(clubnaam);
					
					Builder alertDialog = new AlertDialog.Builder(ClubsActivity.this);
					alertDialog.setTitle("info: " + clubnaam);
					alertDialog.setMessage(cursorInfo.getString(2) + "\n" + cursorInfo.getString(3) 
					 + "\n" 	+ cursorInfo.getString(4) + "\n" + cursorInfo.getString(5));
					alertDialog.show();
					cursorInfo.close();
					db.close();
				}
			});
			
			TextView txtAdres = new TextView(this);
			txtAdres.setId(300+i);
			txtAdres.setTag(cursorClub.getString(1));		
			
			tr.addView(txtClubnaam);
			tr.addView(txtAdres);
			table.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			
			cursorClub.moveToNext();
			
		}
		
		cursorClub.close();
		db.close();
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.update:
                clearAll();

                return true;
        }
        return false;
    }

    public void clearAll()
    {

    }
	
	

}
