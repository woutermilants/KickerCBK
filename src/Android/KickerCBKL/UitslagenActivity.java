package Android.KickerCBKL;

import java.util.ArrayList;

import Android.KickerCBKL.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class UitslagenActivity extends Activity {	
			
	    Spinner spnAfdelingen;
	    Spinner spnClubs;	
	
	public void onCreate(final Bundle savedInstanceState)
	{				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spelers);
		
		final ScrollView scrollView = (ScrollView) findViewById(R.id.spelersScrollView);
		spnAfdelingen = (Spinner) findViewById(R.id.spnAfdelingen);		
		spnClubs = (Spinner) findViewById(R.id.spnClubs);		
		
		Intent i = getIntent();
		
		ArrayList<String> arrayAfdelingen = i.getStringArrayListExtra("afdelingen");
		ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayAfdelingen);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAfdelingen.setAdapter(spnAdapter);
             
        spnAfdelingen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {			
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				scrollView.scrollTo(0, 0);	
				//fillTable(spnAfdelingen.getSelectedItem().toString());
				fillClubs(spnAfdelingen.getSelectedItem().toString());
			}
			
			public void onNothingSelected(AdapterView<?> arg0)
			{
				return;				
			}       	
        }); 
        
        spnClubs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() 
        {
        	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        	{
        		scrollView.scrollTo(0, 0);	
			    fillTable(spnClubs.getSelectedItem().toString());				
			}
			
			public void onNothingSelected(AdapterView<?> arg0)
			{												
				return;				
			}
				
        });
	}
	
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu)
	 {
		 MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.spelers_menu, menu);		   
		    return true;
	 }
	 
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) 
	 {
	     // Handle item selection
	     switch (item.getItemId()) 
	     {
	         case R.id.update:
	             update();
	             return true;
	     }
		return false;
	 }
	
	public void fillTable(String club)
	{		
		final DBAdapter db = new DBAdapter(this);
	    db.open();	    	   		
		  		
		TableLayout table = (TableLayout) findViewById(R.id.spelersTableLayout);
		Cursor cursorDatums = null;
		table.removeAllViews();
		
		cursorDatums = db.getAllWedstrijdDatumsFromAfdeling(spnAfdelingen.getSelectedItem().toString());
		
		//db.close();
		
		for (int i=0; cursorDatums.isAfterLast() == false ; i++)
		{
			//table.removeAllViews();
			TableRow tr = new TableRow(this);
			tr.setId(100+i);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			
			TextView txtDatum = new TextView(this);
			txtDatum.setId(200+i);
			txtDatum.setText(cursorDatums.getString(0));
			txtDatum.setTextSize(20);
		//	System.out.println(cursorDatums.getString(0));		
		//	TextView txtThuisClub = new TextView(this);
			
		int amountWedstrijden = 0;	
		Cursor cursorUitslagen = null;
		if (club == "---")
		{
			cursorUitslagen = db.getAllUitslagenFromAfdeling("Competitie", spnAfdelingen.getSelectedItem().toString(), cursorDatums.getString(0));
			amountWedstrijden = 6;
		}
		else
		{
			cursorUitslagen = db.getAllUitslagenFromClub("Competitie", club, cursorDatums.getString(0));
			amountWedstrijden = 1;
		}
		
		tr.addView(txtDatum);
		
		TableRow[] tableRows = new TableRow[amountWedstrijden];
		for (int x=0; x<tableRows.length; x++ )
		{
			tableRows[x] = new TableRow(this);			
		}
		
		for (int j = 0; cursorUitslagen.isAfterLast() == false ; j++)
		{		
			System.out.println(cursorUitslagen.getCount());
			TextView txtWedstrijd1 = new TextView(this);			
			txtWedstrijd1.setId(i*100+j);			
			txtWedstrijd1.setText(cursorUitslagen.getString(1));					
			txtWedstrijd1.append(" - ");
			txtWedstrijd1.append(cursorUitslagen.getString(2));		
			txtWedstrijd1.append("  ");
			txtWedstrijd1.append(cursorUitslagen.getString(3));
			txtWedstrijd1.setTextSize(13);
			System.out.println(j);
			tableRows[j].addView(txtWedstrijd1);
			
			cursorUitslagen.moveToNext();
		}
				table.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			
			for (int x=0; x<tableRows.length; x++)
			{
				table.addView(tableRows[x], new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			}
			
		cursorDatums.moveToNext();
		}
		cursorDatums.close();
		db.close();
	}

	public void fillClubs(String afdeling)
	{
		final DBAdapter db = new DBAdapter(this);
	    db.open();	    
	    
	    Cursor cursorClubs = db.getAllClubsFromAfdeling(afdeling);
	    	    
		ArrayList<String> arrayClubs = new ArrayList<String>();
		arrayClubs.add("---");
		
		
		for (int i = 0; cursorClubs.isAfterLast() == false; i++)
		{
			arrayClubs.add(cursorClubs.getString(0));
			cursorClubs.moveToNext();
		}
		
		ArrayAdapter<String> spnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayClubs);
        spnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnClubs.setAdapter(spnAdapter);
        
        db.close();
	}
	
	public void update()
	{
		System.out.println("update update");		
	}
}
