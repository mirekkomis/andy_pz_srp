package pl.projektzespolowy.srp.fragment;

import pl.projektzespolowy.srp.R;
import pl.projektzespolowy.srp.connection.NewsDownloader;
import pl.projektzespolowy.srp.db.DBHelper;
import pl.projektzespolowy.srp.db.DBOperator;
import pl.projektzespolowy.srp.db.News;
import pl.projektzespolowy.srp.utils.Settings;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract.Helpers;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class NewsFragment extends Fragment {

	ListView mainList;
	private Typeface tf;
	private NewsAdapter adapter;
	DBOperator operator;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.list_fragment, null);
		initViews(view, inflater);
		if(Settings.isOnline(getActivity()))
			(new NewsTask()).execute();
		return view;
	}
	
	private void initViews(View view, LayoutInflater inflater)
	{
		tf =Typeface.createFromAsset(getActivity().getAssets(), "font2.otf");
		operator = DBOperator.getInstance(new DBHelper(getActivity()));
		
		mainList = (ListView)view.findViewById(R.id.main_site_listView);
		adapter = new NewsAdapter(inflater);
		adapter.upDateEntries(operator.getNews());
		
        mainList.setAdapter(adapter);
	}
	
	class NewsAdapter extends BaseAdapter
    {
		News[] content;
		
		LayoutInflater inflater;
		public NewsAdapter(LayoutInflater inflater)
		{
			this.inflater = inflater;
		}

		@Override
		public int getCount() {
			return content == null ? 0 : content.length;
		}

		@Override
		public News getItem(int position) {
			return content[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
				View v = inflater.inflate(R.layout.news_item, null);
				TextView tw;
				News n = getItem(position);
				
				tw = (TextView) v.findViewById(R.id.news_title);
				tw.setText(n.getHeader());
				tw.setTypeface(tf);
				
				tw = (TextView) v.findViewById(R.id.news_content);
				tw.setText(n.getContent());
				tw.setTypeface(tf);
				
				tw = (TextView) v.findViewById(R.id.news_data);
				tw.setText(n.getData());
				tw.setTypeface(tf);
				
				
				return v;
			
		}
		public void upDateEntries(News[] entries) {
	          content = entries;
	          notifyDataSetChanged();
	       }
    }
	
	private class NewsTask extends AsyncTask<Void, Void, News[]>
	{

		ProgressDialog prog;
		
		@Override protected void onPreExecute() {
			prog = ProgressDialog.show(getActivity(), "Pobieranie", "Pobieranie Aktualnoœci");
			super.onPreExecute();
		}
		
		@Override protected News[] doInBackground(Void... params) {
			News[] news = NewsDownloader.downloadNews();
			if(news!=null && news.length > 0)
			{
				operator.updateNews(news);
			}
			return operator.getNews();
		}
		
		@Override protected void onPostExecute(News[] result) {
			prog.dismiss();
			adapter.upDateEntries(result);
			super.onPostExecute(result);
		}
	}
}
