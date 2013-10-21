package com.aikidonord.display;

/*

Copyright (C) 2013  Marc Delerue

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 */

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
    private int nbStage;
    private int numStage;


    public DisplayStage(Stage stage, View view, Activity act, int numStage, int nbStage) {


        this.stage = stage;
        this.view = view;
        this.act = act;
        this.numStage = numStage;
        this.nbStage = nbStage;

    }

    /**
     * Formatage du stage
     * Utilise le layout défini dans vue pour afficher les données
     *
     * @return
     */
    public View formatData() {

        // les dateFormat pour afficher les date
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy",
                Locale.FRANCE);
        SimpleDateFormat sdfCourt = new SimpleDateFormat("EEEE dd",
                Locale.FRANCE);


        String dstr = sdf.format(stage.getDateDebut());
        dstr = dstr.substring(0, 1).toUpperCase() + dstr.substring(1);
        // date
        ((TextView) view.findViewById(R.id.tv_date)).setText(dstr);

        // type de stage
        ((TextView) view.findViewById(R.id.tv_type)).setText(stage.getType());

        // description
        /*
        ((TextView) view.findViewById(R.id.tv_description)).setText(stage
				.getDescription());
        */
        // tarif
        //((TextView) view.findViewById(R.id.tv_tarif)).setText(stage.getTarif());

        // salle
        HashMap<String, String> mapLieu = stage.getMapLieu();

        if (mapLieu.containsKey("salle")) {
            ((TextView) view.findViewById(R.id.tv_salle)).setText(mapLieu.get("salle").replaceAll("\\'", "'"));
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
        adresse.setText(mapLieu.containsKey("ville") ? mapLieu.get("ville").replaceAll("\\'", "'") : addr.replaceAll("\\'", "'"));

        this.addrNavigation = addr.replace(" ", "+");

        ImageView itineraire = ((ImageView) view.findViewById(R.id.tv_itineraire));
        itineraire.setClickable(true);

        // click sur l'itinéraire
        itineraire.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // on lance la navigation
                // google navigation pour le moment
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + addrNavigation));
                act.startActivity(i);
                //return true;
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

                if (cpt < (max - 1)) {
                    animateurs += "\n";
                }
                cpt++;
            } // fin parcours liste

            ((TextView) view.findViewById(R.id.tv_animateurs)).setText(animateurs.replaceAll("\\'", "'"));

            // fin si liste != null && !empty
        } else {
            //((TextView)view.findViewById(R.id.tv_animePar)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.tv_animateurs)).setVisibility(View.GONE);
        }


        // horaires
        LinkedHashMap<Date, List<String[]>> lh = this.stage.getHoraires();
        String horaires = "";

        if (lh != null && !lh.isEmpty()) {

            for (Entry<Date, List<String[]>> entry : lh.entrySet()) {
                horaires += sdf.format(entry.getKey()) + "\n";


                for (String[] tab : entry.getValue()) {
                    horaires += tab[0] + (tab[1] != null ? " - " + tab[1] : "") + "\n";
                }

            }

            ((TextView) view.findViewById(R.id.tv_horaires)).setText(horaires);

        } else {
            ((TextView) view.findViewById(R.id.tv_horairesLabel)).setVisibility(View.GONE);
            ((TextView) view.findViewById(R.id.tv_horaires)).setVisibility(View.GONE);
        }

        // image
        if (this.stage.getImg() != null && !this.stage.getImg().equals("")) {
            ((ImageView) view.findViewById(R.id.iv_affiche))
                    .setImageBitmap(DrawableOperation.getBitmapFromStorage(this.stage.getId(),
                            this.stage.getDateDebut(),
                            this.act.getApplicationContext()));
        } else {
            ((ImageView) view.findViewById(R.id.iv_affiche)).setVisibility(View.GONE);
        }


        TextView tv_num = (TextView) view.findViewById(R.id.tv_num);

        tv_num.setText(++this.numStage + "/" + this.nbStage);


        return view;
    }
}
