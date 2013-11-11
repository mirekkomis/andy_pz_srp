package pl.projektzespolowy.srp.fragment;

import pl.projektzespolowy.srp.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ContactFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_fragment, null);
		return view;
	}
	
	private void showMap()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//		builder.setView(view);
	}
}
