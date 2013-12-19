package pl.projektzespolowy.srp.db;

public class PricesTable {

	public static final String
						PRICES_TABLE = "Prices",
						PRICES_ID = "_ID",
						PRICES_VAL = "Price";
	
	public static final String CREATE_TABLE_PRICES = "CREATE TABLE "+PRICES_TABLE+" ("
			+PRICES_ID+" integer primary key, "
			+PRICES_VAL+" text ); ";
		
	public static final String INSERT_PRICES = "INSERT INTO "+PRICES_TABLE+" VALUES ";
		
	public static final String DELETE_TABLE_PRICES = "DROP TABLE IF EXISTS "+PRICES_TABLE;
	
	public static String update(int id, String val)
	{
		return "UPDATE "+PRICES_TABLE+" SET "+PRICES_VAL
				+"="+val+" WHERE "+PRICES_ID+"="+id;
	}
	
	public static String insert(String value)
	{
		return INSERT_PRICES+" (null,"+value+");";
	}
	
	public static String insert(String[] vals)
	{
		String out = INSERT_PRICES;
		for(int i = 0; i < vals.length-1; i++)
		{
			out += " (null,"+vals[i]+"),";
		}
		out += "(null,"+vals[vals.length-1]+");";
		return out;
	}
}
