package Android.KickerCBKL;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class MenuUpdateActivity extends Activity implements OnClickListener {
    /**
     * Called when the activity is first created.
     */


    Button btnUpdate;
    Button btnExit;
    Button btnKlassement;
    Button btnClubs;
    Button btnSpelers;
    Button btnIngaveWedstrijd;
    Button btnUitslagen;
    EditText txtClub;
    Spinner spnAfdelingen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final DBAdapter db = new DBAdapter(this);


        btnIngaveWedstrijd = (Button) findViewById(R.id.btnIngaveWestrijd);
        btnIngaveWedstrijd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {


            }
        });

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
               // Toast.makeText(getApplicationContext(), "Updating", Toast.LENGTH_LONG).show();
                System.out.println("Button Update pressed");
                Intent i = new Intent(MenuUpdateActivity.this, DatabaseActivity.class);
                startActivity(i);
                final ToastHandler toastHandler = new ToastHandler();
                new Thread(new Runnable() {
                    public void run() {
                        HTMLparser HtmlParser = new HTMLparser();
                        try {
                            System.out.println("update button pressed");
                            HtmlParser.parseClubsAfdeling(getApplicationContext(), toastHandler);
                            HtmlParser.parseSpelers(getApplicationContext());
                            //HtmlParser.parseUitslagen(getApplicationContext());
                            //HtmlParser.parseKlassement(getApplicationContext());
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });

        btnUitslagen = (Button) findViewById(R.id.btnUitslagen);
        btnUitslagen.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent decideMatchTypeScreen = new Intent(getApplicationContext(), DecideMatchTypeActivity.class);
                startActivity(decideMatchTypeScreen);
               /* db.open();
                ArrayList<String> arrayAfdelingen = new ArrayList<String>();
                Cursor c = db.getAllAfdelingen();
                c.moveToFirst();

                while (c.isAfterLast() == false) {
                    arrayAfdelingen.add(c.getString(0));
                    c.moveToNext();
                }

                Intent uitslagenScreen = new Intent(getApplicationContext(), UitslagenActivity.class);

                uitslagenScreen.putExtra("afdelingen", arrayAfdelingen);
                startActivity(uitslagenScreen);

                c.close();
                db.close();*/
            }
        });

        btnKlassement = (Button) findViewById(R.id.btnKlassement);
        btnKlassement.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        spnAfdelingen = (Spinner) findViewById(R.id.spnAfdelingen);

        btnClubs = (Button) findViewById(R.id.btnClubs);
        btnClubs.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                db.open();
                ArrayList<String> arrayAfdelingen = new ArrayList<String>();
                Cursor c = db.getAllAfdelingen();
                c.moveToFirst();

                while (c.isAfterLast() == false) {
                    arrayAfdelingen.add(c.getString(0));
                    c.moveToNext();
                }

                Intent clubsScreen = new Intent(getApplicationContext(), ClubsActivity.class);

                clubsScreen.putExtra("afdelingen", arrayAfdelingen);
                startActivity(clubsScreen);

                c.close();
                db.close();

            }
        });

        btnSpelers = (Button) findViewById(R.id.btnSpelers);
        btnSpelers.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                db.open();
                ArrayList<String> arrayAfdelingen = new ArrayList<String>();
                Cursor c = db.getAllAfdelingen();
                c.moveToFirst();

                while (c.isAfterLast() == false) {
                    arrayAfdelingen.add(c.getString(0));
                    c.moveToNext();
                }

                Intent spelersScreen = new Intent(getApplicationContext(), SpelersActivity.class);

                spelersScreen.putExtra("afdelingen", arrayAfdelingen);
                startActivity(spelersScreen);

                c.close();
                db.close();

            }
        });

        btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }


}