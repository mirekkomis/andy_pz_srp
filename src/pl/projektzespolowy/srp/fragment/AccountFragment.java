package pl.projektzespolowy.srp.fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.projektzespolowy.srp.R;
import pl.projektzespolowy.srp.connection.Login;
import pl.projektzespolowy.srp.utils.Settings;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class AccountFragment extends Fragment {

	private Typeface tf;
	private ViewFlipper vf;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.account_fragment, null);
		initViews(view, inflater);
		
		return view;
	}
	
	private void initViews(View view, LayoutInflater inflater)
	{
		tf =Typeface.createFromAsset(getActivity().getAssets(), "font2.otf");
		vf = (ViewFlipper) view.findViewById(R.id.form_flipper);
		
		initLoginLayout(view);
		initLogoutLayout(view);
		initRegisterLayout(view);
		
		Login.LoginResponse loginRes = Login.getLogin();
		if(loginRes != null)
		{
			if(loginRes.equals("null"))
			{
				name.setText(getString(R.string.noval));
			}
			else
			{
				hello.setText(getString(R.string.hello)+" "+loginRes.imie+"!");
				name.setText(loginRes.imie);
			}

			mail.setText(loginRes.mail.equals(null) ? getString(R.string.noval) : loginRes.mail);
			
			surn.setText(loginRes.nazw.equals(null) ? getString(R.string.noval) : loginRes.nazw);
			phone.setText(loginRes.tel.equals(null) ? getString(R.string.noval) : loginRes.tel);
			vf.showPrevious();
		}
		
	}
	
	EditText login, passw;
	
	private void clearLoginForm()
	{
		login.setText("");
		passw.setText("");
	}
	
	private void initLoginLayout(View view)
	{
		LinearLayout login_layout = (LinearLayout) view.findViewById(R.id.login_form);
		int cnt = login_layout.getChildCount();
		for(;cnt>=0; --cnt)
		{
			View v = login_layout.getChildAt(cnt);
			if(v instanceof TextView)
			{
				((TextView)v).setTypeface(tf);
			}
		}
		
		((TextView)login_layout.findViewById(R.id.link_to_register))
			.setOnClickListener(new View.OnClickListener() {			
				@Override public void onClick(View v) 
				{
					vf.showNext();
				}
			});
		
		login = (EditText) login_layout.findViewById(R.id.email_val);
		passw = (EditText) login_layout.findViewById(R.id.passw_val);
		
		((Button)login_layout.findViewById(R.id.login_btn)).setOnClickListener(
				new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(Settings.isOnline(getActivity()))
				{
					(new LoginTask()).execute();
				}
				else
				{
					AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
					b.setTitle("Jesteœ Offline");
					b.setMessage("Po³¹cz siê z internetem by siê zalogowaæ.");
					b.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							
						}
					});
					AlertDialog dial = b.create();
					dial.show();
				}
			}
		});
		
	}
	
	TextView mail, name, surn, phone, hello;
	
	private void initLogoutLayout(View view)
	{
		TableLayout form = (TableLayout) view.findViewById(R.id.logout_form);
		
		((TextView)form.findViewById(R.id.email_lab)).setTypeface(tf);
		((TextView)form.findViewById(R.id.name_lab)).setTypeface(tf);
		((TextView)form.findViewById(R.id.surname_lab)).setTypeface(tf);
		((TextView)form.findViewById(R.id.phone_lab)).setTypeface(tf);
		
		mail = (TextView)form.findViewById(R.id.email_val);
		name = (TextView)form.findViewById(R.id.name_val);
		surn = (TextView)form.findViewById(R.id.surname_val);
		phone = (TextView)form.findViewById(R.id.phone_val);
		hello = (TextView)form.findViewById(R.id.hello_view);
		
		mail.setTypeface(tf);
		name.setTypeface(tf);
		surn.setTypeface(tf);
		phone.setTypeface(tf);
		hello.setTypeface(tf);
		
		Button b;
		(b = (Button)form.findViewById(R.id.logout_btn)).setOnClickListener(
				new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				Login.logout();
				vf.showNext();
			}
		});
		b.setTypeface(tf);
		
	}
	
	private Pattern pattern, phonePattern;
	private Matcher matcher;
 
	private static final String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
 
	private static final String PHONE_PATTERN = "[0-9]{7,15}";
	
	private boolean validate(final String hex) {
		matcher = pattern.matcher(hex);
		return matcher.matches();
	}
	private boolean validatePhone(final String hex)
	{
		matcher = phonePattern.matcher(hex);
		return matcher.matches();
	}
	
	
	EditText reg_log, reg_pas, reg_pas2, reg_nam, reg_sur, reg_pho;
	TextView wrong_mail, wrong_passw, wrong_passw2, wrong_name, wrong_surn, wrong_phone;
	LinearLayout form_layout;
	
	private void clearRegisterForm()
	{
		int n = form_layout.getChildCount();
		for(;n>=0; --n)
		{
			View v = form_layout.getChildAt(n);
			if(v instanceof EditText)
			{
				((EditText)v).setText("");
			}
		}
	}
	
	private void initRegisterLayout(View view)
	{
		pattern = Pattern.compile(EMAIL_PATTERN);
		phonePattern = Pattern.compile(PHONE_PATTERN);
		
		form_layout = (LinearLayout) view.findViewById(R.id.register_form);
		int n = form_layout.getChildCount();
		for(;n>=0; --n)
		{
			View v = form_layout.getChildAt(n);
			if(v instanceof TextView)
			{
				((TextView)v).setTypeface(tf);
			}
		}
		((TextView)form_layout.findViewById(R.id.link_to_login))
		.setOnClickListener(new View.OnClickListener() {			
			@Override public void onClick(View v) 
			{
				vf.showPrevious();
			}
		});
		
		((Button)form_layout.findViewById(R.id.btnRegister))
		.setOnClickListener(new View.OnClickListener() {			
			@Override public void onClick(View v) 
			{
				if(Settings.isOnline(getActivity()))
				{
					(new RegisterTask()).execute();
				}
				else
				{
					AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
					b.setTitle("Jesteœ Offline");
					b.setMessage("Po³¹cz siê z internetem by dokonaæ rejestracji.");
					b.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							
						}
					});
					AlertDialog dial = b.create();
					dial.show();
				}
			}
		});
		
		reg_log = (EditText)form_layout.findViewById(R.id.email_val);
		reg_pas = (EditText)form_layout.findViewById(R.id.passw_val);
		reg_pas2 = (EditText)form_layout.findViewById(R.id.passw2_val);
		reg_nam = (EditText)form_layout.findViewById(R.id.name_val);
		reg_sur = (EditText)form_layout.findViewById(R.id.surname_val);
		reg_pho = (EditText)form_layout.findViewById(R.id.phone_val);
		
		wrong_mail = (TextView)form_layout.findViewById(R.id.wrong_mail);
		wrong_passw = (TextView)form_layout.findViewById(R.id.wrong_passw);
		wrong_passw2 = (TextView)form_layout.findViewById(R.id.wrong_passw2);
		wrong_name = (TextView)form_layout.findViewById(R.id.wrong_name);
		wrong_surn = (TextView)form_layout.findViewById(R.id.wrong_surn);
		wrong_phone = (TextView)form_layout.findViewById(R.id.wrong_phone);

		
	}
	
	private class LoginTask extends AsyncTask<Void, Void, Void>
	{
		String log, pas;
		ProgressDialog pd;
		Login.LoginResponse resp;
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(AccountFragment.this.getActivity());
			pd.setTitle("Logowanie...");
			pd.setMessage("Proszê Czekaæ.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			
			log = login.getText().toString().trim();
			pas = passw.getText().toString().trim();
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			resp = Login.login(log, pas);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(resp != null && resp.type)
			{
			
				if(resp.imie.equals(null))
				{
					name.setText(getString(R.string.noval));
				}
				else
				{
					hello.setText(getString(R.string.hello)+" "+resp.imie+"!");
					name.setText(resp.imie);
				}
				
				
				mail.setText(resp.mail.equals(null) ? getString(R.string.noval) : resp.mail);
				surn.setText(resp.nazw.equals(null) ? getString(R.string.noval) : resp.nazw);
				phone.setText(resp.tel.equals(null) ? getString(R.string.noval) : resp.tel);
				vf.showPrevious();
				clearLoginForm();
				
			}
			else
			{
				AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
				b.setTitle("Nieudane logowanie");
				b.setMessage("SprawdŸ login oraz has³o");
				b.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						
					}
				});
				AlertDialog dial = b.create();
				dial.show();
			}
			pd.dismiss();
			super.onPostExecute(result);
		}
	}
	
	
	private class RegisterTask extends AsyncTask<Void, Void, Void>
	{
		String log, pas, pas2, nam, sur, tel;
		ProgressDialog pd;
		int resp = 0;
		boolean ok;
		
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(AccountFragment.this.getActivity());
			pd.setTitle("Rejestrowanie...");
			pd.setMessage("Proszê Czekaæ.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
			
			ok = true;
			
			
			log  = reg_log.getText().toString().trim();
			pas  = reg_pas.getText().toString().trim();
			pas2 = reg_pas2.getText().toString().trim();
			nam  = reg_nam.getText().toString().trim();
			sur  = reg_sur.getText().toString().trim();
			tel  = reg_pho.getText().toString().trim();
			
			
			if(!validate(log))
			{
				ok = false;
				if(log.isEmpty())
				{
					wrong_mail.setVisibility(View.VISIBLE);
					wrong_mail.setText(R.string.obligatory_field);
				}
				else
				{
					wrong_mail.setVisibility(View.VISIBLE);
					wrong_mail.setText(R.string.wrong_mail2);
				}
					
			}
			else
				wrong_mail.setVisibility(View.GONE);
			
			if(pas.isEmpty())
			{
				ok = false;
				wrong_passw.setVisibility(View.VISIBLE);
				wrong_passw.setText(R.string.obligatory_field);
			}
			else if(pas.length() < 8)
			{
				ok = false;
				wrong_passw.setVisibility(View.VISIBLE);
				wrong_passw.setText(R.string.wrong_pass2);
			}
			else
				wrong_passw.setVisibility(View.GONE);

			
			
			if(!pas2.contains(pas))
			{
				ok = false;
				wrong_passw2.setVisibility(View.VISIBLE);
				wrong_passw2.setText(R.string.wrong_pass);
			}
			else
			{
				wrong_passw2.setVisibility(View.GONE);
			}
			
			if(nam.isEmpty())
			{
				ok = false;
				wrong_name.setVisibility(View.VISIBLE);
				wrong_name.setText(R.string.obligatory_field);
			}
			else
			{
				wrong_name.setVisibility(View.GONE);
			}
			if(sur.isEmpty())
			{
				ok = false;
				wrong_surn.setVisibility(View.VISIBLE);
				wrong_surn.setText(R.string.obligatory_field);
			}
			else
			{
				wrong_surn.setVisibility(View.GONE);
			}
			
			if(!tel.isEmpty() && !validatePhone(tel))
			{
				ok = false;
				wrong_phone.setVisibility(View.VISIBLE);
				wrong_phone.setText(R.string.wrong_tel);
			}
			else
			{
				wrong_phone.setVisibility(View.GONE);
			}
			
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			if(ok)
			{
				resp = Login.register(log, pas, nam, sur, tel);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(ok)
			{
				if(resp == 2)
				{
					wrong_mail.setVisibility(View.VISIBLE);
					wrong_mail.setText(R.string.wrong_mail);
				}
				else if(resp == 1)
				{
					AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
					b.setTitle("Rejestracja udana");
					b.setMessage("Mo¿esz teraz siê zalogowaæ!");
					b.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							clearRegisterForm();
							vf.showPrevious();
						}
					});
					AlertDialog dial = b.create();
					dial.show();
				}
				else
				{
					AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
					b.setTitle("Problem z komunikacj¹");
					b.setMessage("Nie mo¿na po³¹czyæ z serwisem");
					b.setNeutralButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					AlertDialog dial = b.create();
					dial.show();
				}
			}
			pd.dismiss();
			super.onPostExecute(result);
		}
	}
	
	
}
