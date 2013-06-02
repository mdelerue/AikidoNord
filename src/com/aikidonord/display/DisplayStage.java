package com.aikidonord.display;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import com.aikidonord.R;
import com.aikidonord.metier.Stage;
import com.aikidonord.utils.DrawableOperation;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
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
	 * @return
	 */
	public View formatData() {
		
		// les dateFormat pour afficher les date
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy",
				Locale.FRANCE);
		SimpleDateFormat sdfCourt = new SimpleDateFormat("EEEE dd",
				Locale.FRANCE);

		

		// date
		((TextView) view.findViewById(R.id.tv_date)).setText(sdf.format(stage.getDateDebut()));

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
		
		// animateur
		List<String[]> la = this.stage.getListeAnimateur();
		String animateurs = "";
		int cpt = 0;
		int max = la.size();
		if (la != null && !la.isEmpty()) {
		
			for (String[] tab : la) {
				animateurs += tab[0] + (tab[1] != null ? " (" + tab[1] + ")" : "");

				if (cpt < max) {
					animateurs += "\n";
				}
				cpt++;
			} // fin parcours liste
			
			((TextView)view.findViewById(R.id.tv_animateurs)).setText(animateurs);
			
			// fin si liste != null && !empty
		} else {
			((TextView)view.findViewById(R.id.tv_animePar)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.tv_animateurs)).setVisibility(View.GONE);
		}
		
		
		// horaires
		LinkedHashMap<Date, List<String[]>> lh = this.stage.getHoraires();
		String horaires = "";
		
		if (lh != null &&!lh.isEmpty()) {
			
			for (Entry<Date, List<String[]>> entry : lh.entrySet()) {
				horaires += sdf.format(entry.getKey()) + "\n";
				
				
				for (String[] tab : entry.getValue()) {
					horaires += tab[0] + (tab[1] != null ? " - " + tab[1] : "") + "\n";
				}
				
			}
			
			((TextView)view.findViewById(R.id.tv_horaires)).setText(horaires);
			
		} else {
			((TextView)view.findViewById(R.id.tv_horairesLabel)).setVisibility(View.GONE);
			((TextView)view.findViewById(R.id.tv_horaires)).setVisibility(View.GONE);
		}
		
		// image 
		if (this.stage.getImg() != null && !this.stage.getImg().equals("")) {
			((ImageView)view.findViewById(R.id.iv_affiche))
				.setImageBitmap(DrawableOperation.getBitmapFromStorage(this.stage.getId(),
						this.stage.getDateDebut(),
						this.act.getApplicationContext()));
		} else {
			((ImageView)view.findViewById(R.id.iv_affiche)).setVisibility(View.GONE);
		}
		
		
		
		
		
		
		
		
		return view;
	}
}
