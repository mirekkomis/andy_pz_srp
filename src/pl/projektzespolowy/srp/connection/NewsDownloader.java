package pl.projektzespolowy.srp.connection;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.projektzespolowy.srp.db.DBOperator;
import pl.projektzespolowy.srp.db.News;
import pl.projektzespolowy.srp.db.OpeningHours;
import pl.projektzespolowy.srp.db.PricesTable;

public class NewsDownloader {

	public static final String url = "http://serwer1326625.home.pl/scripts/act.php";
	public static final String upurl = "http://serwer1326625.home.pl/scripts/up.php";
	public static final String downurl = "http://serwer1326625.home.pl/scripts/down.php";
	

	public static News[] downloadNews()
	{
		JSONArray news = null;
		ArrayList<News> newsList = new ArrayList<News>();
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(url);
		
		News[] output = null;
		
		try {
			news = json.getJSONArray(News.NAME_TAG);
            for(int i = 0; i < news.length(); i++){
                JSONObject c = news.getJSONObject(i);
                
                News single = new News(
                		c.getLong(News.ID_TAG), 
                		c.getString(News.HEADER_TAG), 
                		c.getString(News.CONTENT_TAG), 
                		c.getString(News.DATA_TAG));
                
                newsList.add(single);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		output = new News[newsList.size()];
		newsList.toArray(output);
		return output;
	}
	
	public static void updatePrices()
	{
		JSONArray update = null;
		ArrayList<String[]> prices = new ArrayList<String[]>();
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(upurl);
		
		
		try {
            // Getting Array of Contacts
			update = json.getJSONArray(PricesTable.TAG_TABLE);   
            // looping through All Contacts
            for(int i = 0; i < update.length(); i++){
                JSONObject c = update.getJSONObject(i);
                
                String[] single = new String[]{
                		c.getString(PricesTable.TAG_TYPE), 
                		c.getString(PricesTable.TAG_TARIFF), 
                		c.getString(PricesTable.TAG_VAL)};
                
                prices.add(single);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		String[] types = new String[prices.size()];
		String[] tariffs = new String[prices.size()];
		String[] pricess = new String[prices.size()];
		
		String[] out;
		
		for(int i = 0; i < prices.size(); i++)
		{
			out = prices.get(i);
			types[i] = out[0];
			tariffs[i] = out[1];
			pricess[i] = out[2];
		}
		
		DBOperator.getInstance(null).updatePrices(types, tariffs, pricess);
	}
	
	public static void updateOpeningHours()
	{
		JSONArray update = null;
		ArrayList<String[]> prices = new ArrayList<String[]>();
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(downurl);
		
		
		try {
            // Getting Array of Contacts
			update = json.getJSONArray(OpeningHours.TAG_TABLE);   
            // looping through All Contacts
            for(int i = 0; i < update.length(); i++){
                JSONObject c = update.getJSONObject(i);
                
                String[] single = new String[]{
                		c.getString(OpeningHours.TAG_DAY), 
                		c.getString(OpeningHours.TAG_OPEN).substring(0, 5), 
                		c.getString(OpeningHours.TAG_CLOSE).substring(0, 5)};
                
                prices.add(single);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
		
		String[] types = new String[prices.size()];
		String[] tariffs = new String[prices.size()];
		String[] pricess = new String[prices.size()];
		
		String[] out;
		
		for(int i = 0; i < prices.size(); i++)
		{
			out = prices.get(i);
			types[i] = out[0];
			tariffs[i] = out[1];
			pricess[i] = out[2];
		}
		
		DBOperator.getInstance(null).updateHours(types, tariffs, pricess);
	}
}
