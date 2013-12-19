package pl.projektzespolowy.srp.db;

import android.content.ContentValues;

public class News {

	public static final String NAME_TAG = "news";
	public static final String NAME_TABLE = "aktualnosci";
	public static final String ID_TAG = "idAktualnosci";
	public static final String HEADER_TAG = "naglowek";
	public static final String CONTENT_TAG = "tresc";
	public static final String ISACTUAL_TAG = "czyAktualne";
	public static final String DATA_TAG = "Data";
	
	public static final String CREATE_TABLE_NEWS = "CREATE TABLE "+NAME_TABLE+"("
			+ID_TAG+" integer primary key, "
			+HEADER_TAG+" text NOT NULL,"
			+CONTENT_TAG+" text NOT NULL,"
			+DATA_TAG+" text DEFAULT NULL); ";
		
	public static final String INSERT_NEWS = "INSERT INTO "+NAME_TABLE+" VALUES";
		
	public static final String DROP_TABLE_NEWS = "DROP TABLE IF EXISTS "+NAME_TABLE;
	public static final String TRUNCATE_TABLE_NEWS = "TRUNCATE TABLE IF EXISTS "+NAME_TABLE;
	
	private long _id;
	private String header, content, data;
	private int actual;
	
	public News(long _id, String header, String content, String data) {
		super();
		this._id = _id;
		this.header = header;
		this.content = content;
		this.data = data;
	}

	public long get_id() {
		return _id;
	}

	public String getHeader() {
		return header;
	}

	public String getContent() {
		return content;
	}

	public int getActual(){
		return actual; 
	}
	
	public String getData() {
		return data;
	}
	
	public ContentValues getInsertValues()
	{
		
		ContentValues cw = new ContentValues();
		cw.put(News.ID_TAG, _id);
		cw.put(News.HEADER_TAG, header);
		cw.put(News.CONTENT_TAG, content);
		cw.put(News.DATA_TAG, data);
		
		return cw;
	}
}
