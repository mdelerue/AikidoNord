package com.aikidonord.display;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.aikidonord.R;
import com.aikidonord.metier.Stage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DisplayStage {

	private Stage stage;
	String addrNavigation;
	private View view;
	private Activity act;

	
	
	public DisplayStage(Stage stage, View view, Activity act) {
		
		
		this.stage = stage;
		this.view = view;
		this.act = act;

	}

	/**
	 * Formatage du stage
	 * Utilise le layout défini dans vue pour afficher les données
	 * @param view
	 * @return
	 */
	public View formatData() {
		
		// le date format pour afficher la date
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/mm/yyyy",
				Locale.FRANCE);

		// date
		((TextView) view.findViewById(R.id.tv_date)).setText(sdf.format(stage
				.getDateDebut()));

		// type de stage
		((TextView) view.findViewById(R.id.tv_type)).setText(stage.getType());

		// description
		((TextView) view.findViewById(R.id.tv_description)).setText(stage
				.getDescription());

		// tarif
		((TextView) view.findViewById(R.id.tv_tarif)).setText(stage.getTarif());

		// salle
		HashMap<String, String> mapLieu = stage.getMapLieu();
		
		if (mapLieu.containsKey("salle")) {
			((TextView) view.findViewById(R.id.tv_salle)).setText(mapLieu.get("salle"));
		}
		
		// construction adresse
		String addr = "";
		
		if (mapLieu.containsKey("rue")) {
			addr += mapLieu.get("rue") + ", ";
		}
		
		if (mapLieu.containsKey("codepostal")) {
			addr += mapLieu.get("codepostal") + " ";
		}
		
		if (mapLieu.containsKey("ville")) {
			addr += mapLieu.get("ville");
		}
		
		TextView adresse = ((TextView) view.findViewById(R.id.tv_adresse));
		adresse.setText(addr);
		
		this.addrNavigation = addr.replace(" ", "+");
		
		adresse.setClickable(true);
		
		// longclick sur l'adresse
		adresse.setOnLongClickListener(new View.OnLongClickListener() {
            
            @Override
			public boolean onLongClick(View v) {
            	// on lance la navigation
            	// google navigation pour le moment
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + addrNavigation ));
				act.startActivity(i);
				return true;
			}
        });
		
		return view;
	}
}
