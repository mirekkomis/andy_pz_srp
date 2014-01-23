package pl.projektzespolowy.srp.connection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Reservation {

	public static final String url = "https://serwer1326625.home.pl/scripts/res.php";
	
	private static final String
		TYPE_TAG = "type",
		INFO_TAG = "info",
		TRACKS_TAG = "Ilosc_Torow",
		TIME_TAG = "time",
		DATA_TAG = "data";
	
	public static String[] info()
	{
		JSONArray info = null;
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(
				url, 
				new String[]{TYPE_TAG},
				new String[]{"1"});
		
		String[] output  = new String[]{};
		
		try {
            // Getting Array of Contacts
			info = json.getJSONArray(INFO_TAG);
             
            JSONObject c = info.getJSONObject(0);
            output = new String[]
            		{
            			c.getString(TRACKS_TAG),
            			c.getString(TIME_TAG)
            		};
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		return output;
	}
	
	public static Res[] getReservations(String data)
	{
		Res[] out = null;

		JSONArray res = null;
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(
				url, 
				new String[]{TYPE_TAG, DATA_TAG},
				new String[]{"2", data});
		
		try {
            // Getting Array of Contacts
			res = json.getJSONArray("res");
			out = new Res[res.length()];
             for(int i = 0; i < res.length(); i++)
             {
            	 JSONObject c = res.getJSONObject(i);
            	 out[i] = new Res(
            			 c.getInt("ID"),
            			 c.getInt("User"),
            			 c.getInt("Tor"),
            			 c.getString("Godz"),
            			 c.getInt("Wyl")==1
            			 );
             }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		return out;
	}
	
	public static Res[] getUserReservations(long user)
	{
		Res[] out = null;

		JSONArray res = null;
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(
				url, 
				new String[]{TYPE_TAG, "user"},
				new String[]{"4", ""+user});
		
		try {
            // Getting Array of Contacts
			res = json.getJSONArray("res");
			out = new Res[res.length()];
             for(int i = 0; i < res.length(); i++)
             {
            	 JSONObject c = res.getJSONObject(i);
            	 out[i] = new Res(
            			 c.getInt("ID"),
            			 c.getInt("User"),
            			 c.getInt("Tor"),
            			 c.getString("Godz"),
            			 c.getInt("Wyl")==1
            			 );
             }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
		return out;
	}
	
	public static int reserve(long usr, String data, int own, int track)
	{
		JSONArray res = null;
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(
				url, 
				new String[]{TYPE_TAG, "user", "time", "own", "track" },
				new String[]{"3", ""+usr, data, ""+own, ""+track});
		
		int out = 812;
		
		try {
            // Getting Array of Contacts
			res = json.getJSONArray("result");
			 JSONObject c = res.getJSONObject(0);
	           out = c.getInt(TYPE_TAG);
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		return out;
	}
	
	public static int delete(int id)
	{
		JSONArray res = null;
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(
				url, 
				new String[]{TYPE_TAG, "id",},
				new String[]{"5", ""+id});
		
		int out = 812;
		
		try {
            // Getting Array of Contacts
			res = json.getJSONArray("result");
			 JSONObject c = res.getJSONObject(0);
	           out = c.getInt(TYPE_TAG);
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		return out;
	}
	
	public static class Res
	{
		public int id, usr, track;
		public String time = "";
		public boolean own = false;
		
		public Res(int id, int usr, int track, String time, boolean own)
		{
			this.id = id;
			this.usr = usr;
			this.track = track;
			this.time = time;
			this.own = own;
		}
		
		public String toString()
		{
			return "ID:"+id+"  u:"+usr+"  t:"+track+" tme:"+time+" o:"+own;
		}
	}
}
