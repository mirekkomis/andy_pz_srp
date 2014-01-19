package pl.projektzespolowy.srp.fragment;

import java.util.Calendar;

import pl.projektzespolowy.srp.R;
import pl.projektzespolowy.srp.db.DBHelper;
import pl.projektzespolowy.srp.db.DBOperator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ReservationFragment extends Fragment {

	//private ListView lv;
	private DaysAdapter calendar_adapter = null;
	private TextView monthName = null;
	
	private GridView grid;
	
	long curr = 86400000;
	int height;
	
	String[] months;
	
	Calendar cal;
	private String[][] open;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.reservation_fragment, null);
		initViews(view, inflater);
		
		return view;
	}
	
	private void initViews(View view, LayoutInflater inflater)
	{
		
		DBOperator operator = DBOperator.getInstance(new DBHelper(getActivity()));
		open = operator.getHours();
		months = getActivity().getResources().getStringArray(R.array.months);
		cal = Calendar.getInstance();
		
		String month_name = months[cal.get(Calendar.MONTH)];
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int first = cal.getFirstDayOfWeek();
		int year = cal.get(Calendar.YEAR);
		(monthName = (TextView)view.findViewById(R.id.month)).setText(month_name+" "+year);
		
		height = ((getActivity().getResources().getDisplayMetrics().widthPixels)/7)-4;
//		lv = (ListView) view.findViewById(R.id.calendar_list);
//		lv.setAdapter(calendar_adapter = new DaysAdapter(inflater, prepareCalendar(days, first)));
		
		grid = (GridView) view.findViewById(R.id.calendar_grid);
		grid.setAdapter(calendar_adapter = new DaysAdapter(inflater,first,days));// /*prepareCalendar(days, */first));//));
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});
		
		((ImageButton)view.findViewById(R.id.prev_month))
			.setOnClickListener(new View.OnClickListener() {	
				@Override
				public void onClick(View v) {
					prevMonth();
				}
			});
		
		((ImageButton)view.findViewById(R.id.next_month))
		.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				nextMonth();
			}
		});
	}
	
	private void nextMonth()
	{
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		if(month < 11)
			++month;
		else
		{
//			month = 0;
//			++year;
		}
		
		
		cal.set(year, month, 1);
		String month_name = months[cal.get(Calendar.MONTH)];
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int first = cal.get(Calendar.DAY_OF_WEEK); 
		//Dummy Java counting days
		first = first == 1 ? 6 : (first-2);
		
		monthName.setText(month_name+" "+year);
		calendar_adapter.updateCalendar(first, days);//(/*prepareCalendar(days, */first);//);
		grid.invalidate();
		
	}
	private void prevMonth()
	{
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		if(month > 0)
			--month;
		else
		{
//			month = 11;
//			--year;
		}
		
		cal.set(year, month, 1);
		String month_name = months[cal.get(Calendar.MONTH)];
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int first = cal.get(Calendar.DAY_OF_WEEK); 
		//Dummy Java counting days
		first = first == 1 ? 6 : (first-2);
		
		monthName.setText(month_name+" "+year);
		calendar_adapter.updateCalendar(first, days);//(/*prepareCalendar(days, */first);//);
		grid.invalidate();
	}
	
	private int[][] prepareCalendar(int maxDays, int firstDay)
	{
		int day = 0;
		int[][] out = new int[6][7];
		int i = firstDay;
		
		for(int j = 0; j < 6 && day < maxDays; j++)
		{
			for(; i<7 && day<maxDays;i++)
			{
				day++;
				out[j][i] = day;
			}
			i=0;
		}
		
		return out;
	}
	
	class DaysAdapter extends BaseAdapter
	{
		LayoutInflater inflater;
		int firstDay = 0, maxDays;
		//int[][] callendar;
		
		public DaysAdapter(LayoutInflater inf, int firstDay, int cnt) 
		{
			inflater = inf;
			this.firstDay = firstDay;
			this.maxDays = cnt + firstDay;
			//callendar = cal;
		}

		@Override
		public int getCount() {
			return 42;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			TextView v = (TextView)inflater.inflate(R.layout.grid_element, null);
			
			if(arg0 >= this.firstDay && arg0 < this.maxDays)
			{
				v.setVisibility(View.VISIBLE);
				v.setHeight(height);
				int day = arg0%7;
				int dayNr = arg0 - firstDay+1;
				v.setText(""+dayNr);
				if(day == 6)
				{
					v.setBackgroundResource(R.drawable.calendar_red_stroke);
				}
				else
				{
					v.setBackgroundResource(R.drawable.calendar_blue_stroke);
				}
			}
			else
			{
				v.setVisibility(View.INVISIBLE);
			}
			
		
			
//			View v = inflater.inflate(R.layout.calendar_element, null);
//
//			
//			TextView mon = (TextView) v.findViewById(R.id.monday);
//			TextView tue = (TextView) v.findViewById(R.id.tuesday);
//			TextView wen = (TextView) v.findViewById(R.id.wensday);
//			TextView thu = (TextView) v.findViewById(R.id.thursday);
//			TextView fri = (TextView) v.findViewById(R.id.friday);
//			TextView sat = (TextView) v.findViewById(R.id.saturday);
//			TextView sun = (TextView) v.findViewById(R.id.sunday);
//			
//			mon.setHeight(height);
//			if(callendar[arg0][0] > 0)
//			{
//				mon.setBackgroundResource(R.drawable.calendar_blue_stroke);
//				mon.setText(""+callendar[arg0][0]);
//				mon.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						Toast.makeText(getActivity(), "hello", Toast.LENGTH_LONG);
//					}
//				});
//			}
//			else
//			{
//				mon.setVisibility(View.INVISIBLE);
//			}
//			
//			tue.setHeight(height);
//			if(callendar[arg0][1] > 0)
//			{
//				tue.setBackgroundResource(R.drawable.calendar_blue_stroke);
//				tue.setText(""+callendar[arg0][1]);
//			}
//			else
//			{
//				tue.setVisibility(View.INVISIBLE);
//			}
//			
//			wen.setHeight(height);
//			if(callendar[arg0][2] > 0)
//			{
//				wen.setBackgroundResource(R.drawable.calendar_blue_stroke);
//				wen.setText(""+callendar[arg0][2]);
//			}
//			else
//			{
//				wen.setVisibility(View.INVISIBLE);
//			}
//			
//			thu.setHeight(height);
//			if(callendar[arg0][3] > 0)
//			{
//				thu.setBackgroundResource(R.drawable.calendar_blue_stroke);
//				thu.setText(""+callendar[arg0][3]);
//			}
//			else
//			{
//				thu.setVisibility(View.INVISIBLE);
//			}
//			
//			fri.setHeight(height);
//			if(callendar[arg0][4] > 0)
//			{
//				fri.setBackgroundResource(R.drawable.calendar_blue_stroke);
//				fri.setText(""+callendar[arg0][4]);
//			}
//			else
//			{
//				fri.setVisibility(View.INVISIBLE);
//			}
//			
//			sat.setHeight(height);
//			if(callendar[arg0][5] > 0)
//			{
//				sat.setBackgroundResource(R.drawable.calendar_blue_stroke);
//				sat.setText(""+callendar[arg0][5]);
//			}
//			else
//			{
//				sat.setVisibility(View.INVISIBLE);
//			}
//			
//			sun.setHeight(height);
//			if(callendar[arg0][6] > 0)
//			{
//				sun.setBackgroundResource(R.drawable.calendar_red_stroke);
//				sun.setText(""+callendar[arg0][6]);
//			}
//			else
//			{
//				sun.setVisibility(View.INVISIBLE);
//			}
//			
			return v;
		}
		
		
		public void updateCalendar(int first, int cnt)
		{
			//callendar = cal;
			firstDay = first;
			this.maxDays  = cnt + firstDay;
			this.notifyDataSetChanged();
		}
	}
}
