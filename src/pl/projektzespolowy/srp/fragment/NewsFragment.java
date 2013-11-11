package pl.projektzespolowy.srp.fragment;

import pl.projektzespolowy.srp.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;


public class NewsFragment extends Fragment {

	ListView mainList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_fragment, null);
		initViews(view, inflater);
		return view;
	}
	
	private void initViews(View view, LayoutInflater inflater)
	{
		mainList = (ListView)view.findViewById(R.id.main_site_listView);
        mainList.setAdapter(new myAdapter(inflater));
	}
	
	class myAdapter extends BaseAdapter
    {
		LayoutInflater inflater;
		public myAdapter(LayoutInflater inflater)
		{
			this.inflater = inflater;
		}

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
				View v = inflater.inflate(R.layout.news_item, null);
				return v;
			
		}
    	
    }
}
