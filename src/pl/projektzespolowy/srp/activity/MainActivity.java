package pl.projektzespolowy.srp.activity;

import pl.projektzespolowy.srp.R;
import pl.projektzespolowy.srp.fragment.ContactFragment;
import pl.projektzespolowy.srp.fragment.GalleryFragment;
import pl.projektzespolowy.srp.fragment.NewsFragment;
import pl.projektzespolowy.srp.fragment.PricesFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;

public class MainActivity extends FragmentActivity {
	
	DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
	Typeface tf;
	LayoutInflater inflater;
	int selected = 0;
    
	String[] catNames;
	
	TextView title_view;
	
	 public boolean onCreateOptionsMenu(Menu menu) {
		 menu.add("")
         .setIcon(android.R.drawable.ic_menu_share)
         .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	
		 return true;
	    }
	 
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		 if(item.getItemId() == 0)
		 {
			 Intent sendIntent = new Intent();
			 sendIntent.setAction(Intent.ACTION_SEND);
			 sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message));
			 sendIntent.setType("text/plain");
			 startActivity(sendIntent);
		 }
		 
		 return super.onMenuItemSelected(featureId, item);
	}

	 
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main_layout);
	        inflater = (LayoutInflater) getApplicationContext()
	        		.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	        
	        tf =Typeface.createFromAsset(getAssets(), "font2.otf");
	        catNames = getResources().getStringArray(R.array.menu_names);
	        
	        
	        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	        mDrawerList = (ListView) findViewById(R.id.listview_drawer);
	        
	        SettingsAdapter ada = new SettingsAdapter();
	        
	        mDrawerList.setAdapter(ada);
	        
	        SpannableString s = new SpannableString("Aktualności");
	        s.setSpan(tf, 0, s.length(),
	                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	        
	        getActionBar().setHomeButtonEnabled(true);
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        getActionBar().setTitle(s);
	        
	        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
	                R.drawable.cust_home_indi, R.string.app_name,
	                R.string.app_name) {
	 
	            public void onDrawerClosed(View view) {
	                super.onDrawerClosed(view);
	            }
	 
	            public void onDrawerOpened(View drawerView) {
	                getActionBar().setTitle("TiT");
	                getActionBar().setLogo(R.drawable.cust_home_indi);
	                super.onDrawerOpened(drawerView);
	            }
	        };
	        
	        
	        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener()
	        {
				@Override public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					selected = arg2;
					((SettingsAdapter)mDrawerList.getAdapter()).notifySet();
					mDrawerLayout.closeDrawers();
					selectCathegory(arg2);
					
				}
	        });
	        selectCathegory(0);
	        
	    }
	
	    private void selectCathegory(int catId)
	    {
	    	FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    	
	    	setTitle(catNames[catId]);
	        
	    	switch (catId) {
			case 0 :
				NewsFragment fag = new NewsFragment();
				ft.replace(R.id.content_frame, fag);
				break;
			case 1 :
				GalleryFragment fag1 = new GalleryFragment();
				ft.replace(R.id.content_frame, fag1);
				break;
			case 2 :
				PricesFragment fag3 = new PricesFragment();
				ft.replace(R.id.content_frame, fag3);
				break;
			case 4 :
				ContactFragment fag2 = new ContactFragment();
				ft.replace(R.id.content_frame, fag2);
				setTitle(getResources().getString(R.string.app_name));
				break;
			default:
				break;
			}
	    	 ft.commit();
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        if (item.getItemId() == android.R.id.home) {
	 
	            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
	                mDrawerLayout.closeDrawer(mDrawerList);
	            } else {
	                mDrawerLayout.openDrawer(mDrawerList);
	            }
	        }
	 
	        return super.onOptionsItemSelected(item);
	    }
	    
	    private void setTitle(String title)
	    {
	    	if(title_view == null)
	    	{
		    	this.getActionBar().setDisplayShowCustomEnabled(true);
		    	this.getActionBar().setDisplayShowTitleEnabled(false);
	
		    	LayoutInflater inflator = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	View v = inflator.inflate(R.layout.actionbar_top, null);
	
		    	//if you need to customize anything else about the text, do it here.
		    	//I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
		    	
		    	(title_view = (TextView)v.findViewById(R.id.myaction_title)).setText(title);
		    	title_view.setTypeface(tf);
		    	//assign the view to the actionbar
		    	this.getActionBar().setCustomView(v);
	    	}
	    	else 
	    		title_view.setText(title);
	    }
	    
	    class SettingsAdapter extends BaseAdapter
	    {
	    	
	    	int[] icons = new int[]
	    			{
	    			R.drawable.ic_news,
	    			R.drawable.ic_gallery,
	    			R.drawable.ic_price,
	    			R.drawable.ic_booking,
	    			R.drawable.ic_info,
	    			R.drawable.ic_login};
	    	
	    	
	    	
	    	public SettingsAdapter()
	    	{
	    		
	    	}
	    	
			@Override
			public int getCount() {
				return catNames.length;
			}

			@Override
			public Object getItem(int position) {
				return catNames[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				
				convertView = inflater.inflate(R.layout.menu_element, null);
				
				((ImageView)convertView.findViewById(R.id.menu_symbol)).setImageResource(icons[position]);
				TextView t;
				(t=(TextView)convertView.findViewById(R.id.menu_name)).setText(catNames[position]);
				t.setTypeface(tf);
				
				
				if(position == selected)
					((View)convertView.findViewById(R.id.menu_selected)).setVisibility(View.VISIBLE);
				else
					((View)convertView.findViewById(R.id.menu_selected)).setVisibility(View.INVISIBLE);	
				
				return convertView;
			}
	    	
			public void notifySet(){
				notifyDataSetChanged();
			}
	    }
	    
	    
}
