package pl.projektzespolowy.srp.connection;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.projektzespolowy.srp.db.News;

public class NewsDownloader {

	public static final String url = "http://192.168.0.15/pz_srp/scripts/act.php";
	

	public static News[] downloadNews()
	{
		JSONArray news = null;
		ArrayList<News> newsList = new ArrayList<News>();
		JSONParser jParser = new JSONParser();
		JSONObject json = jParser.getJSONFromUrl(url);
		
		News[] output = null;
		
		try {
            // Getting Array of Contacts
			news = json.getJSONArray(News.NAME_TAG);
             
            // looping through All Contacts
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
}
