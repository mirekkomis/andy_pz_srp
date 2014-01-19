package pl.projektzespolowy.srp.fragment;

import java.util.Locale;

import pl.projektzespolowy.srp.R;
import pl.projektzespolowy.srp.db.DBHelper;
import pl.projektzespolowy.srp.db.DBOperator;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactFragment extends Fragment {
	
	double latitude = 51.117569d, longitude = 17.036761d;
	private Typeface tf;
	private String[][] open;
	LinearLayout lay;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_fragment, null);
		
		
		initViews(view);
		
		
		return view;
	}
	
	private void initViews(View view)
	{
		DBOperator operator = DBOperator.getInstance(new DBHelper(getActivity()));
		open = operator.getHours();
		lay = (LinearLayout) view.findViewById(R.id.open_layout);
		if(open.length == 6)
		{
			TextView tv;
			(tv =(TextView)lay.findViewById(R.id.monday))
				.setText(getActivity().getString(R.string.short_monday)+" "+open[0][0]+"-"+open[0][1]);
			tv.setVisibility(View.VISIBLE);
			(tv = (TextView)lay.findViewById(R.id.tuesday))
				.setText(getActivity().getString(R.string.short_tuesday)+" "+open[1][0]+"-"+open[1][1]);
			tv.setVisibility(View.VISIBLE);
			(tv = (TextView)lay.findViewById(R.id.wensday))
				.setText(getActivity().getString(R.string.short_wensday)+" "+open[2][0]+"-"+open[2][1]);
			tv.setVisibility(View.VISIBLE);
			(tv = (TextView)lay.findViewById(R.id.thursday))
				.setText(getActivity().getString(R.string.short_thursday)+" "+open[3][0]+"-"+open[3][1]);
			tv.setVisibility(View.VISIBLE);
			(tv = (TextView)lay.findViewById(R.id.friday))
				.setText(getActivity().getString(R.string.short_friday)+" "+open[4][0]+"-"+open[4][1]);
			tv.setVisibility(View.VISIBLE);
			(tv = (TextView)lay.findViewById(R.id.saturday))
				.setText(getActivity().getString(R.string.short_saturday)+" "+open[5][0]+"-"+open[5][1]);
			tv.setVisibility(View.VISIBLE);
		}
		tf =Typeface.createFromAsset(getActivity().getAssets(), "font2.otf");
		
		int cnt = lay.getChildCount();
		for(;cnt>=0; cnt--)
		{
			View v = lay.getChildAt(cnt);
			if(v instanceof TextView)
			{
				((TextView) v).setTypeface(tf);
			}
		}
		
		((TextView)view.findViewById(R.id.cotact_info)).setTypeface(tf);
		
		((ImageView)view.findViewById(R.id.contact_map)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+latitude+","+longitude);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				getActivity().startActivity(intent);
				
			}
		});
		
		Button b;
		(b = (Button)view.findViewById(R.id.navi_button)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr="+latitude+","+longitude);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				getActivity().startActivity(intent);
				
			}
		});
		b.setTypeface(tf);
		
		(b = (Button)view.findViewById(R.id.call_button)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String number = "888021123";
			    Intent intent = new Intent(Intent.ACTION_CALL);
			    intent.setData(Uri.parse("tel:" +number));
			    startActivity(intent);
				
			}
		});
		b.setTypeface(tf);
		
		(b = (Button)view.findViewById(R.id.mail_button)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
			            "mailto","kontakt@delfinek.pl", null));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Delfinek");
				startActivity(Intent.createChooser(emailIntent, "Send email..."));
			}
		});
		b.setTypeface(tf);
	}
}
