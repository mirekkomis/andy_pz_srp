package pl.projektzespolowy.srp.fragment;

import pl.projektzespolowy.srp.R;
import pl.projektzespolowy.srp.db.DBHelper;
import pl.projektzespolowy.srp.db.DBOperator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PricesFragment extends Fragment {

	ListView mainList;
	private Typeface tf;
	private String[] descr;
	private String[] prices;
	private String val;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_fragment, null);
		initViews(view, inflater);
		return view;
	}
	
	private void initViews(View view, LayoutInflater inflater)
	{
		descr = getResources().getStringArray(R.array.prices_desc);
		val = getResources().getString(R.string.val);
		DBOperator operator = DBOperator.getInstance(new DBHelper(getActivity()));
		prices = operator.getPrices();
		
		tf =Typeface.createFromAsset(getActivity().getAssets(), "font2.otf");
		View convertView = inflater.inflate(R.layout.prices_element, null);
		
		TextView tw;
		(tw = (TextView)convertView.findViewById(R.id.prices_title))
			.setText("");
		tw.setTypeface(tf);
		
		
		(tw = (TextView)convertView.findViewById(R.id.prices_val1))
			.setText(getResources().getString(R.string.term1));
		tw.setTypeface(tf);
		
		
		(tw = (TextView)convertView.findViewById(R.id.prices_val2))
			.setText(getResources().getString(R.string.term2));
		tw.setTypeface(tf);
		
		
		mainList = (ListView)view.findViewById(R.id.main_site_listView);
		mainList.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);
        mainList.addHeaderView(convertView);
		mainList.setAdapter(new myAdapter(inflater));
	}
	
	class myAdapter extends BaseAdapter
    {
		LayoutInflater inflater;
		public myAdapter(LayoutInflater inflater)
		{
			this.inflater = inflater;
		}

		@Override																// 0   1    2    3  4 
		public int getCount() {													// 01  23   45
			return descr.length;
		}

		@Override
		public Object getItem(int position) {
			return descr[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
				convertView = inflater.inflate(R.layout.prices_element, null);
				
				TextView tw;
				(tw = (TextView)convertView.findViewById(R.id.prices_title))
					.setText(descr[position]);
				tw.setTypeface(tf);
				
				
				(tw = (TextView)convertView.findViewById(R.id.prices_val1))
					.setText(prices[position*2]+val);
				tw.setTypeface(tf);
				
				
				(tw = (TextView)convertView.findViewById(R.id.prices_val2))
					.setText(prices[position*2+1]+val);
				tw.setTypeface(tf);
				
				return convertView;
			
		}
    	
    }
	
}
