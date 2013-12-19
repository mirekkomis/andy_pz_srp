package pl.projektzespolowy.srp.db;



import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOperator {

	private SQLiteDatabase database;
	private SQLiteOpenHelper repo;
	
	private static DBOperator instance = null;
	
	
	public static DBOperator getInstance(SQLiteOpenHelper helper){
		return instance != null ? instance : (instance = new DBOperator(helper));
	}
	
	private DBOperator(SQLiteOpenHelper helper)
	{
		repo = helper;
	}
	
	public void open()
	{
		database = repo.getWritableDatabase();
	}
	
	public void close()
	{
		repo.close();
	}
	
	public Cursor getPricesCursor()
	{
		String query = "SELECT * FROM "+PricesTable.PRICES_TABLE;
		return database.rawQuery(query, null);
	}
	
	public String[] getPrices()
	{
		open();
		Cursor cursor = getPricesCursor();
		int ccount = cursor.getCount();
		String[] scores = new String[ccount];
		cursor.moveToFirst();
		for(int i = 0; i<ccount; i++)
		{
			scores[i] =  cursor.getString(1);
			cursor.moveToNext();
		}
		close();
		return scores;
	}
	
	public Cursor getNewsCursor()
	{
		String query = "SELECT * FROM "+News.NAME_TABLE;
		return database.rawQuery(query, null);
	}
	
	public News[] getNews()
	{
		open();
		Cursor cursor = getNewsCursor();
		int ccount = cursor.getCount();
		News[] scores = new News[ccount];
		cursor.moveToFirst();
		for(int i = 0; i<ccount; i++)
		{
			scores[i] =  new News(
					cursor.getLong(0), 
					cursor.getString(1), 
					cursor.getString(2), 
					cursor.getString(3));
			cursor.moveToNext();
		}
		close();
		return scores;
	}
	
	public void updateNews(News[] table)
	{
		open();
		database.rawQuery(News.DROP_TABLE_NEWS, null);
		for(News n : table)
		{
			database.rawQuery(n.getInsertQuery(), null);
		}
		close();
	}
}
