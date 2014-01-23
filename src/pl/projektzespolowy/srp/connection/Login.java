package pl.projektzespolowy.srp.connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Login {

	public static final String url = "https://serwer1326625.home.pl/scripts/log.php";
	public static final String regurl = "https://serwer1326625.home.pl/scripts/reg.php";
	
	public static final String
		TYPE_TAG = "type",
		ID_KLIENT_TAG = "idKlient",
		IMIE_TAG = "Imie",
		NAZWISKO_TAG = "Nazwisko",
		TEL_TAG = "Telefon",
		ID_UZYTKO = "idUzytkownik",
		MAIL_TAG = "mail";
	
	private static LoginResponse login = null;
	
	public static LoginResponse login(String usr, String pass)
	{
		JSONArray login = null;
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(
				url, 
				new String[]{"user", "pass"}, 
				new String[]{usr,pass});
		
		LoginResponse output = null;
		
		try {
            // Getting Array of Contacts
			login = json.getJSONArray("result");
             
            JSONObject c = login.getJSONObject(0);
            output = new LoginResponse(c.getInt(TYPE_TAG));
            if(output.type)
            {
            	output.mail = usr;
            	output.idKlient = c.getLong(ID_KLIENT_TAG);
            	output.imie = c.getString(IMIE_TAG);
            	output.nazw = c.getString(NAZWISKO_TAG);
            	output.tel = c.getString(TEL_TAG);
            	output.idUzytkownik = c.getLong(ID_UZYTKO);
            	setLogin(output);
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		return output;
	}
	
	public static int register(String usr, String pass, String name, String surn, String phone)
	{
		JSONArray login = null;
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(
				regurl, 
				new String[]{"user", "pass", "name", "surn", "phon"}, 
				new String[]{ usr,	  pass,	  name,   surn,   phone});
		int out = 0;
		try {
            // Getting Array of Contacts
			login = json.getJSONArray("result");
             
            JSONObject c = login.getJSONObject(0);
            out = c.getInt(TYPE_TAG);
            
        } catch (JSONException e) {
            e.printStackTrace();
            out = -1;
        }
		
		return out;
	}
	
	public static void logout()
	{
		setLogin(null);
	}
	
	public static LoginResponse getLogin() {
		return login;
	}

	private static void setLogin(LoginResponse login) {
		Login.login = login;
	}

	public static class LoginResponse
	{
		//private String prefs = "prefs_file";
		public boolean type = false;
		public String mail, imie, nazw, tel;
		public long idKlient, idUzytkownik;
		
		public LoginResponse(int type)
		{
			this.type = type == 1;
		}
		
//		public void saveToSharedPrefs(Activity activity)
//		{
//			SharedPreferences.Editor edit = 
//					activity.getSharedPreferences(prefs, 0).edit();
//			
//			edit.putString(IMIE_TAG, imie);
//			edit.putString(NAZWISKO_TAG, nazw);
//			edit.putString(TEL_TAG, tel);
//			edit.putString(MAIL_TAG, mail);
//			edit.putLong(ID_KLIENT_TAG, idKlient);
//			edit.putLong(ID_UZYTKO, idUzytkownik);
//			
//			edit.commit();
//			
//		}
//		
//		public void logout(Activity activity)
//		{
//			SharedPreferences.Editor edit = 
//					activity.getSharedPreferences(prefs, 0).edit();
//			
//			edit.putString(IMIE_TAG, "");
//			edit.putString(NAZWISKO_TAG, "");
//			edit.putString(TEL_TAG, "");
//			edit.putString(MAIL_TAG, "");
//			edit.putLong(ID_KLIENT_TAG, 0l);
//			edit.putLong(ID_UZYTKO, 0);
//			
//			edit.commit();
//			
//		}
//		
//		public boolean loginFromPrefs(Activity activity)
//		{
//			SharedPreferences pref= 
//					activity.getSharedPreferences(prefs, 0);
//			
//			imie = pref.getString(IMIE_TAG, "");
//			nazw = pref.getString(NAZWISKO_TAG, "");
//			tel = pref.getString(TEL_TAG, "");
//			mail = pref.getString(MAIL_TAG, "");
//			idKlient = pref.getLong(ID_KLIENT_TAG, 0l);
//			idUzytkownik = pref.getLong(ID_UZYTKO, 0);
//			
//			return idKlient != 0 && !mail.contains("");
//		}
	}
}
