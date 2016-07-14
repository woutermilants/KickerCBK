package Android.KickerCBKL;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class HTMLparser
{

	public void parseClubsAfdeling(Context context, ToastHandler toastHandler) throws Exception
	{

		DBAdapter db = new DBAdapter(context);
		db.open();
        db.deleteClubs();


        Handler hm = toastHandler.returnHandler();
        Message m = new Message();
        Bundle bundle = new Bundle(1);
        hm.handleMessage(new Message());

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		URL url = new URL("http://www.cbkregio-oost.be/index.php?page=adressen");
		BufferedReader reader = null;
		StringBuilder builder = new StringBuilder();
		try
		{
			reader = new BufferedReader(new InputStreamReader(url.openStream(),
					"UTF-8"));
			for (String line; (line = reader.readLine()) != null;)
			{
				builder.append(line.trim());
			}
		}
		finally
		{
			if (reader != null)
				try
				{
					reader.close();
				}
				catch (IOException logOrIgnore)
				{
				}
		}

		String start = ";afdeling=";
		String end = "\">";
		int startIndexAfdeling = 0;
		while (startIndexAfdeling != -1)
		{
			String part = builder.substring(builder.indexOf(start,
					startIndexAfdeling + 1) + start.length());
			startIndexAfdeling = builder.indexOf(start, startIndexAfdeling + 1);

			String afdeling = part.substring(0, part.indexOf(end));
			System.out.println(afdeling);
			
			

			String startClubInfoEven = "class=\"even\">";
			String startClubInfoOdd = "class=\"odd\">";
			String endClubInfo = "</TD>";
			int startIndexClubInfo = 0;
			String Clubnaam = null;
			String Lokaal = null;
			String Adres = null;
			String Gemeente = null;
			String Telefoon = null;
			int index = 0;
			Boolean even = true;

			// StrictMode.ThreadPolicy policy = new
			// StrictMode.ThreadPolicy.Builder().permitAll().build();
			// StrictMode.setThreadPolicy(policy);
			URL urlClubInfo = new URL(
					"http://www.cbkregio-oost.be/index.php?page=adressen&afdeling="
							+ afdeling);
			BufferedReader readerClubInfo = null;
			StringBuilder builderClubInfo = new StringBuilder();
			try
			{
				readerClubInfo = new BufferedReader(new InputStreamReader(
						urlClubInfo.openStream(), "UTF-8"));
				for (String line; (line = readerClubInfo.readLine()) != null;)
				{
					builderClubInfo.append(line.trim());
				}
			}
			finally
			{
				if (readerClubInfo != null)
					try
					{
						readerClubInfo.close();
					}
					catch (IOException logOrIgnore)
					{
					}
			}

			while (startIndexClubInfo != -1)
			{

				String partClubInfo;
				if (even == true)
				{
					partClubInfo = builderClubInfo.substring(builderClubInfo
							.indexOf(startClubInfoEven, startIndexClubInfo + 1)
							+ startClubInfoEven.length());
					startIndexClubInfo = builderClubInfo.indexOf(
							startClubInfoEven, startIndexClubInfo + 1);
				}
				else
				{
					partClubInfo = builderClubInfo.substring(builderClubInfo
							.indexOf(startClubInfoOdd, startIndexClubInfo + 1)
							+ startClubInfoOdd.length());
					startIndexClubInfo = builderClubInfo.indexOf(
							startClubInfoOdd, startIndexClubInfo + 1);
				}

				String ClubInfo = partClubInfo.substring(0,
						partClubInfo.indexOf(endClubInfo));
				// System.out.println(ClubInfo);

				if (index % 5 == 0)
					Clubnaam = ClubInfo;
				else if (index % 5 == 1)
					Lokaal = ClubInfo;
				else if (index % 5 == 2)
				{
					Adres = ClubInfo.substring(0, ClubInfo.indexOf(","));
					Gemeente = ClubInfo.substring(ClubInfo.indexOf(",") + 2);
				}
				else if (index % 5 == 3)
				{
					Telefoon = ClubInfo;
					long id;
					System.out.println(afdeling + Clubnaam + Lokaal + Adres
							+ Gemeente + Telefoon);
					id = db.insertClubInAfdeling(afdeling, Clubnaam, Lokaal,
							Adres, Gemeente, Telefoon);
				}
				else
				{
					System.out.println("google maps: " + ClubInfo);
					if (even == true)
						even = false;
					else
						even = true;
				}

				index++;
				System.out.println(index);
			}
		}
		db.close();

	}

	public void parseSpelers(Context context) throws Exception
	{
		DBAdapter db = new DBAdapter(context);
		db.open();
        db.deletePlayers();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Cursor c = db.getAllAfdelingen();

		while (c.isAfterLast() == false)
		{
			int indexSpelerInfo = 0;
			BufferedReader reader = null;
			StringBuilder builder = new StringBuilder();
			String spelersURL = "http://cbkregio-oost.be/index.php?page=spelerslijst&afdeling="
					+ c.getString(0);
			System.out.println("url" + c.getString(0));
			URL url = new URL(spelersURL);
			System.out.println(c.getString(0));

			try
			{
				reader = new BufferedReader(new InputStreamReader(
						url.openStream(), "UTF-8"));
				for (String line; (line = reader.readLine()) != null;)
				{
					builder.append(line.trim());
				}
			}
			finally
			{
				if (reader != null)
					try
					{
						reader.close();
					}
					catch (IOException logOrIgnore)
					{
					}
			}

			int spelerInfoIndex = 0;
			int teller = 0;

			String spelerInfoStart = "<tr class=\"spelerslijstodd\"><td class=\"spelerslijstodd\" align=\"center\">";
			String spelerInfoEnd = "/A></td></tr>";

			String spelerInfoInnerEnd = "</td><td class=\"spelerslijstodd\" >";

			String spelerInfoInnerEnd2 = "</td><td class=\"spelerslijstodd\" align=\"center\">";

			String spelerInfo = builder.substring(
					builder.indexOf(spelerInfoStart, spelerInfoIndex)
							+ spelerInfoStart.length(),
					builder.indexOf(spelerInfoEnd, spelerInfoIndex)
							+ spelerInfoEnd.length());// ,
														// builder.indexOf(spelerInfoEnd));
			spelerInfoIndex = builder.indexOf(spelerInfoEnd, spelerInfoIndex)
					+ spelerInfoEnd.length() + 1;

			while (spelerInfoIndex != -1)
			{

				indexSpelerInfo = 0;
				String spelerAfdeling = spelerInfo.substring(0, (spelerInfo
						.substring(indexSpelerInfo))
						.indexOf(spelerInfoInnerEnd));
				indexSpelerInfo = spelerInfo.indexOf(spelerInfoInnerEnd)
						+ spelerInfoInnerEnd.length();

				String spelerNaam = (spelerInfo.substring(indexSpelerInfo))
						.substring(0, spelerInfo.substring(indexSpelerInfo)
								.indexOf(spelerInfoInnerEnd));
				indexSpelerInfo += spelerNaam.length()
						+ spelerInfoInnerEnd.length();

				String spelerVoorNaam = (spelerInfo.substring(indexSpelerInfo))
						.substring(0, spelerInfo.substring(indexSpelerInfo)
								.indexOf(spelerInfoInnerEnd2));
				indexSpelerInfo += spelerVoorNaam.length()
						+ spelerInfoInnerEnd2.length();

				String spelerClub = (spelerInfo.substring(indexSpelerInfo))
						.substring(0, spelerInfo.substring(indexSpelerInfo)
								.indexOf(spelerInfoInnerEnd));
				indexSpelerInfo += spelerClub.length()
						+ spelerInfoInnerEnd.length();

				String spelerLidnr = (spelerInfo.substring(indexSpelerInfo))
						.substring(0, spelerInfo.substring(indexSpelerInfo)
								.indexOf(spelerInfoInnerEnd));
				indexSpelerInfo += spelerLidnr.length()
						+ spelerInfoInnerEnd.length();

				String spelerGeboorteDatum = (spelerInfo
						.substring(indexSpelerInfo))
						.substring(0, spelerInfo.substring(indexSpelerInfo)
								.indexOf(spelerInfoInnerEnd));
				indexSpelerInfo += spelerGeboorteDatum.length()
						+ spelerInfoInnerEnd.length();

				String spelerRanking = (spelerInfo.substring(indexSpelerInfo))
						.substring(0, spelerInfo.substring(indexSpelerInfo)
								.indexOf(spelerInfoInnerEnd2));
				indexSpelerInfo += spelerRanking.length()
						+ spelerInfoInnerEnd2.length();
				System.out.println(spelerAfdeling + spelerNaam + spelerVoorNaam
						+ spelerClub + spelerLidnr + spelerGeboorteDatum
						+ spelerRanking);
				db.insertSpeler(spelerNaam, spelerVoorNaam, spelerClub,
						spelerAfdeling, spelerLidnr, spelerGeboorteDatum,
						spelerRanking);
				spelerInfo = builder.substring(builder.indexOf(spelerInfo)
						+ spelerInfo.length());

				if (spelerInfo.substring(0, 100).indexOf("spelerslijstodd") == -1)
				{
					spelerInfoStart = "<tr class=\"spelerslijsteven\"><td class=\"spelerslijsteven\" align=\"center\">";
					spelerInfoInnerEnd = "</td><td class=\"spelerslijsteven\" >";
					spelerInfoInnerEnd2 = "</td><td class=\"spelerslijsteven\" align=\"center\">";
				}
				else
				{
					spelerInfoStart = "<tr class=\"spelerslijstodd\"><td class=\"spelerslijstodd\" align=\"center\">";
					spelerInfoInnerEnd = "</td><td class=\"spelerslijstodd\" >";
					spelerInfoInnerEnd2 = "</td><td class=\"spelerslijstodd\" align=\"center\">";
				}
				
				if (spelerInfo.indexOf(spelerInfoEnd) != -1)
				{
					spelerInfo = spelerInfo.substring(
							spelerInfoStart.length(),
							spelerInfo.indexOf(spelerInfoEnd)
									+ spelerInfoEnd.length());
					spelerInfoIndex = builder.indexOf(spelerInfoEnd,
							spelerInfoIndex) + spelerInfoEnd.length() + 1;
				}
				else
				{
					break;
				}

				teller++;
			}
			c.moveToNext();
		}

		c.close();
		db.close();
	}

	public void parseUitslagen(Context context) throws Exception
	{
		DBAdapter db = new DBAdapter(context);
		db.open();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Cursor c = db.getAllAfdelingen();

		while (c.isAfterLast() == false)
		{
	//		int indexUitslagen = 0;
			BufferedReader reader = null;
			StringBuilder builder = new StringBuilder();
			String uitslagenURL = "http://cbkregio-oost.be/index.php?page=competitie&afdeling="
					+ c.getString(0);
			System.out.println("url" + c.getString(0));
			URL url = new URL(uitslagenURL);
			System.out.println(c.getString(0));

			try
			{
				reader = new BufferedReader(new InputStreamReader(
						url.openStream(), "UTF-8"));
				Boolean startWriting = false;
				for (String line; (line = reader.readLine()) != null;)
				{
					if (startWriting == false && (line.contains("<TR><TD colspan=\"4\" class=\"th\">&nbsp;<img src=\"images/rank_even.gif\">&nbsp;")) )
						startWriting = true;
					
					if (startWriting)
					builder.append(line.trim());
				}
			}
			finally
			{
				if (reader != null)
					try
					{
						reader.close();
					}
					catch (IOException logOrIgnore)
					{
					}
			}

//			System.out.println(builder);
			int uitslagenIndex = 0;
//			int teller = 0;

			String uitslagenStart = "<TR><TD colspan=\"4\" class=\"th\">&nbsp;<img src=\"images/rank_even.gif\">&nbsp;";
//			String uitslagenEnd = "contact op te";
//			String uitslagenInnerStart = "<TR><TD class=\"content\" width=\"150px;\">";
//			String uitslagenInnerEnd = "<img src=\"images/vergrootglas16.png\" alt=\"wedstrijdblad bekijken\" border=\"0\"></A></TD></TR>";

			String thuisClubEnd = "</TD><TD class=\"content\">-</TD><TD class=\"content\" width=\"150px;\">";
			String uitClubEnd = "</TD><TD class=\"uitslag\"  width=\"75px;\">";
			String uitslagEnd = "</TD><TD class=\"content2\" width=\"400px;\" >";
			//String uitslagenDatumEnd = " - Competitie</TD><TD class=\"th\">Opmerking</TD><TD class=\"th\"></TD></TR><TR><TD class=\"content\" width=\"150px;\">";

//			String uitslagen = builder.substring(
//					builder.indexOf(uitslagenStart),
//					builder.indexOf(uitslagenEnd));
//			String uitslagen2 = uitslagen.substring(
//					uitslagen.indexOf(uitslagenInnerStart)
//							+ uitslagenInnerStart.length(),
//					uitslagen.indexOf(uitslagenInnerEnd));
			
			uitslagenIndex = uitslagenStart.length();
			//
			
			String datumEnd = " - Competitie";
			String thuisClubBegin = "<TR><TD class=\"content\" width=\"150px;\">";
			String uitClubBegin = "</TD><TD class=\"content\" width=\"150px;\">";
			String uitslagBegin = "</TD><TD class=\"uitslag\"  width=\"75px;\">";
			//String wedstrijdEnd = "<img src=\"images/vergrootglas16.png\" alt=\"wedstrijdblad bekijken\" border=\"0\">";
			String wedstrijdEnd = "></TD></TR>";
			int indexDatum = builder.indexOf(datumEnd);
			int indexDat=-13;
			
//			int indexClub = builder.indexOf(thuisClubBegin);
			
			outerWhile:
			while (indexDat != -1)
			{
				indexDatum += indexDat +datumEnd.length();
				String Datum = builder.substring(indexDatum-10, indexDatum);
				indexDat = (builder.substring(indexDatum+datumEnd.length())).indexOf(datumEnd);				
				System.out.println(Datum);
				uitslagenIndex = indexDatum;
				int uitslagIndex=0;
				
				for(int i=0; i<6; i++)
				{
					uitslagenIndex += uitslagIndex;
					String thuisClub = builder.substring(uitslagenIndex).substring(builder.substring(uitslagenIndex).indexOf(thuisClubBegin)+thuisClubBegin.length(), builder.substring(uitslagenIndex).indexOf(thuisClubEnd));
					System.out.println(thuisClub);
					String uitClub =  builder.substring(uitslagenIndex).substring(builder.substring(uitslagenIndex).indexOf(uitClubBegin)+uitClubBegin.length(), builder.substring(uitslagenIndex).indexOf(uitClubEnd));
					System.out.println(uitClub);
					String uitslag =  builder.substring(uitslagenIndex).substring(builder.substring(uitslagenIndex).indexOf(uitslagBegin)+uitslagBegin.length(), builder.substring(uitslagenIndex).indexOf(uitslagEnd));
					System.out.println(uitslag);
					uitslagIndex = builder.substring(uitslagenIndex+wedstrijdEnd.length()).indexOf(wedstrijdEnd) + wedstrijdEnd.length();	
					if(uitslag.length() <= 3)
						break outerWhile;
					
					db.insertUitslag(thuisClub, uitClub, uitslag, Datum, c.getString(0), "Competitie");
				}				
			}
			c.moveToNext();
		}

		db.close();
	}

	public void parseKlassement(Context context) throws Exception
	{
		DBAdapter db = new DBAdapter(context);
		db.open();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		Cursor c = db.getAllAfdelingen();

		while (c.isAfterLast() == false)
		{
			int indexSpelerInfo = 0;
			BufferedReader reader = null;
			StringBuilder builder = new StringBuilder();
			String klassementURL = "http://www.cbkregio-oost.be/index.php?page=competitie&afdeling="
					+ c.getString(0);
			System.out.println("url" + c.getString(0));
			URL url = new URL(klassementURL);
			System.out.println(c.getString(0));

			try
			{
				reader = new BufferedReader(new InputStreamReader(
						url.openStream(), "UTF-8"));
				for (String line; (line = reader.readLine()) != null;)
				{
					builder.append(line.trim());
				}
			}
			finally
			{
				if (reader != null)
					try
					{
						reader.close();
					}
					catch (IOException logOrIgnore)
					{
					}
			}

			int klassementIndex = 0;
			int teller = 0;

			String klassementStart = "<TABLE cellspacing=\"1\" cellpadding=\"0\" class=\"klassementtbl\">";
			String klassementEnd = "<td colspan=\"3\" class=\"\" style=\"padding-left: 1px;padding-top: 1px;padding-bottom: 1px;padding-right: 1px;\">";

			String klassementInnerEnd = "<TR>";

			String klassementInnerEnd2 = "</TR>";

			String klassement = builder.substring(builder.indexOf(klassementStart, klassementIndex)	+ klassementStart.length(),	builder.indexOf(klassementEnd, klassementIndex)	+ klassementEnd.length());
			
			// ,
														// builder.indexOf(klassementEnd));
			klassementIndex = builder.indexOf(klassementEnd, klassementIndex)
					+ klassementEnd.length() + 1;

			String plaatsStart = "<TD class=\"th\" >";
			String plaatsEnd = "</TD>";
			
			String datumEnd = " - Competitie";
			String clubEnd = "<TR><TD class=\"content\" width=\"150px;\">";
			int indexDatum = builder.indexOf(datumEnd);
			int indexDat=0;
			
			int indexClub = builder.indexOf(clubEnd);
			while (indexDat != -1)
				{
				indexDatum += indexDat +datumEnd.length();
				String Datum = builder.substring(indexDatum-10, indexDatum);
				indexDat = (builder.substring(indexDatum+datumEnd.length())).indexOf(datumEnd);				
				System.out.println(Datum);
				
				while(indexClub != -1)
				{
					
				}
				
				
				}			
				
				
			
			c.moveToNext();
		}

		c.close();
	}
}
