package pl.projektzespolowy.srp.fragment;

import pl.projektzespolowy.srp.R;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GalleryFragment extends Fragment {

	ListView mainList;
	private Typeface tf;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_fragment, null);
		initViews(view, inflater);
		return view;
	}
	
	private void initViews(View view, LayoutInflater inflater)
	{
		tf =Typeface.createFromAsset(getActivity().getAssets(), "font2.otf");
		mainList = (ListView)view.findViewById(R.id.main_site_listView);
        mainList.setAdapter(new myAdapter(inflater));
	}
	
	class myAdapter extends BaseAdapter
    {
		
		int[] pics = new int[]
				{
				R.drawable.pic1,
				R.drawable.pic2,
				R.drawable.pic3,
				R.drawable.pic4
				};
		
		String[] desc = getResources().getStringArray(R.array.gallery_descriptions);
		
		LayoutInflater inflater;
		public myAdapter(LayoutInflater inflater)
		{
			this.inflater = inflater;
		}

		@Override
		public int getCount() {
			return pics.length;
		}

		@Override
		public Object getItem(int position) {
			return desc[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
				convertView = inflater.inflate(R.layout.gallery_element, null);
				
				((ImageView)convertView.findViewById(R.id.gallery_image))
					.setImageResource(pics[position]);
				TextView tw;
				(tw = (TextView)convertView.findViewById(R.id.gallery_description))
					.setText(desc[position]);
				tw.setTypeface(tf);
				
				return convertView;
			
		}
    	
    }
}
