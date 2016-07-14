package Android.KickerCBKL;

import android.app.Activity;
import android.os.Bundle;

public class DatabaseActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DBAdapter db = new DBAdapter(this); 
        
        db.open();        
        long id;
        System.out.println("Voor insert KCK UNITED");
   //    id = db.insertSpeler("Milants", "Wouter", "KCK UNITED", "1A", "4803", "12-04-1990", "3P");
     //   id = db.insertClubInAfdeling("2B", "Royal Basterds", "Sporthal De Bloken", "Kortestraat 12", "3830 Wellen", "0478/188395");
     //   id = db.insertClubInAfdeling("2B", "Lindaboys", "Kloosterstraat 4", "3800 Sint-Truiden");
    //    id = db.insertClubInAfdeling("2B", "De Linde", "Kortestraat 12", "3830 Wellen");
    //    id = db.insertClubInAfdeling("2B", "Happy Snooker", "Kloosterstraat 4", "3800 Sint-Truiden");
     //   id = db.insertClubInAfdeling("2B", "Barca Beyne", "Kortestraat 12", "3830 Wellen");
     //   id = db.insertClubInAfdeling("2B", "Cosmos", "Kloosterstraat 4", "3800 Sint-Truiden");
    //    id = db.insertClubInAfdeling("2B", "Cafeeke tongeren", "Kortestraat 12", "3830 Wellen");
     //   id = db.insertClubInAfdeling("2B", "Liquido", "Kloosterstraat 4", "3800 Sint-Truiden");
    //    id = db.insertClubInAfdeling("2B", "Marilyne KC", "Kortestraat 12", "3830 Wellen");
   //     id = db.insertClubInAfdeling("2B", "Poeikes", "Kloosterstraat 4", "3800 Sint-Truiden");
    //    id = db.insertClubInAfdeling("2B", "Star Beyne", "Kortestraat 12", "3830 Wellen");
    //    id = db.insertClubInAfdeling("1A", "Marioshooters", "Kortestraat 12", "3830 Wellen");
        db.close();
        finish();
        
        
    }
    }
