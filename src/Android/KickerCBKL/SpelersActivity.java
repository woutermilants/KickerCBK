package Android.KickerCBKL;

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
import android.widget.*;

import java.util.ArrayList;

public class SpelersActivity extends Activity {	
			
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
        
        if(spnAfdelingen.getSelectedItem() == null)
        {
     //   Button btnAnnuleer = new Button(getApplicationContext());
        Builder alertDialog = new AlertDialog.Builder(SpelersActivity.this);
		alertDialog.setTitle("Geen spelers beschikbaar");
		alertDialog.setMessage("Er bevinden zich geen spelers in de database. Wilt u deze nu updaten ?");
		alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

        final ToastHandler toastHandler = new ToastHandler();
				new Thread(new Runnable()
        		{
        	        public void run()
        	        {
        	        			HTMLparser HtmlParser = new HTMLparser();
        	        			try 
        	        			{    
        	        				HtmlParser.parseClubsAfdeling(getApplicationContext(), toastHandler);
        	        				HtmlParser.parseSpelers(getApplicationContext());
        	        				onCreate(savedInstanceState);
        	        			} 
        	        			catch (Exception e) 
        	        			{
        	        				// TODO Auto-generated catch block
        	        				e.printStackTrace();
        	        			}        
        	        }
        	    }).start();
			}
		});
		alertDialog.show();	
        }
        
        //fillTable(spnAfdelingen.getSelectedItem().toString());		
        
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
		Cursor cursorSpeler = null;
		
		if (club == "---")
		cursorSpeler = db.getAllSpelersFromAfdeling(spnAfdelingen.getSelectedItem().toString());
		else
		cursorSpeler = db.getAllSpelersFromClub(club);
		db.close();
		
				
		
		table.removeAllViews();
		for (int i = 0; cursorSpeler.isAfterLast() == false ; i++)
		{
			TableRow tr = new TableRow(this);
			tr.setId(100+i);
			tr.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			
			TextView txtSpelernaam = new TextView(this);			
			txtSpelernaam.setId(200+i);			
			txtSpelernaam.setText(cursorSpeler.getString(0) + " " + cursorSpeler.getString(1));
			System.out.println(cursorSpeler.getString(0) + " " + cursorSpeler.getString(1));
			
		    final String Spelerlidnr = cursorSpeler.getString(2);
			txtSpelernaam.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			txtSpelernaam.setOnClickListener(new OnClickListener() 
			{				
				@Override
				public void onClick(View v) 
				{
					//popup code	
					db.open();
					Cursor cursorInfo = db.getInfoFromSpeler(Spelerlidnr);
					
					Builder alertDialog = new AlertDialog.Builder(SpelersActivity.this);
					alertDialog.setTitle("info: " + cursorInfo.getString(0) + " " + cursorInfo.getString(1));
					alertDialog.setMessage("Club: " + cursorInfo.getString(2) + "\n" 
					+ "Afdeling: " + cursorInfo.getString(3) + "\n"
					+ "Lidnr: " + cursorInfo.getString(4) + "\n" 
					+ "Geboortedatum: " + cursorInfo.getString(5) + "\n"
					+ "Ranking: " + cursorInfo.getString(6));
					alertDialog.show();
					cursorInfo.close();
					db.close();
				}
			});
			
			TextView txtAdres = new TextView(this);
			txtAdres.setId(300+i);
			txtAdres.setTag(cursorSpeler.getString(1));		
			
			tr.addView(txtSpelernaam);
			tr.addView(txtAdres);
			table.addView(tr, new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			
			cursorSpeler.moveToNext();
			
		}
		
		cursorSpeler.close();
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
