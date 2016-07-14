package Android.KickerCBKL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{
	//tabel clubs
	public static final String KEY_CLUBS_Afdeling = "afdeling";
    public static final String KEY_CLUBS_Club = "club";
    public static final String KEY_CLUBS_Lokaal = "lokaal";
    public static final String KEY_CLUBS_Adres = "adres";
    public static final String KEY_CLUBS_Gemeente = "gemeente";
    public static final String KEY_CLUBS_Telefoon = "telefoon";
    
    //tabel spelers
    public static final String KEY_SPELERS_Naam = "naam";
    public static final String KEY_SPELERS_Voornaam = "voornaam";
    public static final String KEY_SPELERS_Club = "club";
    public static final String KEY_SPELERS_Afdeling = "afdeling";
    public static final String KEY_SPELERS_Lidnr = "lidnr";
    public static final String KEY_SPELERS_Geboortedatum = "geboortedatum";
    public static final String KEY_SPELERS_Ranking = "ranking";
    
    //tabel uitslagen
    public static final String KEY_UITSLAGEN_thuisClub = "thuisploeg";
    public static final String KEY_UITSLAGEN_uitClub = "uitploeg";
    public static final String KEY_UITSLAGEN_Uitslag = "uitslag";
    public static final String KEY_UITSLAGEN_Datum = "datum";
    public static final String KEY_UITSLAGEN_Afdeling = "afdeling";
    public static final String KEY_UITSLAGEN_Type = "type";
    
   
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "cbkdata";
    private static final String DATABASE_TABLE_CLUBS = "clubs";
    private static final String DATABASE_TABLE_SPELERS = "spelers";
    private static final String DATABASE_TABLE_UITSLAGEN = "uitslagen";    
    
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_CLUBS =
        "create table clubs (afdeling text not null, "
        + "club text not null unique, lokaal text not null, adres text not null, "
        + "gemeente text not null, telefoon text not null)";
    
    private static final String CREATE_TABLE_SPELERS = 
    		"create table spelers (naam text not null, "
    		+ "voornaam text not null, club text not null, " 
    		+ "afdeling text not null, lidnr text not null unique, " 
    		+ "geboortedatum text not null, ranking text not null) ";
    		
    private static final String CREATE_TABLE_UITSLAGEN = 
    		"create table uitslagen (ID integer not null primary key, thuisploeg text not null, "
    		+ "uitploeg text not null, uitslag text not null, " 
    		+ "datum text not null, afdeling text not null, type text not null)";  //type is beker of competitie
       
    private static final String CREATE_TABLE_KLASSEMENT =
    		"create table klassement (plaats text not null, "
    		+ "club text not null, gespeeld text not null"
    		+ "winst text not null, verlies text not null"
    		+ "gelijk text not null, setpunten text not null"
    		+ "punten text not null, puntenlimburgs1 text not null"
    		+ "puntenlimburgs2 text not null)";
    
    private final Context context;  
    
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	System.out.println("db create");
            db.execSQL(CREATE_TABLE_CLUBS);
            db.execSQL(CREATE_TABLE_SPELERS);
            db.execSQL(CREATE_TABLE_UITSLAGEN);
        }        
       

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
                              int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                  + " to "
                  + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS titles");
            onCreate(db);
        }
    }
    
  //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
    
    //---insert a title into the database---
    public long insertClubInAfdeling(String afdeling, String club, String lokaal, String adres, String gemeente, String telefoon) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CLUBS_Afdeling, afdeling);
        initialValues.put(KEY_CLUBS_Club, club); 
        initialValues.put(KEY_CLUBS_Lokaal, lokaal);
        initialValues.put(KEY_CLUBS_Adres, adres);
        initialValues.put(KEY_CLUBS_Gemeente, gemeente);
        initialValues.put(KEY_CLUBS_Telefoon, telefoon);
        return db.insertWithOnConflict(DATABASE_TABLE_CLUBS, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);        
    }
    
    public long insertSpeler(String naam, String voornaam, String club, String afdeling, String lidnr, String geboortedatum, String ranking)
    {
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_SPELERS_Naam, naam);
    	initialValues.put(KEY_SPELERS_Voornaam, voornaam);
    	initialValues.put(KEY_SPELERS_Club, club);
    	initialValues.put(KEY_SPELERS_Afdeling, afdeling);
    	initialValues.put(KEY_SPELERS_Lidnr, lidnr);
    	initialValues.put(KEY_SPELERS_Geboortedatum, geboortedatum);
    	initialValues.put(KEY_SPELERS_Ranking, ranking);    	
       	
    	return db.insertWithOnConflict(DATABASE_TABLE_SPELERS, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);
    }
        
    //---deletes a particular club---
    public boolean deleteTitle(String club) 
    {
        return db.delete(DATABASE_TABLE_CLUBS, KEY_CLUBS_Club + "=" + club, null) > 0;
    }

    //---retrieves all the clubs---
    public Cursor getAllClubs() 
    {
//    	String query = "select " + KEY_CLUBS_Afdeling + "," + KEY_CLUBS_Club + "," + KEY_CLUBS_Adres +","
//				+ KEY_CLUBS_Gemeente + " from " + DATABASE_TABLE_CLUBS;
//    	Cursor mCursor = db.rawQuery(query, null);      	
//    	
        return db.query(DATABASE_TABLE_CLUBS, new String[] {
        		KEY_CLUBS_Afdeling, 
        		KEY_CLUBS_Club, 
        		KEY_CLUBS_Adres,
        		KEY_CLUBS_Gemeente},
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public Cursor getAllClubsFromAfdeling(String afdeling)
    {
    	String query = "select " + KEY_CLUBS_Club + "," + KEY_CLUBS_Adres +","
				+ KEY_CLUBS_Gemeente + "," + KEY_CLUBS_Afdeling + " from " + DATABASE_TABLE_CLUBS +  " where " + KEY_CLUBS_Afdeling + " = ?";
    	Cursor mCursor = db.rawQuery(query, new String[] {afdeling});      				
    	if (mCursor != null)
    	{
            mCursor.moveToFirst();
        }   			
    	
    	return mCursor;
    }
    
    public Cursor getAllSpelersFromAfdeling(String afdeling)
    {
    	String query = "select " + KEY_SPELERS_Naam + "," + KEY_SPELERS_Voornaam +","
				+ KEY_SPELERS_Lidnr + " from " + DATABASE_TABLE_SPELERS +  " where " + KEY_SPELERS_Afdeling + " = ?";
    	Cursor mCursor = db.rawQuery(query, new String[] {afdeling});  
    	
    	if (mCursor != null)    	
    		mCursor.moveToFirst();    	
    	
    	return mCursor;
    }
    
    public Cursor getAllSpelersFromClub(String club)
    {    	
    	String query = "select " + KEY_SPELERS_Naam + "," + KEY_SPELERS_Voornaam +","
    					+ KEY_SPELERS_Lidnr + " from " + DATABASE_TABLE_SPELERS +  " where " + KEY_SPELERS_Club + " = ?";
    	Cursor mCursor = db.rawQuery(query, new String[] {club});       	    	
    	if (mCursor != null)
    		mCursor.moveToFirst();
    	
    	return mCursor;
    			
    }
    
    public Cursor getAllUitslagenFromAfdeling(String type, String afdeling, String datum)
    {
    	String query = "select " + KEY_UITSLAGEN_Datum + "," + KEY_UITSLAGEN_thuisClub + "," + KEY_UITSLAGEN_uitClub + ","
    					+ KEY_UITSLAGEN_Uitslag + " from " + DATABASE_TABLE_UITSLAGEN + " where " + KEY_UITSLAGEN_Type + " = ? AND " 
    					+ KEY_UITSLAGEN_Afdeling + " = ? AND " + KEY_UITSLAGEN_Datum + " = ?";
    	Cursor mCursor = db.rawQuery(query, new String[] {type, afdeling, datum});
    	if (mCursor != null)
    		mCursor.moveToFirst();
    	
    	return mCursor;    	
    }
    
    public Cursor getAllUitslagenFromClub(String type, String club, String datum)
    {
    	String query = "select " + KEY_UITSLAGEN_Datum + "," + KEY_UITSLAGEN_thuisClub + "," + KEY_UITSLAGEN_uitClub + ","
    					+ KEY_UITSLAGEN_Uitslag + " from " + DATABASE_TABLE_UITSLAGEN + " where " + KEY_UITSLAGEN_Type + " = ? AND " 
    					+ KEY_UITSLAGEN_thuisClub + " = ? AND " + KEY_UITSLAGEN_Datum + " = ?";
    	Cursor mCursor = db.rawQuery(query, new String[] {type, club, datum});
    	if (mCursor != null)
    		mCursor.moveToFirst();
    	
    	return mCursor;    	
    }

    //---retrieves a particular afdeling---
    public Cursor getAfdelingFromClub(String club) throws SQLException 
    {    	
        Cursor mCursor =
                db.query(true, DATABASE_TABLE_CLUBS, new String[] {     
                		KEY_CLUBS_Afdeling}, 
                		KEY_CLUBS_Club + "=" + "'" + club +"'" , 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) 
        {
            mCursor.moveToFirst();
        }
        
        return mCursor;
    }
    
    public Cursor getInfoFromClub(String club) throws SQLException
    {
    	String query = "select * from " + DATABASE_TABLE_CLUBS +  " where " + KEY_CLUBS_Club + " = ?";
    	Cursor mCursor = db.rawQuery(query, new String[] {club});    	
    	
    	 if (mCursor != null) 
         {
             mCursor.moveToFirst();
         }
         
         return mCursor;
    }
    
    public Cursor getInfoFromSpeler(String lidnr) throws SQLException
    {
    	String query = "select * from " + DATABASE_TABLE_SPELERS + " where " + KEY_SPELERS_Lidnr + " = ?";
    	Cursor mCursor = db.rawQuery(query, new String[] {lidnr});
    	
    	if (mCursor != null)
    	{
    		mCursor.moveToFirst();
    	}
    	
    	return mCursor;
    }
    
    public Cursor getAllAfdelingen()
    {
    	Cursor mCursor = 
    		db.query(true, DATABASE_TABLE_CLUBS, new String[] {
    				KEY_CLUBS_Afdeling},
    				null,
    				null,
    				null,
    				null,
    				null,
    				null);
    	if (mCursor != null)
    		mCursor.moveToFirst();
    	
    		return mCursor;
    }
    
    public Cursor getAllWedstrijdDatumsFromAfdeling(String afdeling)
    {
    	String query = "select DISTINCT "+ KEY_UITSLAGEN_Datum + " from " + DATABASE_TABLE_UITSLAGEN +  " where " + KEY_UITSLAGEN_Afdeling + " = ? ORDER BY ID";
    	Cursor mCursor = db.rawQuery(query, new String[] {afdeling});    	
    	
    	 if (mCursor != null) 
         {
             mCursor.moveToFirst();
         }
         
         return mCursor;
    }

    //---updates a club---
    public boolean updateTitle(String club, String afdeling, String adres, String gemeente) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_CLUBS_Afdeling, afdeling);
        args.put(KEY_CLUBS_Club, club);     
        args.put(KEY_CLUBS_Adres, adres);
        args.put(KEY_CLUBS_Gemeente, gemeente);
        return db.update(DATABASE_TABLE_CLUBS, args, 
                         KEY_CLUBS_Club + "=" + club, null) > 0;
    }
    
    public long insertUitslag(String thuisploeg, String uitploeg, String uitslag, String datum, String afdeling, String type) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_UITSLAGEN_thuisClub, thuisploeg);
        initialValues.put(KEY_UITSLAGEN_uitClub, uitploeg);
        initialValues.put(KEY_UITSLAGEN_Uitslag, uitslag);
        initialValues.put(KEY_UITSLAGEN_Datum, datum);
        initialValues.put(KEY_UITSLAGEN_Afdeling, afdeling);
        initialValues.put(KEY_UITSLAGEN_Type, type);
      
        return db.insertWithOnConflict(DATABASE_TABLE_UITSLAGEN, null, initialValues, SQLiteDatabase.CONFLICT_IGNORE);         
    }

    public long deleteClubs()
    {
       return db.delete(DATABASE_TABLE_CLUBS, null, null);
    }

    public long deletePlayers()
    {
       return db.delete(DATABASE_TABLE_SPELERS, null, null);
    }


}

