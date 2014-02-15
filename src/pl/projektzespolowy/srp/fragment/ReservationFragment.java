package pl.projektzespolowy.srp.fragment;

import java.util.Calendar;

import pl.projektzespolowy.srp.R;
import pl.projektzespolowy.srp.activity.MainActivity;
import pl.projektzespolowy.srp.connection.Login;
import pl.projektzespolowy.srp.connection.Reservation;
import pl.projektzespolowy.srp.connection.Login.LoginResponse;
import pl.projektzespolowy.srp.connection.Reservation.Res;
import pl.projektzespolowy.srp.db.DBHelper;
import pl.projektzespolowy.srp.db.DBOperator;
import pl.projektzespolowy.srp.utils.Settings;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ReservationFragment extends Fragment {

	// ============================= Calendar Fields ===================================================//
	private DaysAdapter calendar_adapter = null;
	private TextView monthName = null;
	private GridView grid;
	int height;
	String[] months;
	Calendar cal;
	//===================================================================================================//
	
	// ================================ Tracks Fields ===================================================//
	private TracksAdapter tracksAdapter = null;
	private TextView dayName = null;
	private ListView tracksView;
	private String[][] open;
	private String[] days;
	//===================================================================================================//
	
	private int minDay, minMonth, minYear, minHour;
	
	AlertDialog noConnection, dialog, resDialog;
	
	ViewFlipper mainFlipper;
	
	long UserId = -1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.reservation_fragment, null);
		
		
		if(Login.getLogin() != null)
		{
			UserId = Login.getLogin().idUzytkownik;
		}
		
		mainFlipper = (ViewFlipper) view.findViewById(R.id.mainReservationFlipper);
		mainFlipper.setVisibility(View.INVISIBLE);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.noconection);
		builder.setCancelable(false);
		builder.setNegativeButton(R.string.refresh, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				noConnection.cancel();
				refreshInfo();
			}
		});
		noConnection = builder.create();
		
		AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
		b.setMessage(R.string.logtores);
		b.setPositiveButton(getActivity().getString(R.string.logtores_yes), 
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				((MainActivity)getActivity()).selectCat(5);
				dialog.cancel();
			}
			
		});
		b.setNegativeButton(getActivity().getString(R.string.logtores_no), 
				new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				dialog.cancel();
			}
			
		});
		dialog = b.create();
		
		initCallendarViews(view, inflater);
		initTracksViews(view, inflater);
		
		refreshInfo();
		return view;
	}
	
	private void refreshInfo()
	{
			(new InfoTask()).execute();
	}
	private void setInfo(String[] info)
	{
		String time[] =  info[1].split(":");
		
		minHour = Integer.parseInt(time[0]);
		minDay = Integer.parseInt(time[1]);
		minMonth = Integer.parseInt(time[2])-1;
		minYear = Integer.parseInt(time[3]);
		
		calendar_adapter.notifyDataSetChanged();
		tracksAdapter.tracksNumber = Integer.parseInt(info[0]);
	}
	
	public boolean onBackPressed()
	{
		if(mainFlipper.getDisplayedChild() == 1)
		{
			int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			int first = cal.getFirstDayOfWeek();
			calendar_adapter.updateCalendar(first, days);
			mainFlipper.showPrevious();
		}
		return false;
	}
	
	private void initCallendarViews(View view, LayoutInflater inflater)
	{
		months = getActivity().getResources().getStringArray(R.array.months);
		cal = Calendar.getInstance();
		
		int month = cal.get(Calendar.MONTH);
		String month_name = months[month];
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int first = cal.getFirstDayOfWeek();
		int year = cal.get(Calendar.YEAR);
		(monthName = (TextView)view.findViewById(R.id.month)).setText(month_name+" "+year);
		
		height = ((getActivity().getResources().getDisplayMetrics().widthPixels)/7)-4;
		
		grid = (GridView) view.findViewById(R.id.calendar_grid);
		grid.setAdapter(calendar_adapter = new DaysAdapter(inflater,first,days));
		calendar_adapter.setMonth(month);
		calendar_adapter.setYear(year);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) 
			{
				
				int d = calendar_adapter.getDaynInMonth(arg2);
				int m = calendar_adapter.getMonth();
				int y = calendar_adapter.getYear();
			
				if(m > minMonth || ( m == minMonth && d >= minDay ))
				{
					tracksAdapter.setDate(d, m, y);
					mainFlipper.showNext();
				}
				
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
	
	private void initTracksViews(View view, LayoutInflater inflater)
	{
		DBOperator operator = DBOperator.getInstance(new DBHelper(getActivity()));
		open = operator.getHours();
		days = getActivity().getResources().getStringArray(R.array.week_shorts);
		
		dayName = (TextView)view.findViewById(R.id.day);
		tracksView = (ListView) view.findViewById(R.id.tracks_list);
		tracksView.setAdapter(tracksAdapter = new TracksAdapter(inflater));
		
		((ImageButton)view.findViewById(R.id.prev_day))
		.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				tracksAdapter.prevDay();
			}
		});
	
	((ImageButton)view.findViewById(R.id.next_day))
	.setOnClickListener(new View.OnClickListener() {	
		@Override
		public void onClick(View v) {
			tracksAdapter.nextDay();
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
		{}
		
		cal.set(year, month, 1);
		String month_name = months[cal.get(Calendar.MONTH)];
		int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int first = cal.get(Calendar.DAY_OF_WEEK); 
		//Dummy Java counting days
		first = first == 1 ? 6 : (first-2);
		
		monthName.setText(month_name+" "+year);
		calendar_adapter.updateCalendar(first, days);
		calendar_adapter.setMonth(month);
		calendar_adapter.setYear(year);
		grid.invalidate();
		
	}
	private void prevMonth()
	{
		int month = cal.get(Calendar.MONTH);
		int year = cal.get(Calendar.YEAR);
		if(month > 0 && month > minMonth)
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
		calendar_adapter.updateCalendar(first, days);
		calendar_adapter.setMonth(month);
		calendar_adapter.setYear(year);
		grid.invalidate();
	}
	
	private String[] getOpenHours(int day)
	{
		if(day > 5 || day < 0)
		{
			return new String[]{};
		}
		
		int start = 10 * Integer.parseInt(""+open[day][0].charAt(0)) + Integer.parseInt(""+open[day][0].charAt(1));
		int end = 10 * Integer.parseInt(""+open[day][1].charAt(0)) + Integer.parseInt(""+open[day][1].charAt(1));
		
		String[] out = new String[end - start];
		
		for(int i = start; i < end; i++)
		{
			out[i - start] = i < 10 ? "0"+i+":00" : ""+i+":00";
		}
		return out;
	}
	
	private int getClosingHour(int day)
	{
		if(day < 0 || day > 6)
			return 0;
		else
			return 10 * Integer.parseInt(""+open[day][1].charAt(0)) + Integer.parseInt(""+open[day][1].charAt(1));
	}
	
	class DaysAdapter extends BaseAdapter
	{
		LayoutInflater inflater;
		int firstDay = 0, maxDays;
		int month, year;
		
		public DaysAdapter(LayoutInflater inf, int firstDay, int cnt) 
		{
			inflater = inf;
			this.firstDay = firstDay;
			this.maxDays = cnt + firstDay;
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
			return arg0;
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
				if(month == minMonth && (dayNr < minDay || (dayNr == minDay && (minHour+1) >= getClosingHour(day) )) )
				{
					v.setBackgroundResource(R.drawable.calendar_gray_stroke);
				}
				else if(day == 6)
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
			return v;
		}
		
		
		public void updateCalendar(int first, int cnt)
		{
			firstDay = first;
			this.maxDays  = cnt + firstDay;
			this.notifyDataSetChanged();
		}

		public int getFirstDay() {
			return firstDay;
		}

		public int getMaxDays() {
			return maxDays;
		}
		
		public int getDaynInMonth(int id)
		{
			return (id - firstDay)+1;
		}

		public int getMonth() {
			return month;
		}

		public void setMonth(int month) {
			this.month = month;
		}

		public int getYear() {
			return year;
		}

		public void setYear(int year) {
			this.year = year;
		}
	}
	
	class TracksAdapter extends BaseAdapter
	{
		LayoutInflater inflater;
		String[] openingHours = new String[]{}; 
		int tracksNumber  = 6;
		int day, month, year;
		
		Res[] reservations = new Res[]{};
		
		public TracksAdapter(LayoutInflater inflater) {
			super();
			this.inflater = inflater;
		}

		@Override
		public int getCount() {
			return tracksNumber;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View row;
			TrackHolder holder = null;
			row = inflater.inflate(R.layout.track_element, null);
			((TextView)row.findViewById(R.id.trackNr))
				.setText(""+(arg0+1));
			LinearLayout llayout = (LinearLayout)row.findViewById(R.id.hoursList);
			holder = new TrackHolder(openingHours.length);
			
			if(openingHours.length == 0)
			{
				Button b = (Button)inflater.inflate(R.layout.hour_element, null);
				b.setText(R.string.closed);
				b.setBackgroundResource(R.drawable.calendar_red_stroke);
				b.setPadding(30, 0, 30, 0);
				llayout.addView(b);
			}
			else
			{
				for(int i = 0; i < openingHours.length; i++)
				{
					if(month == minMonth && day == minDay && getIntVal(getMinHour(minHour))+1 >= getIntVal(openingHours[i])) 
					{
						holder.resType[i] = -2;
						holder.hours[i] = (Button)inflater.inflate(R.layout.hour_element, null);
						holder.hours[i].setText(openingHours[i]);
						holder.hours[i].setBackgroundResource(R.drawable.calendar_gray_stroke);
					}
					else
					{
						int resCnt = 0;
						for(Res r : reservations)
						{
							if((r.track-1) == arg0 && r.time.equals(openingHours[i]))
							{
								if(UserId != -1 && r.usr == UserId)
								{
									resCnt=-1;
								}
								else if(r.own && resCnt > -1)
									resCnt+=3;
								else if(resCnt > -1)
									resCnt++;
							}
						}
						
						holder.resType[i] = resCnt;
						holder.hours[i] = (Button)inflater.inflate(R.layout.hour_element, null);
						holder.hours[i].setId(arg0+1);
						holder.hours[i].setText(openingHours[i]);
						holder.hours[i].setBackgroundResource(resCnt == 0 ? 
								R.drawable.calendar_blue_stroke : 
									resCnt < 0 ? R.drawable.calendar_green_stroke : 
										resCnt < 3 ? R.drawable.calendar_yellow_stroke : R.drawable.calendar_red_stroke);
						
						holder.hours[i].setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								reserve(v.getId(), ""+((TextView)v).getText());
							}
						});
					}
					llayout.addView(holder.hours[i]);
				}
			}
			row.setTag(holder);
			return row;
		}
		public String getMinHour(int hour)
		{
			return hour < 10 ? "0"+hour+":00" : hour + ":00"; 
		}
		public void setRes(Res[] r)
		{
			reservations = r;
			notifyDataSetChanged();
		}
		
		public void setDate(int day, int month, int year)
		{
			this.day = day;
			this.month = month;
			this.year = year;
			cal.set(year, month, day);
			
			int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
			dayofweek = dayofweek == 1 ? 6 : (dayofweek-2);
			openingHours = getOpenHours(dayofweek);
			dayName.setText(days[dayofweek]+", "+getDate("."));
			mainFlipper.setVisibility(View.INVISIBLE);
			(new ResTask()).execute(resData());
		}
		
		public void refresh()
		{
			(new ResTask()).execute(resData());
		}
		
		public void nextDay()
		{
			if(day == cal.getActualMaximum(Calendar.DAY_OF_MONTH))
			{
				if(month == 11)
				{
					
				}
				else
				{
					month++;
					day = 1;
				}
			}
			else
			{
				day++;
			}
			
			cal.set(year, month, day);
			int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
			dayofweek = dayofweek == 1 ? 6 : (dayofweek-2);
			openingHours = getOpenHours(dayofweek);
			dayName.setText(days[dayofweek]+", "+getDate("."));
			mainFlipper.setVisibility(View.INVISIBLE);
			(new ResTask()).execute(resData());
		}
		
		public void prevDay()
		{
			if(day == 1)
			{
				if(month == 0 || month <= minMonth)
				{
					
				}
				else
				{
					month--;
					day = cal.getMaximum(Calendar.DAY_OF_MONTH);
				}
			}
			else
			{
				int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
				int preDayOfWeek = dayofweek == 0 ? 6 : dayofweek-1;
				if(month == minMonth && (day > minDay+1 || day == minDay+1 && minHour+1 < getClosingHour(preDayOfWeek)))
					day--;
			}
			
			cal.set(year, month, day);
			int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
			dayofweek = dayofweek == 1 ? 6 : (dayofweek-2);
			openingHours = getOpenHours(dayofweek);
			dayName.setText(days[dayofweek]+", "+getDate("."));
			mainFlipper.setVisibility(View.INVISIBLE);
			(new ResTask()).execute(resData());
		}
		
		public String getDate(String separator)
		{
			return (day < 10 ? "0"+day : ""+day) +separator+ (month < 10 ? "0"+(month+1) : ""+(month+1)) +separator+year;
		}
		
		public String resData()
		{
			return year+"-"+(month < 10 ? "0"+(month+1) : ""+(month+1))+"-"+(day < 10 ? "0"+day : ""+day);
		}
		public String resData(String hour)
		{
			return year+"-"+(month < 10 ? "0"+(month+1) : ""+(month+1))+"-"+(day < 10 ? "0"+day : ""+day)+" "+hour+":00";
		}
	}
	
	public int getIntVal(String hour)
	{
		return 10 * Integer.parseInt(""+hour.charAt(0)) + Integer.parseInt(""+hour.charAt(1));
	}
	
	private class TrackHolder
	{
		public Button[] hours;
		public int[] resType;
		
		public TrackHolder(int hoursCnt)
		{
			this.hours = new Button[hoursCnt];
			this.resType = new int[hoursCnt];
		}
	}
	
	int tmp_trackID = 0;
	String tmp_time = "";
	int tmp_ckeckbox = 0;
	public void reserve(int trackID, String hour)
	{
		if(Login.getLogin()!=null)
		{
			LoginResponse l = Login.getLogin();
			
			tmp_trackID = trackID;
			tmp_time = tracksAdapter.resData(hour);
			
			Res[] res = tracksAdapter.reservations;
			
			int resCnt = 0;
			boolean my = false;
			
			for(Res r : res)
			{
				if(r.track == trackID && r.time.equals(hour))
				{
					if(r.own)
					{
						resCnt+=3;
					}
					else
					{
						resCnt++;
					}
					if(r.usr == l.idUzytkownik)
					{
						my = true;
					}
				}
			}
			int places = resCnt > 3 ? 0 : 3-resCnt;
			
			AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
			View view = getActivity().getLayoutInflater().inflate(R.layout.reservation_dialog, null);
			
			
			((TextView)view.findViewById(R.id.places_val))
				.setText(""+places);
			((TextView)view.findViewById(R.id.begin_val))
				.setText(tracksAdapter.resData()+"\n"+hour);
			((TextView)view.findViewById(R.id.track_val))
				.setText(""+(trackID));
			((TextView)view.findViewById(R.id.user_val))
				.setText(l.mail);
			
			CheckBox box;	
			( box = (CheckBox)view.findViewById(R.id.own_check))
			.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					tmp_ckeckbox = isChecked ? 1 : 0;
				}
			});
			
			if(places == 3)
				box.setVisibility(View.VISIBLE);
			
			((Button)view.findViewById(R.id.cancel))
			.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					resDialog.cancel();
				}
			});
			Button tmp;
			(tmp = (Button)view.findViewById(R.id.reserve))
			.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					resDialog.cancel();
					runReservation(
							Login.getLogin().idUzytkownik,
							tmp_trackID, 
							tmp_time, 
							tmp_ckeckbox);
					
				}
				});
			tmp.setEnabled(places > 0);
			if(my)
			{
				((Button)view.findViewById(R.id.youhaveres1))
					.setVisibility(View.VISIBLE);
				Button but;
				(but = (Button)view.findViewById(R.id.youhaveres2))
					.setVisibility(View.VISIBLE);
				but.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						((MainActivity)getActivity()).selectCat(5);
						resDialog.cancel();
					}
				});
			}
			b.setView(view);
			resDialog = b.create();
			resDialog.show();
		}
		else
		{
			dialog.show();
		}
		
	}
	
	private void runReservation(long idUzytkownik, int track, String data, int own)
	{
		(new ResTask().setType(1).setParams(idUzytkownik, track, data, own)).execute("");
	}

	private class InfoTask extends AsyncTask<Void, Void, Void>
	{
		ProgressDialog pd;
		String[] resp;
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ReservationFragment.this.getActivity());
			pd.setTitle("£¹czenie z serwerem");
			pd.setMessage("Proszê Czekaæ.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			if(Settings.isOnline(getActivity()))
				resp =  Reservation.info();
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			pd.cancel();
			if(resp == null || resp.length == 0)
			{
				noConnection.show();
			}
			else
			{
				setInfo(resp);
				mainFlipper.setVisibility(View.VISIBLE);
				
			}
			super.onPostExecute(result);
		}
	}
	
	private class ResTask extends AsyncTask<String, Void, Void>
	{
		ProgressDialog pd;
		Res[] resp;
		
		int type = 0;
		
		long user;
		int track, own, re = 0;
		String data;
		
		public ResTask setType(int type)
		{
			this.type = type;
			return this;
		}
		
		public ResTask setParams(long idUzytkownik, int track, String data, int own)
		{
			this.user = idUzytkownik;
			this.track = track;
			this.data = data;
			this.own = own;
			return this;
		}
		
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(ReservationFragment.this.getActivity());
			pd.setTitle("£¹czenie z serwerem");
			pd.setMessage("Proszê Czekaæ.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			if(Settings.isOnline(getActivity()))
			{
				if(type ==  0)
				{
					resp = Reservation.getReservations(params[0]);
				}
				else if(type == 1)
				{
					re = Reservation.reserve(user, data, own, track);
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			pd.cancel();
			if(type == 0)
			{
				if(resp == null)
				{
					noConnection.show();
				}
				else
				{
					tracksAdapter.setRes(resp);
					mainFlipper.setVisibility(View.VISIBLE);
					
				}
			}
			else if(type == 1)
			{
				if(re == 0)
				{
					noConnection.show();
				}
				else
				{
					tracksAdapter.refresh();
				}
			}
			super.onPostExecute(result);
		}
	}
}
