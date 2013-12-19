package pl.projektzespolowy.srp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	 private static final String DATABASE_NAME = "dolphin_db";
	 private static final int DATABASE_VERSION = 1;
	 
	 
	 String[] startPrices = 
		 {
			"15", "18",
			"9", "10",
			"11", "12",
			"9", "10",
			"9", "10"
		 };
	 
	 public DBHelper(Context context) 
	 {
		    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	 }


	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL(News.CREATE_TABLE_NEWS);
		db.execSQL(PricesTable.CREATE_TABLE_PRICES);
		db.execSQL(PricesTable.insert(startPrices));
	}


	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
