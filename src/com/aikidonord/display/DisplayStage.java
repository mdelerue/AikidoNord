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
	private LayoutInflater inflater;
	private Context context;
	private int resource;
	String addrNavigation;
	final Activity act;

	
	/**
	 * Constructeur DisplayStage - Se chaarge de prendre un layout existant, de le rempli et de l'intégrer
	 * dans un layout défini
	 * @param context Le contexte général de l'application
	 * @param resource l'id du layout à utiliser
	 * @param stage le stage à afficher
	 * @param root le layout dans lequel il faut insérer root
	 * @param activite l'activité qui appelle le formatage
	 */
	public DisplayStage(Context context, int resource, Stage stage, LinearLayout root, Activity activite) {
		
		this.resource = resource;
		this.stage = stage;
		this.context = context;
		this.act = activite;
		
		// On appelle l'inflate pour pouvoir récupérer la vue
		this.inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
		View view = inflater.inflate(this.resource, root, false);
		
		// appel du formatage
		view = this.formatData(view);

		// on balance la view complétée dans le layout cible 
		root.addView(view);
	}

	/**
	 * Formatage du stage
	 * Utilise le layout défini dans vue pour afficher les données
	 * @param view
	 * @return
	 */
	private View formatData(View view) {
		
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
