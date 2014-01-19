package pl.projektzespolowy.srp.db;

public class PricesTable {

	public static final String[] initial_prices
		= new String[]{"15","7","10"};
	public static final String[] initial_types
	= new String[]{"Normalny","Student","Dziecko"};
	public static final String[] initial_tariffs
	= new String[]{"Otwarty","Otwarty","Otwarty"};
	
	public static final String
						PRICES_TABLE = "Prices",
						PRICES_ID = "_ID",
						PRICES_TYPE = "PersonType",
						PRICES_TARIFF = "Tariff",
						PRICES_VAL = "Price";
	public static final String
			TAG_TABLE = "cennik",
			TAG_TYPE = "OsobaTyp",
			TAG_TARIFF = "Taryfa",
			TAG_VAL = "Cena";
	
	public static final String CREATE_TABLE_PRICES = "CREATE TABLE "+PRICES_TABLE+" ("
			+PRICES_ID+" integer primary key, "
			+PRICES_TYPE+" text, "
			+PRICES_TARIFF+" text, "
			+PRICES_VAL+" text ); ";
		
	public static final String INSERT_PRICES = "INSERT INTO "+PRICES_TABLE+" VALUES ";
		
	public static final String DELETE_TABLE_PRICES = "DROP TABLE IF EXISTS "+PRICES_TABLE;
	
	public static String update(int id, String personType, String tariff, String price)
	{
		return "UPDATE "+PRICES_TABLE+" SET "
					+PRICES_VAL+"="+price+", "
					+PRICES_TYPE+"="+personType+", "
					+PRICES_TARIFF+"="+tariff+" "
					+" WHERE "+PRICES_ID+"="+id;
	}
	
	public static String insert(String personType, String tariff, String price)
	{
		return INSERT_PRICES+" (null, "+personType+", "+tariff+", "+price+");";
	}
	
	public static String initialInsert()
	{
		return insert(initial_types,initial_tariffs,initial_prices);
	}
	
	public static String insert(String[] personTypes, String[] tariff, String[] prices)
	{
		String out = INSERT_PRICES;
		for(int i = 0; i < prices.length-1; i++)
		{
			out += " (null, \""+personTypes[i]+"\", \""+tariff[i]+"\", \""+prices[i]+"\"),";
		}
		int i = prices.length-1;
		out += "(null, \""+personTypes[i]+"\", \""+tariff[i]+"\", \""+prices[i]+"\");";
		return out;
	}
}
