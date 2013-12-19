package pl.projektzespolowy.srp.fragment;

import java.util.Locale;

import pl.projektzespolowy.srp.R;
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
import android.widget.TextView;

public class ContactFragment extends Fragment {
	
	double latitude = 51.117569d, longitude = 17.036761d;
	private Typeface tf;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_fragment, null);
		
		
		initViews(view);
		
		
		return view;
	}
	
	private void initViews(View view)
	{
		
		tf =Typeface.createFromAsset(getActivity().getAssets(), "font2.otf");
		
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
	
//	private void showMap()
//	{
//		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////		builder.setView(view);
//	}
}
